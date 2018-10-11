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
    private MutableLiveData<JobEntry> mMutableLiveJob;
    private LiveData<List<CategoryEntry>> mLiveDataCategories;
    private MutableLiveData<List<ExpandableListChild>> mMutableLiveExpenseChildes;

    public JobViewModel(@NonNull Application application) {
        super(application);
        mCategoriesRepository = CategoriesRepository.getInstance(application.getApplicationContext());
        mJobsRepository = JobsRepository.getInstance(application.getApplicationContext());
        mExpensesRepository = ExpensesRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
        mMutableLiveJob = new MutableLiveData<>();
        mLiveDataCategories = mCategoriesRepository.getCategories(JOB);
        mMutableLiveExpenseChildes = new MutableLiveData<>();
    }

    /**
     * Get categories for selection
     * @return categories live data
     */
    public LiveData<List<CategoryEntry>> getLiveDataCategories() {
        return mLiveDataCategories;
    }

    /**
     * Insert new category inserted by user
     * @param name category name
     * @param listener notify listener with new category id
     */
    public void insertNewCategory(final String name, CategoriesRepository.OnCategoryListener listener) {
        CategoryEntry categoryEntry = new CategoryEntry(name, JOB);
        mCategoriesRepository.insertCategory(categoryEntry, listener);
    }

    /**
     * Load job data using jobId into MutableLiveData for observation
     * @param jobId job id to load job by
     */
    public void setMutableLiveJob(final long jobId){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                JobEntry job = mJobsRepository.getJobById(jobId);
                mMutableLiveJob.postValue(job);
            }
        });
    }

    /**
     * Get the Mutable live data for Job loading observation
     * @return Mutable live data of Job Entry
     */
    public MutableLiveData<JobEntry> getMutableLiveJob(){
        return mMutableLiveJob;
    }

    /**
     * Insert Job to DB
     */
    public void insertJob(Long jobId, long categoryId, String description, LocalDate jobDate, LocalDate jobPaymentDate,
                          double jobIncome, double jobExpense, double jobProfit, JobsRepository.OnJobListener listener){
        JobEntry job = new JobEntry(jobId, categoryId, description, jobDate, jobPaymentDate,
                jobIncome, jobExpense, jobProfit);
        mJobsRepository.insertJob(job, listener);
    }

    /**
     * Delete expense by id - done from expenses list
     * @param expenseId id of the expense needs deletion
     */
    public void deleteExpenseById(long expenseId) {
        mExpensesRepository.deleteExpense(expenseId);
    }

    /**
     * Get the Mutable live data for Expenses childes observation
     * @return Mutable live data of ExpandableListChild
     */
    public MutableLiveData<List<ExpandableListChild>> getMutableLiveExpenseChildes() {
        return mMutableLiveExpenseChildes;
    }

    /**
     * Set expenses childes list using a list of expenses ids
     * @param expensesIds expenses to load by id
     */
    public void setMutableLiveExpenseChildes(final List<Long> expensesIds) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Long[] array = new Long[expensesIds.size()];
                expensesIds.toArray(array);
                List<ExpandableListChild> childes = mExpensesRepository.getExpensesByIds(array);
                mMutableLiveExpenseChildes.postValue(childes);
            }
        });
    }
    
    public void insertExpense(List<ExpenseEntry> expenses) {
        mExpensesRepository.insertExpenses(expenses);
    }

}
