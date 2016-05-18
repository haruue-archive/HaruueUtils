package moe.haruue.util.abstracts;

import java.io.Serializable;

/**
 * 实现此虚类以进行全局数据的临时存储<br>
 *     全局数据存储的类必须使用单例模式
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class InstanceSavable implements Serializable {

    public InstanceSavable() {

    }

    /**
     * 返回存储全局数据的静态单例
     * @return 全局数据的静态单例
     */
    public abstract Serializable getSavableInstance();

    /**
     * 恢复存储全局数据到静态单例
     * @param instance 从 savedInstanced 恢复的静态实例
     */
    public abstract void onRestoreInstance(Serializable instance);

    /**
     * 单例模式，类名相同即判等
     * @param o 另一个类
     * @return 类名是否相同
     */
    @Override
    public boolean equals(Object o) {
        return this.getClass().getName().equals(o.getClass().getName());
    }
}
