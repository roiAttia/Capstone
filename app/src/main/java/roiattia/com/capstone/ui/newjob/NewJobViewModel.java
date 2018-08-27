package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobEntry;

public class NewJobViewModel extends ViewModel {

    private static final String TAG = NewJobViewModel.class.getSimpleName();

    private LiveData<List<CategoryEntry>> mJobCategories;
    private LiveData<JobEntry> mJob;
    private List<ExpenseEntry> mExpensesList;
    private JobRepository mRepository;


    NewJobViewModel(JobRepository jobRepository, Long jobId) {
        mRepository = jobRepository;
        mExpensesList = new ArrayList<>();
        mJobCategories = mRepository.getCategories(CategoryEntry.Type.JOB);
        if(jobId != null){
            mJob = mRepository.loadJobById(jobId);
        }
    }

    /**
     * Handle categories retrieval from db
     * @return categories wrapped with live_data
     */
    public LiveData<List<CategoryEntry>> getJobCategories() {
        return mJobCategories;
    }


    public void debugPrint(){
        mRepository.debugPrint();
    }

    public LiveData<JobEntry> getJob() {
        return mJob;
    }


    public LiveData<List<ExpenseEntry>> loadExpensesById(long[] expensesId) {
        return mRepository.loadExpensesByIds(expensesId);
    }

    public void addExpenses(List<ExpenseEntry> expensesList) {
        mExpensesList.addAll(expensesList);
    }

    public void insertNewCategory(String categoryName) {
        CategoryEntry categoryEntry = new CategoryEntry(categoryName, CategoryEntry.Type.JOB);
        mRepository.insertCategory(categoryEntry);
    }

    public void insertNewJob(long categoryId, String description, LocalDate jobDate,
                             LocalDate jobPaymentDate, double jobIncome, double jobExpense, double jobProfit) {
        JobEntry jobEntry = new JobEntry(categoryId, description, jobDate, jobPaymentDate, jobIncome,
                jobExpense, jobProfit);
        mRepository.insertJob(jobEntry);
    }

    public void updateJob(long jobId, long categoryId, String description, LocalDate jobDate,
                          LocalDate jobPaymentDate, double jobIncome, double jobExpense, double jobProfit) {
        JobEntry jobEntry = new JobEntry(jobId, categoryId, description, jobDate, jobPaymentDate, jobIncome,
                jobExpense, jobProfit);
        mRepository.updateJob(jobEntry);
    }

    public List<ExpenseEntry> getExpensesList() {
        return mExpensesList;
    }

    public void updateExpenses() {
        mRepository.updateExpenses(mExpensesList);
    }
}
