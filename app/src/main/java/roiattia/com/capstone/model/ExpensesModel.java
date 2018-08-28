package roiattia.com.capstone.model;

public class ExpensesModel {

    private Long mCategoryId;
    private String mName;
    private int mCount;
    private double mCost;

    public ExpensesModel(Long categoryId, String name, int count, double cost) {
        mCategoryId = categoryId;
        mName = name;
        mCount = count;
        mCost = cost;
    }

    public Long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(Long categoryId) {
        mCategoryId = categoryId;
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
