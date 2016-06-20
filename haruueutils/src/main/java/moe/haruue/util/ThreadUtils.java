package moe.haruue.util;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线程管理器<br>
 *     在任何地方建立新线程、插入主线程的消息队列。将一个线程与其他东西（如 Activity 绑定）<br>
 *     请在 {@link Application} 中使用 {@link ThreadUtils#initialize(Application)} 进行初始化，或者使用 {@link moe.haruue.util.abstracts.HaruueApplication}<br>
 *     在需要停止的地方（如 {@link Activity#onDestroy()} 中）加入 {@link ThreadUtils#interruptThreadsByObject(Object, boolean)}，或者直接使用 {@link moe.haruue.util.abstracts.HaruueActivity}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ThreadUtils {

    private static ThreadUtils utils;
    private Handler handler;
    private Map<Thread, Object> objectThreadMap;

    private final static Object objectMapLock = new Object();

    private ThreadUtils() {

    }

    /**
     * 在 {@link Application} 的子类中初始化，如果继承 {@link moe.haruue.util.abstracts.HaruueApplication} 则无需再次初始化
     * @param application Application 的 this 引用
     */
    public static void initialize(Application application) {
        utils = new ThreadUtils();
        utils.handler = new Handler(application.getMainLooper());
        utils.objectThreadMap = new HashMap<>(0);
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
     * 在主线程（UI 线程）中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     */
    public static void runOnUIThread(Runnable runnable) {
        utils.handler.post(runnable);
    }

    /**
     * 将一个线程和任何对象进行绑定，一个线程只能绑定一个对象，一个对象可以绑定多个线程
     * @param thread 需要被绑定的线程
     * @param object 需要被绑定的对象
     */
    public static void bindThreadWithObject(Thread thread, Object object) {
        synchronized (objectMapLock) {
            utils.objectThreadMap.put(thread, object);
        }
    }

    /**
     * 在新线程中运行并绑定一个对象
     * @param object 需要绑定的对象
     * @param runnable 需要运行的 {@link Runnable}
     * @return 正在运行的 {@link Thread} 实例
     */
    public static Thread runOnNewThread(Object object, Runnable runnable) {
        Thread thread = runOnNewThread(runnable);
        bindThreadWithObject(thread, object);
        thread.start();
        return thread;
    }

    /**
     * 利用一个对象来寻找所有绑定它的线程
     * @param object 绑定有线程的对象
     * @return 包含有被绑定线程的 {@link ArrayList} ，如果没有线程与它绑定，返回一个空的 {@link ArrayList}
     */
    public static ArrayList<Thread> findThreadsByObject(Object object) {
        ArrayList<Thread> list = new ArrayList<>(0);
        synchronized (objectMapLock) {
            for (Map.Entry<Thread, Object> entry : utils.objectThreadMap.entrySet()) {
                if (entry.getValue().equals(object)) {
                    list.add(entry.getKey());
                }
            }
        }
        return list;
    }

    /**
     * 利用一个线程来寻找绑定它的对象
     * @param thread 绑定有对象的线程
     * @return 被这个线程绑定的对象，如果找不到，返回 null
     */
    @Nullable
    public static Object findObjectByThread(Thread thread) {
        synchronized (objectMapLock) {
            for (Map.Entry<Thread, Object> entry : utils.objectThreadMap.entrySet()) {
                if (entry.getKey().equals(thread)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 解绑指定的线程和对象的关系
     * @param thread 绑定有对象的线程
     */
    public static void unbindThread(Thread thread) {
        synchronized (objectMapLock) {
            utils.objectThreadMap.remove(thread);
        }
    }

    /**
     * 解除指定的对象和线程的关系
     * @param object 绑定有线程的对象
     */
    public static void unbindObject(Object object) {
        for (Thread t : findThreadsByObject(object)) {
            unbindThread(t);
        }
    }

    /**
     * 中断绑定某个对象的所有线程
     * @param object 绑定有线程的对象
     * @param autoUnbind 是否自动解除绑定
     */
    public static void interruptThreadsByObject(Object object, boolean autoUnbind) {
        for (Thread t: findThreadsByObject(object)) {
            t.interrupt();
            if (autoUnbind) {
                unbindThread(t);
            }
        }
    }

    /**
     * 清理停止的线程，避免内存泄漏
     */
    public static void clearInterruptThreads() {
        List<Thread> list = new ArrayList<>(0);
        synchronized (objectMapLock) {
            for (Map.Entry<Thread, Object> entry: utils.objectThreadMap.entrySet()) {
                if (entry.getKey().isInterrupted()) {
                    list.add(entry.getKey());
                }
            }
        }
        for (Thread t: list) {
            unbindThread(t);
        }
    }

    /**
     * 在每个 Activity 的 {@link Activity#onDestroy()} 方法中调用此方法，如果继承 {@link moe.haruue.util.abstracts.HaruueActivity} 则无需再调用
     * @param activity 对应 Activity 的 this 引用
     * @deprecated 使用统一的 {@link ThreadUtils#interruptThreadsByObject(Object, boolean)} 代替它
     */
    @Deprecated
    public static void onActivityDestroy(Activity activity) {
        interruptThreadsByObject(activity, true);
    }

    /**
     * 在每个 Fragment 的 {@link Fragment#onDestroy()} 方法中调用此方法
     * @param fragment 对应 Fragment 的 this 引用
     * @deprecated 使用统一的 {@link ThreadUtils#interruptThreadsByObject(Object, boolean)} 代替它
     */
    @Deprecated
    public static void onFragmentDestroy(Fragment fragment) {
        interruptThreadsByObject(fragment, true);
    }

}
