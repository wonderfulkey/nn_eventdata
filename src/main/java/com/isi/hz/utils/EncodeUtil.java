package com.isi.hz.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodeUtil {
    // base62
    static String[] str62key = {"0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z"};

    // base64
    static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();
    static private byte[] codes = new byte[256];

    static {
        for (int i = 0; i < 256; i++)
            codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++)
            codes[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++)
            codes[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++)
            codes[i] = (byte) (52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    /**
     * url转化成mid的�?
     *
     * @param url
     * @return
     */
    public static String url2mid(String url) {
        String mid = "";
        for (int i = url.length() - 4; i > -4; i = i - 4) {// 分别以四个为�?��
            int offset1 = i < 0 ? 0 : i;
            int offset2 = i + 4;
            String str = url.toString().substring(offset1, offset2);
            str = str62to10(str);// String类型的转化成十进制的�?
            // 若不是第�?��，则不足7位补0
            if (offset1 > 0) {
                while (str.length() < 7) {
                    str = '0' + str;
                }
            }
            mid = str + mid;
        }

        return mid;
    }

    /**
     * mid转换成url编码以后的�?
     *
     * @param mid
     * @return
     */
    public static String mid2url(String mid) {
        String url = "";
        for (int j = mid.length() - 7; j > -7; j = j - 7) {// �?个数字为�?��单位进行转换
            int offset3 = j < 0 ? 0 : j;
            int offset4 = j + 7;
            // String l = mid.substring(mid.length() - 14, mid.length() - 13);
            if ((j > 0 && j < 6)
                    && (mid.substring(mid.length() - 14, mid.length() - 13)
                    .equals("0") && mid.length() == 19)) {
                String num = mid.toString().substring(offset3 + 1, offset4);
                num = int10to62(Integer.valueOf(num));// 十进制转换成62进制
                url = 0 + num + url;
                if (url.length() == 9) {
                    url = url.substring(1, url.length());
                }
            } else {
                String num = mid.toString().substring(offset3, offset4);
                num = int10to62(Integer.valueOf(num));
                // 不足4位补0
                if (offset3 > 0) {
                    while (num.length() < 4) {
                        num = "0" + num;
                    }
                }
                url = num + url;
            }
        }

        return url;
    }

    /**
     * 62进制转换�?0进制
     *
     * @param str
     * @return
     */
    private static String str62to10(String str) {
        String i10 = "0";
        int c = 0;
        for (int i = 0; i < str.length(); i++) {
            int n = str.length() - i - 1;
            String s = str.substring(i, i + 1);
            for (int k = 0; k < str62key.length; k++) {
                if (s.equals(str62key[k])) {
                    int h = k;
                    c += (int) (h * Math.pow(62, n));
                    break;
                }
            }
            i10 = String.valueOf(c);
        }
        return i10;
    }

    /**
     * 10进制转换�?2进制
     *
     * @param int10
     * @return
     */
    private static String int10to62(double int10) {
        String s62 = "";
        int w = (int) int10;
        int r = 0;
        int a = 0;
        while (w != 0) {
            r = (int) (w % 62);
            s62 = str62key[r] + s62;
            a = (int) (w / 62);
            w = (int) Math.floor(a);
        }
        return s62;
    }

    /**
     * SHA1 加密
     *
     * @param url
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSHA1Encoder(String url)
            throws NoSuchAlgorithmException {
        if (url == null || url.trim().equals("")) {
            return null;
        }
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] bytes = digest.digest(url.getBytes());
        StringBuilder output = new StringBuilder(bytes.length);
        for (Byte entry : bytes) {
            output.append(String.format("%02x", entry));
        }
        digest.reset();
        return output.toString();
    }

    /**
     * 将原始数据编码为base64编码
     */
    public static char[] getBase64Encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;
            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }

    /**
     * 将base64编码的数据解码成原始数据
     */
    public static byte[] getBase64Decode(char[] data) {
        int len = ((data.length + 3) / 4) * 3;
        if (data.length > 0 && data[data.length - 1] == '=')
            --len;
        if (data.length > 1 && data[data.length - 2] == '=')
            --len;
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (int ix = 0; ix < data.length; ix++) {
            int value = codes[data[ix] & 0xFF];
            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if (shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }
        if (index != out.length)
            throw new Error("miscalculated data length!");
        return out;
    }

    /**
     * 获取某个字符串的md5�?
     *
     * @param text
     * @return
     */
    public synchronized static String getMD5(String text) {
        MessageDigest digest;
        String md5Value = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(text.getBytes("utf-8"));
            StringBuilder output = new StringBuilder(bytes.length);
            for (Byte entry : bytes) {
                output.append(String.format("%02x", entry));
            }
            digest.reset();
            md5Value = output.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Value;
    }

    /**
     * 转换16进制的Unicode<br>
     * 比如将�?\u5352”转换为“卒�?
     *
     * @param code 转换前字�?
     * @return String 转换结果
     */
    public static String convertFromHex(String code) {
        StringBuffer sb = new StringBuffer(code);
        int pos;
        while ((pos = sb.indexOf("\\u")) > -1) {

            String tmp = sb.substring(pos, pos + 6);
            sb.replace(pos, pos + 6, Character.toString((char) Integer
                    .parseInt(tmp.substring(2), 16)));
        }
        return code = sb.toString();
        // return toUtf8(code);
    }

    /**
     * 得到url的hot 如http://www.baidu.com host �?baidu.com
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {
        if (url == null) {
            return null;
        }
        url = url.replaceAll("^https?://", "").replaceAll("([^/]+).*", "$1");
        return url;
    }

    /**
     * 获取某个字符串的md5�?
     *
     * @param text
     * @return
     */
    public synchronized static String MD5(String text) {
        MessageDigest digest;
        String md5Value = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(text.getBytes("utf-8"));
            StringBuilder output = new StringBuilder(bytes.length);
            for (Byte entry : bytes) {
                output.append(String.format("%02x", entry));
            }
            digest.reset();
            md5Value = output.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Value;
    }

    public static String getCleanString(String html) {
        if (html == null || html.equals("")) {
            return null;
        }
        String temphtml = html.replaceAll("\\s", "");
        temphtml = temphtml.replace("\n", "");
        temphtml = temphtml.replace("\r", "");
        temphtml = temphtml.replace("\t", "");
        temphtml = temphtml.replace("\\n", "");
        temphtml = temphtml.replace("\\r", "");
        temphtml = temphtml.replace("\\t", "");
        return temphtml;
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GB2312
                String s = encode;
                return s; //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    public static void main(String[] args) {

        // try {
        // System.out.println(getSHA1Encoder("a"));
        // } catch (NoSuchAlgorithmException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        //
        // String aa = url2mid("xtMaBAmVR");
        // System.out.println(aa);
        // String bb = mid2url("3370966818667963");
        // System.out.println(bb);
        //
        //
        /*
         * String strSrc =
         * "isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中国isiteam我爱你中�?
         * ; for(int i = 0; i < 100; i++){ strSrc += strSrc+"\n"; }
         * System.out.println(strSrc.length()); String strOut = new
         * String(getBase64Encode(strSrc.getBytes())); char[] temp =
         * strOut.toCharArray(); String temp2 = "";
         *
         * System.out.println("1------"+new String(temp)); // for(int i = 0 ; i
         * < temp.length; i ++){ // boolean get = false; // for(int j=0; j <
         * str62key.length; j++){ //
         * if(Character.toString(temp[i]).equals(str62key[j])){ // int point =
         * (j + 3)%str62key.length; // temp2 += str62key[point]; // get = true;
         * // break; // } // } // if(!get){ // temp2 += temp[i]; // } // }
         * System.out.println("2------"+new String(temp2));
         *
         * System.out.println(strOut); // String strOut2 = new
         * String(getBase64Decode(strOut.toCharArray())); String strOut2 = new
         * String(getBase64Decode(temp2.toCharArray()));
         * System.out.println(strOut2); // // //select userid from tw_userinfo
         * where md5='b91500aa72666e0aada844db7a5933ca'; //
         * System.out.println(getMD5("http://weibo.com/2267093583"));
         *
         * System.out.println(mid2url("3478352352486156"));
         */
        // System.out.println(MD5("http://t.qq.com/p/t/277985119951402"));

        // System.out.println(getBase64Encode("http://world.people.com.cn/n/2012/0726/c1002-18604130.html".getBytes()));

        // System.out.println(MD5("http://t.qq.com/wxnf-b-_c8"));
        // String headImg =
        // "background-image: url(\"http://s5.cr.itc.cn/mblog/icon/60/c7/78625388857247873.gif\");";
        // System.out.println(headImg.substring(headImg.indexOf("http"),
        // headImg.lastIndexOf("\"")));

        // System.out.println(url2mid("5en0V1m2jEC"));
        // System.out.println(mid2url("6575164431"));

        // System.out.println(convertFromHex("\u5e94\u7528\u8bc4\u5206\u5931\u8d25"));

        System.out.println(url2mid("A2T0B8ncr"));
        System.out.println(url2mid("A4ROs0ivG"));//3611601720071156
//		System.out.println(mid2url("3611862015607676"));
        System.out.println(mid2url("3606881131995807"));
        System.out.println(mid2url("3611601720071156"));

        System.out.println(url2mid("A0T0B8ncr"));
        // System.out.println(url2mid("A4ROs0ivG"));//3611601720071156
        // System.out.println(mid2url("3611862015607676"));
        System.out.println(mid2url("3602114571995807"));
        // System.out.println(mid2url("3611601720071156"));

    }
}
