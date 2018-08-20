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
import android.widget.DatePicker;
import android.widget.EditText;
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

public class JobExpensesFragment extends BaseFragment {

    @BindView(R.id.spinner_expense_category) Spinner mCategoriesSpinner;
    @BindView(R.id.et_expense_cost) EditText mCost;
    @BindView(R.id.et_num_of_payments) EditText mNumPayments;
    @BindView(R.id.et_payment_date) EditText mPaymentDateView;
    @BindView(R.id.tv_payments_details) TextView mPaymentsDetailsView;
    @BindView(R.id.et_expense_category) EditText mCategory;

    private NewJobViewModel mViewModel;
    private LocalDate mPaymentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_job_expenses, container, false);
        ButterKnife.bind(this, rootView);

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(mListener, CategoryEntry.Type.EXPENSE);
        mViewModel = ViewModelProviders.of(mListener, factory)
                .get(NewJobViewModel.class);
        mViewModel.getCategories(CategoryEntry.Type.EXPENSE).observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null) {
                    JobExpensesFragment.super.setupCategoriesSpinner(
                            mCategoriesSpinner, categoryEntries);
                }
            }
        });

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
                        !mCost.getText().toString().equals("") ) {
                    int cost = Integer.parseInt(mCost.getText().toString());
                    int numberOfPayments = Integer.parseInt(mNumPayments.getText().toString());
                    if (numberOfPayments > 0) {
                        mViewModel.calculatePayments(cost, numberOfPayments, mPaymentDate);
                        updateUiWithPayments();
                    }
                }
            }
        };

        mCost.addTextChangedListener(textWatcher);
        mNumPayments.addTextChangedListener(textWatcher);
        mPaymentDateView.addTextChangedListener(textWatcher);

        return rootView;
    }

    private void updateUiWithPayments() {
        List<ExpenseEntry> expenseEntries = mViewModel.getExpensesList();
        for(int i = 0; i<expenseEntries.size(); i++){
            mPaymentsDetailsView.append(Html.fromHtml("<br>" + (i+1) + " payment: "
                    + expenseEntries.get(i).getCost() +
                    ", payment date: " + expenseEntries.get(i).getPaymentDate()));
        }
    }

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

    public void checkCategory() {
        String categoryName = mCategory.getText().toString();
        mViewModel.insertNewCategory(categoryName, CategoryEntry.Type.EXPENSE);
    }

    public Boolean checkInputValidation() {
        boolean isInputValid = true;
        // check category validation
        if(mCategoriesSpinner.getSelectedItemPosition() == 0 &&
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
        if(mPaymentDateView.getText().toString().trim().equals("")){
            mPaymentDateView.setError("Must enter a payment date");
            isInputValid = false;
        }
        return isInputValid;
    }


}
