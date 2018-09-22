package roiattia.com.capstone.ui.finances;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

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
