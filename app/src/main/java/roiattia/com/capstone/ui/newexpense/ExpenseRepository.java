package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import roiattia.com.capstone.database.CategoryDao;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.utils.AppExecutors;

public class ExpenseRepository {
    private static final String TAG = ExpenseRepository.class.getSimpleName();

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
        void onCategoryInserted(Long categoryId);
        void onExpensesInserted(long[] expensesId);
    }

    public void updateExpense(final ExpenseEntry expenseEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "expenseId: " + expenseEntry.getExpenseId());
                int rows = mExpenseDao.updateExpense(expenseEntry);
                Log.i(TAG, "rows: " + rows);
            }
        });
    }

    public void setCallbackListener(GetIdHandler getIdCallback){
        mGetIdCallback = getIdCallback;
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mCategoryDao.loadCategories(type);
    }

    public LiveData<ExpenseEntry> loadExpenseById(long expensesId) {
        return mExpenseDao.loadExpenseById(expensesId);
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

    public void insertExpenses(final List<ExpenseEntry> expenseEntries) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long[] expensesIds = mExpenseDao.insertExpenses(expenseEntries);
                mGetIdCallback.onExpensesInserted(expensesIds);
                for(Long id : expensesIds)
                    Log.i(TAG, String.valueOf(id));
            }
        });
    }
}
