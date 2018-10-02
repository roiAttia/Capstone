package roiattia.com.capstone.model;

import org.joda.time.LocalDate;

public class FinancialModel {

    private double mIncome;
    private double mExpenses;
    private double mProfits;
//    private LocalDate mFrom;
//    private LocalDate mTo;

    public FinancialModel(double income, double expenses, double profits) {
        mIncome = income;
        mExpenses = expenses;
        mProfits = profits;
//        mFrom = from;
//        mTo = to;
    }

    public double getIncome() {
        return mIncome;
    }

    public void setIncome(double income) {
        mIncome = income;
    }

    public double getExpenses() {
        return mExpenses;
    }

    public void setExpenses(double expenses) {
        mExpenses = expenses;
    }

    public double getProfits() {
        return mProfits;
    }

    public void setProfits(double profits) {
        mProfits = profits;
    }

//    public LocalDate getFrom() {
//        return mFrom;
//    }
//
//    public void setFrom(LocalDate from) {
//        mFrom = from;
//    }
//
//    public LocalDate getTo() {
//        return mTo;
//    }
//
//    public void setTo(LocalDate to) {
//        mTo = to;
//    }
}
