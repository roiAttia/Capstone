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
import roiattia.com.capstone.ui.newjob.NewJobViewModel;
import roiattia.com.capstone.ui.newjob.NewJobViewModelFactory;
import roiattia.com.capstone.utils.InjectorUtils;

public class AddExpenseActivity extends AppCompatActivity {

    private static final String TAG = AddExpenseActivity.class.getSimpleName();

    @BindView(R.id.et_expense_category) EditText mCategory;
    @BindView(R.id.spinner_expense_category) Spinner mCategorySpinner;
    @BindView(R.id.et_expense_cost) EditText mCost;
    @BindView(R.id.et_num_of_payments) EditText mNumPayments;
    @BindView(R.id.et_payment_date) EditText mPaymentDate;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;

    private NewJobViewModel mViewModel;
    private LocalDate mLocalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        ButterKnife.bind(this);

    }

    private void calculatePayments(String costString, String numberOfPaymentsString, String paymentDateString) {
        if(!numberOfPaymentsString.equals("") && !paymentDateString.equals("") &&
                !costString.equals("") ){
            mPaymentsDetailsView.setText("");
            double numberOfPayments = Double.parseDouble(numberOfPaymentsString);
            double cost = Double.parseDouble(costString);
            mPaymentsDetailsView.setText("Payments details:");

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
        CategoryEntry categoryEntry = mViewModel.getCategories(CategoryEntry.Type.EXPENSE).getValue()
                .get(mCategorySpinner.getSelectedItemPosition()-1);
        int categoryId = categoryEntry.getId();
        // get expense number of payments
        int numOfPayments = Integer.parseInt(mNumPayments.getText().toString());
        // get expense cost
        double cost = Double.parseDouble(mCost.getText().toString());
        ExpenseEntry expenseEntry = new ExpenseEntry(categoryId, cost, numOfPayments,
                mLocalDate);
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

        finish();
    }

}
