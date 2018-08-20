package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.database.Repository;
import roiattia.com.capstone.utils.InjectorUtils;

public class NewJobViewModel extends ViewModel
    implements Repository.GetCategoryIdHandler {

    private static final String TAG = NewJobViewModel.class.getSimpleName();

    private JobEntry mJobEntry;
    private List<ExpenseEntry> mExpenses;
    private Repository mRepository;

    NewJobViewModel(Repository repository) {
        mRepository = repository;
        mJobEntry = InjectorUtils.provideJobEntry();
        mExpenses = new ArrayList<>();
    }

    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mRepository.getCategories(type);
    }

    public void addNewJOb(){

    }

    public void addExpenses(List<ExpenseEntry> expenses){
        mExpenses = expenses;
    }

    public void calculateProfit(){
        double profit = mJobEntry.getIncome() - mJobEntry.getExpenses();
        mJobEntry.setProfit(profit);
    }

    public double getProfit(){
        return mJobEntry.getProfit();
    }

    public void insertFee(int income) {
        mJobEntry.setIncome(income);
        calculateProfit();
    }

    public double getIncome() {
        return mJobEntry.getIncome();
    }

    public double getExpenses() {
        return mJobEntry.getExpenses();
    }

    public void setJobDate(LocalDate localDate) {
        mJobEntry.setJobDate(localDate);
    }

    public LocalDate getJobDate() {
        return mJobEntry.getJobDate();
    }

    public void setJobPaymentDate(LocalDate localDate) {
        mJobEntry.setDateOfPayment(localDate);
    }

    public void calculatePayments(int cost, int numberOfPayments,
                                  LocalDate paymentDate) {
        double monthlyCost = cost / numberOfPayments;
        for(int i = 0; i<numberOfPayments; i++){
            ExpenseEntry expenseEntry = new ExpenseEntry(monthlyCost, 1, paymentDate);
            mExpenses.add(expenseEntry);
            paymentDate = paymentDate.plusMonths(1);
        }
    }

    public List<ExpenseEntry> getExpensesList() {
        return mExpenses;
    }

    public void insertNewCategory(String name, CategoryEntry.Type type) {
        mRepository.setGetJobIdInBackground(this);
        CategoryEntry categoryEntry = new CategoryEntry(name, type);
        mRepository.insertCategory(categoryEntry, type);
    }

    @Override
    public void onFinish(long newId, CategoryEntry.Type type) {
        if(type.equals(CategoryEntry.Type.JOB)){
            insertExpensesWithJobId(newId);
        } else if(type.equals(CategoryEntry.Type.EXPENSE)){
            insertExpensesWithNewCategoryId(newId);
        }
    }

    private void insertExpensesWithJobId(long newId) {
        for(ExpenseEntry expenseEntry : mExpenses){
            expenseEntry.setJobId((int) newId);
            mRepository.insertExpense(expenseEntry);
        }
    }

    private void insertNewJobWithNewCategoryId(long categoryId) {
        mJobEntry.setCategoryId((int) categoryId);
        insertNewJob();
    }

    private void insertNewJob() {
        mRepository.insertJob(mJobEntry);
    }

    private void insertExpensesWithNewCategoryId(long categoryId) {
        for(ExpenseEntry expenseEntry : mExpenses){
            expenseEntry.setCategoryId((int) categoryId);
        }
    }

    public void updateExpenses() {
        int totalExpenses = 0;
        for(ExpenseEntry expenseEntry : mExpenses){
            totalExpenses += expenseEntry.getCost();
        }
        mJobEntry.setExpenses(totalExpenses);
    }
}
