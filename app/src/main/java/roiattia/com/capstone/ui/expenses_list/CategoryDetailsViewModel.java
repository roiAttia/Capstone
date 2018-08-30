package roiattia.com.capstone.ui.expenses_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.model.PaymentItemModel;

public class CategoryDetailsViewModel extends ViewModel {

    private LiveData<List<ExpenseEntry>> mExpenseDetails;
    private LiveData<List<PaymentItemModel>> mPaymentsDetails;
    private CategoryDetailsRepository mRepository;

    CategoryDetailsViewModel(CategoryDetailsRepository repository,
                             long categoryId, LocalDate startDate, LocalDate endDate) {
        mRepository = repository;
        mExpenseDetails = mRepository.loadExpensesByCategoryIdAndDates(
                categoryId, startDate, endDate);
        mPaymentsDetails = mRepository.loadPaymentsByCategoryIdAndDates(
                categoryId, startDate, endDate);
    }

    public LiveData<List<ExpenseEntry>> getExpenseDetails(){
        return mExpenseDetails;
    }

    public LiveData<List<PaymentItemModel>> getPaymentsDetails() {
        return mPaymentsDetails;
    }

    public void deleteExpense(ExpenseEntry expenseEntry) {
        mRepository.deleteExpense(expenseEntry);
    }
}
