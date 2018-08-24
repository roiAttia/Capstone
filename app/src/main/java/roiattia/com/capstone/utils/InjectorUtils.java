package roiattia.com.capstone.utils;

import android.content.Context;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.JobEntry;
import roiattia.com.capstone.ui.calendar.CalendarRepository;
import roiattia.com.capstone.ui.finances.FinancesRepository;
import roiattia.com.capstone.ui.finances.FinancesViewModelFactory;
import roiattia.com.capstone.ui.newexpense.ExpenseRepository;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.ui.calendar.CalendarViewModelFactory;
import roiattia.com.capstone.ui.newjob.NewJobViewModelFactory;

public class InjectorUtils {

    public static JobRepository provideJobRepository(Context context, JobRepository.GetIdHandler getIdHandler) {
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return JobRepository.getInstance(database.jobDao(), database.categoryDao(),
                database.expenseDao(), executors, getIdHandler);
    }

    public static CalendarRepository provideCalendarRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        return CalendarRepository.getInstance(database.jobDao());
    }

    public static ExpenseRepository provideExpenseRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return ExpenseRepository.getInstance(database.categoryDao(), database.expenseDao(), executors);
    }

    public static FinancesRepository provideFinancesRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return FinancesRepository.getInstance(database.jobDao(), database.categoryDao(),
                database.expenseDao(), executors);
    }

    public static FinancesViewModelFactory provideFinancesViewModelFactory(Context context) {
        return new FinancesViewModelFactory(context);
    }

    public static CalendarViewModelFactory provideCalendarViewModelFactory(Context context) {
        CalendarRepository repository = provideCalendarRepository(context.getApplicationContext());
        return new CalendarViewModelFactory(repository);
    }

    public static NewJobViewModelFactory provideExpenseViewModelFactory(Context context){
        return new NewJobViewModelFactory(context);
    }

    public static JobEntry provideJobEntry(){
        return new JobEntry();
    }
}
