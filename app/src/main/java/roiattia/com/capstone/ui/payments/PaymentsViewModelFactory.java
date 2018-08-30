package roiattia.com.capstone.ui.payments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public class PaymentsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private long mCategoryId;
    private LocalDate mStartDate;
    private LocalDate mEndDate;
    private PaymentsRepository mRepository;

    public PaymentsViewModelFactory(long categoryId, LocalDate startDate, LocalDate endDate,
                                    PaymentsRepository repository) {
        mCategoryId = categoryId;
        mStartDate = startDate;
        mEndDate = endDate;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PaymentsViewModel(mRepository, mCategoryId, mStartDate, mEndDate);
    }
}
