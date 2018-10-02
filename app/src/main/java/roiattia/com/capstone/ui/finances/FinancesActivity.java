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

import butterknife.OnClick;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.ui.dialogs.RadioButtonsDialog;
import roiattia.com.capstone.ui.job.JobActivity;
import roiattia.com.capstone.ui.payments.PaymentsActivity;
import roiattia.com.capstone.ui.expense.ExpenseActivity;
import roiattia.com.capstone.utils.DateUtils;

import static roiattia.com.capstone.utils.Constants.BUNDLE_CATEGORY_DATA;
import static roiattia.com.capstone.utils.Constants.BUNDLE_CATEGORY_ID;
import static roiattia.com.capstone.utils.Constants.BUNDLE_CATEGORY_NAME;
import static roiattia.com.capstone.utils.Constants.BUNDLE_END_DATE;
import static roiattia.com.capstone.utils.Constants.BUNDLE_START_DATE;
import static roiattia.com.capstone.utils.Constants.FRAGMENT_TO_OPEN;
import static roiattia.com.capstone.utils.Constants.OVERALL;

public class FinancesActivity extends AppCompatActivity
    implements RadioButtonsDialog.NoticeDialogListener, ExpensesAdapter.OnExpenseClickHandler{

    private static final String TAG = FinancesActivity.class.getSimpleName();

    private LocalDate mStartDate = new LocalDate();
    private LocalDate mEndDate = new LocalDate();
    private OverallFragment mOverallFragment;
    private IncomeFragment mIncomeFragment;
    private FinanceExpensesFragment mExpensesFragment;
    private FinancesViewModel mViewModel;
    private DateModel mDateModel;


    @OnClick(R.id.fab_add_expense)
    public void addExpense(){
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_add_job)
    public void addNewJob(){
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        mDateModel = DateModel.getInstance();

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

    }


    @Override
    public void onExpenseClick(long categoryId, String categoryName) {
        Bundle categoryDataBundle = new Bundle();
        categoryDataBundle.putString(BUNDLE_CATEGORY_NAME, categoryName);
        categoryDataBundle.putLong(BUNDLE_CATEGORY_ID, categoryId);
        categoryDataBundle.putString(BUNDLE_START_DATE, mDateModel.getCurrentFromDate().toString());
        categoryDataBundle.putString(BUNDLE_END_DATE, mDateModel.getExpectedToDate().toString());
        Intent intent = new Intent(this, PaymentsActivity.class);
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

    @Override
    public void onDialogFinishClick(int whichSelected) {
        mDateModel.setDates(whichSelected);
        mViewModel.updateDates();
    }
}
