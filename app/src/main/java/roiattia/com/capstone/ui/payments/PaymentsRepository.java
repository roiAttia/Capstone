package roiattia.com.capstone.ui.payments;

import android.arch.lifecycle.LiveData;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.dao.PaymentDao;
import roiattia.com.capstone.model.PaymentItemModel;

public class PaymentsRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PaymentsRepository sInstance;
    private final PaymentDao mPaymentDao;

    private PaymentsRepository(PaymentDao paymentDao){
        mPaymentDao = paymentDao;
    }

    public synchronized static PaymentsRepository getInstance(PaymentDao paymentDao) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PaymentsRepository(paymentDao);
            }
        }
        return sInstance;
    }

    public LiveData<List<PaymentItemModel>> loadPaymentsByCategoryIdAndDates(
            long categoryId, LocalDate from, LocalDate to){
        return mPaymentDao.getPaymentsInDateRange(from, to, categoryId);
    }

}
