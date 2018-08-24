package roiattia.com.capstone.model;

public class ExpensesModel {

    private String mName;
    private int mCount;
    private double mCost;

    public ExpensesModel(String name, int count, double cost) {
        mName = name;
        mCount = count;
        mCost = cost;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getCount() {
        return mCount;
    }

    public double getCost() {
        return mCost;
    }
}
