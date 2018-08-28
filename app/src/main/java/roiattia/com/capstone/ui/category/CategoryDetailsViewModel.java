package roiattia.com.capstone.ui.category;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.ExpenseEntry;

public class CategoryDetailsViewModel extends ViewModel {

    private LiveData<List<ExpenseEntry>> mExpenseDetails;
    private CategoryDetailsRepository mRepository;

    CategoryDetailsViewModel(CategoryDetailsRepository repository,
                             long categoryId, LocalDate startDate, LocalDate endDate) {
        mRepository = repository;
        mExpenseDetails = mRepository.loadExpensesByCategoryIdAndDates(
                categoryId, startDate, endDate);
    }

    public LiveData<List<ExpenseEntry>> getExpenseDetails(){
        return mExpenseDetails;
    }

    public void deleteExpense(ExpenseEntry expenseEntry) {
        mRepository.deleteExpense(expenseEntry);
    }
}
