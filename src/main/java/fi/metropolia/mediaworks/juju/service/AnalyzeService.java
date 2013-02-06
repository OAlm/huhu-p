package fi.metropolia.mediaworks.juju.service;

import java.util.HashSet;
import java.util.List;

import fi.metropolia.mediaworks.juju.model.PRFBean;
import fi.metropolia.mediaworks.juju.syntax.fi.omorfi.Lemmatizer;

public class AnalyzeService {
	private Lemmatizer lemmatizer = new Lemmatizer();
	
	public PRFBean analyze(List<String> actuals, List<String> expecteds) {
		int counter = 0;

		HashSet<String> founds = new HashSet<String>();
		for (String found : actuals) {
			founds.add(lemmatizer.lemmatize(found));
		}

		for (String added : expecteds) {
			String addedLemma = lemmatizer.lemmatize(added);
			if (founds.contains(addedLemma)) {
				counter++;
			}
		}

		PRFBean prf = new PRFBean();
		if (actuals.size() > 0) {
			prf.precision = (double) counter / (double) actuals.size();
		}
		if (expecteds.size() > 0) {
			prf.recall = (double) counter / (double) expecteds.size();
		}

		prf.fMeasure = calculateFMeasure(prf.precision, prf.recall);
		
		return prf;
	}
	
	public static double calculateFMeasure(double precision, double recall) {
		if (precision + recall > 0) {
			return 2.0 * (precision * recall) / (precision + recall);
		} else {
			return 0.0;
		}
	}
	
	public static void main(String[] args) {
		double p = 0.31;
		double r = 0.32;
		double f = calculateFMeasure(p, r);
		System.out.printf("P: %f R: %f F: %f\n", p, r, f);
	}
}
