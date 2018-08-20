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
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.ui.newjob.NewJobActivity;
import roiattia.com.capstone.utils.InjectorUtils;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = CalendarActivity.class.getSimpleName();
    public static final String DATE = "date";

    private CalendarJobsAdapter mJobsAdapter;
    private CalendarViewModel mCalendarJobsViewModel;
    private LocalDate mLocalDate;

    @BindView(R.id.rv_job_list) RecyclerView mJobsListView;
    @BindView(R.id.calendarView) CalendarView mCalendarView;
    @BindView(R.id.tv_empty_list) TextView mEmptyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        CalendarViewModelFactory factory =
                InjectorUtils.provideCalendarViewModelFactory(this.getApplicationContext());
        mCalendarJobsViewModel = ViewModelProviders.of(this, factory).get(CalendarViewModel.class);

        mJobsAdapter = new CalendarJobsAdapter(this);
        mJobsListView.setAdapter(mJobsAdapter);
        mJobsListView.setLayoutManager(new LinearLayoutManager(this));
        mJobsListView.setHasFixedSize(true);

        mLocalDate = new LocalDate();
        setupViewModel(mLocalDate);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month+1) + "-" + dayOfMonth;
                mLocalDate = LocalDate.parse(date);
                setupViewModel(mLocalDate);
            }
        });
    }

    private void setupViewModel(LocalDate localDate) {
        mCalendarJobsViewModel.getJobs(localDate).observe(CalendarActivity.this,
                new Observer<List<JobEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<JobEntry> jobEntries) {
                        mJobsAdapter.setJobs(jobEntries);
                        if(jobEntries != null && jobEntries.size() > 0) {
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
        intent.putExtra(DATE, mLocalDate.toString());
        startActivity(intent);
    }

//    public void debugPrint(View view){
//        JobRepository repository = InjectorUtils.provideJobRepository(this);
//        repository.debugPrint();
//    }
}
