package roiattia.com.capstone.ui.newjob;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static roiattia.com.capstone.ui.newjob.NewJobActivity.JOB_DATE;
import static roiattia.com.capstone.ui.newjob.NewJobActivity.JOB_PAYMENT_DATE;

public class NewJobFragment extends BaseFragment {

    @BindView(R.id.et_date) EditText mJobDateView;
    @BindView(R.id.et_date_of_payment)EditText mPaymentDateView;
    @BindView(R.id.et_fee)EditText mFeeView;
    @BindView(R.id.et_category)EditText mJobCategoryView;
    @BindView(R.id.spinner_job_category) Spinner mCategorySpinner;
    @BindView(R.id.tv_expenses) TextView mExpensesView;
    @BindView(R.id.tv_profit) TextView mProfitView;
    @BindView(R.id.tv_income) TextView mIncomeView;
    @BindView(R.id.ll_expense_list_view) LinearLayout mExpensesListView;
    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;

    private NewJobViewModel mViewModel;
    private LocalDate mJobDate;
    private LocalDate mJobPaymentDate;
    private int mJobIncome;
    private String mJobDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_job_details, container, false);
        ButterKnife.bind(this, rootView);

        mCategorySpinner.setEnabled(false);
        mJobCategoryView.setEnabled(false);

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(mListener);
        mViewModel = ViewModelProviders.of(mListener, factory)
                .get(NewJobViewModel.class);
        mViewModel.getCategories(CategoryEntry.Type.JOB).observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null) {
                    mViewModel.setCategories(categoryEntries);
                    NewJobFragment.super.setupCategoriesSpinner(mCategorySpinner, categoryEntries);
                }
            }
        });

        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        mJobDateView.setText(fmt.print(mViewModel.getJobDate()));
        mJobDate = mViewModel.getJobDate();

        // setup job_date and payment_date with on_click_listeners
        mJobDateView.setFocusable(false);
        mPaymentDateView.setFocusable(false);
        mJobDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(mJobDateView, JOB_DATE);
            }
        });
        mPaymentDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(mPaymentDateView, JOB_PAYMENT_DATE);
            }
        });

        // setup fee change listener
        mFeeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    int fee = Integer.parseInt(s.toString());
                    mJobIncome = fee;
//                    mViewModel.insertFee(fee);
                    updateSummaryCard();
                }
            }
        });

        mExistedCategoryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mJobCategoryView.setEnabled(false);
                    mCategorySpinner.setEnabled(true);
                }
            }
        });

        mNewCategoryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mJobCategoryView.setEnabled(true);
                    mJobCategoryView.requestFocus();
                    mCategorySpinner.setEnabled(false);
                }
            }
        });

        return rootView;
    }

    /**
     * Update job summary card_view with new sums
     */
    public void updateSummaryCard() {
        mIncomeView.setText(String.format("%s", mViewModel.getIncome()));
        mExpensesView.setText(String.format("%s", mViewModel.getExpenses()));
        mProfitView.setText(String.format("%s", mViewModel.getProfit()));
    }

    /**
     * Setup date picker for job_date and payment_date
     * @param jobDateView the view which was clicked and need to update with new date
     * @param dateType the type of date from job_date and payment_date
     */
    private void setupDatePicker(final EditText jobDateView, final String dateType) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mListener,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = year + "-" + (month+1) + "-" + dayOfMonth;
                        LocalDate localDate = new LocalDate(dateString);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                        jobDateView.setText(fmt.print(localDate));
                        if(dateType.equals(JOB_DATE)) mViewModel.setJobDate(localDate);
                        if(dateType.equals(JOB_PAYMENT_DATE)) mViewModel.setJobPaymentDate(localDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * Check which option selected - new or existed category
     * and start insert new job sequence
     */
    public void insertJobSequence() {
        if(mNewCategoryButton.isChecked()){
            String categoryName = mJobCategoryView.getText().toString();
            mViewModel.insertNewCategory(categoryName, CategoryEntry.Type.JOB);
        } else if(mExistedCategoryButton.isChecked()){
            int categoryPosition = mCategorySpinner.getSelectedItemPosition();
            long categoryId = mViewModel.getCategoryId(categoryPosition);
            mViewModel.setJobCategoryId(categoryId);
            mViewModel.insertNewJob();
        }
    }

    public Boolean checkInputValidation() {
        boolean isInputValid = true;
        // check category validation
        if(!mNewCategoryButton.isChecked() && !mExistedCategoryButton.isChecked()){
            Toast.makeText(mListener, "Invalid category", Toast.LENGTH_SHORT).show();
            isInputValid = false;
        }
        // check cost validation
        if(mJobDateView.getText().toString().trim().equals("")){
            mJobDateView.setError("Must enter a date");
            isInputValid = false;
        }
        // check number_of_payments validation
        if(mPaymentDateView.getText().toString().trim().equals("")){
            mPaymentDateView.setError("Must enter a date");
            isInputValid = false;
        }
        // check payment_date validation
        if(mFeeView.getText().toString().trim().equals("")){
            mFeeView.setError("Must enter a fee");
            isInputValid = false;
        }
        return isInputValid;
    }

    public void updateExpenses() {
        for(ExpenseEntry expenseEntry : mViewModel.getExpensesList()){
            TextView textView = new TextView(mListener);
            textView.setText(expenseEntry.getCost() + "");
            mExpensesListView.addView(textView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateExpenses();
        updateSummaryCard();
    }
}
