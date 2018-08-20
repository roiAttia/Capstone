package roiattia.com.capstone.ui.newjob;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;

public class BaseFragment extends Fragment {

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

    /**
     * Setup categories spinner with values from category table
     * @param categoryEntries categories to inert to spinner
     */
    public void setupCategoriesSpinner(Spinner spinner, List<CategoryEntry> categoryEntries) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add(getString(R.string.spinner_category_default_value));
        if(categoryEntries != null) {
            for (CategoryEntry categoryEntry : categoryEntries) {
                categoriesNames.add(categoryEntry.getName());
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    mListener, android.R.layout.simple_spinner_item, categoriesNames);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        }
    }
}
