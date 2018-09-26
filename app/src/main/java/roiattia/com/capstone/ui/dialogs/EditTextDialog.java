package roiattia.com.capstone.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import roiattia.com.capstone.R;

public class EditTextDialog extends DialogFragment {

    private static final String TAG = EditTextDialog.class.getSimpleName();
    private String mTitle;
    private EditTextDialogListener mListener;

    public void setTitle(String title){
        mTitle = title;
    }

    public interface EditTextDialogListener {
        void onDialogFinishClick(String input);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_text, null, false);

        builder.setView(view)
                .setTitle(mTitle)
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view.findViewById(R.id.et_user_input);
                        mListener.onDialogFinishClick(editText.getText().toString());
                        dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the EditTextDialogListener so we can send events to the host
            mListener = (EditTextDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditTextDialogListener");
        }
    }
}
