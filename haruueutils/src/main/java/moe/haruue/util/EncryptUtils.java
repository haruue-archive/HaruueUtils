package moe.haruue.util;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 加密、散列、编码解码解决方案工具类<br>
 *     提供以下功能：
 *     <ul>
 *         <li>sha1 编码</li>
 *         <li>MD5 编码</li>
 *         <li>Base64 编码/解码</li>
 *         <li>URL 编码/解码</li>
 *         <li>Unicode 编码/解码</li>
 *     </ul>
 *     所有方法都不会抛出“必须捕捉的异常”，当发生这种异常时，方法将会返回空值（空数组或者空字符串），但这并不意味着不会有任何异常被抛出，仍然建议对可能的异常进行捕捉。
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class EncryptUtils {

    /**
     * 将 {@link String} 转换成 {@link byte[]} 以便直接处理
     *
     * @param s 需要转换成 {@link byte[]} 数组的字符串
     * @return 转换完成的 {@link byte[]} 数组
     */
    public static byte[] stringToBytes(String s) {
        return s.getBytes();
    }

    public static String bytesToString(byte[] data) {
        return new String(data);
    }

    /**
     * 取 sha1 散列
     *
     * @param data 需要取 sha1 散列的 {@link byte[]} 数组，对于 String 可取 {@link EncryptUtils#stringToBytes(String)}
     * @return 所需的 sha1 散列，小写
     */
    public static String SHA1(byte[] data) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(data);
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取 MD5 散列
     *
     * @param data 需要取 MD5 散列的 {@link byte[]} 数组，对于 {@link String} 可取 {@link EncryptUtils#stringToBytes(String)}
     * @return 所需的 MD5 散列，小写
     */
    public static String MD5(byte[] data) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(data);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};

    private static byte[] base64DecodeChars = new byte[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    /**
     * Base64 编码
     *
     * @param data 需要被编码的数据的 {@link byte} 数组，对于 String 可取 {@link EncryptUtils#stringToBytes(String)}
     * @return 编码结果
     */
    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    /**
     * Base64 解码
     *
     * @param str 需要解码的 base64 字符串
     * @return 解码完成的 base64 数组
     */
    public static byte[] base64Decode(String str) {
        try {
            StringBuffer sb = new StringBuffer();
            byte[] data = new byte[0];
            data = str.getBytes("US-ASCII");
            int len = data.length;
            int i = 0;
            int b1, b2, b3, b4;
            while (i < len) {
            /* b1 */
                do {
                    b1 = base64DecodeChars[data[i++]];
                } while (i < len && b1 == -1);
                if (b1 == -1) break;
            /* b2 */
                do {
                    b2 = base64DecodeChars[data[i++]];
                } while (i < len && b2 == -1);
                if (b2 == -1) break;
                sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
            /* b3 */
                do {
                    b3 = data[i++];
                    if (b3 == 61) return sb.toString().getBytes("ISO-8859-1");
                    b3 = base64DecodeChars[b3];
                } while (i < len && b3 == -1);
                if (b3 == -1) break;
                sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
            /* b4 */
                do {
                    b4 = data[i++];
                    if (b4 == 61) return sb.toString().getBytes("ISO-8859-1");
                    b4 = base64DecodeChars[b4];
                } while (i < len && b4 == -1);
                if (b4 == -1) break;
                sb.append((char) (((b3 & 0x03) << 6) | b4));
            }
            return sb.toString().getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * 对字符串进行 URL 编码
     * @param s 需要进行 URL 编码的字符串
     * @return 需要的 URL 编码
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 对字符串进行 URL 解码
     * @param s 需要进行 URL 解码的字符串
     * @return 需要的解码结果
     */
    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }


    // TODO: 找到一种更好的 Unicode 转码方案

    /**
     * 将可读的字符串转换为 Unicode 编码字符串<br>
     *     例如： {@code 春上冰月 -> \u6625\u4e0a\u51b0\u6708}
     * @param s 需要转码的字符串
     * @return 所需的 Unicode 编码的字符串
     * @deprecated 不完全转换
     */
    @Deprecated
    public static String nativeToAscii(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch: s.toCharArray()) {
            if (Pattern.matches("[A-Za-z0-9]", ch + "")) continue;
            sb.append("\\u").append(Integer.toHexString(ch));
        }
        return sb.toString();
    }

    /**
     * 将 Unicode 编码的字符串转换为可读的字符串<br>
     *     例如：{@code \u6625\u4e0a\u51b0\u6708 -> 春上冰月}
     * @param ascii 需要转码的 Unicode 字符串
     * @return 所需的可读字符串
     * @deprecated 不完全转换
     */
    @Deprecated
    public static String asciiToNative(String ascii) {
        Matcher matcher = Pattern.compile("\\\\[Uu](....)").matcher(ascii);
        matcher.reset();
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, (char) Integer.parseInt(matcher.group(1), 16) + "");
        }
        return matcher.appendTail(buffer).toString();
    }

    /**
     * 使用前缀和参数获取完整 URL
     * @param url URL 接口前缀，如果没有的话可以传入空值
     * @param fieldMap 参数 Map
     * @return 完整的 URL
     */
    public static String gainCompleteUrl(@Nullable String url, Map<?, ?> fieldMap) {
        boolean isFirst = false;
        if (fieldMap == null || fieldMap.isEmpty()) {
            return url;
        }
        url = StandardUtils.defaultObject(url, "");
        if (!Pattern.matches(".*\\?.*", url)) {
            isFirst = true;
        }
        StringBuilder sb = new StringBuilder(url);
        for (Object k: fieldMap.keySet()) {
            if (isFirst) {
                sb.append("?");
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(urlEncode(k.toString())).append("=").append(urlEncode(fieldMap.get(k).toString()));
        }
        return sb.toString();
    }

}
