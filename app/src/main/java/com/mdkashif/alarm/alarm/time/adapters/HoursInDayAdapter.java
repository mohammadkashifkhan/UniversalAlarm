package com.mdkashif.alarm.alarm.time.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;
import com.mdkashif.alarm.R;
import com.mdkashif.alarm.custom.TextDrawable;

import java.util.List;

/**
 * Created by Kashif on 24-Apr-18.
 */
public class HoursInDayAdapter extends WheelArrayAdapter {
    List<String> hours;
    Context context;

    public HoursInDayAdapter(List<String> hours,Context context) {
        super(hours);
        this.hours = hours;
        this.context=context;
    }

    @Override
    public Drawable getDrawable(int position) {
        Drawable[] drawable = new Drawable[] {
                createOvalDrawable(),
                new TextDrawable(hours.get(position))
        };
        return new LayerDrawable(drawable);
    }

    private Drawable createOvalDrawable() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(context.getResources().getColor(R.color.leastDarkBlue));
        return shapeDrawable;
    }

    @Override
    public int getCount() {
        return hours.size();
    }

}
