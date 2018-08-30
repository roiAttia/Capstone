package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.PaymentEntry;

public class ExpenseViewModel extends ViewModel {

    private static final String TAG = ExpenseViewModel.class.getSimpleName();

    private LiveData<List<CategoryEntry>> mCategories;
    private LiveData<ExpenseEntry> mExpense;
    private List<PaymentEntry> mPaymentEntryList;
    private ExpenseRepository mRepository;

    ExpenseViewModel(ExpenseRepository repository, Long expensesId) {
        mRepository = repository;
        mPaymentEntryList = new ArrayList<>();
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

    public void insertNewExpense(Long categoryId, double cost, int numberOfPayments, LocalDate paymentDate) {
        ExpenseEntry expenseEntry = new ExpenseEntry(categoryId, cost, numberOfPayments, paymentDate);
        createPaymentsFromExpense(expenseEntry);
        mRepository.insertExpense(expenseEntry);
    }

    private void createPaymentsFromExpense(ExpenseEntry expenseEntry) {
        int paymentsNumber = expenseEntry.getNumberOfPayments();
        double monthlyCost = expenseEntry.getExpenseCost()/paymentsNumber;
        LocalDate startDate = expenseEntry.getExpensePaymentDate();
        for(int i = 0 ; i<paymentsNumber; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            PaymentEntry paymentEntry = new PaymentEntry(monthlyCost, (i+1), paymentDate);
            mPaymentEntryList.add(paymentEntry);
        }
    }

    public void updateExpense(long expenseId, Long jobId,
                              long categoryId, double cost, int numberOfPayments, LocalDate paymentDate) {
        ExpenseEntry expenseEntry = new ExpenseEntry(expenseId, jobId, categoryId,
                cost, numberOfPayments, paymentDate);
        mRepository.updateExpense(expenseEntry);

    }

    public void updatePaymentsWithExpenseID(long expenseId) {
        for(PaymentEntry paymentEntry : mPaymentEntryList){
            paymentEntry.setExpenseId(expenseId);
        }
    }

    public void insertPayments() {
        mRepository.insertPayments(mPaymentEntryList);
    }
}
