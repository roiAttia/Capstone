package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;

import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.utils.AppExecutors;

public class ExpenseRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ExpenseRepository sInstance;
    private final CategoryDao mCategoryDao;
    private final ExpenseDao mExpenseDao;
    private final AppExecutors mExecutors;

    private ExpenseRepository(CategoryDao categoryDao, ExpenseDao expenseDao,
                          AppExecutors appExecutors){
        mCategoryDao = categoryDao;
        mExpenseDao = expenseDao;
        mExecutors = appExecutors;
    }

    public synchronized static ExpenseRepository getInstance(
            CategoryDao categoryDao, ExpenseDao expenseDao,
            AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ExpenseRepository(categoryDao, expenseDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mCategoryDao.loadCategories(type);
    }

    public LiveData<ExpenseEntry> loadExpenseById(long expensesId) {
        return mExpenseDao.loadExpense(expensesId);
    }

    public void insertCategory(CategoryEntry categoryEntry){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //TODO: finish
            }
        });
    }

    public void insertExpense(ExpenseEntry expenseEntry){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //TODO: finish
            }
        });
    }
}
