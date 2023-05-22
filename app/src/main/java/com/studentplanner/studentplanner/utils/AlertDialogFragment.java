package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.studentplanner.studentplanner.R;


public class AlertDialogFragment {
    private final Context context;
    public boolean isDeleted = false;

    public AlertDialogFragment(Context context) {
        this.context = context;
    }

    public void showTermsPolicyError(){
        new AlertDialog.Builder(context)
                .setTitle("Please accept the terms and conditions")
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
    public void showTermsAndConditions(){

        String title = "Student Planner Terms and Conditions";
        String message = "Don't bother typing “lorem ipsum” into Google translate. If you already tried, you may have gotten anything from \"NATO\" to \"China\", depending on how you capitalized the letters. The bizarre translation was fodder for conspiracy theories, but Google has since updated its “lorem ipsum” translation to, boringly enough, “lorem ipsum”.\n" +
                "\n" +
                "One brave soul did take a stab at translating the almost-not-quite-Latin. According to The Guardian, Jaspreet Singh Boparai undertook the challenge with the goal of making the text “precisely as incoherent in English as it is in Latin - and to make it incoherent in the same way”. As a result, “the Greek 'eu' in Latin became the French 'bien' [...] and the '-ing' ending in 'lorem ipsum' seemed best rendered by an '-iendum' in English.”";
        message+="The French lettering company Letraset manufactured a set of dry-transfer sheets which included the lorem ipsum filler text in a variety of fonts, sizes, and layouts. These sheets of lettering could be rubbed on anywhere and were quickly adopted by graphic artists, printers, architects, and advertisers for their professional look and ease of use.\n" +
                "\n";
        message+="The French lettering company Letraset manufactured a set of dry-transfer sheets which included the lorem ipsum filler text in a variety of fonts, sizes, and layouts. These sheets of lettering could be rubbed on anywhere and were quickly adopted by graphic artists, printers, architects, and advertisers for their professional look and ease of use.\n" +
                "\n";
        message+="The French lettering company Letraset manufactured a set of dry-transfer sheets which included the lorem ipsum filler text in a variety of fonts, sizes, and layouts. These sheets of lettering could be rubbed on anywhere and were quickly adopted by graphic artists, printers, architects, and advertisers for their professional look and ease of use.\n" +
                "\n";
        message+="The French lettering company Letraset manufactured a set of dry-transfer sheets which included the lorem ipsum filler text in a variety of fonts, sizes, and layouts. These sheets of lettering could be rubbed on anywhere and were quickly adopted by graphic artists, printers, architects, and advertisers for their professional look and ease of use.\n" +
                "\n";
        message+="The French lettering company Letraset manufactured a set of dry-transfer sheets which included the lorem ipsum filler text in a variety of fonts, sizes, and layouts. These sheets of lettering could be rubbed on anywhere and were quickly adopted by graphic artists, printers, architects, and advertisers for their professional look and ease of use.\n" +
                "\n";


        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.close), (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    public boolean showModuleDeleteDialog(){
        final boolean[] d = {false};
        new AlertDialog.Builder(context)
                .setMessage("Doing so will delete all associated coursework and classes with this module").setCancelable(false)
                .setTitle("Are you sure you want to delete this module?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d[0] = true;
                    }
                })
//                .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
//                    return true;
//                })
                .setNegativeButton(context.getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();
        return d[0];

    }
}
