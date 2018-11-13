package com.mdkashif.alarm.alarm.time.adapters;

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
public class TimeTypeInDayAdapter implements WheelAdapter {
    List<String> type;
    Context context;

    public TimeTypeInDayAdapter(List<String> type,Context context) {
        this.type = type;
        this.context=context;
    }

    @Override
    public Drawable getDrawable(int position) {
        Drawable[] drawable = new Drawable[] {
                createOvalDrawable(),
                new TextDrawable(type.get(position))
        };
        return new LayerDrawable(drawable);
    }

    private Drawable createOvalDrawable() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(context.getResources().getColor(R.color.colorPrimaryOrange));
        return shapeDrawable;
    }

    @Override
    public int getCount() {
        return type.size();
    }

}
