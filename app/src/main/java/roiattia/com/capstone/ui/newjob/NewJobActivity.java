package roiattia.com.capstone.ui.newjob;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import roiattia.com.capstone.database.Repository;
import roiattia.com.capstone.model.JobModel;
import roiattia.com.capstone.ui.calendar.CalendarActivity;
import roiattia.com.capstone.ui.expense.AddExpenseActivity;
import roiattia.com.capstone.ui.expense.CategoriesViewModel;
import roiattia.com.capstone.ui.expense.CategoriesViewModelFactory;
import roiattia.com.capstone.utils.InjectorUtils;

public class NewJobActivity extends AppCompatActivity {

    public static final String TAG = NewJobActivity.class.getSimpleName();
    private JobModel mJobModel;
    private CategoriesViewModel mViewModel;

    @BindView(R.id.et_date) EditText mJobDateView;
    @BindView(R.id.et_date_of_payment)EditText mPaymentDateView;
    @BindView(R.id.et_fee)EditText mFeeView;
    @BindView(R.id.et_category)EditText mJobCategoryView;
    @BindView(R.id.spinner_job_category) Spinner mCategorySpinner;
    @BindView(R.id.tv_expenses) TextView mExpensesView;
    @BindView(R.id.tv_profit) TextView mProfitView;
    @BindView(R.id.tv_income) TextView mIncomeView;
    @BindView(R.id.ll_expense_list_view) LinearLayout mExpensesListView;

    private static final String JOB_DATE = "job_date";
    private static final String JOB_PAYMENT_DATE = "job_payment_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        ButterKnife.bind(this);

        mJobModel = JobModel.getInstance();

        Intent intent = getIntent();
        String dateAsString = intent.getStringExtra(CalendarActivity.DATE);
        LocalDate localDate = new LocalDate(dateAsString);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        mJobDateView.setText(fmt.print(localDate));

        CategoriesViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(this, CategoryEntry.Type.JOB);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(CategoriesViewModel.class);
        mViewModel.getCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null) {
                    setupCategoriesSpinner(categoryEntries);
                }
            }
        });

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

        mFeeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    int fee = Integer.parseInt(s.toString());
                    mJobModel.getJobEntry().setIncome(fee);
                    mJobModel.calculateJobProfits();
                    mProfitView.setText((int) mJobModel.getJobEntry().getProfit() + "");
                    mIncomeView.setText((int) mJobModel.getJobEntry().getIncome() + "");
                }
            }
        });
    }

    private void setupCategoriesSpinner(List<CategoryEntry> categoryEntries) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add("Pick category from list");
        if(categoryEntries != null) {
            for (CategoryEntry categoryEntry : categoryEntries) {
                categoriesNames.add(categoryEntry.getName());
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categoriesNames);
            // attaching data adapter to spinner
            mCategorySpinner.setAdapter(dataAdapter);
        }
    }

    private void setupDatePicker(final EditText editText, final String dateType) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateString = year + "-" + (month+1) + "-" + dayOfMonth;
                LocalDate localDate = new LocalDate(dateString);
                DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                editText.setText(fmt.print(localDate));
                if(dateType.equals(JOB_DATE)) mJobModel.setJobDate(localDate);
                if(dateType.equals(JOB_PAYMENT_DATE)) mJobModel.setJobPaymentDate(localDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void addExpense(View view){
        Intent intent = new Intent(NewJobActivity.this, AddExpenseActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.job_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_done){
            confirmJob();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmJob() {
        if(checkInputValidation()) {
            // New category is inserted
            if (mCategorySpinner.getSelectedItemPosition() == 0) {
                addJobWithNewCategory();
            }
            // Category picked from spinner
            else {
                addJob();
            }
        }
        else {
            Toast.makeText(this, "Invalid details", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void addJob() {
        // get picked category id
        CategoryEntry categoryEntry = mViewModel.getCategories().getValue()
                .get(mCategorySpinner.getSelectedItemPosition()-1);
        int categoryId = categoryEntry.getId();

        mJobModel.addJob(InjectorUtils.provideRepository(this), categoryId);
    }

    private void addJobWithNewCategory() {
        // Initialize new Category
        String name = mJobCategoryView.getText().toString();
        CategoryEntry categoryEntry = new CategoryEntry(name, CategoryEntry.Type.JOB);

        mJobModel.addJobWithNewCategory(InjectorUtils.provideRepository(this),
                categoryEntry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mJobModel.getExpenses().size() > mExpensesListView.getChildCount()) {
            checkExpenses();
        }
    }

    private void checkExpenses() {
        mExpensesListView.removeAllViews();
        mJobModel.calculateTotalExpense();
        double expenses = 0;
        List<ExpenseEntry> expenseEntries;
        expenseEntries = mJobModel.getExpenses();
        if(expenseEntries.size() > 0) {
            mJobModel.calculateTotalExpense();
            for (ExpenseEntry expenseEntry : mJobModel.getExpenses()){
                TextView textView = new TextView(this);
                textView.setText(expenseEntry.getCost()  + "");
                mExpensesListView.addView(textView);
                expenses += expenseEntry.getCost();
            }
        }
        mJobModel.setJobTotalExpenses(expenses);
        mJobModel.calculateJobProfits();
        mExpensesView.setText(String.valueOf(mJobModel.getJobEntry().getExpenses()));
        mProfitView.setText((int) mJobModel.getJobEntry().getProfit() + "");
        mIncomeView.setText((int) mJobModel.getJobEntry().getIncome() + "");
    }

    private boolean checkInputValidation() {
        boolean isInputValid = true;
        // check category validation
        if(mCategorySpinner.getSelectedItemPosition() == 0 &&
                mJobCategoryView.getText().toString().trim().equalsIgnoreCase("")){
            mJobCategoryView.setError("Must enter new category or chose from the list");
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
}
