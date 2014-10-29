package com.xiyoulinux.hive.model;

/**
 * Created by Junco on 14-10-29.
 */
import java.io.IOException;

import com.xiyoulinux.hive.bloomfilter.hiveBloomFilter;
import com.xiyoulinux.hive.redis.hiveRedis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class hiveVertical {

    /**
     * Method Description: 根据指定的URL，获取当前页面的内容
     * @date 2014年7月29日   上午10:01:33
     * @author Junco
     * @param url
     * @throws IOException
     */
    public Document getText( String url ) throws IOException {
        Document doc = Jsoup.connect(url).timeout(5000).get();
        return doc;
    }

    /**
     * Method Description: 根据Jsoup解析出来的Document，提取出其中所有的链接
     * @date 2014年7月29日   上午11:37:21
     * @author Junco
     * @param doc
     * @return
     */
    public void getAllURL( Document doc, String host, hiveRedis redis, hiveBloomFilter filter ) {
        Elements links = doc.select("a[href]");
        for ( Element link : links ) {
            String url = link.attr("abs:href");
            if( url.contains(host) && !filter.exist(url) ) {
                redis.putURL(url);
                System.out.println(url);
            }
            else {
                continue;
            }
        }
    }

    /**
     * Method Description: 获取原始url的host，以便链接过滤用
     * @date 2014年7月30日   下午2:43:39
     * @author Junco
     * @param url
     * @return
     */
    public String getHost( String url ) {
        String part_of_url[] = url.split("/");
        return part_of_url[2];
    }

    public void saveText2File( Document doc ) {

    }

    public static void main( String[] args ) throws IOException {
        String url = "http://politics.people.com.cn/n/2014/0801/c1024-25380603.html";
        hiveRedis redis = new hiveRedis("Junco");
        hiveVertical hv = new hiveVertical();
        hiveBloomFilter filter = new hiveBloomFilter();
        redis.connectRedis();
        redis.putURL(url);
        while( redis.getLength() != 0 ) {
            Document doc = hv.getText(url);
            hv.saveText2File(doc);
            hv.getAllURL(doc, hv.getHost(url), redis, filter);
            redis.remove(url);
            url = redis.getURL();
            if( url == null ) {
                System.out.println("Done");
                break;
            }
        }
        redis.destory();
        System.gc();
    }
}
