package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;

public class BaseFinancialFragment extends Fragment {

    public FragmentActivity mListener;
    public Unbinder mUnbinder;
    public FinancesViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(mListener).get(FinancesViewModel.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mListener));
    }

}
