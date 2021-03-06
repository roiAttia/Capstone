package roiattia.com.capstone.repositories;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.model.ExpandableListChild;
import roiattia.com.capstone.model.ExpenseListModel;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;

public class ExpensesRepository {

    private static final String TAG = ExpensesRepository.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ExpensesRepository sInstance;
    private AppDatabase mDb;
    private AppExecutors mExecutors;

    private ExpensesRepository(Context context){
        mDb = AppDatabase.getsInstance(context);
        mExecutors = AppExecutors.getInstance();
    }

    public synchronized static ExpensesRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ExpensesRepository(context);
            }
        }
        return sInstance;
    }

    public void deleteExpense(final long expenseEntry) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.expenseDao().deleteExpense(expenseEntry);
            }
        });
    }

    public OverallExpensesModel getExpensesBetweenDates(LocalDate start, LocalDate end) {
        return mDb.expenseDao().loadExpensesBetweenDates(start, end);
    }

    public LiveData<List<ExpenseListModel>> getExpenses() {
        return mDb.expenseDao().loadExpenses();
    }

    public List<ExpandableListChild> getExpensesByIds(Long[] expensesIds) {
        return mDb.expenseDao().loadExpensesByIds(expensesIds);
    }

    public List<ExpensesModel> getExpensesModelBetweenDates(
            LocalDate currentFromDate, LocalDate expectedToDate) {
        return mDb.expenseDao().loadPaymentsPerCategoryBetweenDates(currentFromDate, expectedToDate);
    }

    public ExpandableListChild getExpenseChild(long expenseId) {
        return mDb.expenseDao().loadExpenseChildById(expenseId);
    }

    public interface OnExpenseListener{
        void onExpenseInserted(long expenseId);
    }

    public void insertExpenses(final List<ExpenseEntry> expenseEntries){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.expenseDao().insertExpenses(expenseEntries);
            }
        });
    }

    public void insertExpense(
            final ExpenseEntry expenseEntry, final OnExpenseListener listener){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long expenseId = mDb.expenseDao().insertExpense(expenseEntry);
                listener.onExpenseInserted(expenseId);
            }
        });
    }

    public ExpenseEntry getExpenseById(long expenseId) {
        return mDb.expenseDao().loadExpenseById(expenseId);
    }
}
