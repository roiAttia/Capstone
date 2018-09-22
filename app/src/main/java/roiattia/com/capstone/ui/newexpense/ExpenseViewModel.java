package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.PaymentEntry;

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
        mRepository.insertCategory(categoryEntry);
    }

    public void insertNewExpense(Long categoryId, double cost, int numberOfPayments, LocalDate paymentDate) {
        createPaymentsFromExpense(numberOfPayments, cost, paymentDate);
        ExpenseEntry expenseEntry;
        PaymentEntry paymentEntry = mPaymentEntryList.get(mPaymentEntryList.size() - 1);
        if(mPaymentEntryList.size() > 1){
            expenseEntry = new ExpenseEntry(categoryId, cost, numberOfPayments,
                    paymentEntry.getPaymentCost(), paymentDate, paymentEntry.getPaymentDate());
        } else {
            expenseEntry = new ExpenseEntry(categoryId, cost, numberOfPayments,
                   0, paymentDate, paymentEntry.getPaymentDate() );
        }
        mRepository.insertExpense(expenseEntry);
    }

    private void createPaymentsFromExpense(int numberOfPayments, double cost, LocalDate firstPaymentDate) {
        double monthlyCost = cost/ numberOfPayments;
        for(int i = 0; i< numberOfPayments; i++) {
            LocalDate paymentDate = firstPaymentDate.plusMonths(i);
            PaymentEntry paymentEntry = new PaymentEntry(monthlyCost, (i+1), paymentDate);
            mPaymentEntryList.add(paymentEntry);
        }
    }

    public void updateExpense(long expenseId, Long jobId, long categoryId, double cost,
                              int numberOfPayments, double monthlyCost, LocalDate firstPaymentDate,
                              LocalDate lastPaymentDate) {
        ExpenseEntry expenseEntry = new ExpenseEntry(expenseId, jobId, categoryId,
                cost, numberOfPayments, monthlyCost, firstPaymentDate, lastPaymentDate);
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
