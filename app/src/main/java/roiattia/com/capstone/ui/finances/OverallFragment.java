package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.ui.dialogs.RadioButtonsDialog;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class OverallFragment extends BaseFinancialFragment {

    public static final String TAG = OverallFragment.class.getSimpleName();

    private FinancesViewModel mViewModel;
    private DateModel mDateModel;
    private Unbinder unbinder;

    @BindView(R.id.tv_current_income)TextView mCurrentIncomeView;
    @BindView(R.id.tv_current_expenses)TextView mCurrentExpensesView;
    @BindView(R.id.tv_current_profits)TextView mCurrentProfitsView;
    @BindView(R.id.tv_expected_income)TextView mExpectedIncomeView;
    @BindView(R.id.tv_expected_expenses)TextView mExpectedExpensesView;
    @BindView(R.id.tv_expected_profits)TextView mExpectedProfitsView;
    @BindView(R.id.tv_overall_income)TextView mOverallIncomeView;
    @BindView(R.id.tv_overall_expenses)TextView mOverallExpensesView;
    @BindView(R.id.tv_overall_profits)TextView mOverallProfitsView;
    @BindView(R.id.tv_current_date)TextView mCurrentDateText;
    @BindView(R.id.tv_expected_date)TextView mExpectedText;
    @BindView(R.id.tv_overall_date)TextView mOverallText;
    @BindView(R.id.btn_select_period)Button mSelectPeriodButton;

    @OnClick(R.id.btn_select_period)
    public void selectPeriod(){
        RadioButtonsDialog pickPeriodDialog = new RadioButtonsDialog();
        final String[] itemsFromR = getResources().getStringArray(R.array.period_selection_options);
        pickPeriodDialog.setData(itemsFromR);
        pickPeriodDialog.setTitle("Select period");
        pickPeriodDialog.show(getChildFragmentManager(), "pop");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overall, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mDateModel = DateModel.getInstance();

        setupViewModel();

        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setupViewModel() {
        final double[] profits = {0};
        mViewModel = ViewModelProviders.of(getActivity()).get(FinancesViewModel.class);
        // current
        mViewModel.getCurrentFromIncomeProfitLiveData().observe(this, new Observer<OverallIncomeModel>() {
            @Override
            public void onChanged(@Nullable OverallIncomeModel overallIncomeModel) {
                if(overallIncomeModel != null) {
                    mCurrentIncomeView.setText(String.format("%s%s", getString(R.string.overall_income),
                            AmountUtils.getStringFormatFromDouble(overallIncomeModel.getIncome())));
                    profits[0] = overallIncomeModel.getProfit();
                    mCurrentDateText.setText(String.format("%s %s - %s", getString(R.string.current),
                            DateUtils.getDateStringFormat(mDateModel.getCurrentFromDate()),
                            DateUtils.getDateStringFormat(mDateModel.getCurrentToDate())));
                }
            }
        });
        mViewModel.getCurrentExpensesLiveData().observe(this, new Observer<OverallExpensesModel>() {
            @Override
            public void onChanged(@Nullable OverallExpensesModel overallExpensesModel) {
                if(overallExpensesModel != null){
                    mCurrentExpensesView.setText(String.format("%s%s", getString(R.string.overall_expenses),
                            AmountUtils.getStringFormatFromDouble(overallExpensesModel.getCost())));
                    profits[0] -= overallExpensesModel.getCost();
                    mCurrentProfitsView.setText(String.format("%s%s", getString(R.string.overall_profits),
                            AmountUtils.getStringFormatFromDouble(profits[0])));
                }
            }
        });
        profits[0] = 0;
        // expected
        mViewModel.getExpectedIncomeProfitLiveData().observe(this, new Observer<OverallIncomeModel>() {
            @Override
            public void onChanged(@Nullable OverallIncomeModel overallIncomeModel) {
                if(overallIncomeModel != null) {
                    mExpectedIncomeView.setText(String.format("%s%s", getString(R.string.overall_income),
                            AmountUtils.getStringFormatFromDouble(overallIncomeModel.getIncome())));
                    profits[0] = overallIncomeModel.getProfit();
                    mExpectedText.setText(String.format("%s %s - %s", getString(R.string.current),
                            DateUtils.getDateStringFormat(mDateModel.getExpectedFromDate()),
                            DateUtils.getDateStringFormat(mDateModel.getExpectedToDate())));
                }
            }
        });
        mViewModel.getExpectedExpensesLiveData().observe(this, new Observer<OverallExpensesModel>() {
            @Override
            public void onChanged(@Nullable OverallExpensesModel overallExpensesModel) {
                if(overallExpensesModel != null){
                    mExpectedExpensesView.setText(String.format("%s%s", getString(R.string.overall_expenses),
                            AmountUtils.getStringFormatFromDouble(overallExpensesModel.getCost())));
                    profits[0] -= overallExpensesModel.getCost();
                    mExpectedProfitsView.setText(String.format("%s%s", getString(R.string.overall_profits),
                            AmountUtils.getStringFormatFromDouble(profits[0])));
                }
            }
        });
        profits[0] = 0;
        // overall
        mViewModel.getOverallIncomeProfitLiveData().observe(this, new Observer<OverallIncomeModel>() {
            @Override
            public void onChanged(@Nullable OverallIncomeModel overallIncomeModel) {
                if(overallIncomeModel != null) {
                    mOverallIncomeView.setText(String.format("%s%s", getString(R.string.overall_income),
                            AmountUtils.getStringFormatFromDouble(overallIncomeModel.getIncome())));
                    profits[0] = overallIncomeModel.getProfit();
                    mOverallText.setText(String.format("%s %s - %s", getString(R.string.current),
                            DateUtils.getDateStringFormat(mDateModel.getCurrentFromDate()),
                            DateUtils.getDateStringFormat(mDateModel.getExpectedToDate())));
                }
            }
        });
        mViewModel.getOverallExpensesLiveData().observe(this, new Observer<OverallExpensesModel>() {
            @Override
            public void onChanged(@Nullable OverallExpensesModel overallExpensesModel) {
                if(overallExpensesModel != null){
                    mOverallExpensesView.setText(String.format("%s%s", getString(R.string.overall_expenses),
                            AmountUtils.getStringFormatFromDouble(overallExpensesModel.getCost())));
                    profits[0] -= overallExpensesModel.getCost();
                    mOverallProfitsView.setText(String.format("%s%s", getString(R.string.overall_profits),
                            AmountUtils.getStringFormatFromDouble(profits[0])));
                }
            }
        });

    }

    public void updatePeriodText(LocalDate startDate, LocalDate endDate) {
        if(mSelectPeriodButton != null){
            mSelectPeriodButton.setText(DateUtils.getDateStringFormat(startDate) + " - " +
                    DateUtils.getDateStringFormat(endDate));
        }
    }
}
