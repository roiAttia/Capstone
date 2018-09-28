package roiattia.com.capstone.ui.expense;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.PaymentEntry;
import roiattia.com.capstone.repositories.CategoriesRepository;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.repositories.PaymentsRepository;

import static roiattia.com.capstone.database.entry.CategoryEntry.Type.EXPENSE;

public class ExpensesViewModel extends AndroidViewModel {

    private static final String TAG = ExpensesViewModel.class.getSimpleName();

    private ExpensesRepository mExpensesRepository;
    private CategoriesRepository mCategoriesRepository;
    private PaymentsRepository mPaymentsRepository;
    private LiveData<List<CategoryEntry>> mLiveDataCategories;
    private MutableLiveData<ExpenseEntry> mMutableExpense;
    private List<PaymentEntry> mPayments;
    private LocalDate mExpenseLastPaymentDate;
    private AppExecutors mExecutors;

    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        mExpensesRepository = ExpensesRepository.getInstance(application.getApplicationContext());
        mCategoriesRepository = CategoriesRepository.getInstance(application.getApplicationContext());
        mPaymentsRepository = PaymentsRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mLiveDataCategories = mCategoriesRepository.getCategories(EXPENSE);
        mMutableExpense = new MutableLiveData<>();
        mPayments = new ArrayList<>();
    }

    public LiveData<List<CategoryEntry>> getLiveDataCategories() {
        return mLiveDataCategories;
    }

    public MutableLiveData<ExpenseEntry> getMutableExpense() {
        return mMutableExpense;
    }

    public void getExpenseById(final long expenseId){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ExpenseEntry expense = mExpensesRepository.getExpenseById(expenseId);
                mMutableExpense.postValue(expense);
            }
        });
    }

    public void insertNewCategory(final String name, CategoriesRepository.OnCategoryListener listener) {
        CategoryEntry categoryEntry = new CategoryEntry(name, EXPENSE);
        mCategoriesRepository.insertCategory(categoryEntry, listener);
    }

    public void insertExpense(Long jobId, long categoryId, double cost, String description, int paymentsNumber,
                              double monthlyCost, LocalDate firstDate,
                              ExpensesRepository.OnExpenseListener listener){
        ExpenseEntry expense = mMutableExpense.getValue();
        if(expense == null){
            //create new expense
            expense = new ExpenseEntry(jobId, categoryId, cost, description, paymentsNumber, monthlyCost,
                    firstDate, mExpenseLastPaymentDate);
        } else {
            // pass in fields to expense
            expense.setCategoryId(categoryId);
            expense.setExpenseCost(cost);
            expense.setNumberOfPayments(paymentsNumber);
            expense.setMonthlyCost(monthlyCost);
            expense.setExpenseFirstPayment(firstDate);
            if(mExpenseLastPaymentDate != null) {
                expense.setExpenseLastPayment(mExpenseLastPaymentDate);
            }
            expense.setJobId(jobId);
        }
        mExpensesRepository.insertExpense(expense, listener);
    }

    public void createPayments(int numberOfPayments, double monthlyCost, LocalDate firstPaymentDate) {
        LocalDate paymentDate = firstPaymentDate;
        for(int i = 0; i< numberOfPayments; i++) {
            paymentDate = firstPaymentDate.plusMonths(i);
            PaymentEntry paymentEntry = new PaymentEntry(monthlyCost, (i+1), paymentDate);
            mPayments.add(paymentEntry);
        }
        mExpenseLastPaymentDate = paymentDate;
    }

    public void updatePaymentsWithExpenseID(long expenseId) {
        for(PaymentEntry paymentEntry : mPayments){
            paymentEntry.setExpenseId(expenseId);
        }
    }

    public void insertPayments() {
        mPaymentsRepository.insertPayments(mPayments);
    }
}
