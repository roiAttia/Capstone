package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.ui.calendar.CalendarActivity;
import roiattia.com.capstone.utils.InjectorUtils;

public class NewJobActivity extends AppCompatActivity
    implements ExpenseFragment.ConfirmExpenseHandler{

    public static final String TAG = NewJobActivity.class.getSimpleName();

    public static final String JOB_DATE = "job_date";
    public static final String JOB_PAYMENT_DATE = "job_payment_date";

    private NewJobViewModel mViewModel;
    private FragmentManager mFragmentManager;
    private ExpenseFragment mExpenseFragment;
    private JobDetailsFragment mJobDetailsFragment;
    private String choseCategorySpinnerOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        ButterKnife.bind(this);

        // create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mJobDetailsFragment = new JobDetailsFragment();
        }

        choseCategorySpinnerOption = getString(R.string.spinner_category_default_value);

        // setup view_model
        NewJobViewModelFactory factory = InjectorUtils
                .provideNewJobViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(NewJobViewModel.class);
        mViewModel.getJobCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null) {
                    mJobDetailsFragment.setCategories(categoryEntries, choseCategorySpinnerOption);
                    for (CategoryEntry categoryEntry : categoryEntries) {
                        Log.i(TAG, categoryEntry.toString());
                    }
                }
            }
        });
        mViewModel.debugPrint();

        // get date selected for job from calendar activity
        Intent intent = getIntent();
        String dateAsString = intent.getStringExtra(CalendarActivity.DATE);
        LocalDate localDate = new LocalDate(dateAsString);
        mJobDetailsFragment.setJobDate(localDate);
        // set the date as the date of the job via view model

        mFragmentManager.beginTransaction()
                .add(R.id.fl_fragment_placeholder, mJobDetailsFragment)
                .commit();
    }

    /**
     * Handles the "add expense" of job details fragment's button click event
     */
    public void addExpense(View view){
        mExpenseFragment = new ExpenseFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_placeholder, mExpenseFragment)
                .commit();

        mViewModel.getExpenseCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null) {
                    mExpenseFragment.setCategoriesData(categoryEntries);
                    for (CategoryEntry categoryEntry : categoryEntries) {
                        Log.i(TAG, categoryEntry.toString());
                    }
                }
            }
        });
    }

    /**
     * Handles the "cancel expense" of job expense fragment's button click event
     */
    public void cancelExpense(View view){
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_placeholder, mJobDetailsFragment)
                .commit();
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
            if(mJobDetailsFragment.checkInputValidation()) {
                mJobDetailsFragment.insertJobSequence();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfirmExpenseClick(ExpenseEntry expenseEntry, String newCategoryName) {
        Log.i(TAG, expenseEntry.toString() + "   " + newCategoryName);
        // Insert new category
        if(expenseEntry.getCategoryId() == null){
            mViewModel.setExpense(expenseEntry);
            mViewModel.insertNewCategory(newCategoryName, CategoryEntry.Type.EXPENSE);
        }
        // Existed category selected
        else{
            mViewModel.insertNewExpense(expenseEntry);
        }
        debugPrintExpenses();
        mExpenseFragment = new ExpenseFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_placeholder, mJobDetailsFragment)
                .commit();
    }

    private void debugPrintExpenses() {
        for(ExpenseEntry expenseEntry : mViewModel.getExpensesList()){
            Log.i(TAG, expenseEntry.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForExpenses();
    }

    private void checkForExpenses() {
        if(mViewModel.getExpensesList().size() > 0){
            Log.i(TAG, "expenses size: " + mViewModel.getExpensesList().size());
//            mJobDetailsFragment.updateUiWithExpenses(mViewModel.getExpensesList());
        }
    }
}
