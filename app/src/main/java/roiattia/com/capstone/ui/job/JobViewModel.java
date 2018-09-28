package roiattia.com.capstone.ui.job;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.model.ExpandableListChild;
import roiattia.com.capstone.repositories.CategoriesRepository;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.repositories.JobsRepository;

import static roiattia.com.capstone.database.entry.CategoryEntry.Type.JOB;

public class JobViewModel extends AndroidViewModel {

    private CategoriesRepository mCategoriesRepository;
    private JobsRepository mJobsRepository;
    private ExpensesRepository mExpensesRepository;
    private AppExecutors mExecutors;
    private MutableLiveData<ExpenseEntry> mMutableLiveExpense;
    private MutableLiveData<JobEntry> mMutableLiveJob;
    private MutableLiveData<CategoryEntry> mMutableLiveCategory;
    private LiveData<List<CategoryEntry>> mLiveDataCategories;

    public JobViewModel(@NonNull Application application) {
        super(application);
        mCategoriesRepository = CategoriesRepository.getInstance(application.getApplicationContext());
        mJobsRepository = JobsRepository.getInstance(application.getApplicationContext());
        mExpensesRepository = ExpensesRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mMutableLiveExpense = new MutableLiveData<>();
        mMutableLiveJob = new MutableLiveData<>();
        mMutableLiveCategory = new MutableLiveData<>();
        mLiveDataCategories = mCategoriesRepository.getCategories(JOB);
    }

    public LiveData<List<ExpandableListChild>> getChildLiveData(long[] expensesIds) {
        return mExpensesRepository.getExpensesByIds(expensesIds);
    }

    public LiveData<List<CategoryEntry>> getLiveDataCategories() {
        return mLiveDataCategories;
    }

    public void insertNewCategory(final String name, CategoriesRepository.OnCategoryListener listener) {
        CategoryEntry categoryEntry = new CategoryEntry(name, JOB);
        mCategoriesRepository.insertCategory(categoryEntry, listener);
    }

    public void setMutableLiveExpense(final long expenseId){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ExpenseEntry expenseEntry = mExpensesRepository.getExpenseById(expenseId);
                mMutableLiveExpense.postValue(expenseEntry);
            }
        });
    }

    public void setMutableLiveCategory(final long categoryId){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                CategoryEntry categoryEntry = mCategoriesRepository.getCategoryById(categoryId);
                mMutableLiveCategory.postValue(categoryEntry);
            }
        });
    }

    public void setMutableLiveJob(final long jobId){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                JobEntry job = mJobsRepository.getJobById(jobId);
                mMutableLiveJob.postValue(job);
            }
        });
    }

    public MutableLiveData<ExpenseEntry> getMutableLiveExpense(){
        return mMutableLiveExpense;
    }

    public MutableLiveData<JobEntry> getMutableLiveJob(){
        return mMutableLiveJob;
    }

    public MutableLiveData<CategoryEntry> getMutableLiveCategory() {
        return mMutableLiveCategory;
    }

    public void insertJob(long jobId, long categoryId, String description, LocalDate jobDate, LocalDate jobPaymentDate,
                          double jobIncome, double jobExpense, double jobProfit, JobsRepository.OnJobListener listener){
        JobEntry job = new JobEntry(jobId, categoryId, description, jobDate, jobPaymentDate,
                jobIncome, jobExpense, jobProfit);
        mJobsRepository.insertJob(job, listener);
    }

    public void insertExpense(List<ExpenseEntry> expenses) {
        mExpensesRepository.insertExpenses(expenses);
    }

    public void deleteExpenseById(long expenseId) {
        mExpensesRepository.deleteExpense(expenseId);
    }
}
