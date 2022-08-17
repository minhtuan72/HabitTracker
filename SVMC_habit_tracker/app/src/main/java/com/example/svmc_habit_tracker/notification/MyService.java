package com.example.svmc_habit_tracker.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.svmc_habit_tracker.MainActivity;
import com.example.svmc_habit_tracker.R;
import com.example.svmc_habit_tracker.data.model.Habit;
import com.example.svmc_habit_tracker.fragment.habit.HabitRvAdapter;
import com.example.svmc_habit_tracker.fragment.home.FragHome;
import com.example.svmc_habit_tracker.fragment.home.FragItemHome;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {
    String TAG = "Myservice";
    private final IBinder mBinder = new MyBinder(this);

  //  private boolean b = FragItemHome.Check;
  int a = FragItemHome.Check;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //   return null;
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // return super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStart");
        makeLoad();
        return START_STICKY; // tu dong khoi tao lai khi may bi huy
    }


    //Loop Notification
    public void makeLoad() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                // startForeground();
                // timePicker = findViewById(R.id.time_picker);
//                int gio = timePicker.getCurrentHour();
//                int phut = timePicker.getCurrentMinute();
                //String h = String.valueOf(gio);
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startForeground();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() { // huy server
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    //Notification
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    void startForeground() {
        //Lay thoi gian thuc
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millisecond = now.get(Calendar.MILLISECOND);

        //Them hinh anh
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

        //Dieu huong khi user click vao thong bao
        //1. Quay lai khong thoat ung dung
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent penIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //2. Quay lai thoat ung dung
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);



        //Tao channel
        String CHANNEL_ID = "channel_id";
        String CHANNEL_NAME = "service noti";
        int Notification_id = 1;
        int Notification_id_1 = 2;

        int c = a;
        //Action delete
//        Intent completeIntent = new Intent(this, Delete.class);
//        completeIntent.putExtra("channel_id", Notification_id);
//        completeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        PendingIntent snoozePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, completeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Intent dele = new Intent(this, null);
//        PendingIntent resultPending = PendingIntent.getActivity(this, 0, dele, PendingIntent.FLAG_CANCEL_CURRENT);

//        Intent snoozeIntent = new Intent(this, Delete.class);
//        //snoozeIntent.setAction(ACTION_SNOOZE);
//        snoozeIntent.putExtra(String.valueOf(Notification_id), 0);
//        PendingIntent snoozePendingIntent =
//                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        Date date = new Date();
        String strDateFormat24 = "HH:mm:ss";
        SimpleDateFormat sdf =null;
        sdf = new SimpleDateFormat(strDateFormat24);
        sdf.format(date);
//        Habit habit = HabitRvAdapter.habitArrayList.get(HabitRvAdapter.position);
//        Bitmap bitmap1 = BitmapFactory.decodeByteArray(habit.getImageHabit(),0, habit.getImageHabit().length);
//        HabitRvAdapter.holder.iconHabit.setImageBitmap(bitmap);


        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "noti", NotificationManager.IMPORTANCE_DEFAULT); // dinh nghia 1 channel
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);


        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Thong bao")
                .setContentText("Ban da hoan thanh muc tieu" + String.valueOf(a))
                .setChannelId(CHANNEL_ID)
                .setContentIntent(penIntent)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLargeIcon(bitmap)              //icon picture
//                .setStyle(new NotificationCompat.BigPictureStyle()
//                        .setSummaryText("11111111111")  //show text
//                        .bigPicture(bitmap)        //show picture
//                        .bigLargeIcon(null))       //an icon picture
////                .setStyle(new NotificationCompat.BigTextStyle()
////                        .bigText(Delete.s))
// //               .setWhen(millisecond)
                .setAutoCancel(true);
//                .addAction(R.drawable.icon_notification,getString(R.string.action_name),snoozePendingIntent);


        //Dieu kien gui thong bao
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        if (FragItemHome.Check==1) {
            //a=0;
        //   b == true;
            notificationManager.notify(Notification_id, notification);
            Log.d("TAG", "onFinish");
        }

//        if (second == 10 || second == 30 || second == 50) {
//            notificationManager.notify(Notification_id_1, notification);
//        }
    }
}

