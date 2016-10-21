package com.zhuiji7.filedownloader.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhuiji7.filedownloader.download.dbcontrol.bean.SQLDownLoadInfo;
import com.zhuiji7.filedownloader.download.notification.NotificationHelper;

import demo.CheckUpdateHelper;

/**
 * 类功能描述：下载器后台服务</br>
 */

public class DownLoadService extends Service {

    private DownLoadManager  manager;
    private TaskInfo info = new TaskInfo();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(manager == null){
            manager = new DownLoadManager(this);
            info.setFileName("test");
            info.setTaskID("ID00001");
            info.setOnDownloading(true);

            /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
            manager.changeUser("shell");
            /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
            manager.setSupportBreakpoint(true);

            manager.setSingleTaskListener(info.getTaskID(),new DownloadManagerListener());
            /*将任务添加到下载队列，下载器会自动开始下载*/
            manager.addTask(info.getTaskID(), info.getDownloadURL(), info.getFileName());
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private class DownloadManagerListener implements DownLoadListener {
        private NotificationHelper notificationHelper;

        @Override
        public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {
            info.setFileSize(sqlDownLoadInfo.getFileSize());
            notificationHelper.sendNotification();
        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            //根据监听到的信息查找列表相对应的任务，更新相应任务的进度
            info.setDownFileSize(sqlDownLoadInfo.getDownloadSize());
            notificationHelper.updateProgressNotification(info);
        }

        @Override
        public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
        }

        @Override
        public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            info.setOnDownloading(false);
            notificationHelper.sendErrorNotification();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放manager
        manager.stopAllTask();
        manager = null;
    }

    @Override public IBinder onBind(Intent intent) { return null; }


}
