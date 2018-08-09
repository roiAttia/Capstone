package roiattia.com.capstone.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.JobEntry;

public class CalendarViewModel extends AndroidViewModel {

    private LiveData<List<JobEntry>> jobs;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        jobs = database.jobDao().loadAllJobs();
    }

    public LiveData<List<JobEntry>> getJobs(){
        return jobs;
    }
}
