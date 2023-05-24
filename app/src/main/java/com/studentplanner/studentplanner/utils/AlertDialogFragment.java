package com.studentplanner.studentplanner.utils;

import static com.studentplanner.studentplanner.utils.Helper.readStream;

import android.content.Context;
import android.text.Html;

import androidx.appcompat.app.AlertDialog;

import com.studentplanner.studentplanner.R;

import org.apache.commons.text.WordUtils;


public class AlertDialogFragment {
    private final Context context;
    public boolean isDeleted = false;

    public AlertDialogFragment(Context context) {
        this.context = context;
    }

    public void showTermsPolicyError(){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.accept_terms_and_conditions))
                .setPositiveButton(context.getString(R.string.ok), (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }



    public void showTermsAndConditions(){

        String title = WordUtils.capitalizeFully(context.getString(R.string.terms_and_conditions_title));
        String message = readStream(context.getResources().openRawResource(R.raw.policy));

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Html.fromHtml(message,1))
//        setMessage(Html.fromHtml("your message,<a href='http://www.google.com'>link</a>"))
                .setNegativeButton(context.getString(R.string.close), (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }





}
