package main.java.com.hive.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
	public void getAllURL( Document doc, String host ) {
		Elements links = doc.select("a");
		for( Element link : links ) {
			String url = link.attr("href").toString();
			if( url.startsWith("http") ) {
				if( url.contains(host) ) {
					//url可用，存入redis
					//存入操作
					System.out.println(url);
				}
				else {
					continue;
				}
			}
			else if( url.startsWith("/") ) {
				//相对url转换为绝对url，并存入redis
				String complete_url = "http://" + host + url;
				//存入操作
				System.out.println(complete_url);
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
		String url = "http://dianying.fm/movie/the-croods/";
		hiveVertical hv = new hiveVertical();
		Document doc = hv.getText(url);
		hv.saveText2File(doc);
		hv.getAllURL(doc, hv.getHost(url));
	}
}
