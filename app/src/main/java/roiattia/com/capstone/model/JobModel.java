package roiattia.com.capstone.model;

import android.os.Handler;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.database.Repository;

public class JobModel {

    private List<ExpenseEntry> mExpenses;
    private JobEntry mJobEntry;
    private CategoryEntry mCategoryEntry;
    private static JobModel sInstance;

    private JobModel(){
        mExpenses = new ArrayList<>();
        mJobEntry = new JobEntry();
        mCategoryEntry = new CategoryEntry();
    }

    public synchronized static JobModel getInstance(){
        if(sInstance == null){
            sInstance = new JobModel();
        }
        return sInstance;
    }

    public List<ExpenseEntry> getExpenses() {
        return mExpenses;
    }

    public JobEntry getJobEntry() {
        return mJobEntry;
    }

    public void addExpense(ExpenseEntry expenseEntry){
        mExpenses.add(expenseEntry);
    }

    public void calculateTotalExpense(){
        double totalExpenses = 0;
        for(ExpenseEntry expenseEntry : mExpenses){
            totalExpenses += expenseEntry.getCost();
        }
        mJobEntry.setExpenses(totalExpenses);
    }

    public void addExpenseWithNewCategory(final Repository repository, final CategoryEntry categoryEntry,
                                          final ExpenseEntry expenseEntry){
        repository.insertCategory(categoryEntry);
        mExpenses.add(expenseEntry);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mExpenses.get(mExpenses.size()-1).setCategoryId((int) repository.getCatId());
            }
        }, 1000);
    }

    public void addJobWithNewCategory(final Repository repository, CategoryEntry categoryEntry){
        repository.insertCategory(categoryEntry);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                int catId = (int) repository.getCatId();
                mJobEntry.setCategoryId(catId);
                repository.insertJob(mJobEntry);
            }
        }, 1000);
    }

    public void addJob(Repository repository, int categoryId){
        mJobEntry.setCategoryId(categoryId);
        repository.insertJob(mJobEntry);
    }

    // Job methods
    public void setJobTotalExpenses(double expenses) {
        mJobEntry.setExpenses(expenses);
    }

    public void setJobDate(LocalDate localDate){
        mJobEntry.setJobDate(localDate);
    }

    public void setJobPaymentDate(LocalDate localDate){
        mJobEntry.setDateOfPayment(localDate);
    }

    public void calculateJobProfits(){
        double income = mJobEntry.getIncome();
        double expenses = mJobEntry.getExpenses();
        mJobEntry.setProfit(income - expenses);
    }

    // Category methods
    public void setCategoryName(String name){
        mCategoryEntry.setName(name);
    }

    public String getCategoryName(){
        return mCategoryEntry.getName();
    }
}
