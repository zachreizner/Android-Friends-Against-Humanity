package com.zach297.friendsagainsthumanity.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class NameDialog extends DialogFragment {
    public interface NameListener {
        public void onChangeName(String name);
    }

    private EditText editText;
    private String name;
    private NameListener listener;

    public void setName(String name) {
        this.name = name;
        if (editText != null) {
            editText.setText(name);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder
                .setTitle(R.string.action_change_user)
                .setView(inflater.inflate(R.layout.dialog_name, null))
                .setPositiveButton("Change Name", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getTargetFragment() != null) {
                            ((NameListener)getTargetFragment()).onChangeName(editText.getText().toString());
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        editText = (EditText) this.getDialog().findViewById(R.id.name_edit_text);
        editText.setText(name);
    }
}
