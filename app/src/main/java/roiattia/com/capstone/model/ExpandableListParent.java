package roiattia.com.capstone.model;

public class ExpandableListParent {

    private double mExpensesAmount;
    private int mNumberOfExpenses;

    public ExpandableListParent(double expensesAmount, int numberOfExpenses) {
        mExpensesAmount = expensesAmount;
        mNumberOfExpenses = numberOfExpenses;
    }

    public double getExpensesAmount() {
        return mExpensesAmount;
    }

    public void setExpensesAmount(double expensesAmount) {
        mExpensesAmount = expensesAmount;
    }

    public int getNumberOfExpenses() {
        return mNumberOfExpenses;
    }

    public void setNumberOfExpenses(int numberOfExpenses) {
        mNumberOfExpenses = numberOfExpenses;
    }
}
