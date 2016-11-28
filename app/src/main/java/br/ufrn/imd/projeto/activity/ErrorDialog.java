package br.ufrn.imd.projeto.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import br.ufrn.imd.projeto.R;

public class ErrorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int code = getArguments().getInt("code", -1);
        String message;

        switch (code) {
            case 0:
                message = getResources().getString(R.string.error0);
                break;
            case 1:
                message = getResources().getString(R.string.error1);
                break;
            case 2:
                message = getResources().getString(R.string.error2);
                break;
            default:
                message = getResources().getString(R.string.error);
                break;
        }

        builder
                .setTitle(getResources().getString(R.string.error))
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(R.string.ok, null);

        return builder.create();
    }

}
