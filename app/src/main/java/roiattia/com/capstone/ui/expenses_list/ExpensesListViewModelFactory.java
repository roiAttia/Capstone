package roiattia.com.capstone.ui.expenses_list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public class ExpensesListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private LocalDate mStartDate;
    private LocalDate mEndDate;
    private ExpensesListRepository mRepository;

    public ExpensesListViewModelFactory(LocalDate startDate, LocalDate endDate,
                                        ExpensesListRepository repository) {
        mStartDate = startDate;
        mEndDate = endDate;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExpensesListViewModel(mRepository,mStartDate, mEndDate);
    }
}
