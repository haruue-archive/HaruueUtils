package moe.haruue.util;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 线程管理器<br>
 *     在任何地方建立新线程、插入主线程的消息队列。支持在 Activity 或者 Fragment 被 Destroy 之后结束线程<br>
 *     请在 {@link Application} 中使用 {@link ThreadUtils#initialize(Application)} 进行初始化，或者使用 {@link moe.haruue.util.abstracts.HaruueApplication}<br>
 *     请在 Activity 的 {@link Activity#onDestroy()} 中加入 {@link ThreadUtils#onActivityDestroy(Activity)} ，或者使用 {@link moe.haruue.util.abstracts.HaruueActivity}<br>
 *     请在 Fragment 的 {@link Fragment#onDestroy()} 中外入 {@link ThreadUtils#onFragmentDestroy(Fragment)}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ThreadUtils {

    private static ThreadUtils utils;
    private Handler handler;
    private Map<Thread, Activity> activityThreadMap;
    private Map<Thread, Fragment> fragmentThreadMap;

    private final static Object activityMapLock = new Object();
    private final static Object fragmentMapLock = new Object();

    private ThreadUtils() {

    }

    /**
     * 在 {@link Application} 的子类中初始化，如果继承 {@link moe.haruue.util.abstracts.HaruueApplication} 则无需再次初始化
     * @param application Application 的 this 引用
     */
    public static void initialize(Application application) {
        utils = new ThreadUtils();
        utils.handler = new Handler(application.getMainLooper());
        utils.activityThreadMap = new HashMap<>(0);
        utils.fragmentThreadMap = new HashMap<>(0);
    }

    /**
     * 在新线程中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     * @return 正在运行的 {@link Thread}实例
     */
    public static Thread runOnNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    /**
     * 在新线程中运行并绑定到 Activity<br>
     *     如果 Activity 被 Destroy，那么线程将会被停止
     * @param activity 要绑定的 Activity
     * @param runnable 要运行的 {@link Runnable} 实例
     * @return 正在运行的 {@link Thread} 实例
     */
    public static Thread runOnNewThread(Activity activity, Runnable runnable) {
        Thread thread = runOnNewThread(runnable);
        synchronized (activityMapLock) {
            utils.activityThreadMap.put(thread, activity);
        }
        return thread;
    }

    /**
     * 在新线程中运行并绑定到 Fragment<br>
     *     如果 Fragment 被 Destroy ，那么线程将会停止
     * @param fragment 要绑定的 Fragment
     * @param runnable 要运行的 {@link Runnable} 实例
     * @return 正在运行的 {@link Thread} 实例
     */
    public static Thread runOnNewThread(Fragment fragment, Runnable runnable) {
        Thread thread = runOnNewThread(runnable);
        synchronized (fragmentMapLock) {
            utils.fragmentThreadMap.put(thread, fragment);
        }
        return thread;
    }

    /**
     * 在主线程（UI 线程）中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     */
    public static void runOnUIThread(Runnable runnable) {
        utils.handler.post(runnable);
    }

    /**
     * 在每个 Activity 的 {@link Activity#onDestroy()} 方法中调用此方法，如果继承 {@link moe.haruue.util.abstracts.HaruueActivity} 则无需再调用
     * @param activity 对应 Activity 的 this 引用
     */
    public static void onActivityDestroy(Activity activity) {
        synchronized (activityMapLock) {
            HashSet<Map.Entry<Thread, Activity>> entrySet = new HashSet(utils.activityThreadMap.entrySet());
            for (Map.Entry<Thread, Activity> entry: entrySet) {
                if (entry.getValue().equals(activity)) {
                    try {
                        entry.getKey().interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    utils.activityThreadMap.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * 在每个 Fragment 的 {@link Fragment#onDestroy()} 方法中调用此方法
     * @param fragment 对应 Fragment 的 this 引用
     */
    public static void onFragmentDestroy(Fragment fragment) {
        synchronized (fragmentMapLock) {
            HashSet<Map.Entry<Thread, Fragment>> entrySet = new HashSet(utils.fragmentThreadMap.entrySet());
            for (Map.Entry<Thread, Fragment> entry: entrySet) {
                if (entry.getValue().equals(fragment)) {
                    try {
                        entry.getKey().interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    utils.fragmentThreadMap.remove(entry.getKey());
                }
            }
        }
    }

}
