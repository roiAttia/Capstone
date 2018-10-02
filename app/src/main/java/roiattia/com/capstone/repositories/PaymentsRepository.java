package roiattia.com.capstone.repositories;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.PaymentEntry;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.PaymentItemModel;

public class PaymentsRepository {

    private static final String TAG = PaymentsRepository.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PaymentsRepository sInstance;
    private AppDatabase mDb;
    private AppExecutors mExecutors;

    private PaymentsRepository(Context context){
        mDb = AppDatabase.getsInstance(context);
        mExecutors = AppExecutors.getInstance();
    }

    public synchronized static PaymentsRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PaymentsRepository(context);
            }
        }
        return sInstance;
    }

    public void insertPayments(final List<PaymentEntry> payments){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.paymentDao().insertPayments(payments);
            }
        });
    }

    public OverallExpensesModel getPaymentsBetweenDates(LocalDate from, LocalDate to) {
        return mDb.paymentDao().getPaymentsBetweenDates(from, to);
    }

    public List<PaymentItemModel> getPaymentsByCategoryBetweenDates(long categoryId, LocalDate from, LocalDate to) {
        return mDb.paymentDao().loadPaymentByCategoryBetweenDates(from, to, categoryId);
    }
}
