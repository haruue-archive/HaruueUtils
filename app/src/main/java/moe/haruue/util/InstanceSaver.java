package moe.haruue.util;

import android.os.Bundle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import moe.haruue.util.abstracts.InstanceSavable;

/**
 * 全局数据临时存储
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class InstanceSaver implements Serializable {

    private static InstanceSaver saver;

    private Set<InstanceSavable> models;

    private InstanceSaver() {

    }

    /**
     * 在 {@link android.app.Application} 的子类里初始化，如果继承了 {@link moe.haruue.util.abstracts.HaruueApplication} 则无需再进行初始化
     */
    public static void initialize() {
        saver = new InstanceSaver();
        saver.models = new HashSet<>(0);
    }

    /**
     * 添加需要管理的单例模式实例到 InstanceSaver
     * @param instanceSavable 需要添加的单例，直接传入同类的任意对象都可以
     */
    public static void add(InstanceSavable instanceSavable) {
        saver.models.add(instanceSavable);
    }

    /**
     * 移除指定的单例模式实例，使其不会被管理
     * @param instanceSavable 需要移除的单例，直接传入同类的任意对象都可以
     */
    public static void remove(InstanceSavable instanceSavable) {
        saver.models.remove(instanceSavable);
    }

    /**
     * 保存单例到 savedInstance ，请添加以下代码到 {@link android.app.Activity} 的子类中，如果继承 {@link moe.haruue.util.abstracts.HaruueActivity} 则无需再添加
     * {@code
     *      @Override
     *      protected void onSaveInstanceState(Bundle outState) {
     *          super.onSaveInstanceState(outState);
     *          InstanceSaver.saveInstance(outState);
     *      }
     * }
     * @param outState 存储实例的外部状态
     */
    public static void saveInstance(Bundle outState) {
        for (InstanceSavable i: saver.models) {
            try {
                outState.putSerializable(i.getClass().getName(), i.getSavableInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从外部状态恢复全局单例，请添加以下代码到 {@link android.app.Activity} 的子类中，如果继承 {@link moe.haruue.redrockexam.util.abstracts.HaruueActivity} 则无需添加
     * {@code
     *      @Override
     *          protected void onRestoreInstanceState(Bundle savedInstanceState) {
     *          super.onRestoreInstanceState(savedInstanceState);
     *          InstanceSaver.saveInstance(savedInstanceState);
     *      }
     * }
     * @param savedInstance
     */
    public static void restoreInstance(Bundle savedInstance) {
        for (InstanceSavable i: saver.models) {
            try {
                i.onRestoreInstance(savedInstance.getSerializable(i.getClass().getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
