package roiattia.com.capstone.utils;

import android.content.Context;

import org.joda.time.LocalDate;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.JobEntry;

public class DummyData {

    public static void insertCategories(Context context){
        CategoryEntry jobOne = new CategoryEntry("Birthday", CategoryEntry.Type.JOB);
        CategoryEntry jobTwo = new CategoryEntry("Show", CategoryEntry.Type.JOB);
        CategoryEntry expOne = new CategoryEntry("VAT", CategoryEntry.Type.EXPENSE);
        CategoryEntry expTwo = new CategoryEntry("Fuel", CategoryEntry.Type.EXPENSE);

//        JobRepository repository = InjectorUtils.provideJobRepository(context);
    }
//    public JobEntry(int categoryId, String description, Date date, double income, double expenses, double profit) {

    public static void insertJob(Context context){
        LocalDate localDate = new LocalDate("2018-08-23");
        JobEntry jobEntry = new JobEntry(
                "Sarah", localDate, localDate, 1000, 300, 700);
//        JobRepository repository = InjectorUtils.provideJobRepository(context);
//        repository.insertJob(jobEntry);
    }
}
