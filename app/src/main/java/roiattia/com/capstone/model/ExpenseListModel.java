package roiattia.com.capstone.model;

import org.joda.time.LocalDate;

public class ExpenseListModel {

    private Long mExpenseId;
    private String mCategoryName;
    private String mDescription;
    private double mExpenseCost;
    private int mNumberOfPayments;
    private double mMonthlyCost;
    private LocalDate mExpenseFirstPayment;
    private LocalDate mExpenseLastPayment;

    public ExpenseListModel(Long expenseId, String categoryName, String description, double expenseCost,
                            int numberOfPayments, double monthlyCost, LocalDate expenseFirstPayment,
                            LocalDate expenseLastPayment) {
        mExpenseId = expenseId;
        mCategoryName = categoryName;
        mDescription = description;
        mExpenseCost = expenseCost;
        mNumberOfPayments = numberOfPayments;
        mMonthlyCost = monthlyCost;
        mExpenseFirstPayment = expenseFirstPayment;
        mExpenseLastPayment = expenseLastPayment;
    }

    public Long getExpenseId() {
        return mExpenseId;
    }

    public void setExpenseId(Long expenseId) {
        mExpenseId = expenseId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public double getExpenseCost() {
        return mExpenseCost;
    }

    public void setExpenseCost(double expenseCost) {
        mExpenseCost = expenseCost;
    }

    public int getNumberOfPayments() {
        return mNumberOfPayments;
    }

    public void setNumberOfPayments(int numberOfPayments) {
        mNumberOfPayments = numberOfPayments;
    }

    public double getMonthlyCost() {
        return mMonthlyCost;
    }

    public void setMonthlyCost(double monthlyCost) {
        mMonthlyCost = monthlyCost;
    }

    public LocalDate getExpenseFirstPayment() {
        return mExpenseFirstPayment;
    }

    public void setExpenseFirstPayment(LocalDate expenseFirstPayment) {
        mExpenseFirstPayment = expenseFirstPayment;
    }

    public LocalDate getExpenseLastPayment() {
        return mExpenseLastPayment;
    }

    public void setExpenseLastPayment(LocalDate expenseLastPayment) {
        mExpenseLastPayment = expenseLastPayment;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
