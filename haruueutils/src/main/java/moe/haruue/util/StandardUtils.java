package moe.haruue.util;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

/**
 * 标准工具类 <br>
 *     这个类必须在{@link Application}的子类里初始化，使用 {@link StandardUtils#initialize(Application)}，参数直接传入 this 即可。<br>
 *     也可继承 {@link moe.haruue.util.abstracts.HaruueApplication} 而无需再次初始化。
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class StandardUtils {

    private static StandardUtils utils;

    private Application application;

    private Activity activity;

    private boolean isDebug = false;

    private StandardUtils() {

    }

    /**
     * 初始化标准工具类<br>
     *     必须在{@link Application}的子类里完成初始化操作，直接传入 this ，也可继承 {@link moe.haruue.util.abstracts.HaruueApplication} 则无需初始化
     * @param application {@link Application}的子类中的 this 对象
     */
    public static void initialize(Application application) {
        utils = new StandardUtils();
        utils.application = application;
    }

    /**
     * 设定是否为 Debug 模式
     * @param isDebug true 则为 Debug 模式，可直接传入项目的 {@link BuildConfig#DEBUG}
     */
    public static void setDebug(boolean isDebug) {
        utils.isDebug = isDebug;
    }

    /**
     * 在每个 Activity 中初始化<br>
     *     必须在 {@link Activity} 的子类里完成初始化操作，直接传入 this ，也可继承 {@link moe.haruue.util.abstracts.HaruueActivity} 则无需初始化
     * @param activity {@link Activity} 中的 this
     */
    public static void initializeInActivity(Activity activity) {
        utils.activity = activity;
    }

    /**
     * 打印 Log ，以 Activity 的类名为 tag
     * @param message log 的内容
     */
    public static void log(CharSequence message) {
        if (utils.isDebug) {
            Log.d(utils.activity.getLocalClassName(), message.toString());
        }
    }

    /**
     * 打印 log
     * @param tag 指定的 tag
     * @param message log 的内容
     */
    public static void log(CharSequence tag, CharSequence message) {
        if (utils.isDebug) {
            Log.d(tag.toString(), message.toString());
        }
    }

    /**
     * 对异常打印调用栈
     * @param t 需要打印调用栈的异常
     */
    public static void printStack(Throwable t) {
        if (utils.isDebug) {
            Log.d(defaultObject(t.getClass().getSimpleName(), ""), defaultObject(t.getMessage(), ""));
            t.printStackTrace();
        }
    }

    /**
     * 显示一个短时 Toast
     * @param text Toast 内容
     */
    public static void toast(CharSequence text) {
        Toast.makeText(utils.application, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个短时 Toast
     * @param resId Toast 内容
     */
    public static void toast(@StringRes int resId) {
        Toast.makeText(utils.application, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个长时 Toast
     * @param text Toast 内容
     */
    public static void toastLong(CharSequence text) {
        Toast.makeText(utils.application, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示一个长时 Toast
     * @param resId Toast 内容
     */
    public static void toastLong(@StringRes int resId) {
        Toast.makeText(utils.application, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 当前 Activity 中的 {@link Activity#findViewById(int)}
     * @param resourceId 需要 find 的 Id
     * @param <T> View 类型
     * @return 需要的 View ，已经进行强制类型转换
     */
    public static <T extends View> T $(@IdRes int resourceId) {
        return (T) utils.activity.findViewById(resourceId);
    }

    /**
     * 指定 View 中的 {@link View#findViewById(int)}
     * @param view 指定的 View
     * @param resourceId 需要 find 的 Id
     * @param <T> View 类型
     * @return 需要的 View ，已经进行强制类型转换
     */
    public static <T extends View> T $(View view, @IdRes int resourceId) {
        return (T) view.findViewById(resourceId);
    }

    /**
     * 在任何地方获取应用的{@link Application}
     * @return 应用的 {@link Application}实例
     */
    public static Application getApplication() {
        return utils.application;
    }

    /**
     * 在任何地方获取当前 Activity 实例
     * @return 当前 {@link Activity} 实例
     */
    public static Activity getActivity() {
        return utils.activity;
    }

    /**
     * 获取当前的时间戳
     * @return 当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前的语言国家代码，如 zh-CN，en-US 等等
     * @return
     */
    public static String getLanguageCountryCode() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * 默认值函数，判断第一个参数是否为 null ，若为 null 则返回第二个参数
     * @param o 需要判断是否为 null 的对象
     * @param defaultObject 当第一个参数为 null 时返回的对象
     * @param <T> 类型通配泛型
     * @return 结果，相当于 {@code o == null ? defaultObject : o}
     */
    public static <T> T defaultObject(T o, T defaultObject) {
        return o == null ? defaultObject : o;
    }

    /**
     * 复制文本到剪贴板
     * @param text 需要复制到剪贴板的文本
     */
    public static void copyToClipboard(String text){
        ClipboardManager clipboardManager = (ClipboardManager) utils.application.getSystemService(Activity.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(utils.application.getPackageName(), text));
    }

    /**
     * 通过 Drawable Resource Id 获取此 Drawable Resource 的 Bitmap<br>
     * @param res 指定的 Drawable Resource Id
     * @return 获取的 Bitmap
     */
    public static Bitmap getDrawableResourceAsBitmap(@DrawableRes int res) {
        return BitmapFactory.decodeResource(getApplication().getResources(), res);
    }

}
