package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.ui.newjob.JobRepository;

public class CalendarViewModel extends ViewModel {

    private CalendarRepository mRepository;

    CalendarViewModel(CalendarRepository repository) {
        mRepository = repository;
    }

    /**
     * Get jobs with category name on a specific date
     * @param datePicked date picked
     * @return a live_data contains a list of jobs
     */
    public LiveData<List<JobCalendarModel>> getJobsAtDate(LocalDate datePicked){
        return mRepository.getJobsByDate(datePicked);
    }

}
