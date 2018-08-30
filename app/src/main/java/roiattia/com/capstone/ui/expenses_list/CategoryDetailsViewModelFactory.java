package roiattia.com.capstone.ui.expenses_list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public class CategoryDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private long mCategoryId;
    private LocalDate mStartDate;
    private LocalDate mEndDate;
    private CategoryDetailsRepository mRepository;

    public CategoryDetailsViewModelFactory(long categoryId, LocalDate startDate, LocalDate endDate,
                                           CategoryDetailsRepository repository) {
        mCategoryId = categoryId;
        mStartDate = startDate;
        mEndDate = endDate;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoryDetailsViewModel(mRepository, mCategoryId, mStartDate, mEndDate);
    }
}
