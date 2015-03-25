package Preprocessing;

import weka.core.stemmers.SnowballStemmer;

public class Preprocess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 SnowballStemmer stemmer = new SnowballStemmer();
	        stemmer.setStemmer("english");
	        System.out.println(stemmer.stem("computerized"));

	}
}
