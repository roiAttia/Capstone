package roiattia.com.capstone;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.database.Repository;
import roiattia.com.capstone.utils.InjectorUtils;

public class DummyData {

    public static void insertCategories(Context context){
        CategoryEntry jobOne = new CategoryEntry("Birthday", CategoryEntry.Type.JOB);
        CategoryEntry jobTwo = new CategoryEntry("Show", CategoryEntry.Type.JOB);
        CategoryEntry expOne = new CategoryEntry("VAT", CategoryEntry.Type.EXPENSE);
        CategoryEntry expTwo = new CategoryEntry("Fuel", CategoryEntry.Type.EXPENSE);

        Repository repository = InjectorUtils.provideRepository(context);
        repository.insertCategory(jobOne);
        repository.insertCategory(jobTwo);
        repository.insertCategory(expOne);
        repository.insertCategory(expTwo);
    }
//    public JobEntry(int categoryId, String description, Date date, double income, double expenses, double profit) {

    public static void insertJob(Context context){
        LocalDate localDate = new LocalDate("2018-08-23");
        JobEntry jobEntry = new JobEntry(4,
                "Sarah", localDate, localDate, 1000, 300, 700);
        Repository repository = InjectorUtils.provideRepository(context);
        repository.insertJob(jobEntry);
    }
}
