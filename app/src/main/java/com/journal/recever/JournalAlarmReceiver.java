package com.journal.recever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.journal.service.AlarmService;
import com.journal.utils.Constants;


/**
 * 类名称：com.journal.recever.JournalAlarmAlertReceiver
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class JournalAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String tips = intent.getStringExtra(Constants.KEY_TIPS_SERVICE);
        Intent clockIntent = new Intent(context, AlarmService.class);
        clockIntent.putExtra(Constants.KEY_TIPS_SERVICE, tips);
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(clockIntent);
    }


}
