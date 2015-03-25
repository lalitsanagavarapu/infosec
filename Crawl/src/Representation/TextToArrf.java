package Representation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

public class TextToArrf {

	/**
	 * @param args
	 * @throws IOException 
	 */
	/*public static void main(String[] args) throws IOException {
		TextDirectoryLoader td=new TextDirectoryLoader();
		td.setDirectory(new File("C:\\Users\\admin\\Desktop\\Crawler XML\\docsTestTfidf"));
		TextDirectoryLoader.main(new String[]{"C:\\Users\\admin\\Desktop\\Crawler XML\\docsTestTfidf"});
	}*/
	
	public Instances createDataset(String directoryPath) throws Exception {
		 
	    FastVector atts = new FastVector(2);
	    atts.addElement(new Attribute("filename", (FastVector) null));
	    atts.addElement(new Attribute("contents", (FastVector) null));
	    Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);
	 
	    File dir = new File(directoryPath);
	    String[] files = dir.list();
	    for (int i = 0; i < files.length; i++) {
	      if (files[i].endsWith(".txt")) {
	    try {
	      double[] newInst = new double[2];
	      newInst[0] = (double)data.attribute(0).addStringValue(files[i]);
	      File txt = new File(directoryPath + File.separator + files[i]);
	      InputStreamReader is;
	      is = new InputStreamReader(new FileInputStream(txt));
	      StringBuffer txtStr = new StringBuffer();
	      int c;
	      while ((c = is.read()) != -1) {
	        txtStr.append((char)c);
	      }
	      newInst[1] = (double)data.attribute(1).addStringValue(txtStr.toString());
	      data.add(new Instance(1.0, newInst));
	    } catch (Exception e) {
	      //System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
	    }
	      }
	    }
	    return data;
	  }
	 
	  public static void main(String[] args) {
	 
		  for (String a : args) {
			  System.out.println("arg="+a);
		}
		  System.out.println("Here 1");
		  
	    if (args.length == 1) {
	    	TextToArrf tdta = new TextToArrf();
	      try {
	    Instances dataset = tdta.createDataset(args[0]);
	    System.out.println(dataset);
	      } catch (Exception e) {
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	      }
	    } else {
	      System.out.println("Usage: java TextDirectoryToArff <directory name>");
	    }
	  }

}
