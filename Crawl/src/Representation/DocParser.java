package Representation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DocParser {

	//This variable will hold all terms of each document in an array.
	private List termsDocsArray = new ArrayList<>();
	private List allTerms = new ArrayList<>(); //to hold all terms
	private List tfidfDocsVector = new ArrayList<>();
	
	double[] tfs=new double[20];
	double[] idfs=new double[20];

	/**
	 * Method to read files and store in array.
	 * @param filePath : source file path
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parseFiles(String filePath) throws FileNotFoundException, IOException {
		File[] allfiles = new File(filePath).listFiles();
		BufferedReader in = null;
		for (File f : allfiles) {
			if (f.getName().endsWith(".txt")) {
				in = new BufferedReader(new FileReader(f));
				StringBuilder sb = new StringBuilder();
				String s = null;
				while ((s = in.readLine()) != null) {
					sb.append(s);
				}
				String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
				for (String term : tokenizedTerms) {
					if (!allTerms.contains(term)) {  //avoid duplicate entry
						allTerms.add(term);
					}
				}
				termsDocsArray.add(tokenizedTerms);
			}
		}

	}

	/**
	 * Method to create termVector according to its tfidf score.
	 */
	public void tfIdfCalculator() {
		double tf; //term frequency
		double idf; //inverse document frequency
		double tfidf; //term requency inverse document frequency  
		
		for (Object termDoc : termsDocsArray) {
			
			double[] tfs=new double[allTerms.size()];
			double[] idfs=new double[allTerms.size()];
			
			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			String[] docTermsArray=(String[])termDoc;
			
			for (Object terms : allTerms) {
				String term=(String)terms;
				tf = new TfIdf().tfCalculator(docTermsArray, term);
				idf = new TfIdf().idfCalculator(termsDocsArray, term);
				tfidf = tf * idf;
				
				System.out.print(term+":"+tf+":"+idf+" ");
				
				tfidfvectors[count] = tfidf;
				
				tfs[count]=tf;
				idfs[count]=idf;
				
				count++;
			}
			tfidfDocsVector.add(tfidfvectors);  //storing document vectors;
			
			System.out.println("\ntfs:");
			/*for (double tfs1 : tfs) {
				System.out.print(tfs1+" ");
			}*/
			
			System.out.println("\nidfs:");
			/*for (double idf1 : idfs) {
				System.out.print(idf1+" ");
			}*/
		}
	}

	 public static void main(String args[]) throws FileNotFoundException, IOException
	    {
	        DocParser dp = new DocParser();
	        dp.parseFiles("C:\\Users\\admin\\Desktop\\Crawler XML\\docsTestTfidf\\simpleDocs\\testDocs"); // give the location of source file
	        dp.tfIdfCalculator(); //calculates tfidf
	        dp.printDocVector();
	     }
	 
	 void printDocVector(){
		 DecimalFormat df2 = new DecimalFormat("###.##");
		 for (Object obj : tfidfDocsVector) {
			 System.out.println("\n\nTfisf vector:");
				double[] docV=(double[])obj;
				System.out.print("[");
				for (double s : docV) {
					System.out.printf(Double.valueOf(df2.format(s))+", ");
					//System.out.printf(s+" ");
				}
				System.out.print("]");
				System.out.println();
			}
	 }
	 
	 /*List getTfIdfVector() throws FileNotFoundException, IOException{
		 DocParser dp = new DocParser();
	        dp.parseFiles("C:\\Users\\admin\\Desktop\\Crawler XML\\docsTestTfidf"); // give the location of source file
	        dp.tfIdfCalculator(); //calculates tfidf
	       return tfidfDocsVector;
	 }*/
}
