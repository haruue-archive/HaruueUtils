package moe.haruue.util;

import java.util.regex.Pattern;

/**
 * 常用的正则表达式工具
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class RegExUtils {

    /**
     * 检查邮箱格式是否合法
     * @param s 待检查的邮箱字符串
     * @return 检查结果
     */
    public static boolean checkEmail(String s) {
        return Pattern.matches("^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$", s);
    }

}
