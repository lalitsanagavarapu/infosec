package Intelligence;

import java.io.BufferedReader;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.SparseInstance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.SnowballStemmer;


public class SVMClassify {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//BufferedReader reader;
		try {
			DataSource source = new DataSource("/some/where/data.arff");
			 Instances data = source.getDataSet();
			 // setting class attribute if the data format does not provide this information
			 // For example, the XRFF format saves the class attribute information as well
			 if (data.classIndex() == -1)
			   data.setClassIndex(data.numAttributes() - 1);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
