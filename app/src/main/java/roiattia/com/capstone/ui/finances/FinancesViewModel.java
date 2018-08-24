package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.FinancialModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.utils.InjectorUtils;

public class FinancesViewModel extends ViewModel {

    private static final String TAG = FinancesViewModel.class.getSimpleName();

    private FinancesRepository mRepository;
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    FinancesViewModel(Context context) {
        mRepository = InjectorUtils.provideFinancesRepository(context);
        mStartDate = new LocalDate();
        mStartDate = mStartDate.plusMonths(0).withDayOfMonth(1);
        mEndDate = new LocalDate();
        mEndDate = mEndDate.plusMonths(1).withDayOfMonth(1);
    }

    public LiveData<List<FinancialModel>> getFinancialReport(LocalDate startDate, LocalDate endDate) {
        return mRepository.getJobsBetweenDates(startDate, endDate);
    }

    public LiveData<List<IncomeModel>> getIncomeReport(CategoryEntry.Type type) {
        Log.i(TAG, "get income report start date: " + mStartDate + ", end date: " + mEndDate);
        return mRepository.getIncomeBetweenDates(mStartDate, mEndDate, type);
    }

    public LiveData<List<ExpensesModel>> getExpensesReport(CategoryEntry.Type type) {
        return mRepository.getExpensesBetweenDates(mStartDate, mEndDate, type);
    }

    public void setDatesRange(LocalDate startDate, LocalDate endDate){
        mStartDate = startDate;
        mEndDate = endDate;
    }

    public LiveData<List<ExpenseEntry>> getExpensesByCategory(long categoryId){
        return mRepository.getExpensesByCategoryId(categoryId);
    }

}
