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
    private List<CategoryEntry> mCategories;
    private JobRepository mRepository;

    NewJobViewModel(Context context) {
        mRepository = InjectorUtils.provideJobRepository(context, this);
        mJobEntry = InjectorUtils.provideJobEntry();
        mExpenses = new ArrayList<>();
    }

    /**
     * Handle categories retrieval from db
     * @param type the type of categories needed - JOB or EXPENSE
     * @return categories wrapped with live_data
     */
    public LiveData<List<CategoryEntry>> getCategories(CategoryEntry.Type type) {
        return mRepository.getCategories(type);
    }

    @Override
    public void onCategoryInserted(long categoryId, CategoryEntry.Type type) {
        if(type.equals(CategoryEntry.Type.JOB)){
            updateJobCategoryId(categoryId);
            insertNewJob();
        } else if(type.equals(CategoryEntry.Type.EXPENSE)){
            insertExpensesWithNewCategoryId(categoryId);
        }
    }

    @Override
    public void onJobInserted(long jobId) {
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

    public void updateJobDetails(LocalDate jobDate, LocalDate jobPaymentDate,
                                 int jobIncome, int jobExpense, int jobProfit, String jobDescription) {
        mJobEntry = new JobEntry(jobDescription, jobDate, jobPaymentDate,
                jobIncome, jobExpense, jobProfit);
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

    public void setCategories(List<CategoryEntry> categoryEntries) {
        mCategories = categoryEntries;
    }

    public long getCategoryId(int categoryPosition) {
        return mCategories.get(categoryPosition).getCategoryId();
    }

    public void setExpenseDetails(long categoryId, int cost, int numberOfPayments, LocalDate paymentDate) {
        ExpenseEntry expenseEntry = new ExpenseEntry(categoryId, cost, numberOfPayments, paymentDate);
        mExpenses.add(expenseEntry);
    }

    public void debugPrint(){
        mRepository.debugPrint();
    }


}
