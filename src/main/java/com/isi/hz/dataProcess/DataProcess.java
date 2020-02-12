package com.isi.hz.dataProcess;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.isi.hz.mapper.DataMapper;
import com.isi.hz.pojo.XunchaDataInfo;
import com.isi.hz.utils.EncodeUtil;
import com.isi.hz.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DataProcess {
    private int limit = 1500;
    @Value("${userId}")
    private Integer userId;
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private DataMapper dataMapper;

    @Scheduled(fixedDelay = 1000 * 80)
    public void internetData() {
        JSONArray data = new JSONArray();
        try {
            List<Integer> ids = dataMapper.idList(userId);
            if (null == ids || ids.size() < 1) {
                System.out.println(format2.format(System.currentTimeMillis()) + " 暂无预警任务");
                return;
            }
            StringBuffer eidsb = new StringBuffer();
            for (Integer id : ids) {
                eidsb.append(id + ",");
            }
            String s = eidsb.toString();
            String eids = s.substring(0, s.length() - 1);
            System.out.println(format2.format(System.currentTimeMillis()) + " 获取eid: " + eids);
            //1:新闻;2:博客;3:论坛;4:微博;5:视频;6：QQ;7:微信公众号;8:APP;9:电子报纸;10:今日头条 11 youtube;12：facebook;13:Instagram  14：twitter
            data = this.processNews(data, eids);
            data = this.processBlog(data, eids);
            data = this.processForum(data, eids);
            data = this.processMblog(data, eids);
            data = this.processVideo(data, eids);
            data = this.processWechat(data, eids);
            data = this.processApp(data, eids);
            if (null != data && data.size() > 0) {
                String dataS = data.toJSONString();
                FileUtils.writeTOFile(dataS);
                System.out.println(format2.format(System.currentTimeMillis()) + " 本轮数据采取完毕,共采取" + data.size() + "条数据,等待下一轮");
            } else {
                System.out.println(format2.format(System.currentTimeMillis()) + " 没有数据,等待下一轮");
                return;
            }
        } catch (Exception e) {
            System.out.println("系统异常,10分钟后查询");
            e.printStackTrace();
            return;
        }
    }

    private JSONArray processNews(JSONArray data, String eids) throws IOException {
        String newsCusor = "0"; //增量标识
        newsCusor = FileUtils.getCusor(1);
        System.out.println(format2.format(System.currentTimeMillis()) + " 新闻: 开始获取数据,此时id为 :" + newsCusor);
        List<XunchaDataInfo> newsList = dataMapper.getNewsList(newsCusor, eids);
        //newsList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUrl())))));
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(1, xunchaDataInfo);
                data.add(jsonObject);
            }
            newsCusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(1, newsCusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " 新闻:该批次数据已经处理到id为 :" + newsCusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " 新闻:该批次无该类型数据");

        }
        return data;
    }

    private JSONArray processBlog(JSONArray data, String eids) throws IOException {
        String Cusor = "0"; //增量标识
        Cusor = FileUtils.getCusor(2);
        System.out.println(format2.format(System.currentTimeMillis()) + " 博客: 开始获取数据,此时id为 :" + Cusor);
        List<XunchaDataInfo> newsList = dataMapper.getBlogList(Cusor, eids);
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(2, xunchaDataInfo);
                data.add(jsonObject);
            }
            Cusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(2, Cusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " 博客:该批次数据已经处理到id为 :" + Cusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " 博客:该批次无该类型数据");

        }
        return data;
    }

    private JSONArray processForum(JSONArray data, String eids) throws IOException {
        String Cusor = "0"; //增量标识
        Cusor = FileUtils.getCusor(3);
        System.out.println(format2.format(System.currentTimeMillis()) + " 论坛: 开始获取数据,此时id为 :" + Cusor);
        List<XunchaDataInfo> newsList = dataMapper.getForumList(Cusor, eids);
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(3, xunchaDataInfo);
                data.add(jsonObject);
            }
            Cusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(3, Cusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " 论坛:该批次数据已经处理到id为 :" + Cusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " 论坛:该批次无该类型数据");

        }
        return data;
    }

    private JSONArray processMblog(JSONArray data, String eids) throws IOException {
        String Cusor = "0"; //增量标识
        Cusor = FileUtils.getCusor(4);
        System.out.println(format2.format(System.currentTimeMillis()) + " 微博: 开始获取数据,此时id为 :" + Cusor);
        List<XunchaDataInfo> newsList = dataMapper.getMblogList(Cusor, eids);
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(4, xunchaDataInfo);
                data.add(jsonObject);
            }
            Cusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(4, Cusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " 微博:该批次数据已经处理到id为 :" + Cusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " 微博:该批次无该类型数据");
        }
        return data;
    }

    private JSONArray processVideo(JSONArray data, String eids) throws IOException {
        String Cusor = "0"; //增量标识
        Cusor = FileUtils.getCusor(5);
        System.out.println(format2.format(System.currentTimeMillis()) + " 视频: 开始获取数据,此时id为 :" + Cusor);
        List<XunchaDataInfo> newsList = dataMapper.getVideoList(Cusor, eids);
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(5, xunchaDataInfo);
                data.add(jsonObject);
            }
            Cusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(5, Cusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " 视频:该批次数据已经处理到id为 :" + Cusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " 视频:该批次无该类型数据");
        }
        return data;
    }

    private JSONArray processWechat(JSONArray data, String eids) throws IOException {
        String Cusor = "0"; //增量标识
        Cusor = FileUtils.getCusor(6);
        System.out.println(format2.format(System.currentTimeMillis()) + " 微信: 开始获取数据,此时id为 :" + Cusor);
        List<XunchaDataInfo> newsList = dataMapper.getWechatList(Cusor, eids);
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(7, xunchaDataInfo);
                data.add(jsonObject);
            }
            Cusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(6, Cusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " 微信:该批次数据已经处理到id为 :" + Cusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " 微信:该批次无该类型数据");
        }
        return data;
    }

    private JSONArray processApp(JSONArray data, String eids) throws IOException {
        String Cusor = "0"; //增量标识
        Cusor = FileUtils.getCusor(7);
        System.out.println(format2.format(System.currentTimeMillis()) + " APP: 开始获取数据,此时id为 :" + Cusor);
        List<XunchaDataInfo> newsList = dataMapper.getAppList(Cusor, eids);
        if (null != newsList && newsList.size() > 0) {
            for (XunchaDataInfo xunchaDataInfo : newsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = transEntityToJSON(8, xunchaDataInfo);
                data.add(jsonObject);
            }
            Cusor = String.valueOf(newsList.get(newsList.size() - 1).getDataId() + 1);
            FileUtils.updateCusor(7, Cusor);
            System.out.println(format2.format(System.currentTimeMillis()) + " APP:该批次数据已经处理到id为 :" + Cusor);
        } else {
            System.out.println(format2.format(System.currentTimeMillis()) + " APP:该批次无该类型数据");

        }
        return data;
    }

    private JSONObject transEntityToJSON(int type, XunchaDataInfo xunchaDataInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data_id", xunchaDataInfo.getDataId());
        jsonObject.put("view_num", xunchaDataInfo.getViewNum() == null ? 0 : xunchaDataInfo.getViewNum());
        jsonObject.put("cmt_num", xunchaDataInfo.getCmtNum() == null ? 0 : xunchaDataInfo.getCmtNum());
        jsonObject.put("rtt_num", xunchaDataInfo.getRttNum() == null ? 0 : xunchaDataInfo.getRttNum());
        //jsonObject.put("rtt_num", xunchaDataInfo.getLikeNum() == null ? 0 : xunchaDataInfo.getLikeNum());
        jsonObject.put("eid", xunchaDataInfo.getEid());
        jsonObject.put("info_type", type);
        jsonObject.put("title", xunchaDataInfo.getTitle());
        jsonObject.put("content", xunchaDataInfo.getContent());
        jsonObject.put("url", xunchaDataInfo.getUrl());
        jsonObject.put("urlMD5", StringUtils.isEmpty(xunchaDataInfo.getUrl()) ? null : EncodeUtil.getMD5(xunchaDataInfo.getUrl()));
        jsonObject.put("site_name", xunchaDataInfo.getSiteName());
        jsonObject.put("emotion_level", xunchaDataInfo.getEmotionLevel());
        jsonObject.put("pubtime", xunchaDataInfo.getPubtime());
        jsonObject.put("summary", xunchaDataInfo.getSummary());
        jsonObject.put("keywords", xunchaDataInfo.getKeywords());
        jsonObject.put("is_dup", xunchaDataInfo.getIsDup());
        jsonObject.put("dup_id", xunchaDataInfo.getDupId());
        jsonObject.put("author", xunchaDataInfo.getAuthor());
        return jsonObject;
    }
}
