package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TableRow;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.ExifDelegate;
import com.github.boybeak.timepaper.model.Exif;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

public class ExifHolder extends AbsViewHolder<ExifDelegate> {

    private TableRow makeRow, modelRow, etRow, apertureRow, flRow, isoRow;
    private AppCompatTextView makeTv, modelTv, etTv, apertureTv, flTv, isoTv;

    public ExifHolder(View itemView) {
        super(itemView);

        makeRow = findViewById(R.id.exif_make);
        modelRow = findViewById(R.id.exif_model);
        etRow = findViewById(R.id.exif_exposure_time);
        apertureRow = findViewById(R.id.exif_aperture);
        flRow = findViewById(R.id.exif_focal_length);
        isoRow = findViewById(R.id.exif_iso);

        makeTv = findViewById(R.id.exif_make_value);
        modelTv = findViewById(R.id.exif_model_value);
        etTv = findViewById(R.id.exif_exposure_time_value);
        apertureTv = findViewById(R.id.exif_aperture_value);
        flTv = findViewById(R.id.exif_focal_length_value);
        isoTv = findViewById(R.id.exif_iso_value);
    }

    @Override
    public void onBindView(Context context, ExifDelegate t, int position, DelegateAdapter adapter) {
        Exif exif = t.getSource();

        makeRow.setVisibility(visibility(exif.make));
        modelRow.setVisibility(visibility(exif.model));
        etRow.setVisibility(visibility(exif.exposure_time));
        apertureRow.setVisibility(visibility(exif.aperture));
        flRow.setVisibility(visibility(exif.focal_length));
        isoRow.setVisibility(exif.iso == 0 ? View.GONE : View.VISIBLE);

        makeTv.setText(exif.make);
        modelTv.setText(exif.model);
        etTv.setText(exif.exposure_time);
        apertureTv.setText(exif.aperture);
        flTv.setText(exif.focal_length);
        isoTv.setText(exif.iso + "");
    }

    private int visibility (String value) {
        return TextUtils.isEmpty(value) ? View.GONE : View.VISIBLE;
    }
}
