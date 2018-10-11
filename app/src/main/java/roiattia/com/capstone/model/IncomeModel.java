package roiattia.com.capstone.model;

public class IncomeModel {

    private String mName;
    private long mCategoryId;
    private int mCount;
    private double mIncome;
    private double mProfit;

    public IncomeModel(String name,long categoryId, int count, double income, double profit) {
        mName = name;
        mCategoryId = categoryId;
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

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        mCategoryId = categoryId;
    }
}
