package roiattia.com.capstone.model;

import org.joda.time.LocalDate;

public class JobCalendarModel {

    private String mCategoryName;
    private double mJobIncome;
    private String mJobDescription;
    private LocalDate mJobDate;

    public JobCalendarModel(String categoryName, double jobIncome, String jobDescription, LocalDate jobDate) {
        mCategoryName = categoryName;
        mJobIncome = jobIncome;
        mJobDescription = jobDescription;
        mJobDate = jobDate;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public double getJobIncome() {
        return mJobIncome;
    }

    public void setJobIncome(double jobIncome) {
        mJobIncome = jobIncome;
    }

    public String getJobDescription() {
        return mJobDescription;
    }

    public void setJobDescription(String jobDescription) {
        mJobDescription = jobDescription;
    }

    public LocalDate getJobDate() {
        return mJobDate;
    }

    public void setJobDate(LocalDate jobDate) {
        mJobDate = jobDate;
    }
}
