package roiattia.com.capstone.repositories;

import android.content.Context;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.PaymentEntry;

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
}
