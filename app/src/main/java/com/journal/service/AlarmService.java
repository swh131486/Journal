package com.journal.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;

import com.journal.R;
import com.journal.activity.HomeActivity;
import com.journal.utils.Constants;
import com.journal.utils.NotifyUtil;

/**
 * 类名称：com.exing.service
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.23
 */
public class AlarmService extends Service {
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private int requestCode = (int) SystemClock.uptimeMillis();

    /**
     * 弹出顶部通知栏消息
     *
     * @param context
     * @param tips
     */
    private void notify(Context context, String tips) {
        vibrator.vibrate(new long[]{100, 10, 100, 600}, -1);
        //设置想要展示的数据内容
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.ic_launcher;
        String ticker = "您有一条提醒";
        String title = tips;
        String content = "";

        //实例化工具类，并且调用接口
        NotifyUtil notify1 = new NotifyUtil(context, 1);
        notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, true, true, false);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String tips = intent.getStringExtra(Constants.KEY_TIPS_SERVICE);
        notify(this, tips);
        return super.onStartCommand(intent, flags, startId);
    }
}
