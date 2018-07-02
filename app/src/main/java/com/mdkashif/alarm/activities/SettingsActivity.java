package com.mdkashif.alarm.activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mdkashif.alarm.R;

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
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_settings);

//            Preference aboutdev= findPreference(getString(R.string.aboutDev));
//            Preference rate = findPreference(getString(R.string.rate));
//            Preference share = findPreference(getString(R.string.share));
//            Preference appver = findPreference("appver");
//            Preference faq = findPreference(getString(R.string.title_faq));
//            Preference pp = findPreference(getString(R.string.privacy_policy));
//            Preference tnc = findPreference(getString(R.string.title_terms));
//            Preference feedback = findPreference(getString(R.string.key_send_feedback));
//            Preference notification=findPreference(getString(R.string.notifications_new_message));
//            Preference vibrate=findPreference(getString(R.string.key_vibrate));
//
//            aboutdev.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    MaterialDialog about=new MaterialDialog.Builder(getActivity())
//                            .theme(Theme.LIGHT)
//                            .cancelable(true)
//                            .title("About the Developer")
//                            .customView(R.layout.about,true)
//                            .show();
//
//                    View view = about.getCustomView();
//
//                    ImageView linkedin,gmail,googleplay;
//                    TextView openlicenses;
//
//                    linkedin=(ImageView)view.findViewById(R.id.linkedin);
//                    gmail=(ImageView)view.findViewById(R.id.gmail);
//                    googleplay=(ImageView)view.findViewById(R.id.googleplay);
//                    openlicenses=(TextView)view.findViewById(R.id.opensource);
//
//                    linkedin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + "mdkashif2093"));
//                            final PackageManager packageManager = getActivity().getPackageManager();
//                            final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                            if (list.isEmpty()) {
//                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + "mdkashif2093"));
//                            }
//                            startActivity(intent);
//                        }
//                    });
//
//                    gmail.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            String addresses="mohammadkshf2093@gmail.com";
//                            Uri uri = Uri.parse("mailto:" + addresses).buildUpon().build();
//                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
//                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query!");
//                            getActivity().startActivity(emailIntent);
//
//                        }
//                    });
//
//                    googleplay.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            try {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Mohammad+Kashif+Khan")));
//                            } catch (android.content.ActivityNotFoundException anfe) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Mohammad+Kashif+Khan")));
//                            }
//                        }
//                    });
//
//                    openlicenses.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new MaterialDialog.Builder(getActivity())
//                                    .theme(Theme.LIGHT)
//                                    .cancelable(true)
//                                    .title("Open Source Licenses")
//                                    .customView(R.layout.opensource,true)
//                                    .show();
//                        }
//                    });
//
//                    return true;
//                }
//            });
//
//            notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object o) {
//
//                    if(Boolean.valueOf(o.toString()))
//                        new PrefManager(getActivity()).setringstatus(true);
//                    else
//                        new PrefManager(getActivity()).setringstatus(false);
//                    return true;
//                }
//            });
//
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));
//
//            vibrate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object o) {
//                    if(Boolean.valueOf(o.toString()))
//                        new PrefManager(getActivity()).setvibratestatus(true);
//                    else
//                        new PrefManager(getActivity()).setvibratestatus(false);
//                    return true;
//                }
//            });
//
//            feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                public boolean onPreferenceClick(Preference preference) {
//                    sendFeedback(getActivity());
//                    return true;
//                }
//            });
//
//            try {
//                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
//                appver.setSummary(pInfo.versionName+" build("+pInfo.versionCode+")");
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    try
//                    { Intent i = new Intent(Intent.ACTION_SEND);
//                        i.setType("text/plain");
//                        i.putExtra(Intent.EXTRA_SUBJECT, "Share my Ride");
//                        String sAux = "\nLet me recommend you this application\n\n";
//                        sAux = sAux + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
//                        i.putExtra(Intent.EXTRA_TEXT, sAux);
//                        startActivity(Intent.createChooser(i, "Share with"));
//                    }
//                    catch(Exception e)
//                    { //e.toString();
//                    }
//                    return true;
//
//                }
//            });
//
//            rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
//                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    try {
//                        startActivity(goToMarket);
//                    } catch (ActivityNotFoundException e) {
//                        startActivity(new Intent(Intent.ACTION_VIEW,
//                                Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
//                    }
//                    return true;
//
//                }
//            });
//
//            faq.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    String endpoint=new PrefManager(getContext()).returnurl()+"FAQ.html";
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(endpoint));
//                    startActivity(browserIntent);
//                    return true;
//                }
//            });
//
//            pp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    String endpoint=new PrefManager(getContext()).returnurl()+"privacy-policy.html";
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(endpoint));
//                    startActivity(browserIntent);
//                    return true;
//                }
//            });
//
//            tnc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    String endpoint=new PrefManager(getContext()).returnurl()+"terms-and-conditions.html";
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(endpoint));
//                    startActivity(browserIntent);
//                    return true;
//                }
//            });


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
//            if (preference instanceof RingtonePreference) {
//                if (TextUtils.isEmpty(stringValue)) {
//                    preference.setSummary(R.string.pref_ringtone_silent);
//
//                } else {
//                    Ringtone ringtone = RingtoneManager.getRingtone(
//                            preference.getContext(), Uri.parse(stringValue));
//                    new PrefManager(preference.getContext()).seturi(Uri.parse(stringValue));
//
//                    if (ringtone == null) {
//
//                        preference.setSummary(R.string.summary_choose_ringtone);
//                    } else {
//                        String name = ringtone.getTitle(preference.getContext());
//                        preference.setSummary(name);
//                    }
//                }
//            }
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
