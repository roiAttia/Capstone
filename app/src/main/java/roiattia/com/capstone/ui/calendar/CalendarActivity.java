package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.repositories.CategoriesRepository;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.ui.expenses_list.ExpensesListActivity;
import roiattia.com.capstone.ui.finances.FinancesActivity;
import roiattia.com.capstone.ui.expense.ExpenseActivity;
import roiattia.com.capstone.ui.job.JobActivity;
import roiattia.com.capstone.utils.DummyData;

import static roiattia.com.capstone.utils.Constants.JOB_DATE;
import static roiattia.com.capstone.utils.Constants.JOB_ID_UPDATE;

public class CalendarActivity extends AppCompatActivity
    implements CalendarJobsAdapter.OnJobClickHandler{

    private static final String TAG = CalendarActivity.class.getSimpleName();


    private CalendarJobsAdapter mJobsAdapter;
    private CalendarViewModel mViewModel;
    private LocalDate mSelectedDate;
    private InterstitialAd mInterstitialAd;

    @BindView(R.id.rv_job_list) RecyclerView mJobsListView;
    @BindView(R.id.calendarView) CalendarView mCalendarView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListView;

    /**
     * Handles the add job button click event
     */
    @OnClick(R.id.fab_add_job)
    public void addJob(){
        Intent intent = new Intent(CalendarActivity.this, roiattia.com.capstone.ui.job.JobActivity.class);
        intent.putExtra(JOB_DATE, mSelectedDate.toString());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        mSelectedDate = new LocalDate();

//        initializeAd();

        setupRecyclerView();

        setupViewModel();

        setupCalendarView();

        loadJobsAtDate(mSelectedDate);

    }

    /**
     * Setup calendar_view with date change listener
     */
    private void setupCalendarView() {
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month+1) + "-" + dayOfMonth;
                Log.i(TAG, date);
                LocalDate localDate = LocalDate.parse(date);
                mSelectedDate = localDate;
                loadJobsAtDate(localDate);
            }
        });
    }

    /**
     * Load jobs at selected date
     * @param date selected date
     */
    private void loadJobsAtDate(LocalDate date){
        mViewModel.loadJobsByDate(date);
    }

    /**
     * Setup recycler_view and Jobs_adapter
     */
    private void setupRecyclerView() {
        // initialize jobs_list and it's adapter
        mJobsAdapter = new CalendarJobsAdapter(this, this);
        mJobsListView.setAdapter(mJobsAdapter);
        mJobsListView.setLayoutManager(new LinearLayoutManager(this));
        mJobsListView.setHasFixedSize(true);
    }

    /**
     * Setup ViewModel with observe to update jobs list when selecting date
     * with calendar_view
     */
    private void setupViewModel() {
        // initialize view_model
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        mViewModel.getMutableJobsList().observe(this, new Observer<List<JobCalendarModel>>() {
            @Override
            public void onChanged(@Nullable List<JobCalendarModel> jobCalendarModelList) {
                Log.i(TAG, "onChanged");
                if(jobCalendarModelList != null) {
                    // pass jobs list to adapter
                    mJobsAdapter.setJobs(jobCalendarModelList);
                    // check if there are jobs at the date, if so then show list
                    // else show "No jobs" text
                    if (jobCalendarModelList.size() > 0) {
                        mEmptyListView.setVisibility(View.INVISIBLE);
                    } else mEmptyListView.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.mi_finances:
                intent = new Intent(CalendarActivity.this, FinancesActivity.class);
                startActivity(intent);
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d(TAG, "The interstitial wasn't loaded yet.");
//                }
                break;
            case R.id.mi_expenses_list:
                intent = new Intent(this, ExpensesListActivity.class);
                startActivity(intent);
                break;
            case R.id.mi_new_expense:
                intent = new Intent(this, ExpenseActivity.class);
                startActivity(intent);
                break;
            case R.id.mi_new_job:
                intent = new Intent(this, JobActivity.class);
                startActivity(intent);
                break;
            case R.id.mi_settings:
                intent = new Intent(this, FinancesActivity.class);
                startActivity(intent);
                break;
            case R.id.mi_debug:
                mViewModel.debugPrint();
                break;
            case R.id.mi_insert_dummmy_categories:
                CategoriesRepository categoriesRepository = CategoriesRepository.getInstance(this);
                categoriesRepository.insertCategories(DummyData.getDummyCategories());
                break;
            case R.id.mi_insert_dummy_jobs:
                mViewModel.insertJobs(DummyData.getDummyJobs());
                break;
            case R.id.mi_insert_dummy_expenses:
                ExpensesRepository expensesRepository = ExpensesRepository.getInstance(this);
                expensesRepository.insertExpenses(DummyData.getDummyExpenses());
                break;
            case R.id.mi_delete_all_data:
                mViewModel.deleteAllData();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeAd() {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(CalendarActivity.this, FinancesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onJobClick(long jobId) {
        Intent intent = new Intent(CalendarActivity.this, roiattia.com.capstone.ui.job.JobActivity.class);
        intent.putExtra(JOB_ID_UPDATE, jobId);
        startActivity(intent);
    }

    //TODO: 1 - add time to each list item
    //TODO: 2 - decide on if to show profit in each list item - if so then maybe add dollar icon

}
