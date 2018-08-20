package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.ui.newjob.JobRepository;

public class CalendarViewModel extends ViewModel {

    private CalendarRepository mRepository;

    CalendarViewModel(CalendarRepository repository) {
        mRepository = repository;
    }

    public LiveData<List<JobEntry>> getJobs(LocalDate localDate){
        return mRepository.getJobsByDate(localDate);
    }

}
