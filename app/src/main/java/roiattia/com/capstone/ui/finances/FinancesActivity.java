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
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.ui.newexpense.ExpenseActivity;
import roiattia.com.capstone.utils.InjectorUtils;

public class FinancesActivity extends AppCompatActivity
    implements PickPeriodDialog.NoticeDialogListener{

    private static final String TAG = FinancesActivity.class.getSimpleName();

    public static final int OVERALL = 0;
    public static final int INCOME = 1;
    public static final int EXPENSES = 2;
    public static final String FRAGMENT_TO_OPEN = "fragment_to_open";

    private LocalDate mStartDate = new LocalDate();
    private LocalDate mEndDate = new LocalDate();
    private OverallFragment mOverallFragment;
    private IncomeFragment mIncomeFragment;
    private FinanceExpensesFragment mExpensesFragment;
    private FinancesViewModel mViewModel;

//    @BindView(R.id.fragment_placeholder) LinearLayout mFragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        Intent intent  = getIntent();
        int fragmentNumber = intent.getIntExtra(FRAGMENT_TO_OPEN, 0);

        // setup view_model
        FinancesViewModelFactory factory = InjectorUtils
                .provideFinancesViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(FinancesViewModel.class);

        mOverallFragment = new OverallFragment();
        mIncomeFragment = new IncomeFragment();
        mExpensesFragment = new FinanceExpensesFragment();

        setupViewpager(fragmentNumber);
        loadIncomeReport();
        loadExpensesReport();
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
                mStartDate = mStartDate.withDayOfWeek(1);
                mEndDate = mEndDate.withDayOfWeek(7);
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
        loadIncomeReport();
        loadExpensesReport();
    }

    public void loadIncomeReport(){
        mViewModel.getIncomeReport(CategoryEntry.Type.JOB).
                observe(this, new Observer<List<IncomeModel>>() {
                    @Override
                    public void onChanged(@Nullable List<IncomeModel> jobJoinCategoryModels) {
                        if(jobJoinCategoryModels != null) {
                            mIncomeFragment.setData(jobJoinCategoryModels);
                            for (IncomeModel jobCategory : jobJoinCategoryModels) {
                                Log.i(TAG, "INCOME** category name: " + jobCategory.getName());
                                Log.i(TAG, "count: " + jobCategory.getCount());
                                Log.i(TAG, "income: " + jobCategory.getIncome());
                                Log.i(TAG, "profits: " + jobCategory.getProfit());
                            }
                        }
                    }
                });
    }

    public void loadExpensesReport(){
        mViewModel.getExpensesReport(CategoryEntry.Type.EXPENSE)
                .observe(this, new Observer<List<ExpensesModel>>() {
                    @Override
                    public void onChanged(@Nullable List<ExpensesModel> expensesModels) {
                        if(expensesModels != null){
                            mExpensesFragment.setData(expensesModels);
                            for (ExpensesModel expensesModel : expensesModels) {
                                Log.i(TAG, "EXPENSE** category name: " + expensesModel.getName());
                                Log.i(TAG, "count: " + expensesModel.getCount());
                                Log.i(TAG, "cost: " + expensesModel.getCost());
                            }
                        }
                    }
                });
    }

//    public void loadExpensesByCategory(){
//        mViewModel.getExpensesByCategory(0)
//                .observe(this, new Observer<List<ExpenseEntry>>() {
//                    @Override
//                    public void onChanged(@Nullable List<ExpenseEntry> expensesModels) {
//                        if(expensesModels != null){
//                            mExpensesFragment.setData(expensesModels);
//                            for (ExpensesModel expensesModel : expensesModels) {
//                                Log.i(TAG, "EXPENSE** category name: " + expensesModel.getName());
//                                Log.i(TAG, "count: " + expensesModel.getCount());
//                                Log.i(TAG, "cost: " + expensesModel.getCost());
//                            }
//                        }
//                    }
//                });
//    }

    public void addExpense(View view){
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

}
