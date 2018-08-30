package roiattia.com.capstone.ui.payments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.model.PaymentItemModel;

public class PaymentsViewModel extends ViewModel {

    private LiveData<List<PaymentItemModel>> mPaymentsDetails;
    private PaymentsRepository mRepository;

    PaymentsViewModel(PaymentsRepository repository,
                      long categoryId, LocalDate startDate, LocalDate endDate) {
        mRepository = repository;
        mPaymentsDetails = mRepository.loadPaymentsByCategoryIdAndDates(
                categoryId, startDate, endDate);
    }

    public LiveData<List<PaymentItemModel>> getPaymentsDetails() {
        return mPaymentsDetails;
    }

}
