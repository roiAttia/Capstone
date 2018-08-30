package roiattia.com.capstone.ui.expenses_list;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.ExpenseDao;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.PaymentDao;
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.utils.AppExecutors;

public class CategoryDetailsRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CategoryDetailsRepository sInstance;
    private final ExpenseDao mExpenseDao;
    private final PaymentDao mPaymentDao;
    private final AppExecutors mExecutors;

    private CategoryDetailsRepository(ExpenseDao expenseDao, PaymentDao paymentDao,
                                      AppExecutors appExecutors){
        mExpenseDao = expenseDao;
        mPaymentDao = paymentDao;
        mExecutors = appExecutors;
    }

    public synchronized static CategoryDetailsRepository getInstance(ExpenseDao expenseDao,
                                                                     PaymentDao paymentDao, AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CategoryDetailsRepository(expenseDao, paymentDao, appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<ExpenseEntry>> loadExpensesByCategoryIdAndDates(
            long categoryId, LocalDate from, LocalDate to){
        return mExpenseDao.loadExpensesByCategoryIdAndDates(categoryId, from, to);
    }

    public LiveData<List<PaymentItemModel>> loadPaymentsByCategoryIdAndDates(
            long categoryId, LocalDate from, LocalDate to){
        return mPaymentDao.getPaymentsInDateRange(from, to, categoryId);
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
