package com.mdkashif.alarm.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Kashif on 16-Apr-18.
 */
public class AnimationSingleton {
    private static AnimationSingleton INSTANCE = null;
    private static LayoutAnimationController controller;

    private AnimationSingleton() {}

    // Animation for day slots
    public static void set_days_Animation(RecyclerView recyclerView) {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(800);
        set.addAnimation(animation);

        controller = new LayoutAnimationController(set, 0.5f);
        recyclerView.setLayoutAnimation(controller);
    }

    // Animation for alarms
    public static void set_alarms_Animation(RecyclerView recyclerView) {
        AnimationSet network_prov_plan_set = new AnimationSet(true);

        Animation network_prov_plan_Animation= new AlphaAnimation(0.0f, 1.0f);
        network_prov_plan_Animation.setDuration(100);
        network_prov_plan_set.addAnimation(network_prov_plan_Animation);

        network_prov_plan_Animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        network_prov_plan_Animation.setDuration(800);
        network_prov_plan_set.addAnimation(network_prov_plan_Animation);

        controller = new LayoutAnimationController(network_prov_plan_set, 0.5f);
        recyclerView.setLayoutAnimation(controller);
    }
}
