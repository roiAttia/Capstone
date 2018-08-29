package roiattia.com.capstone.model;

public class OverallIncomeModel {

    private double mIncome;
    private double mProfit;

    public OverallIncomeModel(double income, double profit) {
        mIncome = income;
        mProfit = profit;
    }

    public double getIncome() {
        return mIncome;
    }

    public double getProfit() {
        return mProfit;
    }

    public void setIncome(double income) {
        mIncome = income;
    }

    public void setProfit(double profit) {
        mProfit = profit;
    }
}
