package com.mdkashif.alarm.utils;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.mdkashif.alarm.R;
import com.mdkashif.alarm.security.AntiTheftUnlockActivity;

public class AlarmApplication extends Application {

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: remove at production!
        Stetho.initializeWithDefaults(this);
        LockManager<AntiTheftUnlockActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, AntiTheftUnlockActivity.class);
        lockManager.getAppLock().setLogoId(R.mipmap.ic_launcher_foreground);
    }
}
