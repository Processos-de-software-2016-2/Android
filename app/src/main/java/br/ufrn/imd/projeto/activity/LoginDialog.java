package br.ufrn.imd.projeto.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import br.ufrn.imd.projeto.R;

public class LoginDialog extends DialogFragment {
    private static final String TAG1 = "add abilities: ";
    private EditText user;
    private EditText password;

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG1,"second");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_login, null);

        user = (EditText) view.findViewById(R.id.etUser);
        password = (EditText) view.findViewById(R.id.etPassword);

        builder.setView(view);
        return builder.create();
    }

    public String getUser() {
        return user.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }

}