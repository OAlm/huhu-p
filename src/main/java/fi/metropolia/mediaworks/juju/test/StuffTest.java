package fi.metropolia.mediaworks.juju.test;

import java.util.List;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.Lists;

public class StuffTest {
	private static double getMean(List<Double> numbars) {
		DescriptiveStatistics test = new DescriptiveStatistics();
		for (double i : numbars) {
			test.addValue(i);
		}
		return test.getMean();
	}
	
	private static double getAverage(List<Double> numbars) {
		int sum = 0;
		for (double i : numbars) {
			sum += i;
		}
		return (double)sum / (double)numbars.size();
	}
	
	private static boolean test() {
		List<Double> numbars = Lists.newArrayList();
		Random r = new Random();
		int numbrs = r.nextInt(1000)+1;
		for (int i = 0; i < numbrs; i++) {
			numbars.add(r.nextDouble() * 1000);
		}
		
		double mean = getMean(numbars);
		double average = getAverage(numbars);
		
		double diff = mean - average;
		
		System.out.printf("%f = %f (%s)\n", mean, average, diff);
		return Math.abs(diff) < 1;
	}
	
	public static void main(String[] args) {
		while (true) {
			if (!test()) {
				break;
			}
		}
		System.out.println("FAIL!");
	}
}
