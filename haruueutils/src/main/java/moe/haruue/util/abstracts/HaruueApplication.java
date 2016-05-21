package moe.haruue.util.abstracts;

import android.app.Application;

import moe.haruue.util.ActivityManager;
import moe.haruue.util.InstanceSaver;
import moe.haruue.util.StandardUtils;
import moe.haruue.util.ThreadUtils;

/**
 * 继承这个 Application ，轻松获取 Utils 所需的一切
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class HaruueApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StandardUtils.initialize(this);
        ActivityManager.initialize();
        InstanceSaver.initialize();
        ThreadUtils.initialize(this);
    }

}
