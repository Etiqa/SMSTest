package it.etiqa.smstest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log

class SmsListener : BroadcastReceiver() {

    val TAG = "SMSActivity"

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPref = context.getSharedPreferences (TAG, Context.MODE_PRIVATE) ?: return

        val recording = sharedPref.getBoolean(context.getString(R.string.activated_state_label), false)
        val targetUrl = sharedPref.getString(context.getString(R.string.server_url_label), "")

        Log.i(TAG,"Sms received!________________________________________")
        if (recording) {
            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                for (smsMessage: SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    val messageBody = smsMessage.messageBody
                    SMSForward().execute(targetUrl, messageBody)
                }
            }
        } else {
            Log.i(TAG, "Message received - but transferral has not been enabled!")
        }
    }
}
