package moe.haruue.util.abstracts;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import moe.haruue.util.ActivityCollector;
import moe.haruue.util.InstanceSaver;
import moe.haruue.util.StandardUtils;
import moe.haruue.util.ThreadUtils;

/**
 * 继承这个 Activity，轻松获取 Utils 所需的一切
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class HaruueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.push(this);
        StandardUtils.initializeInActivity(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        InstanceSaver.saveInstance(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        InstanceSaver.saveInstance(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.pop(this);
        ThreadUtils.interruptThreadsByObject(this, true);
    }

    /**
     * 替代 findViewById 更方便地 find View
     * @param res 需要 find 的 Id
     * @param <T> View 类型，自动强制类型转换
     * @return 强制类型转换好的 View
     */
    protected <T extends View> T $(@IdRes int res) {
        return (T) findViewById(res);
    }

}
