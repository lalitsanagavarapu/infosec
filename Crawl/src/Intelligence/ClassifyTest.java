package Intelligence;

import java.io.File;
import java.util.Date;

import weka.classifiers.meta.FilteredClassifier;

import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

public class ClassifyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	//	public int classifyPage(){

		//Instances dataRaw=null;
		Instances testRaw=null;
		int svmClass=0;

		try{
			// convert the directory into a dataset

			System.out.println("STart time="+new Date());
			
			TextDirectoryLoader loader2 = new TextDirectoryLoader();
			loader2.setDirectory(new File("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTestDocs\\docs2"));
			testRaw=loader2.getDataSet();

			System.out.println("0..");

			/*BufferedReader reader = new BufferedReader(
					new FileReader("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\rawTrainData.arff"));
			ArffReader arff = new ArffReader(reader);
			dataRaw = arff.getData();	    
			reader.close();*/
			// setting class attribute
		//	if (dataRaw.classIndex() == -1)
			//	dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

			System.out.println("-1.."+new Date());

		/*	SnowballStemmer stemmer = new SnowballStemmer();
			stemmer.setStemmer("english");

			StringToWordVector filter = new StringToWordVector();
			filter.setLowerCaseTokens(true);
			filter.setUseStoplist(true);
			filter.setStemmer(stemmer);
			filter.setTFTransform(true);
			filter.setIDFTransform(true);
			filter.setInputFormat(dataRaw);*/

			System.out.println("1.."+new Date());


			//Filter.useFilter(dataRaw, filter);
			//Instances testDataFiltered = Filter.useFilter(testRaw, filter);
			
			//System.out.println("\n\nFiltered train data:\n\n"+abc.toString().substring(0, 55000));
			//System.out.println("\n\nFiltered test data:\n\n" + testDataFiltered);


			/*reader = new BufferedReader(
					new FileReader("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\filterTrainData.arff"));

			ArffReader arff2 = new ArffReader(reader);
			Instances trainDataFiltered = arff2.getData();	    
			reader.close();
			if (trainDataFiltered.classIndex() == -1)
				//trainDataFiltered.setClassIndex(trainDataFiltered.numAttributes() - 1);
				trainDataFiltered.setClassIndex(0);*/


			
			//Classification
			//Classifier deserialization
			//Classifier svm=(Classifier) weka.core.SerializationHelper.read("C:\\Users\\admin\\Desktop\\CraI2\\SVMClassifier6.model");
			//Classifier nb=(Classifier) weka.core.SerializationHelper.read("C:\\Users\\admin\\Desktop\\CraI2\\NBClassifier6.model");
			System.out.println("3.."+new Date());
			
			//Using filteredClassifier
			// FilteredClassifier fc = new FilteredClassifier();
			 //   fc.setFilter(filter);
			 //   fc.setClassifier(svm);
			    
			    System.out.println("testrAw"+testRaw.toString());
			    
			 //   fc.buildClassifier(dataRaw);
			    System.out.println("5.."+new Date());
			    
			    //weka.core.SerializationHelper.write("C:\\Users\\admin\\Desktop\\CraI2\\FilteredCls1.fc",fc);
			    
			    FilteredClassifier fc=(FilteredClassifier) weka.core.SerializationHelper.read("C:\\Users\\admin\\Desktop\\CraI2\\FilteredCls1.fc");
			    
			    svmClass=(int)fc.classifyInstance(testRaw.firstInstance());
			    
			    System.out.println("class="+svmClass+" ,"+testRaw.classAttribute().value(svmClass));
			    
			    System.out.println("End time="+new Date());

			/*Evaluation eval = new Evaluation(trainDataFiltered);
			eval.evaluateModel(svm, testDataFiltered);

			System.out.println(eval.toSummaryString("\n SVM Results\n======\n", false));
			
			svmClass=(int)svm.classifyInstance(testDataFiltered.firstInstance());
			
			System.out.println("out 34="+svmClass);			

			Evaluation eval2 = new Evaluation(trainDataFiltered);
			eval2.evaluateModel(nb, testDataFiltered);
			System.out.println(eval2.toSummaryString("\n NB Results\n======\n", false));*/
			    
			   
			
			//System.out.println("\n\nClassifier model:\n\n" + svm);
			
			

		}
		catch(Exception e){
			System.out.println("Exception occured hereee:\n");	
			e.printStackTrace();
		}
		//return svmClass;
	}

}
