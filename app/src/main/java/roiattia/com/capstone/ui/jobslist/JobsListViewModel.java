package roiattia.com.capstone.ui.jobslist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.repositories.JobsRepository;
import roiattia.com.capstone.repositories.PaymentsRepository;

public class JobsListViewModel extends AndroidViewModel {

    private JobsRepository mJobsRepository;
    private AppExecutors mExecutors;
    private MutableLiveData<List<JobEntry>> mJobsMutableLiveData;

    public JobsListViewModel(@NonNull Application application) {
        super(application);
        mJobsRepository = JobsRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mJobsMutableLiveData = new MutableLiveData<>();
    }


    public void setJobsList(
            final long categoryId, final LocalDate from, final LocalDate to) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<JobEntry> payments =
                        mJobsRepository.getJobsByCategoryBetweenDates(categoryId, from, to);
                mJobsMutableLiveData.postValue(payments);
            }
        });
    }

    public MutableLiveData<List<JobEntry>> getJobsMutableLiveData() {
        return mJobsMutableLiveData;
    }
}
