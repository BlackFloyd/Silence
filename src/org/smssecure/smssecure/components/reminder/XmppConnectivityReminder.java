package org.smssecure.smssecure.components.reminder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import org.smssecure.smssecure.R;
import org.smssecure.smssecure.util.SilencePreferences;
import org.smssecure.smssecure.util.XmppUtil;

public class XmppConnectivityReminder extends Reminder {

  private static final String TAG = XmppConnectivityReminder.class.getSimpleName();

  public XmppConnectivityReminder(final Context context) {
    super(context.getString(R.string.XmppService_xmpp_connection_failed),
          context.getString(R.string.XmppService_xmpp_features_in_silence_are_disabled),
          null);
  }

  public static boolean isEligible(Context context) {
    return SilencePreferences.isXmppRegistered(context) &&
          !XmppUtil.isXmppAvailable(context);
  }

  public boolean isDismissable() {
    return false;
  }
}
