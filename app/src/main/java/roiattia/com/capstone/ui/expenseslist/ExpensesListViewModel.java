package roiattia.com.capstone.ui.expenseslist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.model.ExpenseListModel;
import roiattia.com.capstone.repositories.ExpensesRepository;

public class ExpensesListViewModel extends AndroidViewModel {

    private LiveData<List<ExpenseListModel>> mExpenseDetails;
    private ExpensesRepository mRepository;
    private AppExecutors mExecutors;

    public ExpensesListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ExpensesRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mExpenseDetails = mRepository.getExpenses();
    }

    public LiveData<List<ExpenseListModel>> getExpenses(){
        return mExpenseDetails;
    }

    public void deleteExpense(long expenseId) {
        mRepository.deleteExpense(expenseId);
    }
}
