package roiattia.com.capstone.ui.finances;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.repositories.JobsRepository;
import roiattia.com.capstone.repositories.PaymentsRepository;

public class FinancesViewModel extends AndroidViewModel {

    private static final String TAG = FinancesViewModel.class.getSimpleName();

    private ExpensesRepository mExpensesRepository;
    private JobsRepository mJobsRepository;
    PaymentsRepository mPaymentsRepository;
    private AppExecutors mExecutors;
    private DateModel mDateModel;
    private MutableLiveData<OverallIncomeModel> mCurrentFromIncomeProfitLiveData;
    private MutableLiveData<OverallExpensesModel> mCurrentExpensesLiveData;
    private MutableLiveData<OverallIncomeModel> mExpectedIncomeProfitLiveData;
    private MutableLiveData<OverallExpensesModel> mExpectedExpensesLiveData;
    private MutableLiveData<OverallIncomeModel> mOverallIncomeProfitLiveData;
    private MutableLiveData<OverallExpensesModel> mOverallExpensesLiveData;

    public FinancesViewModel(@NonNull Application application) {
        super(application);
        mExecutors = AppExecutors.getInstance();
        mDateModel = DateModel.getInstance();
        mExpensesRepository = ExpensesRepository.getInstance(application.getApplicationContext());
        mJobsRepository = JobsRepository.getInstance(application.getApplicationContext());
        mPaymentsRepository = PaymentsRepository.getInstance(application.getApplicationContext());
        mCurrentFromIncomeProfitLiveData = new MutableLiveData<>();
        mCurrentExpensesLiveData = new MutableLiveData<>();
        mExpectedIncomeProfitLiveData = new MutableLiveData<>();
        mExpectedExpensesLiveData = new MutableLiveData<>();
        mOverallIncomeProfitLiveData = new MutableLiveData<>();
        mOverallExpensesLiveData = new MutableLiveData<>();
    }

    public void setDates(final LocalDate from, final LocalDate to, final MutableLiveData income,
                         final MutableLiveData expenses){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                OverallIncomeModel incomeModel = mJobsRepository.getIncomeAndProfitsBetweenDates(from, to);
                OverallExpensesModel expensesModel = mPaymentsRepository.getPaymentsBetweenDates(from, to);
                income.postValue(incomeModel);
                expenses.postValue(expensesModel);
            }
        });
    }

    public MutableLiveData<OverallIncomeModel> getCurrentFromIncomeProfitLiveData() {
        return mCurrentFromIncomeProfitLiveData;
    }

    public MutableLiveData<OverallExpensesModel> getCurrentExpensesLiveData() {
        return mCurrentExpensesLiveData;
    }

    public MutableLiveData<OverallIncomeModel> getExpectedIncomeProfitLiveData() {
        return mExpectedIncomeProfitLiveData;
    }

    public MutableLiveData<OverallExpensesModel> getExpectedExpensesLiveData() {
        return mExpectedExpensesLiveData;
    }

    public MutableLiveData<OverallIncomeModel> getOverallIncomeProfitLiveData() {
        return mOverallIncomeProfitLiveData;
    }

    public MutableLiveData<OverallExpensesModel> getOverallExpensesLiveData() {
        return mOverallExpensesLiveData;
    }

    public void updateDates() {
        setDates(mDateModel.getCurrentFromDate(), mDateModel.getCurrentToDate(), mCurrentFromIncomeProfitLiveData,
                 mCurrentExpensesLiveData);
        setDates(mDateModel.getExpectedFromDate(), mDateModel.getExpectedToDate(), mExpectedIncomeProfitLiveData,
                mExpectedExpensesLiveData);
        setDates(mDateModel.getCurrentFromDate(), mDateModel.getExpectedToDate(), mOverallIncomeProfitLiveData,
                mOverallExpensesLiveData);

    }


}
