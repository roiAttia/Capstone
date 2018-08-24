package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.FinancialModel;
import roiattia.com.capstone.utils.InjectorUtils;

public class OverallFragment extends BaseFinancialFragment {

    public static final String TAG = OverallFragment.class.getSimpleName();
    private FinancesViewModel mViewModel;

    @BindView(R.id.tv_current_income)TextView mCurrentIncomeView;
    @BindView(R.id.tv_current_expenses)TextView mCurrentExpensesView;
    @BindView(R.id.tv_current_profits)TextView mCurrentProfitsView;
    @BindView(R.id.tv_expected_income)TextView mExpectedIncome;
    @BindView(R.id.tv_expected_expenses)TextView mExpectedExpenses;
    @BindView(R.id.tv_expected_profits)TextView mExpectedProfits;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overall, container, false);
        ButterKnife.bind(this, rootView);

        LocalDate startDate = new LocalDate();
        startDate = startDate.plusMonths(0).withDayOfMonth(1);
        LocalDate finishDate = new LocalDate();

        // setup view_model
        FinancesViewModelFactory factory = InjectorUtils
                .provideFinancesViewModelFactory(mListener);
        mViewModel = ViewModelProviders.of(mListener, factory)
                .get(FinancesViewModel.class);
        final LocalDate finalStartDate = startDate;
        final LocalDate finalFinishDate = finishDate;
        mViewModel.getFinancialReport(startDate, finishDate)
                .observe(mListener, new Observer<List<FinancialModel>>() {
            @Override
            public void onChanged(@Nullable List<FinancialModel> financialModels) {
                if(financialModels != null) {
                    for (FinancialModel financialModel : financialModels) {
                        updateCurrentCard(financialModel);
                        Log.i(TAG, "first day: " + finalStartDate + ", end day: " + finalFinishDate);
                        Log.i(TAG, "current income: " + financialModel.getIncome());
                        Log.i(TAG, "current expenses: " + financialModel.getExpenses());
                        Log.i(TAG, "current profits: " + financialModel.getProfit());
                    }
                }
            }
        });

        startDate = new LocalDate();
        finishDate = startDate.plusMonths(1).withDayOfMonth(1);
        final LocalDate finalStartDate1 = startDate;
        final LocalDate finalFinishDate1 = finishDate;
        mViewModel.getFinancialReport(startDate, finishDate)
                .observe(mListener, new Observer<List<FinancialModel>>() {
                    @Override
                    public void onChanged(@Nullable List<FinancialModel> financialModels) {
                        if(financialModels != null) {
                            for (FinancialModel financialModel : financialModels) {
                                updateExpectedCard(financialModel);
                                Log.i(TAG, "first day: " + finalStartDate1 + ", end day: " + finalFinishDate1);
                                Log.i(TAG, "expected income: " + financialModel.getIncome());
                                Log.i(TAG, "expected expenses: " + financialModel.getExpenses());
                                Log.i(TAG, "expected profits: " + financialModel.getProfit());
                            }
                        }
                    }
                });

        return rootView;
    }

    private void updateExpectedCard(FinancialModel financialModel) {
        mExpectedIncome.append(String.valueOf(financialModel.getIncome()));
        mExpectedExpenses.append(String.valueOf(financialModel.getExpenses()));
        mExpectedProfits.append(String.valueOf(financialModel.getProfit()));
    }

    private void updateCurrentCard(FinancialModel financialModel) {
        mCurrentIncomeView.append(String.valueOf(financialModel.getIncome()));
        mCurrentExpensesView.append(String.valueOf(financialModel.getExpenses()));
        mCurrentProfitsView.append(String.valueOf(financialModel.getProfit()));
    }

    public void loadCurrentData(){

    }

    public void loadExpectedData(){

    }
}
