package roiattia.com.capstone.ui.newexpense;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.utils.InjectorUtils;

public class ExpenseActivity extends AppCompatActivity
    implements ExpenseRepository.GetIdHandler{

    private static final String TAG = ExpenseActivity.class.getSimpleName();

    public static final String EXTRA_EXPENSE_ID = "expense_id";
    public static final String EXPENSE_FOR_RESULT = "expense_for_result";
    private static final long DEFAULT_EXPENSE_ID = -1;
    private long mExpenseId = DEFAULT_EXPENSE_ID;

    private ExpenseViewModel mViewModel;

    private int mCost;
    private int mNumberOfPayments;
    private LocalDate mPaymentDate;
    private List<CategoryEntry> mCategoriesList;

    @BindView(R.id.spinner_expense_category) Spinner mCategoriesSpinner;
    @BindView(R.id.et_expense_cost) EditText mCostView;
    @BindView(R.id.et_num_of_payments) EditText mNumPayments;
    @BindView(R.id.et_payment_date) EditText mPaymentDateView;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;
    @BindView(R.id.et_expense_category) EditText mCategoryView;
    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_expense);
        ButterKnife.bind(this);

        // check for intent extra in case of expense update operation
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_EXPENSE_ID)) {
            mExpenseId = intent.getLongExtra(EXTRA_EXPENSE_ID, DEFAULT_EXPENSE_ID);
        }

        // create view_model
        ExpenseViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(this, mExpenseId, this);
        mViewModel = ViewModelProviders.of(this, factory).get(ExpenseViewModel.class);

        // if it's an update then load expense_entry by it's id and call for
        // update ui with it's details
        if(mExpenseId != DEFAULT_EXPENSE_ID){
            mViewModel.getExpense().observe(this, new Observer<ExpenseEntry>() {
                @Override
                public void onChanged(@Nullable ExpenseEntry expenseEntry) {
                    mViewModel.getExpense().removeObserver(this);
                    if(expenseEntry != null) {
                        updateUiWithExpenseDetails(expenseEntry);
                    } else Log.i(TAG, "expenseEntry is null");
                }
            });
        }

        // load categories for spinner
        mViewModel.getCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null){
                    mCategoriesList = categoryEntries;
                    setupSpinner(categoryEntries);
                    for(CategoryEntry categoryEntry : categoryEntries){
                        Log.i(TAG, categoryEntry.toString());
                    }
                }
            }
        });

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
                if(!mNumPayments.getText().toString().equals("") &&
                        !mPaymentDateView.getText().toString().equals("") &&
                        !mCostView.getText().toString().equals("") ) {
                    mCost = Integer.parseInt(mCostView.getText().toString());
                    mNumberOfPayments = Integer.parseInt(mNumPayments.getText().toString());
                    if (mNumberOfPayments > 0) {
                        updateUiWithPayments(mPaymentDate);
                    }
                }
            }
        };
        mCostView.addTextChangedListener(textWatcher);
        mNumPayments.addTextChangedListener(textWatcher);
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

    private void setupSpinner(List<CategoryEntry> categoryEntries) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add(this.getString(R.string.spinner_category_default_value));
        if(categoryEntries != null) {
            for (CategoryEntry categoryEntry : categoryEntries) {
                categoriesNames.add(categoryEntry.getCategoryName());
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categoriesNames);
            // attaching data adapter to spinner
            mCategoriesSpinner.setAdapter(dataAdapter);
        }
    }

    private void updateUiWithPayments(LocalDate paymentDate) {
        double monthlyCost = mCost / mNumberOfPayments;
        for(int i = 0; i<mNumberOfPayments; i++){
            mPaymentsDetailsView.append(Html.fromHtml("<br>" + (i+1) + " payment: "
                    + monthlyCost +
                    ", payment date: " + paymentDate));
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
                        mPaymentDate = LocalDate.parse(dateString);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                        mPaymentDateView.setText(fmt.print(mPaymentDate));
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
        if(mNumPayments.getText().toString().trim().equals("")){
            mNumPayments.setError("Must enter number of payments");
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
        mCostView.setText(String.valueOf(expenseEntry.getExpenseCost()));
        mPaymentDateView.setText(String.valueOf(expenseEntry.getExpensePaymentDate()));
        mExistedCategoryButton.setChecked(true);
    }

    public void confirmExpense(View view){
        // check if all needed input exists
        if(checkInputValidation()) {
            // check if a new category needs to insert
            if(mNewCategoryButton.isChecked()){
                String categoryName = mCategoryView.getText().toString();
                mViewModel.insertNewCategory(categoryName);
            } else {
                int categoryPosition = mCategoriesSpinner.getSelectedItemPosition() - 1;
                Long categoryId = mCategoriesList.get(categoryPosition).getCategoryId();
                confirmExpenseWithCategoryId(categoryId);
            }
        }
    }

    private void confirmExpenseWithCategoryId(Long categoryId) {
        // confirm new expense
        if (mExpenseId == DEFAULT_EXPENSE_ID) {
            mViewModel.insertNewExpense(categoryId, mCost, mNumberOfPayments, mPaymentDate);
        }
        // update expense
        else {
            mViewModel.updateExpense(mExpenseId, categoryId, mCost, mNumberOfPayments, mPaymentDate);
        }
    }

    public void cancelExpense(View view){
        finish();
    }

    @Override
    public void onCategoryInserted(Long categoryId) {
        Log.i(TAG, "new category id: " + categoryId);
        confirmExpenseWithCategoryId(categoryId);
    }

    @Override
    public void onExpensesInserted(long[] expensesId) {
        Log.i(TAG, "new expenses ids array length: " + expensesId.length);
        if(getCallingActivity() != null){
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXPENSE_FOR_RESULT, expensesId);
            setResult(Activity.RESULT_OK, resultIntent);
        }
        finish();
    }
}
