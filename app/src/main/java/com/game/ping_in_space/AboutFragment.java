package com.game.ping_in_space;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

@SuppressWarnings("WeakerAccess")
public class AboutFragment extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.about, null);

        TextView textViewMail = view.findViewById(R.id.textViewMail);
        TextView textViewTel = view.findViewById(R.id.textViewTel);

        textViewMail.setOnClickListener(new View.OnClickListener() {        //donne la possibilité de contacter en cliquant sur le texte
            public void onClick(View v) {
                Uri mail = Uri.parse(getString(R.string.Uri_mail));
                Intent intent = new Intent(Intent.ACTION_SENDTO, mail);
                startActivity(intent);
            }
        });

        textViewTel.setOnClickListener(new View.OnClickListener() {         //donne la possibilité de contacter en cliquant sur le texte
            public void onClick(View v) {
                Uri tel = Uri.parse(getString(R.string.Uri_tel));
                Intent intent = new Intent(Intent.ACTION_DIAL, tel);
                startActivity(intent);
            }
        });

        builder.setView(view);
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {/*ferme la boite de dialogue*/ }});
        return builder.create();
    }
}
