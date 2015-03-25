package Fetching;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class FilemergeUtil {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//File[] files = Utils.filterFiles("C:\\Users\\admin\\Desktop\\CraIRrch\\crawledXMLs\\", ".*\\.txt");
		File[] files = new File("C:\\Users\\admin\\Desktop\\CraI2\\ClassifiedDocs\\2\\XMLs\\").listFiles();
		
		List<Path> inputs=new ArrayList<Path>();
		for(File file : files){
		  if(file.isFile()){
		    System.out.println(file.getAbsolutePath());
		    inputs.add(Paths.get(file.getAbsolutePath()));
		  }
		}
		
		
		 /*List<Path> inputs = Arrays.asList(
		            Paths.get("file1.txt"),
		            Paths.get("file2.txt")
		    );*/
		
		

		    // Output file
		    Path output = Paths.get("C:\\Users\\admin\\Desktop\\CraI2\\ClassifiedDocs\\2\\WebsiteDump2.xml");

		    // Charset for read and write
		    Charset charset = StandardCharsets.UTF_8;

		    //Sanitizing the xml file
		    String xml10pattern = "[^"
                    + "\u0009\r\n"
                    + "\u0020-\uD7FF"
                    + "\uE000-\uFFFD"
                    + "\ud800\udc00-\udbff\udfff"
                    + "]";
		    
		    
		    // Join files (lines)
		    for (Path path : inputs) {
		        List<String> lines = Files.readAllLines(path, charset);
		        List<String> lines2 =new ArrayList<String>();
		        
		        for (Iterator<String> it = lines.iterator(); it.hasNext() ;)
				 {
						//String s=it.next().replaceAll(xml10pattern, "");
						String s=it.next();
						it.remove();
						lines2.add(s);
					}
		        /*for (String s : lines) {
					s=s.replaceAll(xml10pattern, "");
				}*/
		        
		        Files.write(output, lines2, charset, StandardOpenOption.CREATE,
		                StandardOpenOption.APPEND);
		    }
	}

}
