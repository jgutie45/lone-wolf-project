package com.example.lonewolf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmHelpRequest extends DialogFragment {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = db.getReference("message");

    private final String location;

    public ConfirmHelpRequest(String l) {
        this.location = l;
    }

    public interface AlertDialogListener {
        void onDialogPositiveClick(DialogFragment d);
        void onDialogNegativeClick(DialogFragment d);
    }

    AlertDialogListener listener = new AlertDialogListener() {
        @Override
        public void onDialogPositiveClick(DialogFragment d) {
            ref.child("help_requests").setValue(location);
            Log.d("LOC", "location " + location + " sent");
        }

        @Override
        public void onDialogNegativeClick(DialogFragment d) {
            ConfirmHelpRequest.this.dismiss();
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setMessage("Request Help?")
                .setPositiveButton("Yes", (dialog, which) ->
                        listener.onDialogPositiveClick(ConfirmHelpRequest.this))
                .setNegativeButton("No", (dialog, which) ->
                        listener.onDialogNegativeClick(ConfirmHelpRequest.this));
        return b.create();
    }

}
