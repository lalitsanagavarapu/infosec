
import java.io.*;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;



public class documentSummary {
	private static HashMap<String,Integer> sumHashMap = new HashMap<String,Integer>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SAXParserFactory factory = SAXParserFactory.newInstance();
		File summaryFile;
	     try {
	         String inputXML = "C:/data/Final.xml";
	    	 File xmlInput = new File(inputXML);
	         File xmlOutput = new File("C:/data/Modified.xml");
	         SAXParser saxParser = factory.newSAXParser();
	         saxHandler handler   = new saxHandler();
	         saxParser.parse(xmlInput, handler);
	         summaryFile = new File(handler.getSummaryFileName());
	         readSummaryFileAndBuildIndex(handler.getSummaryFileName());
	         writeTheSummaryIntoFinalXML(xmlInput,xmlOutput,handler.getSummaryFileName());
	    
	     } catch (Throwable err) {
	         err.printStackTrace ();
	     }
	}

	private static void writeTheSummaryIntoFinalXML(File xmlInput,
			File xmlOutput, String summaryFileName) throws IOException{
		// TODO Auto-generated method stub
		/* Read the input XML and get the summary for each page from summary file based on the
		 * page Id and write the input XML tags along with summary data into the modified XML.
		*/
		BufferedWriter bWriter =new BufferedWriter(new FileWriter(xmlOutput));
		BufferedReader bReaderInput =new BufferedReader(new FileReader(xmlInput)); 
		//BufferedReader bReaderSummary =new BufferedReader(new FileReader(summaryFile)); 
		RandomAccessFile summaryFile = new RandomAccessFile(summaryFileName,"rw");
		String inputLine,summaryLine="";
		String pageId;
		int offset =0;
		while((inputLine = bReaderInput.readLine()) !=null){
			bWriter.write(inputLine);
			bWriter.newLine();
			if (inputLine.contains("<id>") && inputLine.contains("</id>"))
			{
				System.out.println(inputLine);
				int i,j;
				char ch1='>';
				char ch2='<';
				i=inputLine.indexOf(ch1,0);
				j=inputLine.indexOf(ch2,i);
				String id = inputLine.substring(i+1, j);
				id = id.trim();
				System.out.println("id:"+id);
				if( sumHashMap.containsKey(id) == true) {
					//System.out.println("Key is present");
					offset = sumHashMap.get(id);
					summaryFile.seek(offset);
					String summaryLine1 = summaryFile.readLine();
					//System.out.println(summaryLine1);
					String temp[]=summaryLine1.split(":");
					if (temp.length > 0)
						summaryLine = temp[1];
					else
						summaryLine = "";
					char ch;
			    	 String finalInputData = "";
			    	 for ( int i1=0;i1 < summaryLine.length();i1++){
			    		 ch = summaryLine.charAt(i1);
			    		 if ((Character.isAlphabetic(ch) && (ch <= '\u007F' && ch != '\u0000')) || Character.isWhitespace(ch))
			    			 finalInputData +=Character.toString(ch);
			    	 }
					
					String output = new String("<SUMMARY>");
					output = output+finalInputData+"</SUMMARY>";
					
					bWriter.write(output);
					bWriter.newLine();
				}
				
			}
		}
		bWriter.close();
	}
	private static void readSummaryFileAndBuildIndex(String summaryFileName) throws IOException
    {
    	RandomAccessFile dictFile = new RandomAccessFile(summaryFileName,"rw");
    	
    	int iCount = 0;
    	int OffSet;
    	try {
			dictFile.seek(0);
			while (dictFile.getFilePointer() < dictFile.length()) {
				OffSet = ((int) dictFile.getFilePointer());
				String s = dictFile.readLine();
                String temp[] = s.split(":");
                String key = temp[0];
                key = key.trim();
                //OffSet = ((int) dictFile.getFilePointer());
                sumHashMap.put(key, OffSet);
                }
          } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dictFile.close();
    	/*
    	Set set = sumHashMap.entrySet();
        Iterator i = set.iterator();
      	   while(i.hasNext()) {
	           Map.Entry me = (Map.Entry)i.next();
	           System.out.println((String)me.getKey()+":"+Integer.toString((int) me.getValue()));
	        }
    	*/
    }

}
