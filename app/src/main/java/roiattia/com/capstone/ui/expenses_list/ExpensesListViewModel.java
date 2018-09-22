package roiattia.com.capstone.ui.expenses_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.entry.ExpenseEntry;

public class ExpensesListViewModel extends ViewModel {

    private LiveData<List<ExpenseEntry>> mExpenseDetails;
    private ExpensesListRepository mRepository;

    ExpensesListViewModel(ExpensesListRepository repository, LocalDate from,
                          LocalDate to) {
        mRepository = repository;
        mExpenseDetails = mRepository.getExpensesBetweenDates(from, to);
    }

    public LiveData<List<ExpenseEntry>> getExpenseDetails(){
        return mExpenseDetails;
    }

    public void deleteExpense(ExpenseEntry expenseEntry) {
        mRepository.deleteExpense(expenseEntry);
    }
}
