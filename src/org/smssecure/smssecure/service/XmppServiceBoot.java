package org.smssecure.smssecure.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.smssecure.smssecure.util.SilencePreferences;

public class XmppServiceBoot extends BroadcastReceiver {

  private static final String BOOT_EVENT = "android.intent.action.BOOT_COMPLETED";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (BOOT_EVENT.equals(intent.getAction())) {
      if (!SilencePreferences.isXmppRegistered(context)) return;
      context.startService(new Intent(context, XmppService.class));
    }
  }

}
