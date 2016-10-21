package com.zhuiji7.filedownloader.download.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zhuiji7.filedownloader.R;
import com.zhuiji7.filedownloader.download.TaskInfo;

/**
 * Created by hzjixiaohui on 2016-10-14.
 * api >= 3.0
 */

public class NotificationHelper {

    private Context mContext;
    private NotificationManager manager;
    public static final int PROGRESS = 100020;
    private Notification myNotify;

    public NotificationHelper(Context context){
        mContext = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        initNotification();
    }

    private  Notification initNotification(){
        myNotify = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("应用开始下载了")
                .setWhen(System.currentTimeMillis())
                .build();
        myNotify.flags = Notification.FLAG_NO_CLEAR;
        return myNotify;
    }

    public  void sendNotification(){
        manager.notify(PROGRESS,myNotify);
    }

    public void updateProgressNotification(TaskInfo taskInfo){
        RemoteViews rv = myNotify.contentView;
        if (rv == null){
            rv = myNotify.contentView = new RemoteViews(mContext.getPackageName(),R.layout.layout_notification);
        }
        rv.setTextViewText(R.id.file_name, "小贝壳");
        rv.setTextViewText(R.id.file_size, ""+taskInfo.getDownFileSize()/1024f/1024f+"/"+taskInfo.getFileSize()/1024f/1024f);
        rv.setProgressBar(R.id.progressbar,100,taskInfo.getProgress(),false);
        manager.notify(PROGRESS, myNotify);
    }

    public void sendErrorNotification(){
        myNotify = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("下载了出错了")
                .setContentTitle("小贝壳")
                .setWhen(System.currentTimeMillis())
                .setContentText("下载了出错了")
                .build();
        manager.notify(PROGRESS,myNotify);
    }

}
