package com.isi.hz.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.isi.hz.utils.AESUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@RestController
@RequestMapping("/data")
public class InternetController {
    @RequestMapping("/event.do")
    public JSONObject internet() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println(df.format(System.currentTimeMillis()) + " 接口: 数据请求已进行");
            ArrayList<String> fileList = new ArrayList<>();
            File file1 = new File("data");
            File[] files = file1.listFiles();
            for (File f : files) {
                fileList.add(f.getName());
            }
            if (null != fileList && fileList.size() > 0) {
                String name = fileList.get(0);
                String fileName = "data" + File.separator + name;
                File file = new File(fileName);
                FileUtils.readFileToByteArray(file);
                String data = FileUtils.readFileToString(file);
                System.out.println(df.format(System.currentTimeMillis()) + " 接口: 文本数据已采取");
                JSONArray array = JSONArray.parseArray(data);
                if (array.size() > 0) {
                    boolean flag = FileUtils.deleteQuietly(file);
                    if (flag) {
                        System.out.println(df.format(System.currentTimeMillis()) + " 接口: 文本已删除");
                        jsonObject.put("code", 200);
                        jsonObject.put("data", AESUtil.encrypt(data));
                    }
                } else {
                    jsonObject.put("code", 500);
                    jsonObject.put("data", "暂无数据");
                }
            } else {
                jsonObject.put("code", 500);
                jsonObject.put("data", "暂无数据");
            }
        } catch (IOException e) {
            jsonObject.put("code", 500);
            jsonObject.put("data", "系统异常");
            if (!e.getMessage().contains("does not exist")) {
                System.out.println(df.format(System.currentTimeMillis()) + " 接口: 暂无文本,等待下一次请求");
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}
