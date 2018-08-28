package roiattia.com.capstone.ui.category;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.utils.AppExecutors;

public class CategoryDetailsRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CategoryDetailsRepository sInstance;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;

    private CategoryDetailsRepository(ExpenseDao expenseDao, AppExecutors appExecutors){
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static CategoryDetailsRepository getInstance(ExpenseDao expenseDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CategoryDetailsRepository(expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<ExpenseEntry>> loadExpensesByCategoryIdAndDates(
            long categoryId, LocalDate from, LocalDate to){
        return mExpenseDao.loadExpensesByCategoryIdAndDates(categoryId, from, to);
    }

    public void deleteExpense(final ExpenseEntry expenseEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mExpenseDao.deleteExpense(expenseEntry);
            }
        });
    }
}
