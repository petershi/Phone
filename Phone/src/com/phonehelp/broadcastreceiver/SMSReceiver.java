package com.phonehelp.broadcastreceiver;

import java.util.ArrayList;
import java.util.Date;

import com.phonehelp.db.BlacklistDBUtil;
import com.phonehelp.db.SmsDBUtil;
import com.phonehelp.db.WordDBUtil;
import com.phonehelp.util.NumFormat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private int type = 1;
	private String word = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("SMSReceiver", "onReceive");
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean smsdisable = settings.getBoolean("smsdisable", false);

		if (smsdisable) {
			NumFormat nf = new NumFormat();
			String sumContent = null;
			String sender = null;
			Date date = null;
			BlacklistDBUtil bDBUtil = new BlacklistDBUtil(context);
			bDBUtil.open();

			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			if (pdus != null && pdus.length > 0) {
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					byte[] pdu = (byte[]) pdus[i];
					messages[i] = SmsMessage.createFromPdu(pdu);
				}
				for (SmsMessage message : messages) {
					String content = message.getMessageBody();
					sumContent += content;
					sender = message.getOriginatingAddress();
					date = new Date(message.getTimestampMillis());

					Log.i("MessageOnReceiver", sender);
				}
			}

			sender = nf.format(sender);
			if (bDBUtil.isBlock(sender) || isKeyWord(sumContent, context)) {
				this.abortBroadcast();
				saveBlockedSMS(context, sender, date, sumContent);
			}
		}

	}

	private Boolean isKeyWord(String content, Context context) {

		WordDBUtil wDBUtil = new WordDBUtil(context);
		wDBUtil.open();
		ArrayList<String> keyWordList = wDBUtil.query2();
		wDBUtil.closeDB();
		for (String keyWord : keyWordList) {
			Log.i("keyword", keyWord);
			Log.i("indexOf", content.indexOf(keyWord) + "");
			if (content.indexOf(keyWord) != -1) {
				type = 2;
				word = keyWord;
				break;
				// return true;
			}
		}
		if (word != null) {
			return true;
		} else {
			return false;
		}

	}

	private void saveBlockedSMS(final Context context, final String sender,
			final Date date, final String sumContent) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				SmsDBUtil sDBUtil = new SmsDBUtil(context);
				sDBUtil.open();

				if (type == 1) {
					sDBUtil.addSmsRecord(sender, date, sumContent, type);
					BlacklistDBUtil bDBUtil = new BlacklistDBUtil(context);
					bDBUtil.open();
					bDBUtil.plusOne(sender);
					bDBUtil.closeDB();
				} else if (type == 2) {
					sDBUtil.addSmsRecord(sender, date, sumContent, type, word);
					WordDBUtil wDBTUil = new WordDBUtil(context);
					wDBTUil.open();
					wDBTUil.plusOne(word);
					wDBTUil.closeDB();
				}
				sDBUtil.closeDB();

				return null;
			}

		}.execute();

	}
}
