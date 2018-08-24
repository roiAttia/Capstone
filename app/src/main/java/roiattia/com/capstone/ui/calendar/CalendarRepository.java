package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.utils.AppExecutors;

public class CalendarRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CalendarRepository sInstance;
    private final JobDao mJobDao;

    private CalendarRepository(JobDao jobDao){
        mJobDao = jobDao;
    }

    public synchronized static CalendarRepository getInstance(JobDao jobDao) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CalendarRepository(jobDao);
            }
        }
        return sInstance;
    }

    /**
     * Get jobs with category name on a specific date
     * @param datePicked date picked
     * @return a live_data contains a list of jobs
     */
    public LiveData<List<JobCalendarModel>> getJobsByDate(final LocalDate datePicked){
        return mJobDao.loadJobsAtDate(datePicked);
    }

}
