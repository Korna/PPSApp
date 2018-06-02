package com.coma.go.View.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.coma.go.R;
import com.coma.go.Utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageDialog extends DialogFragment {


    public ImageDialog() {}

    @BindView(R.id.imageView_dialog)
    ImageView imageView_image;

    String image = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        image = this.getArguments().getString("image");
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (image != null)
            Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .transition(withCrossFade())
                    .into(imageView_image);
        else
            ViewUtils.showToast(getContext(), "No image");
    }
}
