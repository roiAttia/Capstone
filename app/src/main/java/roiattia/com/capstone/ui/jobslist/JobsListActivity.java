package roiattia.com.capstone.ui.jobslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.utils.DateUtils;

import static roiattia.com.capstone.utils.Constants.BUNDLE_CATEGORY_DATA;
import static roiattia.com.capstone.utils.Constants.BUNDLE_CATEGORY_ID;
import static roiattia.com.capstone.utils.Constants.BUNDLE_CATEGORY_NAME;
import static roiattia.com.capstone.utils.Constants.BUNDLE_END_DATE;
import static roiattia.com.capstone.utils.Constants.BUNDLE_START_DATE;

public class JobsListActivity extends AppCompatActivity {

    private JobsListAdapter mAdapter;
    private JobsListViewModel mViewModel;

    @BindView(R.id.tv_header) TextView mCategoryHeader;
    @BindView(R.id.rv_jobs) RecyclerView mJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupViewModel();

        Intent intent = getIntent();
        // check for intent extra as bundle
        if(intent != null && intent.hasExtra(BUNDLE_CATEGORY_DATA)){
            loadDataFromIntent(intent);
        }
    }

    private void loadDataFromIntent(Intent intent) {
        // extract all data from bundle
        Bundle categoryDataBundle = intent.getBundleExtra(BUNDLE_CATEGORY_DATA);
        String categoryName = categoryDataBundle.getString(BUNDLE_CATEGORY_NAME);
        long categoryId = categoryDataBundle.getLong(BUNDLE_CATEGORY_ID);
        LocalDate startDate = new LocalDate(
                categoryDataBundle.getString(BUNDLE_START_DATE));
        LocalDate endDate = new LocalDate(
                categoryDataBundle.getString(BUNDLE_END_DATE));
        mViewModel.setJobsList(categoryId, startDate, endDate);

        // set head_line as follows: *category_name* transactions <br> *start_date*
        // - *end_date*. example: Fuel transactions <br> 20/08/2018 - 26/08/2018
        mCategoryHeader.setText(Html.fromHtml(categoryName + " " + getString(R.string.profits)
                + "<br>" + DateUtils.getDateStringFormat(startDate) + " - " +
                DateUtils.getDateStringFormat(endDate)));
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(JobsListViewModel.class);
        mViewModel.getJobsMutableLiveData().observe(this, new Observer<List<JobEntry>>() {
            @Override
            public void onChanged(@Nullable List<JobEntry> jobEntries) {
                if(jobEntries != null){
                    mAdapter.setData(jobEntries);
                }
            }
        });
    }

    private void setupRecyclerView() {
        mAdapter = new JobsListAdapter(this);
        mJobsList.setAdapter(mAdapter);
        mJobsList.setLayoutManager(new LinearLayoutManager(this));
        mJobsList.setHasFixedSize(true);
    }
}
