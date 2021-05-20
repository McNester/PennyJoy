package Notifycations;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pennyjoy.AddGoodAndOtherActivity;
import com.example.pennyjoy.Fragments.AddGoodFragment;
import com.example.pennyjoy.R;
import com.example.pennyjoy.SplashActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ReminderForTimer extends BroadcastReceiver {
    private int num = (int) System.currentTimeMillis();
    private PendingIntent pendingIntent;
    private String nameOfGood;



    @Override
    public void onReceive(Context context, Intent intent) {
        nameOfGood=intent.getStringExtra("nameOfGood");

        intent=new Intent(context, SplashActivity.class);

        pendingIntent=PendingIntent.getActivity(context,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"notifyTimer")
                //здесь должен быть наш лого!!!!!!!!!!!!!!!!!!!!!
                .setSmallIcon(R.drawable.lamp)
                .setContentTitle("Время раздумий закончилось!")
                .setContentText("Таймер на обдумывание "+nameOfGood+ " подошел к концу.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(context);


        notificationManager.notify(num,builder.build());
    }


}
