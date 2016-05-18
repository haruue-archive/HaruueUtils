package moe.haruue.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences 操作工具类
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SharedPreferencesUtils {

    SharedPreferences sharedPreferences;

    /**
     * 获取指定名称的私有 SharedPreferencesUtils
     * @param context {@link Context} 实例
     * @param name 指定的名称
     */
    public SharedPreferencesUtils(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 获取指定名称和模式的 SharedPreferencesUtils
     * @param context {@link Context} 实例
     * @param name 指定的名称
     * @param mode 模式， {@link Context#MODE_PRIVATE} 或 {@link Context#MODE_ENABLE_WRITE_AHEAD_LOGGING} 等等
     */
    public SharedPreferencesUtils(Context context, String name, int mode) {
        sharedPreferences = context.getSharedPreferences(name, mode);
    }

    /**
     * 获取本 SharedPreferences 中的所有数据
     * @return 存放数据的数组
     */
    public Map<String, ?> getAllData() {
        return sharedPreferences.getAll();
    }

    /**
     * 获取某一个数据
     * @param key 数据对应的 key
     * @param <T> 数据的类型
     * @return 获取到的数据
     */
    public <T> T getData(String key) {
        return (T) getAllData().get(key);
    }

    /**
     * 将数据存放到文件里
     * @param key 数据对应的 key
     * @param data 数据
     * @param editor SharedPreferences 编辑器
     * @param <T> 数据的类型
     * @throws ClassNotSupportedBySharedPreferencesException SharedPreferences 不支持该数据类型
     */
    private <T> void doPutData(String key, T data, SharedPreferences.Editor editor) throws ClassNotSupportedBySharedPreferencesException {
        Class dataClass = data.getClass();
        if (dataClass.equals(Integer.TYPE)) {
            editor.putInt(key, (Integer) data);
        } else if (dataClass.equals(Long.TYPE)) {
            editor.putLong(key, (Long) data);
        } else if (dataClass.equals(Boolean.TYPE)) {
            editor.putBoolean(key, (Boolean) data);
        } else if (dataClass.equals(Float.TYPE)) {
            editor.putFloat(key, (Float) data);
        } else if (dataClass.equals(String.class)) {
            editor.putString(key, (String) data);
        } else if (dataClass.equals(Set.class)) {
            try {
                editor.putStringSet(key, (Set<String>) data);
            } catch (ClassCastException e) {
                throw new ClassNotSupportedBySharedPreferencesException();
            }
        } else {
            throw new ClassNotSupportedBySharedPreferencesException();
        }
    }

    /**
     * 将键值对数据存入 SharedPreferences ，自动提交
     * @param key 键
     * @param data 值
     * @param <T> 值的类型
     * @throws ClassNotSupportedBySharedPreferencesException 不支持的类型
     */
    public <T> void putData(String key, T data) throws ClassNotSupportedBySharedPreferencesException {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        doPutData(key, data, editor);
        editor.apply();
    }

    /**
     * 将键值对 Map 存入 SharedPreferences ，自动提交
     * @param dataMap 键值对 Map
     * @throws ClassNotSupportedBySharedPreferencesException 不支持的类型
     */
    public void putDataMap(Map<String, ?> dataMap) throws ClassNotSupportedBySharedPreferencesException {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String k: dataMap.keySet()) {
            doPutData(k, dataMap.get(k), editor);
        }
        editor.apply();
    }

    /**
     * 类型不被 SharedPreferences 支持
     */
    public class ClassNotSupportedBySharedPreferencesException extends RuntimeException {
        public ClassNotSupportedBySharedPreferencesException() {
            super("SharedPreferences do not suppose this Class.");
        }
    }

    /**
     * 获取 {@link SharedPreferences} 实例
     * @return 本工具类对应的 {@link SharedPreferences} 实例
     */
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * 获取本工具类对应的 {@link SharedPreferences.Editor}<br>
     *     直接操作 {@link SharedPreferences.Editor} 时请手动调用 {@link SharedPreferences.Editor#apply()}
     * @return 工具类对应的 {@link SharedPreferences.Editor}
     */
    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }
}
