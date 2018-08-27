package roiattia.com.capstone.ui.newjob;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;

public class BaseJobFragment extends Fragment {

    private static final String TAG = BaseJobFragment.class.getSimpleName();

    /**
     * Setup categories spinner with values from category table
     * @param categoryEntries categories to inert to spinner
     *
     */
    public void setupCategoriesSpinner(Spinner spinner, List<CategoryEntry> categoryEntries) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesNames.add(getActivity().getString(R.string.spinner_category_default_value));
        if(categoryEntries != null) {
            for (CategoryEntry categoryEntry : categoryEntries) {
                categoriesNames.add(categoryEntry.getCategoryName());
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_spinner_item, categoriesNames);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        }
    }
}
