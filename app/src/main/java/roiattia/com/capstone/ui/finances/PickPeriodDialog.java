package roiattia.com.capstone.ui.finances;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import roiattia.com.capstone.R;

public class PickPeriodDialog extends DialogFragment {

    private static final String TAG = PickPeriodDialog.class.getSimpleName();
    private int mWhichSelected = -1;
    private NoticeDialogListener mListener;

    public interface NoticeDialogListener {
        void onDialogFinishClick(int whichSelected);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final String[] itemsFromR = getActivity().getResources().getStringArray(R.array.period_selection_options);
        dialog.setTitle("Pick period")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(itemsFromR, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWhichSelected = which;
                        Log.i(TAG, "item clicked position: " + which + "" +
                                "item in array: "  +itemsFromR[which]);
                    }
                })
                // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogFinishClick(mWhichSelected);
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogFinishClick(mWhichSelected);
                    }
                });
        return dialog.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
