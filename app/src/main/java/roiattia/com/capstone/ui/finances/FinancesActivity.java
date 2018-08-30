package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.ui.category.CategoryDetailsActivity;
import roiattia.com.capstone.ui.newexpense.ExpenseActivity;
import roiattia.com.capstone.ui.newjob.JobActivity;
import roiattia.com.capstone.utils.DateUtils;

public class FinancesActivity extends AppCompatActivity
    implements PickPeriodDialog.NoticeDialogListener, ExpensesAdapter.OnExpenseClickHandler{

    private static final String TAG = FinancesActivity.class.getSimpleName();

    public static final int OVERALL = 0;
    public static final int INCOME = 1;
    public static final int EXPENSES = 2;
    public static final String FRAGMENT_TO_OPEN = "fragment_to_open";

    public static final String BUNDLE_CATEGORY_DATA = "bundle_data";
    public static final String BUNDLE_CATEGORY_NAME = "category_name";
    public static final String BUNDLE_CATEGORY_ID = "category_id";
    public static final String BUNDLE_START_DATE = "start_date";
    public static final String BUNDLE_END_DATE = "end_date";

    private LocalDate mStartDate = new LocalDate();
    private LocalDate mEndDate = new LocalDate();
    private OverallFragment mOverallFragment;
    private IncomeFragment mIncomeFragment;
    private FinanceExpensesFragment mExpensesFragment;
    private FinancesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        int fragmentNumber = OVERALL;
        Intent intent  = getIntent();
        if(intent != null && intent.hasExtra(FRAGMENT_TO_OPEN)) {
            fragmentNumber = intent.getIntExtra(FRAGMENT_TO_OPEN, 0);
        }

        mOverallFragment = new OverallFragment();
        mIncomeFragment = new IncomeFragment();
        mExpensesFragment = new FinanceExpensesFragment();
        setupViewpager(fragmentNumber);

        // initialize start and end dates
        mStartDate = DateUtils.getStartDateOfTheMonth();
        mEndDate = DateUtils.getEndDateOfTheMonth();

        // setup view_model
        mViewModel = ViewModelProviders.of(this).get(FinancesViewModel.class);
        mViewModel.setDatesRange(mStartDate, mEndDate);

        loadIncomeReport();
        loadPaymentsReport();
        loadOverallReport();
    }

    public void selectPeriod(View view){
        PickPeriodDialog pickPeriodDialog = new PickPeriodDialog();
        pickPeriodDialog.show(getSupportFragmentManager(), "pop");
    }

    @Override
    public void onDialogFinishClick(int whichSelected) {
        updatePeriodicSelection(whichSelected);
    }

    private void updatePeriodicSelection(int whichSelected) {
        mStartDate = LocalDate.now();
        mEndDate = LocalDate.now();
        switch (whichSelected){
            case 0: // week
                mStartDate = DateUtils.getStartDateOfTheWeek();
                mEndDate = DateUtils.getEndDateOfTheWeek();
                break;
            case 1: // month
                mStartDate = mStartDate.plusMonths(0).withDayOfMonth(1);
                mEndDate = mEndDate.plusMonths(1).withDayOfMonth(1);
                break;
            case 2: // year
                mStartDate = mStartDate.plusYears(0).plusMonths(0).withDayOfYear(1);
                mEndDate = mEndDate.plusYears(1).plusMonths(0).withDayOfYear(1);
                break;
        }
        Log.i(TAG, "start date: " + mStartDate + ", end date: " + mEndDate);
        mViewModel.setDatesRange(mStartDate, mEndDate);

        loadOverallReport();
        loadIncomeReport();
//        loadExpensesReport();
        loadPaymentsReport();
    }

    private void loadOverallReport() {
        mOverallFragment.updatePeriodText(mStartDate, mEndDate);
        mViewModel.getCurrentOverallReport()
                .observe(this, new Observer<OverallIncomeModel>() {
                    @Override
                    public void onChanged(@Nullable OverallIncomeModel financialModel) {
                        if(financialModel != null) {
                            mOverallFragment.setCurrentData(financialModel);
                        }
                    }
                });
        mViewModel.getExpectedOverallReport()
                .observe(this, new Observer<OverallIncomeModel>() {
                    @Override
                    public void onChanged(@Nullable OverallIncomeModel financialModel) {
                        if(financialModel != null) {
                            mOverallFragment.setExpectedData(financialModel);
                        }
                    }
                });
        mViewModel.getCurrentExpensesReport()
                .observe(this, new Observer<OverallExpensesModel>() {
                    @Override
                    public void onChanged(@Nullable OverallExpensesModel expensesModel) {
                        if(expensesModel != null) {
                            mOverallFragment.setCurrentExpensesData(expensesModel);
                        }
                    }
                });
        mViewModel.getExpectedExpensesReport()
                .observe(this, new Observer<OverallExpensesModel>() {
                    @Override
                    public void onChanged(@Nullable OverallExpensesModel expensesModel) {
                        if(expensesModel != null) {
                            mOverallFragment.setExpectedExpensesData(expensesModel);
                        }
                    }
                });
    }

    private void loadIncomeReport(){
        mViewModel.getIncomeReport(CategoryEntry.Type.JOB)
                .observe(this, new Observer<List<IncomeModel>>() {
                    @Override
                    public void onChanged(@Nullable List<IncomeModel> jobJoinCategoryModels) {
                        if(jobJoinCategoryModels != null) {
                            mIncomeFragment.setData(jobJoinCategoryModels);
                        }
                    }
                });
    }

    private void loadExpensesReport(){
        mViewModel.getExpensesReport(CategoryEntry.Type.EXPENSE)
                .observe(this, new Observer<List<ExpensesModel>>() {
                    @Override
                    public void onChanged(@Nullable List<ExpensesModel> expensesModels) {
                        if(expensesModels != null) {
                            mExpensesFragment.setData(expensesModels);
                        }
                    }
                });
    }

    private void loadPaymentsReport(){
        mViewModel.getPaymentsReport()
                .observe(this, new Observer<List<ExpensesModel>>() {
                    @Override
                    public void onChanged(@Nullable List<ExpensesModel> expensesModels) {
                        if(expensesModels != null) {
                            mExpensesFragment.setData(expensesModels);
                        }
                    }
                });
    }

    public void addExpense(View view){
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

    public void addNewJob(View view){
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);
    }

    @Override
    public void onExpenseClick(long categoryId, String categoryName) {
        Bundle categoryDataBundle = new Bundle();
        categoryDataBundle.putString(BUNDLE_CATEGORY_NAME, categoryName);
        categoryDataBundle.putLong(BUNDLE_CATEGORY_ID, categoryId);
        categoryDataBundle.putString(BUNDLE_START_DATE, mStartDate.toString());
        categoryDataBundle.putString(BUNDLE_END_DATE, mEndDate.toString());
        Intent intent = new Intent(this, CategoryDetailsActivity.class);
        intent.putExtra(BUNDLE_CATEGORY_DATA, categoryDataBundle);
        startActivity(intent);
    }

    private void setupViewpager(int fragmentNumber) {
        ViewPager viewPager = findViewById(R.id.viewpager_fragments);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return mOverallFragment;
                    case 1:
                        return mIncomeFragment;
                    case 2:
                        return mExpensesFragment;
                }
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return getString(R.string.overall_fragment_title);
                    case 1:
                        return getString(R.string.income_fragment_title);
                    case 2:
                        return getString(R.string.expenses_fragment_title);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(fragmentNumber);
    }
}
