package moe.haruue.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;

import moe.haruue.util.abstracts.HaruueActivity;

/**
 * Activity 管理器<br>
 *     请在 {@link Application} 子类中进行初始化，使用 {@link ActivityCollector#initialize()} <br>
 *     您可以让 Activity 继承 {@link HaruueActivity} 或者在每个 {@link Activity} 里加入 {@code
 *
 *         @Override
 *         protected void onCreate(Bundle savedInstanceState) {
 *             super.onCreate(savedInstanceState);
 *             ActivityCollector.push(this);
 *         }
 *
 *         @Override
 *             protected void onDestroy() {
 *             super.onDestroy();
 *             ActivityCollector.pop(this);
 *         }
 *
 *     }
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ActivityCollector {

    private static ActivityCollector manager;

    private ArrayList<Activity> activities = new ArrayList<>(0);

    private ActivityCollector() {

    }

    /**
     * 加入 {@link Application#onCreate()} 中以进行初始化
     */
    public static void initialize() {
        manager = new ActivityCollector();
    }

    /**
     * 加入 {@link Activity#onCreate(Bundle)} 中来让 {@link ActivityCollector} 管理这个 Activity
     * @param activity Activity 中的 this 引用
     */
    public static void push(Activity activity) {
        manager.activities.add(activity);
    }

    /**
     * 加入 {@link Activity#onDestroy()} 来将 Activity 从 {@link ActivityCollector} 中移除
     * @param activity Activity 中的 this 引用
     */
    public static void pop(Activity activity) {
        manager.activities.remove(activity);
    }

    /**
     * 返回当前的 Activity
     * @return 当前 Activity
     */
    public static Activity peek() {
        return manager.activities.get(manager.activities.size() - 1);
    }

    /**
     * 结束当前 Activity
     */
    public static void finishCurrentActivity() {
        manager.activities.get(manager.activities.size() - 1).finish();
    }

    /**
     * 结束上一个 Activity
     */
    public static void finishPreviousActivity() {
        manager.activities.get(manager.activities.size() - 2).finish();
    }

    /**
     * 结束所有的 Activity
     */
    public static void finishAllActivity() {
        for (Activity a: manager.activities) {
            a.finish();
        }
    }

    /**
     * 结束所有的 Activity 并退出程序
     */
    public static void exitApplication() {
        finishAllActivity();
        System.exit(0);
    }

    /**
     * 获取 Activity Array 的拷贝，对该拷贝的任何更改都不会影响到 {@link ActivityCollector}
     * @return Activity Array 的一个拷贝
     */
    public static ArrayList<Activity> getActivities() {
        return new ArrayList<>(manager.activities);
    }

}
