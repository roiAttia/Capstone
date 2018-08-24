package roiattia.com.capstone.ui.finances;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;

public class BaseFinancialFragment extends Fragment {

    public FragmentActivity mListener;

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

}
