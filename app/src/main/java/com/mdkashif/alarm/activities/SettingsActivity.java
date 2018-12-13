package com.mdkashif.alarm.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mdkashif.alarm.R;
import com.mdkashif.alarm.utils.AppConstants;
import com.mdkashif.alarm.utils.SharedPrefHolder;
import com.pkmmte.view.CircularImageView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import jp.wasabeef.blurry.Blurry;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_settings);
            Preference toggleAboutDev= findPreference(getString(R.string.aboutDev));
            Preference toggleRate = findPreference(getString(R.string.rate));
            Preference toggleShare = findPreference(getString(R.string.share));
            Preference toggleAppVersion = findPreference(getString(R.string.keyAppVersion));
            Preference toggleFaq = findPreference(getString(R.string.title_faq));
            Preference togglePrivacyPolicy = findPreference(getString(R.string.privacy_policy));
            Preference toggleTnc = findPreference(getString(R.string.title_terms));
            Preference toggleFeedback = findPreference(getString(R.string.key_send_feedback));
            Preference toggleNotification=findPreference(getString(R.string.notifications_new_message));
            Preference toggleVibrate=findPreference(getString(R.string.key_vibrate));
            final Preference toggleTheme=findPreference(getString(R.string.theme));

            toggleTheme.setSummary(SharedPrefHolder.getInstance(getActivity()).getTheme());
            toggleAboutDev.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MaterialDialog aboutDev=new MaterialDialog.Builder(getActivity())
                            .theme(Theme.LIGHT)
                            .cancelable(true)
                            .title(getString(R.string.aboutDevTitle))
                            .customView(R.layout.layout_about_dev,false)
                            .show();

                    View view = aboutDev.getCustomView();

                    CircularImageView avatar;
                    Bitmap bitmap;
                    ImageView ivDevBlurryImage,ivLinkedIn,ivGmail,ivGooglePlay,ivStackOverFlow,ivGithub;
                    TextView tvMadeWithLove,tvOpenLicenses, tvVersion;

                    avatar = view.findViewById(R.id.avatar);
                    ivLinkedIn=view.findViewById(R.id.linkedin);
                    ivGmail=view.findViewById(R.id.gmail);
                    ivGooglePlay=view.findViewById(R.id.googleplay);
                    tvOpenLicenses=view.findViewById(R.id.opensource);
                    ivStackOverFlow=view.findViewById(R.id.stackOverFlow);
                    ivGithub=view.findViewById(R.id.github);
                    ivDevBlurryImage=view.findViewById(R.id.ivDevBlurryImage);
                    tvMadeWithLove=view.findViewById(R.id.madeWithLove);
                    tvVersion=view.findViewById(R.id.tvVersion);

                    Spannable spannable = new SpannableString(getString(R.string.summary_about));
                    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvMadeWithLove.setText(spannable, TextView.BufferType.SPANNABLE);

                    if(avatar.getDrawable()!=null) {
                        bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                        Blurry.with(getActivity())
                                .radius(5)
                                .sampling(2)
                                .color(Color.argb(100, 0, 0, 0))
                                .async()
                                .animate(1000).from(bitmap).into(ivDevBlurryImage);
                    }

                    ivLinkedIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + "mdkashif2093"));
                            final PackageManager packageManager = getActivity().getPackageManager();
                            final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            if (list.isEmpty()) {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + "mdkashif2093"));
                            }
                            startActivity(intent);
                        }
                    });

                    ivGmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String addresses="mohammadkshf2093@gmail.com";
                            Uri uri = Uri.parse("mailto:" + addresses).buildUpon().build();
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query!");
                            getActivity().startActivity(emailIntent);

                        }
                    });

                    ivGooglePlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Mohammad+Kashif+Khan")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Mohammad+Kashif+Khan")));
                            }
                        }
                    });

                    ivStackOverFlow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/users/5518744/kashif-k")));
                        }
                    });

                    ivGithub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mohammadkashifkhan")));
                        }
                    });

                    tvOpenLicenses.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new MaterialDialog.Builder(getActivity())
                                    .theme(Theme.LIGHT)
                                    .cancelable(true)
                                    .title(getString(R.string.openSourceLicenses))
                                    .customView(R.layout.layout_open_source_licenses,true)
                                    .show();
                        }
                    });

                    try {
                        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        tvVersion.setText(pInfo.versionName);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return true;
                }
            });

            toggleNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {

                    if(Boolean.valueOf(o.toString()))
                        SharedPrefHolder.getInstance(getActivity()).setRingStatus(true);
                    else
                        SharedPrefHolder.getInstance(getActivity()).setRingStatus(false);
                    return true;
                }
            });
//
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));

            toggleVibrate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(Boolean.valueOf(o.toString()))
                        SharedPrefHolder.getInstance(getActivity()).setVibrateStatus(true);
                    else
                        SharedPrefHolder.getInstance(getActivity()).setVibrateStatus(false);
                    return true;
                }
            });

            toggleFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });

            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                toggleAppVersion.setSummary(pInfo.versionName);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            toggleShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try
                    { Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Universal Alarm");
                        String sAux = "\nLet me recommend you this application\n\n";
                        sAux = sAux + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "Share with"));
                    }
                    catch(Exception e)
                    { //e.toString();
                    }
                    return true;

                }
            });

            toggleRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    }
                    return true;

                }
            });

            toggleFaq.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String endpoint=AppConstants.FAQ;
                    Intent browserIntent = new Intent(getActivity(), WebviewActivity.class);
                    browserIntent.putExtra("endpoint",endpoint);
                    startActivity(browserIntent);
                    return true;
                }
            });

            togglePrivacyPolicy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String endpoint=AppConstants.PP;
                    Intent browserIntent = new Intent(getActivity(), WebviewActivity.class);
                    browserIntent.putExtra("endpoint",endpoint);
                    startActivity(browserIntent);
                    return true;
                }
            });

            toggleTnc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String endpoint=AppConstants.TNC;
                    Intent browserIntent = new Intent(getActivity(), WebviewActivity.class);
                    browserIntent.putExtra("endpoint",endpoint);
                    startActivity(browserIntent);
                    return true;
                }
            });

            toggleTheme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.dialogChooseThemeBtTitle)
                            .items(R.array.themes)
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    SharedPrefHolder.getInstance(getActivity()).setTheme(getResources().getStringArray(R.array.themes)[which]);
                                    toggleTheme.setSummary(getResources().getStringArray(R.array.themes)[which]);
                                    return true;
                                }
                            })
                            .positiveText(R.string.dialogChooseThemeBtText)
                            .show();
                    return true;
                }
            });

        }
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));
                    SharedPrefHolder.getInstance(preference.getContext()).setRingtoneUri(Uri.parse(stringValue));

                    if (ringtone == null) {

                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            }
            return true;
        }
    };

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }

        String addresses="mohammadkshf2093@gmail.com";
        Uri uri = Uri.parse("mailto:" + addresses).buildUpon().build();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query from Universal Alarm app");
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(emailIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
        return super.onOptionsItemSelected(item);
    }

}
