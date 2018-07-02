package com.mdkashif.alarm.time.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.mdkashif.alarm.R;
import com.mdkashif.alarm.custom.TextDrawable;

import java.util.List;

/**
 * Created by Kashif on 24-Apr-18.
 */
public class MinutesInHourAdapter implements WheelAdapter {
    List<String> minutes;
    Context context;

    public MinutesInHourAdapter(List<String> minutes,Context context) {
        this.minutes = minutes;
        this.context=context;
    }

    @Override
    public Drawable getDrawable(int position) {
        Drawable[] drawable = new Drawable[] {
                createOvalDrawable(),
                new TextDrawable(minutes.get(position))
        };
        return new LayerDrawable(drawable);
    }

    private Drawable createOvalDrawable() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(context.getResources().getColor(R.color.lesser_dark_blue));
        return shapeDrawable;
    }

    @Override
    public int getCount() {
        return minutes.size();
    }

}
