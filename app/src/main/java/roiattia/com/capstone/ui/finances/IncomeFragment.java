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
import roiattia.com.capstone.model.IncomeModel;

public class IncomeFragment extends BaseFinancialFragment{

    public static final String TAG = IncomeFragment.class.getSimpleName();

    private IncomeAdapter mIncomeJobsAdapter;

    @BindView(R.id.rv_income) RecyclerView mIncomeListView;

    public IncomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);
        super.mUnbinder = ButterKnife.bind(this, rootView);

        setupRecyclerView();

        setupViewModel();

        return rootView;
    }

    private void setupViewModel() {
        mViewModel.getIncomeModelLiveData().observe(this, new Observer<List<IncomeModel>>() {
            @Override
            public void onChanged(@Nullable List<IncomeModel> incomeModelList) {
                mIncomeJobsAdapter.setData(incomeModelList);
            }
        });
    }

    private void setupRecyclerView() {
        mIncomeJobsAdapter = new IncomeAdapter(mListener);
        super.setupRecyclerView(mIncomeListView, mIncomeJobsAdapter);
    }

}
