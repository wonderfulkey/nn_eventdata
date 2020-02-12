package com.isi.hz.mapper;

import com.isi.hz.pojo.XunchaDataInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {
    @SelectProvider(type = SqlProvider.class, method = "idList")
    List<Integer> idList(@Param("userId") Integer userId);

    @SelectProvider(type = SqlProvider.class, method = "getNewsList")
    ArrayList<XunchaDataInfo> getNewsList(String cusor, String eids);

    @SelectProvider(type = SqlProvider.class, method = "getBlogList")
    List<XunchaDataInfo> getBlogList(String cusor, String eids);

    @SelectProvider(type = SqlProvider.class, method = "getForumList")
    List<XunchaDataInfo> getForumList(String cusor, String eids);

    @SelectProvider(type = SqlProvider.class, method = "getMblogList")
    List<XunchaDataInfo> getMblogList(String cusor, String eids);

    @SelectProvider(type = SqlProvider.class, method = "getVideoList")
    List<XunchaDataInfo> getVideoList(String cusor, String eids);

    @SelectProvider(type = SqlProvider.class, method = "getWechatList")
    List<XunchaDataInfo> getWechatList(String cusor, String eids);

    @SelectProvider(type = SqlProvider.class, method = "getAppList")
    List<XunchaDataInfo> getAppList(String cusor, String eids);


    class SqlProvider {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public String idList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("eid");
            sql.FROM("event_info where `status` = 1 and (user_id = " + param.get("param1") + " or (dep_id = 35 and pub_level > 0 )) and event_type = 2 and end_time >= NOW()");
           // System.out.println(df.format(System.currentTimeMillis()) + " sql:" + sql.toString());
            return sql.toString();
        }

        public String getNewsList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("eid");
            sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("site_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_news_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            // System.out.println(df.format(System.currentTimeMillis()) + " sql:" + sql.toString());
            return sql.toString();
        }


        public String getBlogList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("eid");
            sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("site_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_blog_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            //   System.out.println(df.format(System.currentTimeMillis()) + " sql:"+sql.toString());
            return sql.toString();
        }

        public String getForumList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("eid");
            sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("site_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_threads_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            //    System.out.println(df.format(System.currentTimeMillis()) + " sql:"+sql.toString());
            return sql.toString();
        }

        public String getMblogList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("rtt_count 'rttNum'");
            sql.SELECT("eid");
            // sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("website_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary 'title'");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_mblog_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            //  System.out.println(df.format(System.currentTimeMillis()) + " sql:"+sql.toString());
            return sql.toString();
        }

        public String getVideoList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("eid");
            sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("website_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_video_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            //  System.out.println(df.format(System.currentTimeMillis()) + " sql:"+sql.toString());
            return sql.toString();
        }

        public String getWechatList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("rtt_count 'rttNum'");
            sql.SELECT("eid");
            sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("site_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_weichat_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            //  System.out.println(df.format(System.currentTimeMillis()) + " sql:"+sql.toString());
            return sql.toString();
        }

        public String getAppList(Map<String, Object> param) {
            SQL sql = new SQL();
            sql.SELECT("auto_id 'dataId'");
            sql.SELECT("cmt_count 'cmtNum'");
            sql.SELECT("eid");
            sql.SELECT("title");
            sql.SELECT("content");
            sql.SELECT("url");
            sql.SELECT("site_name 'siteName'");
            sql.SELECT("support_level 'emotionLevel'");
            sql.SELECT("pubtime");
            sql.SELECT("summary");
            sql.SELECT("keywords_label 'keywords'");
            sql.SELECT("is_dup 'isDup'");
            sql.SELECT("dup_id 'dupId'");
            sql.SELECT("blogger 'author'");
            sql.FROM("event_appdata_ref where auto_id > " + param.get("param1") + " and eid in (" + param.get("param2") + ") ORDER BY  auto_id asc limit 100");
            //   System.out.println(df.format(System.currentTimeMillis()) + " sql:"+sql.toString());
            return sql.toString();
        }
    }
}
