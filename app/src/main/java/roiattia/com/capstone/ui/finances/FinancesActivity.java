package roiattia.com.capstone.ui.finances;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import roiattia.com.capstone.R;

public class FinancesActivity extends AppCompatActivity {

    public static final int OVERALL = 0;
    public static final int INCOME = 1;
    public static final int EXPENSES = 2;
    public static final String FRAGMENT_TO_OPEN = "fragment_to_open";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        Intent intent  = getIntent();
        int fragmentNumber = intent.getIntExtra(FRAGMENT_TO_OPEN, 0);

        setupViewpager(fragmentNumber);
    }

    private void setupViewpager(int fragmentNumber) {
        final OverallFragment overallFragment = new OverallFragment();
        final IncomeFragment incomeFragment = new IncomeFragment();
        final ExpensesFragment expensesFragment = new ExpensesFragment();
        ViewPager viewPager = findViewById(R.id.viewpager_fragments);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return overallFragment;
                    case 1:
                        return incomeFragment;
                    case 2:
                        return expensesFragment;
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
