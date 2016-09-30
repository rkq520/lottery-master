package cn.true123.lottery.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LotteryApplication extends Application {
    private static final long cacheSize = 10 * 1024 * 1024;
    private static final String TAG = "LotteryApplication";

    @Override
    public void onCreate() {
        super.onCreate();
    }
    //Activity缓存集合
    public static Map<Activity, List> cacheActivity = new HashMap<Activity, List>();
    //正在运行的Activity 顺序性
    public static List<Activity> runningActivities = new ArrayList<>();
    /**
    *添加一个activity到list的方法
    * */
    public void addRunningActivity(Activity activity){
        if(!runningActivities.contains(activity)){
            runningActivities.add(activity);
        }
    }
    /**
     *从list中移除一个activity
     * */
    public void removeRunningActivity(Activity activity){
        if(runningActivities.contains(activity)){
            runningActivities.remove(activity);
        }
        if(activity!=null)activity.finish();
    }
    /**
     * 程序退出方法
     */
    public void exit(){
        if(runningActivities!=null && runningActivities.size()>0){
            Iterator<Activity> it = runningActivities.iterator();
            while(it.hasNext()){
                Activity activity = it.next();
                if(activity!=null) activity.finish();

            }
            runningActivities.clear();;
        }
    }

    /**
     *POP 海报方法
     * @param activity
     * @return
     */
    public static String pop(Activity activity) {
        Log.i(TAG, "pop");
        List list = cacheActivity.get(activity);
        if (list == null || list.size() == 0) return null;
        list.remove(0);
        String tag = null;
        if (list.size()>=1) {
             tag = (String) list.get(0);
        }
        Log.i(TAG, "list.size()="+list.size());
        if (list.size() == 0) return null;
        Log.i(TAG, "pop"+tag);
        return tag;
    }

    /**
     * 添加一个fragment
     * @param activity
     * @param fragment
     */
    public static void add(Activity activity, String fragment) {
        Log.i(TAG, "add"+fragment);
        List list = cacheActivity.get(activity);
        if (list == null) list = new ArrayList();
        int index = list.indexOf(fragment);
        if (index > 0) list.remove(index);
        list.add(0, fragment);
        cacheActivity.put(activity, list);
    }

    /**
     *获取缓存的目录文件夹
     * @return
     */
    public File getCacheDirectory() {
        return getExternalCacheDir();

    }

    /**
     * 获取缓存的大小
     * @return
     */
    public long getCacheSize() {
        return cacheSize;
    }

}
