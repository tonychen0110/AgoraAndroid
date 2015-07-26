package tonychen.agora.FrontEnd.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.Arrays;
import java.util.List;

import tonychen.agora.R;


public class SecondaryPhotoSourceDialog extends DialogFragment {
    private List<String> sources;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_secondary_photo);

        sources = Arrays.asList(getResources().getStringArray(R.array.photo_sources));
        builder.setItems(R.array.photo_sources, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {

                if (sources.get(position).equals(getResources().getString(R.string.camera_source))) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(camera, getResources().getInteger(R.integer.REQUEST_CAMERA_SECONDARY));

                } else if (sources.get(position).equals(getResources().getString(R.string.gallery_source))) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    gallery.setType("image/*");
                    getActivity().startActivityForResult(gallery, getResources().getInteger(R.integer.SELECT_FILE_SECONDARY));
                }
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement PhotoSourceDialogListener");
        }
    }
}
