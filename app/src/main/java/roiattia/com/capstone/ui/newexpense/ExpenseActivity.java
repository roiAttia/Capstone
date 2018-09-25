package roiattia.com.capstone.ui.newexpense;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.repositories.CategoriesRepository;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class ExpenseActivity extends AppCompatActivity
    implements CategoriesRepository.OnCategoryListener, ExpensesRepository.OnExpenseListener{

    private static final String TAG = ExpenseActivity.class.getSimpleName();

    public static final String EXTRA_EXPENSE_ID = "expense_id";
    public static final String EXPENSE_FOR_RESULT = "expense_for_result";
    private static final long DEFAULT_EXPENSE_ID = -1;
    private long mExpenseId = DEFAULT_EXPENSE_ID;

    private ExpensesViewModel mViewModel;

    private double mCost;
    private int mNumberOfPayments;
    private double mMonthlyCost;
    private LocalDate mFirstPaymentDate;
    private LocalDate mLastPaymentDate;
    private List<CategoryEntry> mCategoriesList;
    private long mCategoryId;
    private Long mJobId;
    private String mDescription;

    @BindView(R.id.spinner_expense_category) Spinner mCategoriesSpinner;
    @BindView(R.id.iet_expense_cost) TextInputEditText mCostView;
    @BindView(R.id.iet_number_of_payments) TextInputEditText mNumPaymentsView;
    @BindView(R.id.iet_payment_date) TextInputEditText mPaymentDateView;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;
    @BindView(R.id.iet_category_name) TextInputEditText mCategoryView;
    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(ExpensesViewModel.class);

        // check for intent extra in case of expense update operation
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_EXPENSE_ID)) {
            mExpenseId = intent.getLongExtra(EXTRA_EXPENSE_ID, DEFAULT_EXPENSE_ID);
            mViewModel.getExpenseById(mExpenseId);
        }

        setupViewModel();

        setupUI();
    }

    private void setupUI(){
        // set_enabled false for category spinner and edit_text to be able
        // to interact via radio buttons
        mCategoriesSpinner.setEnabled(false);
        mCategoryView.setEnabled(false);

        // configure selection of payment date
        mPaymentDateView.setFocusable(false);
        mPaymentDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        // configure text_watcher to listen to text input for payments calculations
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // check if all needed details for payments calculations are inserted
                if(!mNumPaymentsView.getText().toString().equals("") &&
                        !mPaymentDateView.getText().toString().equals("") &&
                        !mCostView.getText().toString().equals("") ) {
                    mCost = Double.parseDouble(((mCostView.getText().toString().replaceAll(",",""))));
                    mNumberOfPayments = Integer.parseInt(mNumPaymentsView.getText().toString());
                    if (mNumberOfPayments > 0) {
                        updateUiWithPayments(mFirstPaymentDate);
                    }
                }
            }
        };
        mCostView.addTextChangedListener(textWatcher);
        mNumPaymentsView.addTextChangedListener(textWatcher);
        mPaymentDateView.addTextChangedListener(textWatcher);

        // setup category radio buttons
        mExistedCategoryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mCategoryView.setEnabled(false);
                    mCategoriesSpinner.setEnabled(true);
                }
            }
        });
        mNewCategoryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mCategoryView.setEnabled(true);
                    mCategoryView.requestFocus();
                    mCategoriesSpinner.setEnabled(false);
                }
            }
        });
    }

    private void setupViewModel() {
        mViewModel.getLiveDataCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                mViewModel.getLiveDataCategories().removeObserver(this);
                mCategoriesList = categoryEntries;
                setupSpinner(categoryEntries);
            }
        });
        mViewModel.getMutableExpense().observe(this, new Observer<ExpenseEntry>() {
            @Override
            public void onChanged(@Nullable ExpenseEntry expenseEntry) {
                if(expenseEntry != null) {
                    updateUiWithExpenseDetails(expenseEntry);
                } else Log.i(TAG, "expenseEntry is null");
            }
        });
    }

    private void setupSpinner(List<CategoryEntry> categoryEntries) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add(this.getString(R.string.spinner_category_default_value));
        int categoryIndex = 0;
        if(categoryEntries != null) {
            for (int i = 1; i<categoryEntries.size()+1; i++) {
                categoriesNames.add(categoryEntries.get(i-1).getCategoryName());
                if(categoryEntries.get(i-1).getCategoryId() == mCategoryId){
                    categoryIndex = i;
                }
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categoriesNames);
            // attaching data adapter to spinner
            mCategoriesSpinner.setAdapter(dataAdapter);
        }
        mCategoriesSpinner.setSelection(categoryIndex);
    }

    private void updateUiWithPayments(LocalDate paymentDate) {
        mPaymentsDetailsView.setText("");
        mMonthlyCost = mCost / mNumberOfPayments;
        for(int i = 0; i<mNumberOfPayments; i++){
            mPaymentsDetailsView.append(Html.fromHtml("<br>" + (i+1) + " payment: "
                    + AmountUtils.getStringFormatFromDouble(mMonthlyCost) +
                    ", payment date: " + DateUtils.getDateStringFormat(paymentDate)));
            paymentDate = paymentDate.plusMonths(1);
        }
    }

    private void pickDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = year + "-" + (month+1) + "-" + dayOfMonth;
                        mFirstPaymentDate = LocalDate.parse(dateString);
                        mPaymentDateView.setText(DateUtils.getDateStringFormat(mFirstPaymentDate));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    /**
     * Handles input validation
     * @return true if input valid
     */
    public Boolean checkInputValidation() {
        boolean isInputValid = true;
        // check category validation
        if(!mNewCategoryButton.isChecked() && !mExistedCategoryButton.isChecked()
                && mCategoriesSpinner.getSelectedItemPosition() == 0){
            isInputValid = false;
        }
        // check cost validation
        if(mCostView.getText().toString().trim().equals("")){
            mCostView.setError("Must enter a cost");
            isInputValid = false;
        }
        // check number_of_payments validation
        if(mNumPaymentsView.getText().toString().trim().equals("")){
            mNumPaymentsView.setError("Must enter number of payments");
            isInputValid = false;
        }
        // check payment_date validation
        if(mPaymentDateView.getText().toString().trim().equals("")){
            mPaymentDateView.setError("Must enter a payment date");
            isInputValid = false;
        }
        return isInputValid;
    }

    private void updateUiWithExpenseDetails(ExpenseEntry expenseEntry) {
        mExpenseId = expenseEntry.getExpenseId();
        mCategoryId = expenseEntry.getCategoryId();
        if(expenseEntry.getJobId() != null) mJobId = expenseEntry.getJobId();
        mCost = expenseEntry.getExpenseCost();
        mCostView.setText(AmountUtils.getStringFormatFromDouble(expenseEntry.getExpenseCost()));
        mNumberOfPayments = expenseEntry.getNumberOfPayments();
        mMonthlyCost = expenseEntry.getMonthlyCost();
        mNumPaymentsView.setText(String.valueOf(mNumberOfPayments));
        mFirstPaymentDate = expenseEntry.getExpenseFirstPayment();
        mPaymentDateView.setText(DateUtils.getDateStringFormat(mFirstPaymentDate));
        mLastPaymentDate = expenseEntry.getExpenseLastPayment();
        mExistedCategoryButton.setChecked(true);
        mDescription = expenseEntry.getDescription();
    }

    private void updateExpenseWithCategoryId(long categoryId) {
        mViewModel.insertExpense(categoryId, mCost, mNumberOfPayments,
                mMonthlyCost, mFirstPaymentDate, this);
        finish();
        // confirm new expense
//        if (mExpenseId == DEFAULT_EXPENSE_ID) {
//            mExpenseViewModel.insertNewExpense(categoryId, mCost, mNumberOfPayments, mFirstPaymentDate);
//        }
//        // update expense
//        else {
//            Log.i(TAG, "UPDATE" + " expenseId: " + mExpenseId);
//            mExpenseViewModel.updateExpense(mExpenseId, mJobId, categoryId, mCost, mNumberOfPayments,
//                    mMonthlyCost, mFirstPaymentDate, mLastPaymentDate);
//            finish();
//        }
    }

    @Override
    public void onCategoryInserted(long categoryId) {
        Log.i(TAG, "new category id: " + categoryId);
        mCategoryId = categoryId;
        mViewModel.createPayments(mNumberOfPayments, mMonthlyCost, mFirstPaymentDate);
        mViewModel.insertExpense(categoryId, mCost, mNumberOfPayments, mMonthlyCost, mFirstPaymentDate,this);
    }

    @Override
    public void onExpenseInserted(long expenseId) {
        mViewModel.updatePaymentsWithExpenseID(expenseId);
        mViewModel.insertPayments();
        if(getCallingActivity() != null){
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXPENSE_FOR_RESULT, expenseId);
            setResult(Activity.RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_job_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_done){
            // check if all needed input exists
            if(checkInputValidation()) {
                // check if a new category needs to insert
                if(mNewCategoryButton.isChecked()){
                    insertNewCategory();
                } else {
                    int categoryPosition = mCategoriesSpinner.getSelectedItemPosition() - 1;
                    long categoryId = mCategoriesList.get(categoryPosition).getCategoryId();
                    mViewModel.createPayments(mNumberOfPayments, mMonthlyCost, mFirstPaymentDate);
                    updateExpenseWithCategoryId(categoryId);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertNewCategory() {
        String categoryName = mCategoryView.getText().toString();
        mViewModel.insertNewCategory(categoryName ,this);
    }

    @Override
    public void onBackPressed() {
        // create alert dialog to confirm delete operation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel new expense?")
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
