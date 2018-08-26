package roiattia.com.capstone.ui.newjob;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.ui.finances.PickPeriodDialog;

public class ExpenseFragment extends BaseJobFragment {

    private static final String TAG = ExpenseFragment.class.getSimpleName();

    @BindView(R.id.spinner_expense_category) Spinner mCategoriesSpinner;
    @BindView(R.id.et_expense_cost) EditText mCostView;
    @BindView(R.id.et_num_of_payments) EditText mNumPayments;
    @BindView(R.id.et_payment_date) EditText mPaymentDateView;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;
    @BindView(R.id.et_expense_category) EditText mCategoryView;
    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;
    @BindView(R.id.btn_confirm) Button mConfirmExpenseButton;

    public static final long NEW_CATEGORY_ID_INDICATOR = 0;
    private int mCost;
    private int mNumberOfPayments;
    private LocalDate mPaymentDate;
    private List<CategoryEntry> mCategoriesList;
    private ExpenseEntry mExpense;
    private long mCategoryId = NEW_CATEGORY_ID_INDICATOR;
    private String mCategoryName;
    private ConfirmExpenseHandler mCallback;

    public interface ConfirmExpenseHandler {
        void onConfirmExpenseClick(ExpenseEntry expenseEntry, String newCategoryName);
    }

    public ExpenseFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);
        ButterKnife.bind(this, rootView);

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

        // check if it's an update operation, if so then update UI
        if(mExpense != null){
            updateUiWithDetails();
        } else {
            mExpense = new ExpenseEntry();
        }

        // setup "confirm expense" button listener
        mConfirmExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputValidation()) {
                    setExpenseDetails();
                    mCallback.onConfirmExpenseClick(mExpense, mCategoryName);
                    mListener.onBackPressed();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ConfirmExpenseHandler) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.i(TAG, " must implement ConfirmExpenseHandler");
        }
    }

    /**
     * Handle UI update of payments details
     * @param paymentDate
     */
    private void updateUiWithPayments(LocalDate paymentDate) {
        double monthlyCost = mCost / mNumberOfPayments;
        for(int i = 0; i<mNumberOfPayments; i++){
            mPaymentsDetailsView.append(Html.fromHtml("<br>" + (i+1) + " payment: "
                    + monthlyCost +
                    ", payment date: " + paymentDate));
            paymentDate = paymentDate.plusMonths(1);
        }
    }

    /**
     * Handle date picker dialog click
     */
    private void pickDate(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(mListener,
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
            Toast.makeText(mListener, "Invalid category", Toast.LENGTH_SHORT).show();
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

    /**
     * Handle expenses details update and insertion as new expense
     */
    public void setExpenseDetails() {
        mExpense.setExpenseCost(mCost);
        mExpense.setNumberOfPayments(mNumberOfPayments);
        mExpense.setExpensePaymentDate(mPaymentDate);
        if(mNewCategoryButton.isChecked()){
            if(mCategoryId != NEW_CATEGORY_ID_INDICATOR){
                mExpense.setCategoryId(mCategoryId);
            }
            mCategoryName = mCategoryView.getText().toString();
        } else if(mExistedCategoryButton.isChecked()){
            int categoryPosition = mCategoriesSpinner.getSelectedItemPosition() - 1;
            long categoryId = mCategoriesList.get(categoryPosition).getCategoryId();
            mExpense.setCategoryId(categoryId);
        }
    }

    /**
     * Set categories list for spinner selection
     * @param categoryEntries list of categories
     */
    public void setCategoriesData(List<CategoryEntry> categoryEntries) {
        mCategoriesList = categoryEntries;
        setupCategoriesSpinner(mCategoriesSpinner, mCategoriesList);
    }

    /**
     * Set expense for update operation
     * @param expenseEntry expense entry to update
     */
    public void setExpense(ExpenseEntry expenseEntry) {
        mExpense = expenseEntry;
    }

    /**
     * Update ui with expense details
     */
    private void updateUiWithDetails() {
        mCostView.setText(String.valueOf(mExpense.getExpenseCost()));
        mPaymentDateView.setText(mPaymentDate.toString());
        mExistedCategoryButton.setChecked(true);
    }
}
