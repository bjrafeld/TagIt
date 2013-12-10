package edu.berkeley.cs160.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Author: Daniel Roth
 * Date: 12/10/13
 */
public class PhotoDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add/Edit Help");
        builder.setMessage("TagIt uses actual photographs of boxes, as well as searchable text fields, to allow you to" +
                           "conveniently find and view your boxes.\n\n" +
                           "1) Tap \"Take Contents Photo\" to take a picture of your full, open box.\n\n" +
                           "2) Tap \"Take Tag Photo\" to take a picture of the box's label." +
                           "In the future, you will be able to see the contents of this box just by " +
                           "taking another picture like of the label." +
                           "3) Enter a location for your box.  If you are moving, this might be the " +
                           "room in which this box is associated, or if the box is in storage, this might" +
                           "be the box's physical location.\n\n" +
                           "4) Add as many text descriptions of the contents of your box as you would like.  These " +
                           "descriptors are searchable, so the more the better.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PhotoDialogFragment.this.getDialog().dismiss();
            }
        });
        return builder.create();
    }
}
