package roiattia.com.capstone.utils;

import android.content.Context;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.database.Repository;
import roiattia.com.capstone.ui.calendar.CalendarViewModelFactory;
import roiattia.com.capstone.ui.newjob.NewJobViewModel;
import roiattia.com.capstone.ui.newjob.NewJobViewModelFactory;

public class InjectorUtils {

    public static Repository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return Repository.getInstance(database.jobDao(), database.categoryDao(),
                database.expenseDao(), executors);
    }

    public static CalendarViewModelFactory provideCalendarViewModelFactory(Context context) {
        Repository repository = provideRepository(context.getApplicationContext());
        return new CalendarViewModelFactory(repository);
    }

    public static NewJobViewModelFactory provideExpenseViewModelFactory(Context context,
                                                                        CategoryEntry.Type type) {
        Repository repository = provideRepository(context.getApplicationContext());
        return new NewJobViewModelFactory(repository, type);
    }

    public static JobEntry provideJobEntry(){
        return new JobEntry();
    }
}
