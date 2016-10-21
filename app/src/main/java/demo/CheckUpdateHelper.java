package demo;

import android.content.Context;
import android.content.Intent;

import com.zhuiji7.filedownloader.download.DownLoadService;
import com.zhuiji7.filedownloader.download.TaskInfo;

/**
 * Created by hzjixiaohui on 2016-10-20.
 */

public class CheckUpdateHelper {

    private Context mContext;
    private TaskInfo info = null;

    public CheckUpdateHelper(Context context,TaskInfo taskInfo){
        mContext = context;
        info = taskInfo;
        mContext.startService(new Intent(mContext, DownLoadService.class));
    }

}
