package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.utils.AppExecutors;

public class CalendarRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CalendarRepository sInstance;
    private final JobDao mJobDao;
    private final CategoryDao mCategoryDao;
    private final AppExecutors mExecutors;

    private CalendarRepository(JobDao jobDao, CategoryDao categoryDao, AppExecutors executors){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExecutors = executors;
    }

    public synchronized static CalendarRepository getInstance(JobDao jobDao, CategoryDao categoryDao,
                                                              AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CalendarRepository(jobDao, categoryDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<JobEntry>> getJobsByDate(final LocalDate localDate){
        return mJobDao.loadJobsAtDate(localDate);
    }

    public void extractCategoryName(final long categoryId) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String categoryName = mCategoryDao.getCategoryName(categoryId);
            }
        });
    }
}
