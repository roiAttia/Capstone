package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.ExpensesModel;

public class FinanceExpensesFragment extends BaseFinancialFragment {

    public static final String TAG = FinanceExpensesFragment.class.getSimpleName();

    private ExpensesAdapter mExpensesAdapter;
    private Unbinder mUnbinder;

    @BindView(R.id.rv_expenses) RecyclerView mExpensesListView;

    public FinanceExpensesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finances_expenses, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        setupRecyclerView();
        
        setupViewModel();

        return rootView;
    }

    private void setupRecyclerView() {
        mExpensesAdapter = new ExpensesAdapter(mListener, (ExpensesAdapter.OnExpenseClickHandler) getActivity());
        mExpensesListView.setHasFixedSize(true);
        mExpensesListView.setAdapter(mExpensesAdapter);
        mExpensesListView.setLayoutManager(new LinearLayoutManager(mListener));
    }

    private void setupViewModel() {
        FinancesViewModel viewModel = ViewModelProviders.of(getActivity()).get(FinancesViewModel.class);
        viewModel.getExpensesLiveModel().observe(getActivity(), new Observer<List<ExpensesModel>>() {
            @Override
            public void onChanged(@Nullable List<ExpensesModel> expensesModels) {
                mExpensesAdapter.setData(expensesModels);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
