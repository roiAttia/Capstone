package roiattia.com.capstone.ui.finances;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.utils.InjectorUtils;

public class FinancesViewModel extends AndroidViewModel {

    private static final String TAG = FinancesViewModel.class.getSimpleName();

    private FinancesRepository mRepository;
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    public FinancesViewModel(@NonNull Application application) {
        super(application);
        mRepository = InjectorUtils.provideFinancesRepository(this.getApplication());
        mStartDate = new LocalDate();
        mEndDate = new LocalDate();
    }

    public LiveData<OverallIncomeModel> getCurrentOverallReport() {
        return mRepository.getJobsBetweenDates(mStartDate, new LocalDate());
    }

    public LiveData<OverallIncomeModel> getExpectedOverallReport() {
        return mRepository.getJobsBetweenDates(new LocalDate(), mEndDate);
    }

    public LiveData<OverallExpensesModel> getCurrentExpensesReport() {
        return mRepository.getExpensesBetweenDates(mStartDate, new LocalDate());
    }

    public LiveData<OverallExpensesModel> getExpectedExpensesReport() {
        return mRepository.getExpensesBetweenDates(new LocalDate(), mEndDate);
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

    public LiveData<List<ExpensesModel>> getPaymentsReport() {
        return mRepository.getPaymentsPerCategoryBetweenDates(mStartDate, mEndDate);
    }

}
