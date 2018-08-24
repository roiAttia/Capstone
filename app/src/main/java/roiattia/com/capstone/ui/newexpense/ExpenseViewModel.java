package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;

public class ExpenseViewModel extends ViewModel {

    private static final String TAG = ExpenseViewModel.class.getSimpleName();

    private LiveData<List<CategoryEntry>> mCategories;
    private LiveData<ExpenseEntry> mExpense;

    ExpenseViewModel(ExpenseRepository repository, long expensesId) {
        mCategories = repository.getCategories(CategoryEntry.Type.EXPENSE);
        if(expensesId != -1) {
            mExpense = repository.loadExpenseById(expensesId);
        }
    }

    /**
     * Handle categories retrieval from db
     * @return categories wrapped with live_data
     */
    public LiveData<List<CategoryEntry>> getCategories() {
        return mCategories;
    }

    public LiveData<ExpenseEntry> getExpense(){
        return mExpense;
    }



}
