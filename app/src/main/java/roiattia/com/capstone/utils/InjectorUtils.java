package roiattia.com.capstone.utils;

import android.content.Context;

import org.joda.time.LocalDate;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.ui.calendar.CalendarRepository;
import roiattia.com.capstone.ui.category.CategoryDetailsRepository;
import roiattia.com.capstone.ui.category.CategoryDetailsViewModelFactory;
import roiattia.com.capstone.ui.finances.FinancesRepository;
import roiattia.com.capstone.ui.newexpense.ExpenseRepository;
import roiattia.com.capstone.ui.newexpense.ExpenseViewModelFactory;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.ui.newjob.JobViewModelFactory;

public class InjectorUtils {

    public static JobRepository provideJobRepository(Context context) {
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return JobRepository.getInstance(database.jobDao(), database.categoryDao(),
                database.expenseDao(), executors);
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

    public static CategoryDetailsRepository provideCategoryDetailsRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return CategoryDetailsRepository.getInstance(database.expenseDao(), executors);
    }

    /* FACTORIES */
    public static JobViewModelFactory provideNewJobViewModelFactory(
            Context context, Long jobId, JobRepository.GetJobIdHandler callback){
        JobRepository jobRepository = provideJobRepository(context);
        return new JobViewModelFactory(jobRepository, jobId, callback);
    }

    public static ExpenseViewModelFactory provideExpenseViewModelFactory(
            Context context, Long expenseId, ExpenseRepository.GetIdHandler handler){
        ExpenseRepository expenseRepository = provideExpenseRepository(context);
        return new ExpenseViewModelFactory(expenseRepository, expenseId, handler);
    }

    public static CategoryDetailsViewModelFactory provideCategoryDetailsViewModelFactory(
            Context context, long categoryId, LocalDate start, LocalDate end){
        CategoryDetailsRepository repository = provideCategoryDetailsRepository(context);
        return new CategoryDetailsViewModelFactory(categoryId, start, end, repository);
    }
}
