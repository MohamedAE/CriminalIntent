package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class PhotoPreviewFragment extends DialogFragment {

    private static final String ARG_IMAGE = "image";

    public static PhotoPreviewFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, photoFile);

        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        File photoFile = (File) getArguments().getSerializable(ARG_IMAGE);

        Bitmap image = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        ImageView imageView = (ImageView) v.findViewById(R.id.preview_image);
        imageView.setImageBitmap(image);

        return new AlertDialog.Builder(getActivity()).setView(imageView).create();
    }

}