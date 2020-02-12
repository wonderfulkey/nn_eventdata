package com.isi.hz.utils;

import java.io.*;

public class FileUtils {

    public static String getCusor(Integer i) throws IOException {
        long cusor = 0;
        String fileName = "cusor" + File.separator;
        switch (i) {
            case 1:
                fileName += "news.txt";
                break;
            case 2:
                fileName += "blog.txt";
                break;
            case 3:
                fileName += "forum.txt";
                break;
            case 4:
                fileName += "mblog.txt";
                break;
            case 5:
                fileName += "video.txt";
                break;
            case 6:
                fileName += "wechat.txt";
                break;
            case 7:
                fileName += "app.txt";
                break;
        }
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        if (null == line) {
            line = "0";
        }
        br.close();
        return line;
    }

    public static void writeTOFile(String data) throws IOException {
        String fileName = "data" + File.separator + "data"+System.currentTimeMillis()+".txt";
        File writename = new File(fileName);
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(data);
        out.flush();
        out.close();
    }

    //更新标尺
    public static void updateCusor(int i, String cusor) throws IOException {
        String fileName = "cusor" + File.separator;
        switch (i) {
            case 1:
                fileName += "news.txt";
                break;
            case 2:
                fileName += "blog.txt";
                break;
            case 3:
                fileName += "forum.txt";
                break;
            case 4:
                fileName += "mblog.txt";
                break;
            case 5:
                fileName += "video.txt";
                break;
            case 6:
                fileName += "wechat.txt";
                break;
            case 7:
                fileName += "app.txt";
                break;
        }
        File writename = new File(fileName);
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(cusor);
        out.flush();
        out.close();
    }
}
