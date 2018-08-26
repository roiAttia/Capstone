package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;

public class ExpenseViewModel extends ViewModel
    implements ExpenseRepository.GetIdHandler{

    private static final String TAG = ExpenseViewModel.class.getSimpleName();

    private LiveData<List<CategoryEntry>> mCategories;
    private LiveData<ExpenseEntry> mExpense;
    private ExpenseEntry mExpenseEntry;
    private ExpenseRepository mRepository;

    ExpenseViewModel(ExpenseRepository repository, long expensesId) {
        mRepository = repository;
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


    public void insertNewCategory(String newCategoryName) {
        CategoryEntry categoryEntry = new CategoryEntry(newCategoryName, CategoryEntry.Type.EXPENSE);
        mRepository.setCallbackListener(this);
        mRepository.insertCategory(categoryEntry);
    }

    public void insertNewExpense(ExpenseEntry expenseEntry) {
        int paymentsNumber = expenseEntry.getNumberOfPayments();
        if(paymentsNumber > 1){
            double monthlyCost = expenseEntry.getExpenseCost()/paymentsNumber;
            LocalDate paymentDate = expenseEntry.getExpensePaymentDate();
            for(int i = 0 ; i<paymentsNumber; i++) {
                LocalDate localDate = paymentDate.plusMonths(i);
                expenseEntry.setExpensePaymentDate(localDate);
                expenseEntry.setNumberOfPayments(1);
                expenseEntry.setExpenseCost(monthlyCost);
                mRepository.insertExpense(expenseEntry);
            }

        }
    }

    @Override
    public void onCategoryInserted(long categoryId) {
        mExpenseEntry.setCategoryId(categoryId);
        mRepository.insertExpense(mExpenseEntry);
    }

    public void setExpense(ExpenseEntry expense) {
        mExpenseEntry = expense;
    }
}
