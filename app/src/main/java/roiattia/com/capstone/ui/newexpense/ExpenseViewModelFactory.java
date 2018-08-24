package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ExpenseViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ExpenseRepository mRepository;
    private long mExpenseId;

    public ExpenseViewModelFactory(ExpenseRepository repository, long expenseId) {
        mRepository = repository;
        mExpenseId = expenseId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExpenseViewModel(mRepository, mExpenseId);
    }
}
