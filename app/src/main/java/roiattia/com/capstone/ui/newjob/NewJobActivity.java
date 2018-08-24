package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.joda.time.LocalDate;

import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.ui.calendar.CalendarActivity;
import roiattia.com.capstone.utils.InjectorUtils;

public class NewJobActivity extends AppCompatActivity {

    public static final String TAG = NewJobActivity.class.getSimpleName();

    public static final String JOB_DATE = "job_date";
    public static final String JOB_PAYMENT_DATE = "job_payment_date";

    private NewJobViewModel mViewModel;
    private FragmentManager mFragmentManager;
    private ExpenseFragment mJobExpensesFragment;
    private NewJobFragment mNewJobFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        ButterKnife.bind(this);

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideExpenseViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(NewJobViewModel.class);
        mViewModel.debugPrint();
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
            mJobExpensesFragment = new ExpenseFragment();
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
        mJobExpensesFragment = new ExpenseFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_placeholder, mNewJobFragment)
                .commit();
    }

    /**
     * Handles the "confirm expense" of job expense fragment's button click event
     */
    public void confirmExpense(View view){
        if(mJobExpensesFragment.checkInputValidation()) {
            mJobExpensesFragment.setExpenseDetails();
            mViewModel.updateExpenses();
            mViewModel.calculateProfit();
            // add new Expenses to view_model
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_placeholder, mNewJobFragment)
                    .commit();
            mJobExpensesFragment = new ExpenseFragment();
        }
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
            if(mNewJobFragment.checkInputValidation()) {
                mNewJobFragment.insertJobSequence();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
