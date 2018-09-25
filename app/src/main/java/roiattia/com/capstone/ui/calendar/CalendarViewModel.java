package roiattia.com.capstone.ui.calendar;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.repositories.JobsRepository;
import roiattia.com.capstone.utils.InjectorUtils;

public class CalendarViewModel extends AndroidViewModel {

    private static final String TAG = CalendarViewModel.class.getSimpleName();
    private JobsRepository mRepository;
    private AppExecutors mExecutors;
    private MutableLiveData<List<JobCalendarModel>> mMutableJobsList;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        mRepository = JobsRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mMutableJobsList = new MutableLiveData<>();
    }

    public MutableLiveData<List<JobCalendarModel>> getMutableJobsList() {
        return mMutableJobsList;
    }

    public void insertJob(JobEntry jobEntry){
        mRepository.insertJob(jobEntry);
    }

    public void loadJobsByDate(final LocalDate datePicked){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<JobCalendarModel> jobs = mRepository.getJobsByDate(datePicked);
                Log.i(TAG, "jobs list count: " + jobs.size());
                mMutableJobsList.postValue(jobs);
            }
        });
    }

    public void debugPrint() {
        mRepository.debugPrint();
    }

    public void deleteAllData(){
        mRepository.deleteAllData();
    }

    public void insertJobs(List<JobEntry> jobs) {
        mRepository.insertJobs(jobs);
    }
}
