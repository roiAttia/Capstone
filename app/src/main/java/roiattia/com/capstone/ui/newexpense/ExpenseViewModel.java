package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;

public class ExpenseViewModel extends ViewModel {

    private static final String TAG = ExpenseViewModel.class.getSimpleName();

    private LiveData<List<CategoryEntry>> mCategories;
    private LiveData<ExpenseEntry> mExpense;
    private ExpenseRepository mRepository;

    ExpenseViewModel(ExpenseRepository repository, Long expensesId) {
        mRepository = repository;
        mCategories = mRepository.getCategories(CategoryEntry.Type.EXPENSE);
        if(expensesId != null) {
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
        mRepository.insertCategory(categoryEntry);
    }

    public void insertNewExpense(ExpenseEntry expenseEntry) {
        int paymentsNumber = expenseEntry.getNumberOfPayments();
        List<ExpenseEntry> expenseEntries = new ArrayList<>();
        if(paymentsNumber > 1){
            double monthlyCost = expenseEntry.getExpenseCost()/paymentsNumber;
            LocalDate paymentDate = expenseEntry.getExpensePaymentDate();
            for(int i = 0 ; i<paymentsNumber; i++) {
                LocalDate localDate = paymentDate.plusMonths(i);
                expenseEntry.setExpensePaymentDate(localDate);
                expenseEntry.setNumberOfPayments(1);
                expenseEntry.setExpenseCost(monthlyCost);
                expenseEntries.add(expenseEntry);
            }
        } else {
            expenseEntries.add(expenseEntry);
        }
        mRepository.insertExpenses(expenseEntries);
    }

    public void insertNewExpense(Long categoryId, int cost, int numberOfPayments, LocalDate paymentDate) {
        ExpenseEntry expenseEntry = new ExpenseEntry(categoryId, cost, numberOfPayments, paymentDate);
        insertNewExpense(expenseEntry);
    }

    public void updateExpense(Long expenseId, Long categoryId,
                              int cost, int numberOfPayments, LocalDate paymentDate) {
        ExpenseEntry expenseEntry = new ExpenseEntry(expenseId, categoryId, cost, numberOfPayments, paymentDate);
        mRepository.updateExpense(expenseEntry);

    }
}
