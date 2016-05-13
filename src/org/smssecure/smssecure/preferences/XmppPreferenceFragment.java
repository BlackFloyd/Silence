package org.smssecure.smssecure.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.smssecure.smssecure.ApplicationPreferencesActivity;
import org.smssecure.smssecure.components.SwitchPreferenceCompat;
import org.smssecure.smssecure.R;
import org.smssecure.smssecure.service.XmppService;
import org.smssecure.smssecure.util.SilencePreferences;
import org.smssecure.smssecure.util.XmppUtil;
import org.smssecure.smssecure.XmppRegisterActivity;

public class XmppPreferenceFragment extends PreferenceFragment {

  private static final String TAG = XmppPreferenceFragment.class.getSimpleName();

  private static final int REGISTERING_ACTIVITY_RESULT_CODE = 666;

  @Override
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.preferences_xmpp);

    setXmppUiSettings();

    findPreference(SilencePreferences.XMPP_ENABLED_PREF)
        .setOnPreferenceChangeListener(new RegisterXmppListener());

  }

  private void setXmppUiSettings() {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (SilencePreferences.isXmppRegistered(getActivity())) {
          findPreference(SilencePreferences.XMPP_STATUS)
              .setSummary(R.string.preferences__xmpp_status_registered);
        } else {
          findPreference(SilencePreferences.XMPP_STATUS)
              .setSummary(R.string.preferences__xmpp_status_unregistered);
          ((SwitchPreferenceCompat) findPreference(SilencePreferences.XMPP_ENABLED_PREF))
              .setChecked(false);
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    ((ApplicationPreferencesActivity)getActivity()).getSupportActionBar().setTitle(R.string.preferences__xmpp);
  }

  private class RegisterXmppListener implements Preference.OnPreferenceChangeListener {
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
      final Context context = (Context) getActivity();

      if (!SilencePreferences.isXmppRegistered(context)) {
        startActivityForResult(new Intent(getActivity(), XmppRegisterActivity.class), REGISTERING_ACTIVITY_RESULT_CODE);
      } else {
        Log.w(TAG, "Stopping XmppService...");
        XmppUtil.stopService(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.preferences__xmpp_send_unregistering_notification);
        builder.setMessage(R.string.preferences__xmpp_you_can_notify_your_contacts_that_you_do_not_use_xmpp_anymore);
        builder.setIconAttribute(R.attr.dialog_info_icon);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            XmppUtil.sendNullXmppMessage(context);
          }
        });
        builder.show();
      }
      return true;
    }
  }

  public static CharSequence getSummary(Context context) {
    if (SilencePreferences.isXmppRegistered(context)) {
      return context.getString(R.string.preferences__xmpp_status_registered);
    } else {
      return context.getString(R.string.preferences__xmpp_status_unregistered);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REGISTERING_ACTIVITY_RESULT_CODE) {
      setXmppUiSettings();
    }
  }
}
