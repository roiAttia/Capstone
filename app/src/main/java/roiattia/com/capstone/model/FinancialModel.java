package roiattia.com.capstone.model;

public class FinancialModel {

    private double mIncome;
    private double mExpenses;
    private double mProfit;

    public FinancialModel(double income, double expenses, double profit) {
        mIncome = income;
        mExpenses = expenses;
        mProfit = profit;
    }

    public double getIncome() {
        return mIncome;
    }

    public double getExpenses() {
        return mExpenses;
    }

    public double getProfit() {
        return mProfit;
    }
}
