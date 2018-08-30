package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.utils.AppExecutors;

public class FinancesRepository {
    private static final String TAG = FinancesRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static FinancesRepository sInstance;
    private final JobDao mJobDao;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;

    private FinancesRepository(JobDao jobDao, ExpenseDao expenseDao,
                          AppExecutors appExecutors){
        mJobDao = jobDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static FinancesRepository getInstance(
            JobDao jobDao, ExpenseDao expenseDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new FinancesRepository(jobDao, expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<OverallIncomeModel> getJobsBetweenDates(LocalDate from, LocalDate to){
        return mJobDao.loadJobsBetweenDates(from, to);
    }

    public LiveData<List<IncomeModel>> getIncomeBetweenDates(LocalDate from,
                                                             LocalDate to, CategoryEntry.Type type) {
        return mJobDao.loadIncomeBetweenDates(from, to, type);
    }

    public LiveData<List<ExpensesModel>> getExpensesBetweenDates(LocalDate from,
                                                                 LocalDate to, CategoryEntry.Type type) {
        return mExpenseDao.loadPaymentsBetweenDates(from, to, type);
    }

    public LiveData<List<ExpenseEntry>> getExpensesByCategoryId(long categoryId) {
        return mExpenseDao.loadAllExpenses();
    }

    public LiveData<OverallExpensesModel> getExpensesBetweenDates(LocalDate from, LocalDate to){
        return mExpenseDao.loadPaymentsBetweenDates(from, to);
    }

    public LiveData<List<ExpensesModel>> getPaymentsPerCategoryBetweenDates(
            LocalDate from, LocalDate to){
        return mExpenseDao.loadPaymentsPerCategoryBetweenDates(from, to);

    }

}
