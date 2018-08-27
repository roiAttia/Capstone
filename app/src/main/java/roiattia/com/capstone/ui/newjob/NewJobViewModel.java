package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.utils.InjectorUtils;

public class NewJobViewModel extends ViewModel
    implements JobRepository.GetIdHandler {

    private static final String TAG = NewJobViewModel.class.getSimpleName();

    private JobEntry mJobEntry;
    private List<ExpenseEntry> mExpenses;
    private LiveData<List<CategoryEntry>> mJobCategories;
    private LiveData<List<CategoryEntry>> mExpenseCategories;
    private JobRepository mRepository;
    private ExpenseEntry mExpense;

    NewJobViewModel(Context context) {
        mRepository = InjectorUtils.provideJobRepository(context, this);
        mJobEntry = InjectorUtils.provideJobEntry();
        mExpenses = new ArrayList<>();
        mJobCategories = mRepository.getCategories(CategoryEntry.Type.JOB);
        mExpenseCategories = mRepository.getCategories(CategoryEntry.Type.EXPENSE);
    }

    /**
     * Handle categories retrieval from db
     * @return categories wrapped with live_data
     */
    public LiveData<List<CategoryEntry>> getJobCategories() {
        return mJobCategories;
    }

    /**
     * Handle categories retrieval from db
     * @return categories wrapped with live_data
     */
    public LiveData<List<CategoryEntry>> getExpenseCategories() {
        return mExpenseCategories;
    }

    @Override
    public void onCategoryInserted(Long categoryId, CategoryEntry.Type type) {
        if(type.equals(CategoryEntry.Type.JOB)){
            updateJobCategoryId(categoryId);
            insertNewJob();
        } else if(type.equals(CategoryEntry.Type.EXPENSE)){
            mExpense.setCategoryId(categoryId);
            mExpenses.add(mExpense);
        }
    }

    @Override
    public void onJobInserted(Long jobId) {
        insertExpensesWithJobId(jobId);
    }

    public void calculateProfit(){
        double profit = mJobEntry.getJobIncome() - mJobEntry.getJobExpenses();
        mJobEntry.setJobProfits(profit);
    }

    /**
     * Set job date
     * @param jobDate - date of the job
     */
    public void setJobDate(LocalDate jobDate) {
        mJobEntry.setJobDate(jobDate);
    }

    /**
     * @return job date
     */
    public LocalDate getJobDate() {
        return mJobEntry.getJobDate();
    }

    public List<ExpenseEntry> getExpensesList() {
        return mExpenses;
    }

    public void insertNewCategory(String name, CategoryEntry.Type type) {
        CategoryEntry categoryEntry = new CategoryEntry(name, type);
        mRepository.insertCategory(categoryEntry, type);
    }

    private void insertExpensesWithJobId(long newId) {
        for(ExpenseEntry expenseEntry : mExpenses){
            expenseEntry.setJobId(newId);
            mRepository.insertExpense(expenseEntry);
        }
    }

    public void updateJobCategoryId(long categoryId) {
        mJobEntry.setCategoryId(categoryId);
    }

    public void insertNewJob() {
        mRepository.insertJob(mJobEntry);
    }

    private void insertExpensesWithNewCategoryId(long categoryId) {
        mExpenses.get(mExpenses.size()).setCategoryId(categoryId);
    }

    public void updateExpenses() {
        int totalExpenses = 0;
        for(ExpenseEntry expenseEntry : mExpenses){
            totalExpenses += expenseEntry.getExpenseCost();
        }
        mJobEntry.setJobExpenses(totalExpenses);
    }

    public void insertNewExpense(ExpenseEntry expenseEntry) {
        mExpenses.add(expenseEntry);
    }

    public void setExpense(ExpenseEntry expense) {
        mExpense = expense;
    }

    public void debugPrint(){
        mRepository.debugPrint();
    }

}
