package roiattia.com.capstone.ui.payments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.repositories.PaymentsRepository;

public class PaymentsViewModel extends AndroidViewModel {

    private PaymentsRepository mPaymentsRepository;
    private AppExecutors mExecutors;
    private MutableLiveData<List<PaymentItemModel>> mItemModelMutableLiveData;

    public PaymentsViewModel(@NonNull Application application) {
        super(application);
        mPaymentsRepository = PaymentsRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mItemModelMutableLiveData = new MutableLiveData<>();
    }


    public void setPaymentsList(
            final long categoryId, final LocalDate from, final LocalDate to) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<PaymentItemModel> payments =
                        mPaymentsRepository.getPaymentsByCategoryBetweenDates(categoryId, from, to);
                mItemModelMutableLiveData.postValue(payments);
            }
        });
    }

    public MutableLiveData<List<PaymentItemModel>> getItemModelMutableLiveData() {
        return mItemModelMutableLiveData;
    }
}
