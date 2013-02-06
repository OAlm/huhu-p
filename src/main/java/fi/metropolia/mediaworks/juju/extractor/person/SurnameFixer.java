package fi.metropolia.mediaworks.juju.extractor.person;

import org.apache.log4j.Logger;

public class SurnameFixer {
	private static final Logger log = Logger.getLogger(SurnameFixer.class);
	//TODO: checkcheck, now only positive rules
	public static String doFix(String surname) {
		String before = surname;
		log.debug("FIXING SURNAME '"+surname+"'");
		if(surname.matches(".*(aa|ee|ii|uu|yy)n")) {
			log.debug("ILLATIVE? (-iin)");
			if(surname.matches(".*(aa|ee|ii|uu|yy)n")) { //LEONEEN --> LEONE  
				log.debug("--> I1");
//				System.out.println("--> I1");
				surname = surname.substring(0, surname.length()-2);
			} 
		} else if(surname.endsWith("n")) {
			log.debug("GENETIVE?");
			if(surname.matches(".*[mn]in")) { //walhströmin 
				log.debug("--> 2");
//				System.out.println("--> 2");
				surname = surname.substring(0, surname.length()-2);
			} else if(surname.matches(".*[aeiouöäå]sen")) { //malisen, antikaisen
				log.debug("--> 3");
//				System.out.println("--> 3");
				surname = surname.substring(0, surname.length()-3);
				surname += "nen";
			} else if(surname.matches(".*(sk[iy]|le|eone|io)n")) { //koncheskyn, vallen, leonen, tainion
				surname = surname.substring(0, surname.length()-1);
			} else if(surname.matches(".*emen")) { //niemen
				surname = surname.substring(0, surname.length()-2);
				surname += "i";
			}
//			} else if(surname.matches(".*([^adeikmnorstuy\\W\\d])[aeiouyöäå]n")) { //vallen --> valle, WHY non-word-character and digit?
//			} else if(surname.matches(".*([^adeikmnorstuy])[aeiouyöäå]n")) { //vallen --> valle
//				log.debug("--> 4");
//				System.out.println("--> 4");
//				surname = surname.substring(0, surname.length()-1);
//			}
		} else if(surname.matches(".*st[aä]")) {
			log.debug("ELATIVE? (-sta)");
			if(surname.matches(".*oist[aä]")) { //LEHIKOISTA --> LEHIKOINEN  
				log.debug("--> E1");
//				System.out.println("--> E1");
				surname = surname.substring(0, surname.length()-3);
				surname += "nen";
			} else if(surname.matches(".*[aä]")) { //mäkistä --> mäki,  
				log.debug("--> E2");
//				System.out.println("--> E2");
				surname = surname.substring(0, surname.length()-3);
			}
		} else if(surname.matches(".*[aä]")) {
			log.debug("PARTITIVE?");
			if(surname.matches(".*(r)i[aä]")) { //parkeria --> parker, 
				log.debug("--> 5");
//				System.out.println("--> 5");
				surname = surname.substring(0, surname.length()-2);
			} else if(surname.matches(".*ko[aä]")) { // kirilenkoa 
				log.debug("--> 5.1");
//				System.out.println("--> 5.1");
				surname = surname.substring(0, surname.length()-1);
			} else if(surname.matches(".*([nd|rin])[i][aä]")) { // nia / nea, esim. tsigorinia --> tsigorin, balandinia --> balandin
				log.debug("--> 6");
//				System.out.println("--> 6");
				surname = surname.substring(0, surname.length()-2);
			} else if(surname.matches(".*(bilia|lea)")) { // ginobiliä --> ginobili, vallea --> valle, EI: australia
				log.debug("--> 7");
//				System.out.println("--> 7");
				surname = surname.substring(0, surname.length()-1);
			} else if(surname.matches(".*mäkeä")) { // mäkeä --> mäki
				log.debug("--> 7.1");
//				System.out.println("--> 7.1");
				surname = surname.substring(0, surname.length()-2);
				surname += "i";
			} else if(surname.matches(".*[aiouyäö]([kn])[eio][aä]")) { // nia / nea, esim. tsigorinia.
				log.debug("--> 8");
//				System.out.println("--> 8");
				surname = surname.substring(0, surname.length()-1);
			}
		} else if(surname.endsWith("iin")) {
			log.debug("--> 1");
//			System.out.println("--> 1");
			surname = surname.substring(0, surname.length()-3);
		} else if(surname.endsWith("lle")) {
			log.debug("ALLATIVE? (--lle)");
			
			if(surname.matches(".*[ieouäö]lle")) {
				log.debug("ALLATIVE, VOCAL BEFORE");
				surname = surname.substring(0, surname.length()-3);
			} 
		}
		
//		else if(surname.matches(".*!(m)in")) {
//			log.debug("--> 9");
//			System.out.println("--> 9");
//			surname = surname.substring(0, surname.length()-1);
//		} 
		if(!before.equals(surname)) {
			log.info("SURNAME FIX, " + before+ " --> "+surname);
		}
		
		return surname;
	}
	
	public static void main(String[] args) {
		System.out.println(SurnameFixer.doFix("Zirinovskin"));
		
	}
	
}
