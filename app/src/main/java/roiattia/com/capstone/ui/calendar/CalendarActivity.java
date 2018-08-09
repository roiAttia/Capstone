package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.model.CalendarViewModel;

public class CalendarActivity extends AppCompatActivity {

    private CalendarJobsAdapter mJobsAdapter;
    @BindView(R.id.rv_job_list) RecyclerView mJobsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        mJobsAdapter = new CalendarJobsAdapter();
        mJobsListView.setAdapter(mJobsAdapter);
        mJobsListView.setLayoutManager(new LinearLayoutManager(this));
        mJobsListView.setHasFixedSize(true);

        setupViewModel();
    }

    private void setupViewModel() {
        CalendarViewModel viewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        viewModel.getJobs().observe(this, new Observer<List<JobEntry>>() {
            @Override
            public void onChanged(@Nullable List<JobEntry> jobEntries) {
                mJobsAdapter.setJobs(jobEntries);
            }
        });
    }

    public void addJob(View view){

    }
}
