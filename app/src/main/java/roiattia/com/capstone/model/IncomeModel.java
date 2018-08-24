package roiattia.com.capstone.model;

public class IncomeModel {

    private String mName;
    private int mCount;
    private double mIncome;
    private double mProfit;

    public IncomeModel(String name, int count, double income, double profit) {
        mName = name;
        mCount = count;
        mIncome = income;
        mProfit = profit;
    }

    public String getName() {
        return mName;
    }

    public double getIncome() {
        return mIncome;
    }

    public double getProfit() {
        return mProfit;
    }

    public int getCount() {
        return mCount;
    }

    public void setName(String name) {
        mName = name;
    }

}
