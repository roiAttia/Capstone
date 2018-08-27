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
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.ui.finances.FinancesActivity;
import roiattia.com.capstone.ui.newjob.NewJobActivity;

import static roiattia.com.capstone.ui.newjob.NewJobActivity.JOB_ID_UPDATE;

public class CalendarActivity extends AppCompatActivity
    implements CalendarJobsAdapter.OnJobClickHandler{

    private static final String TAG = CalendarActivity.class.getSimpleName();
    public static final String DATE = "date";

    private CalendarJobsAdapter mJobsAdapter;
    private CalendarViewModel mViewModel;
    private LocalDate mSelectedDate;

    @BindView(R.id.rv_job_list) RecyclerView mJobsListView;
    @BindView(R.id.calendarView) CalendarView mCalendarView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        // initialize view_model
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        // initialize jobs_list and it's adapter
        mJobsAdapter = new CalendarJobsAdapter(this, this);
        mJobsListView.setAdapter(mJobsAdapter);
        mJobsListView.setLayoutManager(new LinearLayoutManager(this));
        mJobsListView.setHasFixedSize(true);

        // initialize date and calendar_view on date listener
        mSelectedDate = new LocalDate();
        setupViewModel(mSelectedDate);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month+1) + "-" + dayOfMonth;
                Log.i(TAG, date);
                mSelectedDate = LocalDate.parse(date);
                setupViewModel(mSelectedDate);
            }
        });
    }

    /**
     * Handle date clicks on calendar to load data on that date
     * @param pickedDate the date picked in calendar
     */
    private void setupViewModel(LocalDate pickedDate) {
        Log.i(TAG, pickedDate.toString());
        mViewModel.getJobsAtDate(pickedDate).observe(CalendarActivity.this,
                new Observer<List<JobCalendarModel>>() {
                    @Override
                    public void onChanged(@Nullable List<JobCalendarModel> jobCalendarList) {
                        Log.i(TAG, "onChanged");
                        mJobsAdapter.setJobs(jobCalendarList);
                        if(jobCalendarList != null && jobCalendarList.size() > 0) {
                            Log.i(TAG, jobCalendarList.size() + "");
                            mEmptyListView.setVisibility(View.INVISIBLE);
                        } else mEmptyListView.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * Handles the add job button click event
     */
    public void addJob(View view){
        Intent intent = new Intent(CalendarActivity.this, NewJobActivity.class);
        intent.putExtra(DATE, mSelectedDate.toString());
        startActivity(intent);
    }

    public void debugPrint(View view){
        Intent intent = new Intent(CalendarActivity.this, FinancesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(long jobId) {
        Intent intent = new Intent(CalendarActivity.this, NewJobActivity.class);
        intent.putExtra(JOB_ID_UPDATE, jobId);
        startActivity(intent);
    }
}
