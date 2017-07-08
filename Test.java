package com.gbzhu.html;

import java.io.IOException;
import java.util.UUID;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	private static String charset = "utf-8";
	private static String YEAR = "2017-";
	private static String HOST = "http://www.tvmao.com/";
	public static void main(String[] args) throws IOException {
		String html = HtmlParser.getHtml("http://www.tvmao.com/program/CCTV-CCTV1-w4.html", charset);
		Element weekDay = HtmlParser.selectTag(html, ".weekcur").first();
		String week = weekDay.select("a").first().text();
		String day = weekDay.select("span").first().text();
		Elements channels = HtmlParser.selectTag(html, ".chlsnav>ul a");
		
		handleDiv(html,week,day);
		for (Element channel : channels) {
			String redirect = HtmlParser.getHtml(HOST+channel.attr("href"), charset);
			handleDiv(redirect,week,day);
		}
		Elements channelTypes = HtmlParser.selectTag(html, ".chlsnav>a");
		for(Element channelType : channelTypes){
			String redirect1 = HtmlParser.getHtml(HOST+channelType.attr("href"),charset);
			Elements channels1 = HtmlParser.selectTag(redirect1, ".chlsnav>ul a");
			for (Element channel : channels1) {
				String redirect = HtmlParser.getHtml(HOST+channel.attr("href"), charset);
				handleDiv(redirect,week,day);
			}
		}
	}
	private static void handleDiv(String html, String week, String day) throws IOException {
		String channelName = HtmlParser.selectTag(html, ".curchn").first().text();
		Elements divs = HtmlParser.selectTag(html, "#pgrow div");
		for (Element div : divs) {
			String id = UUID.randomUUID().toString();
			Element span1 = div.select("span.am").first();
			String time = "";
			if(null != span1){
				time = span1.text();				
			}
			Element span2 = div.select("span.p_show").first();
			String fullTitle = "";
			String title = "";
			String href = "";
			if(null == span2){
				continue;
			}
			fullTitle = span2.text();		
			try {
				title = span2.select("a").first().attr("title");
				href = span2.select("a").first().attr("href");
			} catch (Exception e) {
				continue;
			}
			String redirect = HtmlParser.getHtml(HOST+href, charset);
			Elements redirectE = HtmlParser.selectTag(redirect, ".section-wrap>table td");
			String type = "µçÊÓ¾ç";
			if(!redirectE.isEmpty() && redirectE.size()>3){
				type = redirectE.get(3).text();
			}
			System.out.println(id+ "\t"+ channelName +"\t" + YEAR+day+"\t" + week + "\t" + time + "\t" +fullTitle+"\t" +title + "\t" + type );
		}
	}
}
