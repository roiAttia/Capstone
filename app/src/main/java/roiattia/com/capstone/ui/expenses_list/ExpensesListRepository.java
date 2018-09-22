package roiattia.com.capstone.ui.expenses_list;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.dao.ExpenseDao;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.AppExecutors;

public class ExpensesListRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ExpensesListRepository sInstance;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;

    private ExpensesListRepository(ExpenseDao expenseDao, AppExecutors appExecutors){
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static ExpensesListRepository getInstance(
            ExpenseDao expenseDao, AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ExpensesListRepository(expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<ExpenseEntry>> getExpensesBetweenDates(LocalDate from, LocalDate to){
        return mExpenseDao.loadExpensesBetweenDates(to);
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
