package roiattia.com.capstone.utils;

import android.content.Context;

import org.joda.time.LocalDate;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.ui.calendar.CalendarRepository;
import roiattia.com.capstone.ui.expenses_list.ExpensesListRepository;
import roiattia.com.capstone.ui.expenses_list.ExpensesListViewModelFactory;
import roiattia.com.capstone.ui.payments.PaymentsRepository;
import roiattia.com.capstone.ui.payments.PaymentsViewModelFactory;
import roiattia.com.capstone.ui.finances.FinancesRepository;
import roiattia.com.capstone.ui.newexpense.ExpenseRepository;
import roiattia.com.capstone.ui.newexpense.ExpenseViewModelFactory;
import roiattia.com.capstone.ui.newjob.JobRepository;
import roiattia.com.capstone.ui.newjob.JobViewModelFactory;

public class InjectorUtils {

    public static JobRepository provideJobRepository(Context context, JobRepository.DataInsertHandler callback) {
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return JobRepository.getInstance(database.jobDao(), database.categoryDao(),
                database.expenseDao(), executors, callback);
    }

    public static CalendarRepository provideCalendarRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return CalendarRepository.getInstance(database, executors);
    }

    public static ExpenseRepository provideExpenseRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return ExpenseRepository.getInstance(
                database.categoryDao(), database.expenseDao(), database.paymentDao(), executors);
    }

    public static FinancesRepository provideFinancesRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return FinancesRepository.getInstance(database.jobDao(), database.expenseDao(), executors);
    }

    public static PaymentsRepository providePaymentsRepository(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        return PaymentsRepository.getInstance(database.paymentDao());
    }

    public static ExpensesListRepository provideExpensesListRepository(Context context) {
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return ExpensesListRepository.getInstance(database.expenseDao(), executors);
    }

    /* FACTORIES */
    public static JobViewModelFactory provideNewJobViewModelFactory(
            Context context, Long jobId, JobRepository.DataInsertHandler callback){
        JobRepository jobRepository = provideJobRepository(context, callback);
        return new JobViewModelFactory(jobRepository, jobId);
    }

    public static ExpenseViewModelFactory provideExpenseViewModelFactory(
            Context context, Long expenseId, ExpenseRepository.GetIdHandler handler){
        ExpenseRepository expenseRepository = provideExpenseRepository(context);
        return new ExpenseViewModelFactory(expenseRepository, expenseId, handler);
    }

    public static PaymentsViewModelFactory provideCategoryDetailsViewModelFactory(
            Context context, long categoryId, LocalDate start, LocalDate end){
        PaymentsRepository repository = providePaymentsRepository(context);
        return new PaymentsViewModelFactory(categoryId, start, end, repository);
    }

    public static ExpensesListViewModelFactory provideExpensesListViewModelFactory(
            Context context, LocalDate start, LocalDate end){
        ExpensesListRepository repository = provideExpensesListRepository(context);
        return new ExpensesListViewModelFactory(start, end, repository);
    }

}
