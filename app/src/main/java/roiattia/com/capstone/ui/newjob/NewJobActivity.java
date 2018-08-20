package roiattia.com.capstone.ui.newjob;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import roiattia.com.capstone.database.Repository;
import roiattia.com.capstone.ui.calendar.CalendarActivity;
import roiattia.com.capstone.ui.expense.AddExpenseActivity;
import roiattia.com.capstone.ui.finances.ExpensesFragment;
import roiattia.com.capstone.utils.InjectorUtils;

public class NewJobActivity extends AppCompatActivity {

    public static final String TAG = NewJobActivity.class.getSimpleName();

    public static final String JOB_DATE = "job_date";
    public static final String JOB_PAYMENT_DATE = "job_payment_date";

    private NewJobViewModel mViewModel;
    private FragmentManager mFragmentManager;
    private JobExpensesFragment mJobExpensesFragment;
    private NewJobFragment mNewJobFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        ButterKnife.bind(this);

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(this, CategoryEntry.Type.JOB);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(NewJobViewModel.class);

        // get date selected for job from calendar activity
        Intent intent = getIntent();
        String dateAsString = intent.getStringExtra(CalendarActivity.DATE);
        LocalDate localDate = new LocalDate(dateAsString);
        // set the date as the date of the job via view model
        mViewModel.setJobDate(localDate);

        // create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mNewJobFragment = new NewJobFragment();
            mJobExpensesFragment = new JobExpensesFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fl_fragment_placeholder, mNewJobFragment)
                    .commit();
        }
    }

    /**
     * Handles the "add expense" of job details fragment's button click event
     */
    public void addExpense(View view){
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_placeholder, mJobExpensesFragment)
                .commit();
    }

    /**
     * Handles the "cancel expense" of job expense fragment's button click event
     */
    public void cancelExpense(View view){
        mJobExpensesFragment = new JobExpensesFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_placeholder, mNewJobFragment)
                .commit();
    }

    /**
     * Handles the "confirm expense" of job expense fragment's button click event
     */
    public void confirmExpense(View view){
        Boolean isValid = mJobExpensesFragment.checkInputValidation();
        if(isValid) {
            mViewModel.updateExpenses();
            mViewModel.calculateProfit();
            // add new Expenses to view_model
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_placeholder, mNewJobFragment)
                    .commit();
//            mJobExpensesFragment.checkCategory();
            mJobExpensesFragment = new JobExpensesFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.job_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_done){
            Boolean isValid = mNewJobFragment.checkInputValidation();
            if(isValid) {
                mNewJobFragment.checkCategory();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
