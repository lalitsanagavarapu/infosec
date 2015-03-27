
import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class saxHandler extends DefaultHandler{
	private String currentTag;
    private String bodyText;
    private String currentTitle;
    private String currentID;
    private int iPageCount=0;
    summary sum;
	BufferedWriter bWriter;
	private String summaryFileName;
    saxHandler() throws IOException
    {
    	summaryFileName = "C:/data/summary.txt";
    	bWriter =new BufferedWriter(new FileWriter(summaryFileName));   
    }
    public String getSummaryFileName()
    {
    	return summaryFileName;
    }
    public void startElement(String uri, String name, String qName, Attributes attr){
            currentTag = qName;
            if (qName.equals("page") || qName.equals("PAGE")){
                    bodyText = "";
            	    currentTitle = "";
                    currentID = "";
            }
    }
    
    public void endElement(String uri, String name, String qName){
    	
            if (qName.equals("page") || qName.equals("PAGE")){
            	    //System.out.println("Body Text:"+bodyText);
            	    sum = new summary(bodyText);
            	    sum.summarize();
            	    bodyText = "";
            	    //printPageDetails();
            	    String summaryText = sum.getSummaryText();
            	    try {
						writeSummaryToFile(summaryText);
						currentID = "";
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    iPageCount++;
                                        
            }
            if (qName.equals("FILE")){
            	//printDocPostingListCollection();
            	// Sort the remaining content of unsorted posting collection and dump it to file.
            	// close the modified file.
            	try {
					bWriter.close();
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	}
            
        }
            
        
    public void characters(char ch[], int start, int length){
            if (currentTag.equals("title")){
                    currentTitle = currentTitle.concat(new String(ch, start, length));
            }
            else if (currentTag.equals("id") && (currentID.length() == 0)){
                currentID= new String(ch, start, length);
            }                
            else if (currentTag.equals("BODY") ||currentTag.equals("body")){
            	 bodyText = bodyText.concat(new String(ch, start, length));
            }
    }
    public void writeSummaryToFile(String summaryText) throws IOException
    {
    	try {
			
			bWriter.write(currentID + ":"+summaryText);
	        bWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
   }

