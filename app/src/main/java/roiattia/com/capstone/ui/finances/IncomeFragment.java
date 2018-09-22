package roiattia.com.capstone.ui.finances;

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
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.IncomeModel;

public class IncomeFragment extends BaseFinancialFragment{

    public static final String TAG = IncomeFragment.class.getSimpleName();

    private IncomeAdapter mIncomeJobsAdapter;
    private List<IncomeModel> mIncomeModelList;

    @BindView(R.id.rv_income) RecyclerView mIncomeListView;

    public IncomeFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);
        ButterKnife.bind(this, rootView);

        mIncomeJobsAdapter = new IncomeAdapter(mListener);
        mIncomeListView.setHasFixedSize(true);
        mIncomeListView.setAdapter(mIncomeJobsAdapter);
        mIncomeListView.setLayoutManager(new LinearLayoutManager(mListener));
        if(mIncomeModelList != null){
            mIncomeJobsAdapter.setData(mIncomeModelList);
        } else {
            Log.i(TAG, "mIncomeJobsAdapter is null");
        }

        return rootView;
    }

    public void setData(List<IncomeModel> incomeModelList) {
        mIncomeModelList = incomeModelList;
        if(mIncomeJobsAdapter != null){
            mIncomeJobsAdapter.setData(incomeModelList);
        } else {
            Log.i(TAG, "mIncomeJobsAdapter is null");
        }
    }

}
