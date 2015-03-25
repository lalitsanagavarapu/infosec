package Fetching;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

import edu.uci.ics.crawler4j.crawler.CrawlController;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;

import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;

import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;


public class MultipleCrawlerController {

	public static void main(String[] args) throws Exception {

		Date startTime=new Date();
		Date endTime=null;

		ArrayList<CrawlController> controller = new ArrayList<CrawlController>();

		List<String> links=Arrays.asList(CConstants.seeds);

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

		Calendar calobj = Calendar.getInstance();

		System.out.println("Start Time::"+df.format(calobj.getTime()));

		//String[] crawler1Domains=null;

		String crawlStorageFolder = "/data/crawl/root";

		CrawlConfig config1 =new CrawlConfig();


		try{
			config1.setPolitenessDelay(500);

			config1.setMaxDepthOfCrawling(10);

			config1.setMaxPagesToFetch(1000);

			config1.setProxyHost("172.16.0.200");

			config1.setProxyPort(8080);

			config1.setConnectionTimeout(5000);

			//config1.setUserAgentString("Ibcart");

			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();


	/*	for(int i=0;i<links.size();i++)

		{

			config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler"+i);

			PageFetcher pageFetcher1 = new PageFetcher(config1);

			//We will use the same RobotstxtServer for both of the crawlers.

			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

			CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);

			//crawler1Domains = new String[] { links.get(i) };

			//controller1.setCustomData(crawler1Domains);

			controller1.addSeed(links.get(i));

			controller.add(controller1);

		}

		;

		for(int j=0; j<controller.size();j++)

		{

			controller.get(j).startNonBlocking(BasicCrawler.class, 100);

		}

		

		for(int k=0; k<controller.size();k++)

		{
			controller.get(k).waitUntilFinish();

			System.out.println("Crawler "+k+" is finished.");

		}*/
			 
			/*

			 * crawlStorageFolder is a folder where intermediate crawl data is

			 * stored.

			 */
			
			for(int s=0;s<links.size();s=s+20)	{

				for(int i=s;i<s+20;i++){
					config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler"+i);
					PageFetcher pageFetcher1 = new PageFetcher(config1);
					RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);
					CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);
					controller1.addSeed(links.get(i));
					controller.add(controller1);
				}
				
				for(int j=s; j<s+20;j++){
					controller.get(j).startNonBlocking(BasicCrawler.class, 100);
				}

				for(int k=s; k<s+20;k++){
					controller.get(k).waitUntilFinish();
					System.out.println("Crawler "+k+" is finished.");
				}
			}

			Calendar calobje = Calendar.getInstance();
			System.out.println("End Time::"+df.format(calobje.getTime()));

			endTime=new Date();

			System.out.println("Time elapsed:"+(endTime.getTime()-startTime.getTime())/1000);

		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
