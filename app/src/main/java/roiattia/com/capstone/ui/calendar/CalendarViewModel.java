package roiattia.com.capstone.ui.calendar;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.utils.InjectorUtils;

public class CalendarViewModel extends AndroidViewModel {

    private CalendarRepository mRepository;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        mRepository = InjectorUtils.provideCalendarRepository(this.getApplication());
    }

    /**
     * Get jobs with category name on a specific date
     * @param datePicked date picked
     * @return a live_data contains a list of jobs
     */
    public LiveData<List<JobCalendarModel>> getJobsAtDate(LocalDate datePicked){
        return mRepository.getJobsByDate(datePicked);
    }

    public void debugPrint() {
        mRepository.debugPrint();
    }
}
