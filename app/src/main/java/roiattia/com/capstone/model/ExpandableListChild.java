package roiattia.com.capstone.model;

public class ExpandableListChild {

    private long mExpenseId;
    private String mCategoryName;
    private String mDescription;
    private double mCost;

    public ExpandableListChild(long expenseId, String categoryName, String description, double cost) {
        mExpenseId = expenseId;
        mCategoryName = categoryName;
        mDescription = description;
        mCost = cost;
    }

    public long getExpenseId() {
        return mExpenseId;
    }

    public void setExpenseId(long expenseId) {
        mExpenseId = expenseId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }
}
