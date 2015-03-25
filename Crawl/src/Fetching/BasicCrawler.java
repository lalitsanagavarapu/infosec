package Fetching;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;

import Intelligence.ClassifyTest2;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class BasicCrawler extends WebCrawler {

	private final static Pattern FILTERS =
			Pattern.compile(".*\\.(bmp|gif|jpe?g|png|tiff?|pdf|ico|xaml|pict|rif|pptx?|ps" +
					"|mid|mp2|mp3|mp4|wav|wma|au|aiff|flac|ogg|3gp|aac|amr|au|vox" +
					"|avi|mov|css|mpe?g|ra?m|m4v|smil|wm?v|swf|aaf|asf|flv|mkv" +
					"|zip|rar|gz|7z|aac|ace|alz|apk|arc|arj|dmg|jar|lzip|lha)" +
					"(\\?.*)?$"); // For url Query parts ( URL?q=... )

	private static int count=0;
	private static int count2=0;
	ClassifyTest2 classify=new ClassifyTest2();

	@Override
	public boolean shouldVisit(Page page, WebURL url) {
		String href = url.getURL().toLowerCase();
		try
		{
			if (FILTERS.matcher(href).matches()) {
				return false;
			}
			return true;

		}catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public void visit(Page page) {
		try {

			String fullContent="";
			int pageClass=0;
			String writePath="";
			String url = page.getWebURL().getURL();
			String pageContent = new String(page.getContentData(), page.getContentCharset());
			String content= Jsoup.parse(pageContent).text();

			String xml10pattern = "[^"
					+ "\u0009\r\n"
					+ "\u0020-\uD7FF"
					+ "\uE000-\uFFFD"
					+ "\ud800\udc00-\udbff\udfff"
					+ "]";
			
			String lastModified=null;

			Header[] responseHeaders = page.getFetchResponseHeaders();
			if (responseHeaders != null) {
				//System.out.println("Response headers:");
				for (Header header : responseHeaders) {
					//System.out.println(header.getValue()+":"+ header.getName());
					if(header.getName().equalsIgnoreCase("last-modified"))
						lastModified=header.getValue();
				}
			}

			if (page.getParseData() instanceof HtmlParseData) {

				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				Set<WebURL> links = htmlParseData.getOutgoingUrls();
				String title= htmlParseData.getTitle();

				System.out.println("Parsing html:"+title);
				Map<String, String> meta=htmlParseData.getMetaTags();
				String metaKeywords=null;
				if(meta.get("keywords")!=null){
					metaKeywords=meta.get("keywords");  
					if(meta.get("description")!=null)
						metaKeywords=metaKeywords.concat(meta.get("description")); 
				}
				else if(meta.get("description")!=null)
						metaKeywords=meta.get("description"); 


				//writing to xml file

				//BufferedWriter writer = null;
				BufferedWriter writer2 = null;

				try {
					System.out.println("Writing to file:"+url);

					fullContent=fullContent.concat(url+" \n");
					if(metaKeywords!=null)
						fullContent=fullContent.concat(metaKeywords+" \n");
					fullContent=fullContent.concat(title+" \n");
					fullContent=fullContent.concat(content+" \n");
					for (WebURL webURL : links){
						fullContent=fullContent.concat(webURL.toString()+" ");
					}

					fullContent=fullContent.replaceAll(xml10pattern, "");


					pageClass=classify.classifyPage(fullContent);

					if(pageClass==0);
						//writePath="C:\\Users\\admin\\Desktop\\CraI2\\ClassifiedDocs\\2-1\\irrelevantSites\\"+(++count)+".txt";
					else{
						writePath="C:\\Users\\admin\\Desktop\\CraI2\\ClassifiedDocs\\2-1\\relevantSites\\"+(++count)+".txt";

						url=url.replace("&", "&amp;");
						url=url.replace("<", "&lt;");
						url=url.replace(">", "&gt;");
						url=url.replace("'", "&apos;");
						url=url.replace("\"", "&quot;");
						
						metaKeywords=metaKeywords.replace("&", "&amp;");
						metaKeywords=metaKeywords.replace("<", "&lt;");
						metaKeywords=metaKeywords.replace(">", "&gt;");
						metaKeywords=metaKeywords.replace("'", "&apos;");
						metaKeywords=metaKeywords.replace("\"", "&quot;");

						title=title.replace("&", "&amp;");
						title=title.replace("<", "&lt;");
						title=title.replace(">", "&gt;");
						title=title.replace("'", "&apos;");
						title=title.replace("\"", "&quot;");
						
						content=content.replace("&", "&amp;");
						content=content.replace("<", "&lt;");
						content=content.replace(">", "&gt;");
						content=content.replace("'", "&apos;");
						content=content.replace("\"", "&quot;");

						writer2 = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream("C:\\Users\\slmohan\\Desktop\\CrawlerData\\XMLs\\"+(++count2)+".xml"), "utf-8"));
						
						writer2.write("<PAGE>");
						writer2.newLine();
						writer2.write("<id>"+count2+"</id>");
						writer2.newLine();
						writer2.write("<URL>"+url+"</URL>");
						writer2.newLine();
						writer2.write("<META>"+metaKeywords+"</META>");
						writer2.newLine();
						writer2.write("<TITLE>"+title+"</TITLE>");
						writer2.newLine();
						writer2.write("<BODY>"+content+"</BODY>");
						writer2.newLine();
						writer2.write("<LINKS>");
						String webURL2;
						for (WebURL webURL : links) {
							webURL2=webURL.toString().replace("&", "&amp;");
							writer2.write("<LINK>"+webURL2.toString()+"</LINK>");
						}
						writer2.write("</LINKS>");
						writer2.newLine();
						writer2.write("<LASTMODIFIED>"+lastModified+"</LASTMODIFIED>");
						writer2.newLine();
						writer2.write("</PAGE>");
					}


					/*writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(writePath), "utf-8"));
					writer.write(fullContent);*/
					
					
					

				} catch (Exception ex) {
					ex.printStackTrace();
					// report
				} finally {
					try {
						//if(writer!=null)
							//writer.close(); 
						if(writer2!=null)
						  writer2.close();
						} catch (Exception ex) {ex.printStackTrace();}
				}

				//end

			}



			System.out.println("=============");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
