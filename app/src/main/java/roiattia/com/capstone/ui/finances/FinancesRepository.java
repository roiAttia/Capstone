package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.FinancialModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.utils.AppExecutors;

public class FinancesRepository {
    private static final String TAG = FinancesRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static FinancesRepository sInstance;
    private final JobDao mJobDao;
    private final CategoryDao mCategoryDao;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;

    private FinancesRepository(JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
                          AppExecutors appExecutors){
        mJobDao = jobDao;
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static FinancesRepository getInstance(
            JobDao jobDao, CategoryDao categoryDao, ExpenseDao expenseDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FinancesRepository(jobDao, categoryDao, expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<FinancialModel>> getJobsBetweenDates(LocalDate from, LocalDate to){
        return mJobDao.loadJobsBetweenDates(from, to);
    }

    public LiveData<List<IncomeModel>> getIncomeBetweenDates(LocalDate from,
                                                             LocalDate to, CategoryEntry.Type type) {
        return mJobDao.loadIncomeBetweenDates(from, to, type);
    }

    public LiveData<List<ExpensesModel>> getExpensesBetweenDates(LocalDate from,
                                                                 LocalDate to, CategoryEntry.Type type) {
        return mExpenseDao.loadExpensesBetweenDates(from, to, type);
    }

    public LiveData<List<ExpenseEntry>> getExpensesByCategoryId(long categoryId) {
        return mExpenseDao.loadAllExpenses();
    }
}
