package roiattia.com.capstone.ui.finances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class OverallFragment extends BaseFinancialFragment {

    public static final String TAG = OverallFragment.class.getSimpleName();

    private OverallIncomeModel mCurrentFinancial;
    private OverallIncomeModel mExpectedFinancial;
    private OverallExpensesModel mCurrentExpenses;
    private OverallExpensesModel mExpectedExpenses;
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    @BindView(R.id.tv_current_income)TextView mCurrentIncomeView;
    @BindView(R.id.tv_current_expenses)TextView mCurrentExpensesView;
    @BindView(R.id.tv_current_profits)TextView mCurrentProfitsView;
    @BindView(R.id.tv_expected_income)TextView mExpectedIncomeView;
    @BindView(R.id.tv_expected_expenses)TextView mExpectedExpensesView;
    @BindView(R.id.tv_expected_profits)TextView mExpectedProfitsView;
    @BindView(R.id.btn_select_period)Button mSelectPeriodButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overall, container, false);
        ButterKnife.bind(this, rootView);
        Log.i(TAG, "onCreateView");

        mSelectPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickPeriodDialog pickPeriodDialog = new PickPeriodDialog();
                if (getFragmentManager() != null) {
                    pickPeriodDialog.show(getFragmentManager(), "pop");
                }
            }
        });

        if(mStartDate != null && mEndDate != null) {
            mSelectPeriodButton.setText(DateUtils.getDateStringFormat(mStartDate) + " - " +
                    DateUtils.getDateStringFormat(mEndDate));
        }

        updateCurrentCard();
        updateExpectedCard();
        updateCurrentExpenses();
        updateExpectedExpenses();

        return rootView;
    }

    public void setCurrentData(OverallIncomeModel financialModel){
        mCurrentFinancial = financialModel;
        updateCurrentCard();
    }

    public void setExpectedData(OverallIncomeModel financialModel){
        mExpectedFinancial = financialModel;
        updateExpectedCard();
    }

    public void setCurrentExpensesData(OverallExpensesModel expensesModel){
        mCurrentExpenses = expensesModel;
        updateCurrentExpenses();
    }

    public void setExpectedExpensesData(OverallExpensesModel expensesModel){
        mExpectedExpenses = expensesModel;
        updateExpectedExpenses();
    }

    public void updateCurrentCard() {
        Log.i(TAG, "updateCurrentCard");
        if(mCurrentFinancial != null) {
            mCurrentIncomeView.setText(String.format("INCOME: %s", AmountUtils.getStringFormatFromDouble(
                    mCurrentFinancial.getIncome())));
            mCurrentProfitsView.setText(String.format("PROFITS: %s", AmountUtils.getStringFormatFromDouble(
                    mCurrentFinancial.getProfit())));
        }
    }

    public void updateExpectedCard() {
        Log.i(TAG, "updateExpectedCard");
        if(mExpectedFinancial != null) {
            mExpectedIncomeView.setText(String.format("INCOME: %s", AmountUtils.getStringFormatFromDouble(
                    mExpectedFinancial.getIncome())));
            mExpectedProfitsView.setText(String.format("PROFITS: %s", AmountUtils.getStringFormatFromDouble(
                    mExpectedFinancial.getProfit())));
        }
    }

    public void updateCurrentExpenses() {
        Log.i(TAG, "updateCurrentExpenses");
        if(mCurrentExpenses != null) {
            mCurrentExpensesView.setText(String.format("EXPENSES: %s", AmountUtils.getStringFormatFromDouble(
                    mCurrentExpenses.getCost())));
        }
    }

    public void updateExpectedExpenses() {
        Log.i(TAG, "updateExpectedExpenses");
        if(mExpectedExpenses != null) {
            mExpectedExpensesView.setText(String.format("EXPENSES: %s", AmountUtils.getStringFormatFromDouble(
                    mExpectedExpenses.getCost())));
        }
    }

    public void updatePeriodText(LocalDate startDate, LocalDate endDate) {
        mStartDate = startDate;
        mEndDate = endDate;
        if(mSelectPeriodButton != null){
            mSelectPeriodButton.setText(DateUtils.getDateStringFormat(mStartDate) + " - " +
                    DateUtils.getDateStringFormat(mEndDate));
        }
    }
}
