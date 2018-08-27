package roiattia.com.capstone.ui.newjob;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.ui.calendar.CalendarActivity;
import roiattia.com.capstone.ui.newexpense.ExpenseActivity;
import roiattia.com.capstone.utils.InjectorUtils;

import static roiattia.com.capstone.ui.newexpense.ExpenseActivity.EXPENSE_FOR_RESULT;

public class NewJobActivity extends AppCompatActivity
    implements JobRepository.GetJobIdHandler{

    public static final String TAG = NewJobActivity.class.getSimpleName();

    static final int EXPENSE_ID_REQUEST = 1;  // The request code
    public static final String JOB_DATE = "job_date";
    public static final String JOB_PAYMENT_DATE = "job_payment_date";
    public static final String JOB_ID_UPDATE = "job_id_update";
    private static final long DEFAULT_JOB_ID = -1;
    private long mJobId = DEFAULT_JOB_ID;

    private NewJobViewModel mViewModel;

    private LocalDate mJobDate;
    private LocalDate mJobPaymentDate;
    private double mJobIncome;
    private double mJobExpense;
    private double mJobProfit;
    private long mCategoryId;
    private String mDescription;
    private List<CategoryEntry> mCategories;

    @BindView(R.id.et_date) EditText mJobDateView;
    @BindView(R.id.et_date_of_payment)EditText mJobPaymentDateView;
    @BindView(R.id.et_fee)EditText mFeeView;
    @BindView(R.id.et_category)EditText mJobCategoryView;
    @BindView(R.id.et_job_description)EditText mJobDescriptionView;
    @BindView(R.id.spinner_job_category) Spinner mCategorySpinner;
    @BindView(R.id.tv_expenses) TextView mExpensesView;
    @BindView(R.id.tv_profit) TextView mProfitView;
    @BindView(R.id.tv_income) TextView mJobIncomeView;
    @BindView(R.id.ll_expense_list_view) LinearLayout mExpensesListView;
    @BindView(R.id.rb_existed_category) RadioButton mExistedCategoryButton;
    @BindView(R.id.rb_new_category) RadioButton mNewCategoryButton;
    @BindView(R.id.cardview_summary) CardView mSummaryCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        ButterKnife.bind(this);

        // get date selected for job from calendar activity
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra(CalendarActivity.DATE)) {
                String dateAsString = intent.getStringExtra(CalendarActivity.DATE);
                LocalDate localDate = new LocalDate(dateAsString);
                mJobDate = localDate;
                DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                mJobDateView.setText(fmt.print(localDate));
            }
            if(intent.hasExtra(JOB_ID_UPDATE)){
                mJobId = intent.getLongExtra(JOB_ID_UPDATE, DEFAULT_JOB_ID);
            }
        }

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideNewJobViewModelFactory(this, mJobId, this);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(NewJobViewModel.class);
        // if it's an update then load expense_entry by it's id and call for
        // update ui with it's details
        if(mJobId != DEFAULT_JOB_ID){
            mViewModel.getJob().observe(this, new Observer<JobEntry>() {
                @Override
                public void onChanged(@Nullable JobEntry jobEntry) {
                    mViewModel.getJob().removeObserver(this);
                    if(jobEntry != null) {
                        updateUiWithJobDetails(jobEntry);
                    } else Log.i(TAG, "jobEntry is null");
                }
            });
        }

        mViewModel.getJobCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null) {
                    mCategories = categoryEntries;
                    setupSpinner(categoryEntries);
                    for (CategoryEntry categoryEntry : categoryEntries) {
                        Log.i(TAG, categoryEntry.toString());
                    }
                }
            }
        });
        mViewModel.debugPrint();

        mCategorySpinner.setEnabled(false);
        mJobCategoryView.setEnabled(false);

        // setup job_date and payment_date with on_click_listeners
        mJobDateView.setFocusable(false);
        mJobPaymentDateView.setFocusable(false);
        mJobDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(mJobDateView, JOB_DATE);
            }
        });
        mJobPaymentDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(mJobPaymentDateView, JOB_PAYMENT_DATE);
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
                    mJobIncome = Integer.parseInt(s.toString());
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
            mCategorySpinner.setAdapter(dataAdapter);
        }
    }

    private void updateUiWithJobDetails(JobEntry jobEntry) {
        mJobProfit = jobEntry.getJobProfits();
        mJobExpense = jobEntry.getJobExpenses();
        mJobIncome = jobEntry.getJobIncome();
        mJobIncomeView.setText(String.valueOf(mJobIncome));
        updateSummaryCard();
        mJobDate = jobEntry.getJobDate();
        mJobPaymentDate = jobEntry.getJobDateOfPayment();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        mJobDateView.setText(fmt.print(mJobDate));
        mJobPaymentDateView.setText(fmt.print(mJobPaymentDate));
        mDescription = jobEntry.getJobDescription();
        mJobDescriptionView.setText(mDescription);
        mCategoryId = jobEntry.getCategoryId();
    }

    private void setupDatePicker(final EditText jobDateView, final String dateType) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateString = year + "-" + (month+1) + "-" + dayOfMonth;
                        LocalDate localDate = new LocalDate(dateString);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                        jobDateView.setText(fmt.print(localDate));
                        if(dateType.equals(JOB_DATE)) mJobDate = localDate;
                        if(dateType.equals(JOB_PAYMENT_DATE)) mJobPaymentDate = localDate;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateSummaryCard() {
        mJobProfit = mJobIncome - mJobExpense;
        if(mJobProfit < 0){
            mSummaryCardView.setCardBackgroundColor(getResources().getColor(R.color.colorNegativeRed));
        } else {
            mSummaryCardView.setCardBackgroundColor(getResources().getColor(R.color.colorPositiveGreen));
        }
        mJobIncomeView.setText(String.format("%s", mJobIncome));
        mExpensesView.setText(String.format("%s", mJobExpense));
        mProfitView.setText(String.format("%s", mJobProfit));
    }

    /**
     * Check input validation
     */
    public Boolean checkInputValidation() {
        boolean isInputValid = true;
        // check category validation
        if(!mNewCategoryButton.isChecked() && !mExistedCategoryButton.isChecked()
                && mCategorySpinner.getSelectedItemPosition() == 0){
            isInputValid = false;
        }
        // check cost validation
        if(mJobDateView.getText().toString().trim().equals("")){
            mJobDateView.setError("Must enter a date");
            isInputValid = false;
        }
        // check number_of_payments validation
        if(mJobPaymentDateView.getText().toString().trim().equals("")){
            mJobPaymentDateView.setError("Must enter a date");
            isInputValid = false;
        }
        // check payment_date validation
        if(mFeeView.getText().toString().trim().equals("")){
            mFeeView.setError("Must enter a fee");
            isInputValid = false;
        }
        return isInputValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.job_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Confirm new Job
        if(item.getItemId() == R.id.mi_done){
            // check if all needed input exists
            if(checkInputValidation()) {
                // check if a new category needs to insert
                if(mNewCategoryButton.isChecked()){
                    String categoryName = mJobCategoryView.getText().toString();
                    mViewModel.insertNewCategory(categoryName);
                } else {
                    int categoryPosition = mCategorySpinner.getSelectedItemPosition() - 1;
                    Long categoryId = mCategories.get(categoryPosition).getCategoryId();
                    confirmJobWithCategoryId(categoryId);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmJobWithCategoryId(long categoryId) {
        // confirm new expense
        if (mJobId == DEFAULT_JOB_ID) {
            mViewModel.insertNewJob(categoryId, mDescription, mJobDate, mJobPaymentDate,
                     mJobIncome, mJobExpense, mJobProfit);
        }
        // update expense
        else {
            mViewModel.updateJob(mJobId, categoryId, mDescription, mJobDate, mJobPaymentDate,
                    mJobIncome, mJobExpense, mJobProfit);
        }
    }

    public void addExpense(View view){
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivityForResult(intent, EXPENSE_ID_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EXPENSE_ID_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                long[] expensesId = data.getLongArrayExtra(EXPENSE_FOR_RESULT);
                loadExpensesById(expensesId);
                Log.i(TAG, String.valueOf(expensesId.length));
            }
        }
    }

    private void loadExpensesById(final long[] expensesId) {
        mViewModel.loadExpensesById(expensesId).observe(this, new Observer<List<ExpenseEntry>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseEntry> expensesList) {
                if (expensesList != null) {
                    updateUiWithNewExpenses(expensesList);
                }
            }
        });
    }

    private void updateUiWithNewExpenses(List<ExpenseEntry> expensesList) {
        double expenseCost = 0;
        int numberOfPayments = expensesList.size();
        LocalDate startPaymentDate = expensesList.get(0).getExpensePaymentDate();
        for(ExpenseEntry expenseEntry : expensesList){
            expenseCost += expenseEntry.getExpenseCost();
            mJobExpense += expenseEntry.getExpenseCost();
            Log.i(TAG, expenseEntry.toString());
        }
        TextView textView = new TextView(this);
        textView.setText("cost: "  + expenseCost + ", payments: " + numberOfPayments);
        mExpensesListView.addView(textView);
        updateSummaryCard();
        mViewModel.addExpenses(expensesList);
    }

    @Override
    public void onCategoryInserted(long categoryId) {
        mCategoryId = categoryId;
        Log.i(TAG, "new category id: " + categoryId);
        confirmJobWithCategoryId(categoryId);
    }

    @Override
    public void onJobInserted(long jobId) {
        for(ExpenseEntry expenseEntry : mViewModel.getExpensesList()){
            expenseEntry.setJobId(jobId);
        }
        mViewModel.updateExpenses();
        finish();
    }
}
