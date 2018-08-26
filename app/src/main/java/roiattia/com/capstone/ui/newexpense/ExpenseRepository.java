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
    private GetIdHandler mGetIdCallback;

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

    /**
     * The interface that receives onClick messages.
     */
    public interface GetIdHandler {
        void onCategoryInserted(long categoryId);
    }

    public void setCallbackListener(GetIdHandler getIdCallback){
        mGetIdCallback = getIdCallback;
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mCategoryDao.loadCategories(type);
    }

    public LiveData<ExpenseEntry> loadExpenseById(long expensesId) {
        return mExpenseDao.loadExpense(expensesId);
    }

    public void insertCategory(final CategoryEntry categoryEntry){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long categoryId = mCategoryDao.insertCategory(categoryEntry);
                mGetIdCallback.onCategoryInserted(categoryId);
            }
        });
    }

    public void insertExpense(final ExpenseEntry expenseEntry){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mExpenseDao.insertExpense(expenseEntry);
            }
        });
    }
}
