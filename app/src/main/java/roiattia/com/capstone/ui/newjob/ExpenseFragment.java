package roiattia.com.capstone.ui.newjob;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import roiattia.com.capstone.utils.InjectorUtils;

public class ExpenseFragment extends BaseJobFragment {

    @BindView(R.id.spinner_expense_category) Spinner mCategoriesSpinner;
    @BindView(R.id.et_expense_cost) EditText mCostView;
    @BindView(R.id.et_num_of_payments) EditText mNumPayments;
    @BindView(R.id.et_payment_date) EditText mPaymentDateView;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;
    @BindView(R.id.et_expense_category) EditText mCategoryView;
    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;

    private NewJobViewModel mViewModel;
    private int mCost;
    private int mNumberOfPayments;
    private LocalDate mPaymentDate;
    private List<CategoryEntry> mCategoriesList;
    private ExpenseEntry mExpense;

    public ExpenseFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_job_expenses, container, false);
        ButterKnife.bind(this, rootView);

        mCategoriesSpinner.setEnabled(false);
        mCategoryView.setEnabled(false);

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(mListener);
        mViewModel = ViewModelProviders.of(mListener, factory)
                .get(NewJobViewModel.class);
//        mViewModel.getCategories(CategoryEntry.Type.EXPENSE).observe(this, new Observer<List<CategoryEntry>>() {
//            @Override
//            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
//                if(categoryEntries != null) {
//                    ExpenseFragment.super.setupCategoriesSpinner(
//                            mCategoriesSpinner, categoryEntries);
//                }
//            }
//        });

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

        if(mExpense != null){
            updateUiWithDetails();
        }

        setupCategoriesSpinner(mCategoriesSpinner, mCategoriesList);

        return rootView;
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
        if(mNewCategoryButton.isChecked()){
            mViewModel.setExpenseDetails(0, mCost, mNumberOfPayments, mPaymentDate);
            String categoryName = mCategoryView.getText().toString();
            mViewModel.insertNewCategory(categoryName, CategoryEntry.Type.EXPENSE);
        } else if(mExistedCategoryButton.isChecked()){
            int categoryPosition = mCategoriesSpinner.getSelectedItemPosition() - 1;
            long categoryId = mViewModel.getCategoryId(categoryPosition);
            mViewModel.setExpenseDetails(categoryId, mCost, mNumberOfPayments, mPaymentDate);
        }
    }

    public void setCategoriesData(List<CategoryEntry> categoryEntries) {
        mCategoriesList = categoryEntries;
    }

    public void setExpense(ExpenseEntry expenseEntry) {
        mExpense = expenseEntry;
    }

    private void updateUiWithDetails() {
        mCostView.setText(String.valueOf(mExpense.getExpenseCost()));
        mPaymentDateView.setText(mPaymentDate.toString());
        mExistedCategoryButton.setChecked(true);
    }
}
