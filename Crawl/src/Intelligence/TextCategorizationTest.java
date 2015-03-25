package Intelligence;

import weka.core.*;
import weka.core.converters.*;
import weka.core.stemmers.SnowballStemmer;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.*;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;

import java.io.*;


public class TextCategorizationTest {

  /**
   * Expects the first parameter to point to the directory with the text files.
   * In that directory, each sub-directory represents a class and the text
   * files in these sub-directories will be labeled as such.
   *
   * @param args        the commandline arguments
   * @throws Exception  if something goes wrong
   */
  public static void main(String[] args) throws Exception {
    // convert the directory into a dataset
    TextDirectoryLoader loader = new TextDirectoryLoader();
   /* loader.setDirectory(new File("C:\\Users\\admin\\Desktop\\Crawler XML\\docsTestTfidf\\simpleDocs\\docs"));
    TextDirectoryLoader loader2 = new TextDirectoryLoader();
    loader2.setDirectory(new File("C:\\Users\\admin\\Desktop\\Crawler XML\\docsTestTfidf\\simpleDocs\\testDocs"));*/
    
    loader.setDirectory(new File("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000"));
    TextDirectoryLoader loader2 = new TextDirectoryLoader();
    loader2.setDirectory(new File("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTestDocs\\docs"));
    
    System.out.println("0..");
    
    Instances dataRaw = loader.getDataSet();
    Instances testRaw=loader2.getDataSet();
    
    //Saving training data to arff file
    /*ArffSaver saver = new ArffSaver();
  	 saver.setInstances(dataRaw);
  	 saver.setFile(new File("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\rawTrainData.arff"));
  	 saver.writeBatch();*/
    /*BufferedWriter writer = null;
  	writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\rawTrainData.arff"), "utf-8"));
				
		System.out.println("Writing to file:");
		writer.write(dataRaw.toString());
		writer.close();*/
    
    
   // System.out.println("\n\nImported train data:\n\n" + dataRaw);  
   // System.out.println("\n\nImported test data:\n\n" + testRaw);

    // apply the StringToWordVector
    // (see the source code of setOptions(String[]) method of the filter
    // if you want to know which command-line option corresponds to which
    // bean property)
    SnowballStemmer stemmer = new SnowballStemmer();
    stemmer.setStemmer("english");
    
    StringToWordVector filter = new StringToWordVector();
    filter.setLowerCaseTokens(true);
    filter.setUseStoplist(true);
    filter.setStemmer(stemmer);
    filter.setTFTransform(true);
    filter.setIDFTransform(true);

    
    filter.setInputFormat(dataRaw);
    
    //weka.core.SerializationHelper.write("C:\\Users\\admin\\Desktop\\CraI2\\trainingFilter.filter",filter);
    
    System.out.println("1..");
    
    
    
    
    //Instances trainDataFiltered = Filter.useFilter(dataRaw, filter);
   // Instances testDataFiltered = Filter.useFilter(testRaw, filter);
    
    System.out.println("2..");
    
   // BufferedWriter writer = null;

   	try {
   		/*writer = new BufferedWriter(new OutputStreamWriter(
   				new FileOutputStream("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\filterTrainData.arff"), "utf-8"));
   				
   		System.out.println("Writing to file:");
   		writer.write(trainDataFiltered.toString());
   		writer.close();*/
   		
   		//or
   		/*ArffSaver saver2 = new ArffSaver();
   	 saver2.setInstances(trainDataFiltered);
   	 saver2.setFile(new File("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\filterTrainData.arff"));
   	 saver2.writeBatch();
   	 
   	ArffSaver saver3 = new ArffSaver();
  	 saver3.setInstances(testDataFiltered);
  	 saver3.setFile(new File("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\filterTestData.arff"));
  	 saver3.writeBatch();*/
   		
   		/*writer = new BufferedWriter(new OutputStreamWriter(
   				new FileOutputStream("C:\\Users\\admin\\Desktop\\CraI2\\WebsiteTrainDocs1000,5000\\filterTestData.arff"), "utf-8"));
   		writer.write(testDataFiltered.toString());
   		writer.close();*/
   	}
   	catch(Exception e){
   		
   	}
    
    
  //  System.out.println("\n\nFiltered train data:\n\n" + trainDataFiltered);
  //  System.out.println("\n\nFiltered test data:\n\n" + testDataFiltered);

    weka.classifiers.functions.LibSVM svm=new LibSVM();
    //svm.buildClassifier(trainDataFiltered);
    
    
    //new...
    FilteredClassifier fc = new FilteredClassifier();
    fc.setFilter(filter);
    fc.setClassifier(svm);
    
    fc.buildClassifier(dataRaw);
    
    double pred = fc.classifyInstance(testRaw.instance(0));
    System.out.print("ID: " + testRaw.instance(0).value(0));
    System.out.print(", actual: " + testRaw.classAttribute().value((int) testRaw.instance(0).classValue()));
    System.out.println(", predicted: " + testRaw.classAttribute().value((int) pred));
    
    //weka.core.SerializationHelper.write("C:\\Users\\admin\\Desktop\\CraI2\\FilteredCls1.fc",fc);
    
    /*weka.classifiers.bayes.NaiveBayes nb=new NaiveBayes();
    nb.buildClassifier(trainDataFiltered);*/
   //J48 svm=new J48();
    
   // weka.core.SerializationHelper.write("C:\\Users\\admin\\Desktop\\CraI2\\SVMClassifier6.model",svm);
  //  weka.core.SerializationHelper.write("C:\\Users\\admin\\Desktop\\CraI2\\NBClassifier6.model",nb);
    
     //Classifier deserialization
    //Classifier svm=(Classifier) weka.core.SerializationHelper.read("C:\\Users\\admin\\Desktop\\CraI2\\NBClassifier4.model");
    System.out.println("3..");
    
    //Evaluation eval = new Evaluation(trainDataFiltered);
   // eval.evaluateModel(svm, testDataFiltered);
    
   // System.out.println(eval.toSummaryString("\n SVM Results\n======\n", false));
    
    /*Evaluation eval2 = new Evaluation(trainDataFiltered);
    eval2.evaluateModel(nb, testDataFiltered);
    System.out.println(eval2.toSummaryString("\n NB Results\n======\n", false));
    
    */
   /* for (int j = 0; j < testDataFiltered.numInstances(); j++) {

    	double score=svm.classifyInstance(testDataFiltered.instance(j));
    	System.out.println("class="+score);
    	System.out.println(testDataFiltered.attribute("@@class@@").value((int)score));
    }*/

   // System.out.println("\n\nClassifier model:\n\n" + svm);
    
     //svm.classifyInstance();
    
    
  }
}
