package fi.metropolia.mediaworks.juju.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PRFBean {
	@XmlElement public double precision = 0.0;
	@XmlElement public double recall = 0.0;
	@XmlElement public double fMeasure = 0.0;
	
	public PRFBean() {
		
	}
	
	public PRFBean(double precision, double recall, double fMeasure) {
		this.precision = precision;
		this.recall = recall;
		this.fMeasure = fMeasure;
	}
	
	@Override
	public String toString() {
		return String.format("P: %.4f R: %.4f F: %.4f", precision, recall, fMeasure);
	}
	
	public static PRFBean sum(List<PRFBean> list) {
		PRFBean r = new PRFBean();
		for (PRFBean p : list) {
			r.precision += p.precision;
			r.recall += p.recall;
			r.fMeasure += p.fMeasure;
		}
		return r;
	}
	
	public static PRFBean avg(List<PRFBean> list) {
		PRFBean r = sum(list);
		r.precision /= list.size();
		r.recall /= list.size();
		r.fMeasure /= list.size();
		return r;
	}
}
