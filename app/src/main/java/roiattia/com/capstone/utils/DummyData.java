package roiattia.com.capstone.utils;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.JobEntry;

public class DummyData {

    public static List<CategoryEntry> getDummyCategories(){
        List<CategoryEntry> categoryEntries = new ArrayList<>();
        CategoryEntry category_1 = new CategoryEntry(1,"Birthday", CategoryEntry.Type.JOB);
        CategoryEntry category_2 = new CategoryEntry(2,"Show", CategoryEntry.Type.JOB);
        CategoryEntry category_3 = new CategoryEntry(3,"VAT", CategoryEntry.Type.EXPENSE);
        CategoryEntry category_4 = new CategoryEntry( 4,"Fuel", CategoryEntry.Type.EXPENSE);
        categoryEntries.add(category_1);
        categoryEntries.add(category_2);
        categoryEntries.add(category_3);
        categoryEntries.add(category_4);
        return categoryEntries;
    }

    public static List<ExpenseEntry> getDummyExpenses(){
        List<ExpenseEntry> expenses = new ArrayList<>();
        LocalDate firstPaymentDate = new LocalDate("2018-09-23");
        LocalDate lastPaymentDate = new LocalDate("2018-09-23");
        ExpenseEntry expense_1 = new ExpenseEntry(3, 400, "Lorem ipsum dolor sit amet",
                200, 2, firstPaymentDate, lastPaymentDate);
        firstPaymentDate = firstPaymentDate.plusDays(1);
        lastPaymentDate = new LocalDate("2018-10-23");
        ExpenseEntry expense_2 = new ExpenseEntry(4,1500, "Lorem ipsum dolor sit amet,\" +\n" +
                "\" consectetur adipiscing elit, sed", 500, 3,
                firstPaymentDate, lastPaymentDate);
        expenses.add(expense_1);
        expenses.add(expense_2);
        return expenses;
    }

    public static List<JobEntry> getDummyJobs(){
        List<JobEntry> jobs = new ArrayList<>();
        LocalDate localDate = new LocalDate("2018-09-23");
        JobEntry job_1 = new JobEntry(1,"Lorem ipsum", localDate, localDate,
                1000, 300, 700);
        localDate = localDate.plusDays(1);
        JobEntry job_2 = new JobEntry(2,"Lorem ipsum dolor sit amet," +
                " consectetur adipiscing elit, sed", localDate, localDate,
                1000, 300, 700);
        jobs.add(job_1);
        jobs.add(job_2);
        return jobs;
    }
}
