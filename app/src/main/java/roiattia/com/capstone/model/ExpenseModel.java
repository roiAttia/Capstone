package roiattia.com.capstone.model;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.database.JobEntry;

public class ExpenseModel {

    private List<ExpenseEntry> mExpenses;
    private static ExpenseModel sInstance;

    private ExpenseModel(){
        mExpenses = new ArrayList<>();
    }

    public synchronized static ExpenseModel getInstance(){
        if(sInstance == null){
            sInstance = new ExpenseModel();
        }
        return sInstance;
    }

    public void addExpense(ExpenseEntry expenseEntry){
        mExpenses.add(expenseEntry);
    }

    public List<ExpenseEntry> getExpenses(){
        return mExpenses;
    }

    public void calculateExpenses(double totalCost, double numberOfPayments, LocalDate paymentDate){
        double costPerExpense = totalCost/numberOfPayments;
        for(int i = 0; i<numberOfPayments; i++){
            ExpenseEntry expenseEntry = new ExpenseEntry(costPerExpense, 1, paymentDate);
            mExpenses.add(expenseEntry);
            paymentDate = paymentDate.plusMonths(1);
        }
    }
}
