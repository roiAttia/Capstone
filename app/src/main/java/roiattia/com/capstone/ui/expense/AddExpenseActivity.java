package roiattia.com.capstone.ui.expense;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.model.ExpenseModel;
import roiattia.com.capstone.model.JobModel;
import roiattia.com.capstone.utils.InjectorUtils;

public class AddExpenseActivity extends AppCompatActivity {

    private static final String TAG = AddExpenseActivity.class.getSimpleName();

    @BindView(R.id.et_expense_category) EditText mCategory;
    @BindView(R.id.spinner_expense_category) Spinner mCategorySpinner;
    @BindView(R.id.et_expense_cost) EditText mCost;
    @BindView(R.id.et_num_of_payments) EditText mNumPayments;
    @BindView(R.id.et_payment_date) EditText mPaymentDate;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;

    private CategoriesViewModel mViewModel;
    private JobModel mJobModel;
    private ExpenseModel mExpenseModel;
    private LocalDate mLocalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        ButterKnife.bind(this);

        mJobModel = JobModel.getInstance();
        mExpenseModel = ExpenseModel.getInstance();

        // configure view_model and factory for categories list
        CategoriesViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(this, CategoryEntry.Type.EXPENSE);
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

        // configure selection of payment date
        mPaymentDate.setFocusable(false);
        mPaymentDate.setOnClickListener(new View.OnClickListener() {
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
                String numberOfPaymentsString = mNumPayments.getText().toString();
                String paymentDateString = mPaymentDate.getText().toString();
                String costString = mCost.getText().toString();
                calculatePayments(costString, numberOfPaymentsString, paymentDateString);
            }
        };

        mCost.addTextChangedListener(textWatcher);
        mNumPayments.addTextChangedListener(textWatcher);
        mPaymentDate.addTextChangedListener(textWatcher);
    }

    private void calculatePayments(String costString, String numberOfPaymentsString, String paymentDateString) {
        if(!numberOfPaymentsString.equals("") && !paymentDateString.equals("") &&
                !costString.equals("") ){
            mPaymentsDetailsView.setText("");
            double numberOfPayments = Double.parseDouble(numberOfPaymentsString);
            double cost = Double.parseDouble(costString);
            mExpenseModel.calculateExpenses(cost, numberOfPayments, mLocalDate);
            mPaymentsDetailsView.setText("Payments details:");
            List<ExpenseEntry> expenses = mExpenseModel.getExpenses();
            for(int i = 0; i<expenses.size(); i++){
                mPaymentsDetailsView.append(Html.fromHtml("<br>" + (i+1) + " payment: "
                        + expenses.get(i).getCost() + ", payment date: " + expenses.get(i).getPaymentDate()));
            }
        } else {
            mPaymentsDetailsView.setText("");
        }
    }

    public void confirmExpense(View view){
        if(checkInputValidation()) {
            // New category is inserted
            if (mCategorySpinner.getSelectedItemPosition() == 0) {
                addExpenseWithNewCategory();
            }
            // Category picked from spinner
            else {
                addExpense();
            }
        } else {
            Toast.makeText(this, "Input not valid!", Toast.LENGTH_SHORT).show();
        }
    }


    public void cancelExpense(View view){
        finish();
    }

    private boolean checkInputValidation() {
        boolean isInputValid = true;
        // check category validation
        if(mCategorySpinner.getSelectedItemPosition() == 0 &&
                mCategory.getText().toString().trim().equalsIgnoreCase("")){
            mCategory.setError("Must enter new category or chose from the list");
            isInputValid = false;
        }
        // check cost validation
        if(mCost.getText().toString().trim().equals("")){
            mCost.setError("Must enter a cost");
            isInputValid = false;
        }
        // check number_of_payments validation
        if(mNumPayments.getText().toString().trim().equals("")){
            mNumPayments.setError("Must enter number of payments");
            isInputValid = false;
        }
        // check payment_date validation
        if(mPaymentDate.getText().toString().trim().equals("")){
            mPaymentDate.setError("Must enter a payment date");
            isInputValid = false;
        }
        return isInputValid;
    }

    private void setupCategoriesSpinner(List<CategoryEntry> categoryEntries) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add("Pick category from list");
        if(categoryEntries != null) {
            for (CategoryEntry categoryEntry : categoryEntries) {
                categoriesNames.add(categoryEntry.getName());
                Log.i(TAG, categoryEntry.getId() + "");
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categoriesNames);
            // attaching data adapter to spinner
            mCategorySpinner.setAdapter(dataAdapter);
        }
    }

    /**
     * add new expense
     */
    private void addExpense() {
        // get picked category id
        CategoryEntry categoryEntry = mViewModel.getCategories().getValue()
                .get(mCategorySpinner.getSelectedItemPosition()-1);
        int categoryId = categoryEntry.getId();
        // get expense number of payments
        int numOfPayments = Integer.parseInt(mNumPayments.getText().toString());
        // get expense cost
        double cost = Double.parseDouble(mCost.getText().toString());
        ExpenseEntry expenseEntry = new ExpenseEntry(categoryId, cost, numOfPayments,
                mLocalDate);
        mJobModel.addExpense(expenseEntry);
        finish();
    }

    private void addExpenseWithNewCategory() {
        // Initialize new Category
        String name = mCategory.getText().toString();
        CategoryEntry categoryEntry = new CategoryEntry(name, CategoryEntry.Type.EXPENSE);

        // Initialize new Expense
        int numOfPayments = Integer.parseInt(mNumPayments.getText().toString());
        double cost = Double.parseDouble(mCost.getText().toString());
        ExpenseEntry expenseEntry = new ExpenseEntry(cost, numOfPayments,
                mLocalDate);
        mJobModel.addExpenseWithNewCategory(InjectorUtils.provideRepository(this), categoryEntry,
                expenseEntry);
        finish();
    }

    private void pickDate(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateString = year + "-" + (month+1) + "-" + dayOfMonth;
                mLocalDate = LocalDate.parse(dateString);
                DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
                mLocalDate = new LocalDate(dateString);
                mPaymentDate.setText(fmt.print(mLocalDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
