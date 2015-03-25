package Intelligence;

import java.util.Date;

import weka.classifiers.meta.FilteredClassifier;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ClassifyTest2 {

	/**
	 * @param args
	 */
	//public static void main(String[] args) {
	

			public int classifyPage(String data){

		Instances testRaw=null;
		int svmClass=0;
		//String data="test Data";

		try{
			// convert the directory into a dataset

			System.out.println("STart time="+new Date());

			//creating instances from data
			FastVector atts = new FastVector();
			FastVector classVal = new FastVector();
			classVal.addElement("irrelevant");
			classVal.addElement("relevant");
			atts.addElement(new Attribute("text", (FastVector) null));
			atts.addElement(new Attribute("@@class@@",classVal));
			
			testRaw = new Instances("TestInstances", atts, 0);
			
			System.out.println("testrAw"+testRaw.toString());
			
			 //double[] instanceValue1 = new double[testRaw.numAttributes()];
			double[] instanceValue1 = new double[2];
			 
			  instanceValue1[0] = testRaw.attribute(0).addStringValue(data);
		       instanceValue1[1] = 0;
		       
		       testRaw.add(new Instance(1.0, instanceValue1));
		      // testRaw.setClassIndex(testRaw.numAttributes() - 1);
		       testRaw.setClassIndex(1);
			

			System.out.println("1.."+new Date());
			//System.out.println("testrAw2="+testRaw.toString());
			//System.out.println("5.."+new Date());

			FilteredClassifier fc=(FilteredClassifier) weka.core.SerializationHelper.read("C:\\Users\\slmohan\\Desktop\\CrawlerData\\FilteredCls1.fc");

			svmClass=(int)fc.classifyInstance(testRaw.firstInstance());

			System.out.println("class="+svmClass+" ,"+testRaw.classAttribute().value(svmClass));
			System.out.println("End time="+new Date());
			
		}
		catch(Exception e){
			System.out.println("Exception occured hereee:\n");	
			e.printStackTrace();
		}
		return svmClass;
	}

}
