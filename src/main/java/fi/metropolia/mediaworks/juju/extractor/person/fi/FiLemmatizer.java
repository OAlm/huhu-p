/*******************************************************************************
 * Copyright (c) 2013 Olli Alm / Metropolia www.metropolia.fi
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package fi.metropolia.mediaworks.juju.extractor.person.fi;

import org.apache.log4j.Logger;

public class FiLemmatizer {
	private static final Logger log = Logger.getLogger(FiLemmatizer.class);
	//TODO: checkcheck, now only positive rules, 
	//TODO: reorganize rules, induce known surnames in baseform --> use taivutustyypit 
	/*
	 * 
	 * normalizer for Finnish inflected forms of the surnames, rather ugly solution
	 * 
	 * sijamuodot:  1. nominatiivi (alm, almit), 2. akkusatiivi ([he näkivät] almin, almit) , 3. genetiivi (almin, almien), 
	 * 4. partitiivi (almia, almeja), 5. essiivi (almina, almeina), 6. translatiivi (almiksi, almeiksi), 7. inessiivi (almissa, almeissa), 
	 * 8. illatiivi (almiin, almeihin), 9. elatiivi (almista, almeista), 10. adessiivi (almilla, almeilla), 11. allatiivi (almille, almeille), 
	 * 12. ablatiivi (almilta, almeilta), 13. abessiivi (almitta, almeitta), 14. instruktiivi (almin, almein), 
	 * 15. [paljain jaloin] ja 16. komitatiivi (almeineen). + 17. eksessiivi
	 * --> huom. taivutusluokan käsite on toinen: tietty luokka käyttäytyy samalla tavalla
	 * 
	 * 
	 * 1. nom	-   , -t
	 * 2. akk	-in , -it
	 * 3. gen	-in , -ien
	 * 4. par	-ia , -eja
	 * 5. ess   -ina, -eina
	 * 6. tra	-ksi, -eiksi
	 * 7. ine	-ssa, -issa
	 * 8. ill	-iin, -eihin
	 * 9. ela	-sta, -eista
	 * 10. ade	-lla, -eilla
	 * 11. all	-lle, -eille
	 * 12. abl	-lta, -eilta
	 * 13. abe 	-tta, -eitta [EI ESIINNY]
	 * 14. ins	-min, -mein [EI ESIINNY]
	 * 15. kom  -eineen [EI ESIINNY]
	 */
	
	/**
	 * returns base form of the surname for Finnish
	 * @param s
	 * @return
	 */
	public static String apply(String s) { //TODO: monikko ei esiinny nimissä?
		log.debug("parsing '"+s+"'");
		
		String changed = null;
		
		if (s.matches(".*kin")) { // -kin
			log.debug("-kin");
			
			s = s.substring(0, s.length() - 3);
		}
		if (s.matches(".*lt[aä]")) { // 12. ABL
			log.debug("ABLATIVE");
			if(s.matches(".*ilt[aä]")) {
				changed = s.substring(0, s.length() - 4);
			} else {
				changed =  s.substring(0, s.length() - 3);	
			}
			
		} else if (s.matches(".*lle")) { // 11. ALL
			log.debug("ALLATIVE");
			if(s.matches(".*ille")) {
				changed = s.substring(0, s.length() - 4);
			} else if(s.matches(".*kselle")) {
				changed =  s.substring(0, s.length() - 6)+"s"; // Paciukselle --> Pacius
			} else if(s.matches(".*selle")) { // Räsäselle, Järviselle
				changed =  s.substring(0, s.length() - 5)+"nen";
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*lla")) { // 10. ADE
			log.debug("ADESSIVE");
			if(s.matches(".*ghella")) { // FALSE POSITIVES: garghella
				changed =  s;
			} else if(s.matches(".*illa")) {
				changed =  s.substring(0, s.length() - 4);
			} else if(s.matches(".*sella")) { // TT 38 luukkosella
				changed =  s.substring(0, s.length() - 5)+"nen";
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*st[aä]")) { // 9. ELA
			log.debug("ELATIVE");
			if(s.matches(".*rist[aä]")) {
				changed =  s.substring(0, s.length() - 3);
			} else if(s.matches(".*ist[aä]")) {
				changed = s.substring(0, s.length() - 4);
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
			
		} else if (s.matches(".*[shl(ks)](aa|ee|ii|oo)n")) { // 8 ILL
			log.debug("ILLATIVE");
			if(s.matches(".*ks(aa|ee|ii|oo)n")) { 
				changed =  s.substring(0, s.length() - 5)+"s";
			} else if(s.matches(".*l(aa|ee|ii|oo)n")) { 
				changed =  s.substring(0, s.length() - 2);
			} else if(s.matches(".*heen")) {
				changed =  s.substring(0, s.length() - 2);
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*ss[aä]")) { // 7. INE
			log.debug("INESSIVE");
			
			if(s.matches(".*ngiss[aä]")) { 
				changed =  s.substring(0, s.length() - 5)+"ki";
			} else if(s.matches(".*iss[aä]")) {
				changed =  s.substring(0, s.length() - 4);
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*ksi")) { // 6. TRA
			log.debug("TRANSLATIVE");
			
			if(s.matches(".*iksi")) {
				changed =  s.substring(0, s.length() - 4);
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*in[aä]")) { // 5. ESS
			log.debug("ESSIVE");
			if(s.matches(".*rnina")) { // false positives, ternina
				changed =  s;
			} else {
				changed =  s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*i[aä]")) { // 4. PAR
			log.debug("PARTITIVE");
			if (s.matches(".*([rv]ia)")) { // Harvia
				log.debug("FALSE POS");
				changed =  s;
			} else {
				changed =  s.substring(0, s.length() - 2);
			}
		} else if (s.matches(".*ien")) { //3. GEN PL [EI ESIINNY?]
			log.debug("GENETIVE PLURAL");
			changed =  s.substring(0, s.length() - 2);
		} else if (s.matches(".*n")) { //3. GEN SG, 2. AKK SG
			log.debug("GENETIVE, ACCUSATIVE");
			
			if (s.matches(".*(ir[eé]n|man|xon|din|oln|hen|iksen|son|hn|hren|ean|pton|ron|upin|illan|green|hagen|stein)")) { //false positives: henriksson, henriksen, john, ocean, frampton, cuaron, lupin, mcmillan
				log.debug("FALSE POS");
				changed =  s;
			} else if (s.matches(".*äen")) { // mäen
				log.debug("KOTUS7 - AV5");
				changed =  s.substring(0, s.length() - 2)+"ki";
			} else if (s.matches(".*(con|oon)")) { // lontoon
				log.debug("KOTUS17");
				changed =  s.substring(0, s.length() - 1);
			} else if (s.matches(".*([ll|mm]on)")) { // aallon
				log.debug("KOTUS1, AV1");
				if(s.matches(".*(mmon)")) { //sammon --> sampo
					changed =  s.substring(0, s.length() - 3)+"po";
				} else { //aallon --> aalto
					changed =  s.substring(0, s.length() - 3)+"to";
				}
			} else if (s.matches(".*(lon|panin|non)")) { // salon, tapanin (ei brygmanin
				log.debug("KOTUS1");
				changed =  s.substring(0, s.length() - 1);
			} else if (s.matches(".*nen")) { // manninen
				log.debug("nen");
				changed =  s;
			} else if (s.matches(".*i[ao]n")) {
				log.debug("i[ao]n");
				changed =  s.substring(0, s.length() - 1);
			} else if (s.matches(".*((bb)|(er)|(nd)|(nn)|(rg)|(rk)|(rop)|(ström)|(tz)|(tt)|h|l|m|n)in")) { // Rennerin --> Renner (vrt alla *in --> *i), Arteronin --> Arteron, Scottin, Pavlovitshin
				log.debug("KOTUS5");
				changed =  s.substring(0, s.length() - 2);
			} else if(s.matches(".*[aëiäö]n")) {
				log.debug("KOTUS5/2 [aëiäö]n");
				changed =  s.substring(0, s.length() - 1);
			}  else if (s.matches(".*[ge]en")) { // coolidgen, purjeen
				log.debug("KOTUS48"); 
				if(s.matches(".*een")) {
					changed =  s.substring(0, s.length() - 2);
				} else {
					changed =  s.substring(0, s.length() - 1);
				}
			} else if (s.matches(".*[kl]aisen")) { // TT: 38
				log.debug("KOTUS38");
				changed =  s.substring(0, s.length() - 5) + "ainen";
			} else if (s.matches(".*ksen")) { //serlachiuksen
				log.debug("KOTUS39");
				changed =  s.substring(0, s.length() - 4) + "s";
			} else if(s.matches(".*[aeiouö]sen")) { // järvisen, ylösen, kelasen
				log.debug("sen");
				changed =  s.substring(0, s.length() - 3) + "nen";
			} else if (s.matches(".*hden")) { //lehden --> lehti
				log.debug("hden");
				changed =  s.substring(0, s.length()-3)+"ti";
			} else if(s.matches(".*ven")) { //Kiven --> Kivi
				log.debug("kiven");
				changed =  s.substring(0, s.length()-2)+"i";
			} else if(s.matches(".*(lan|eyn|donan)")) { //Vesalan, Weasleyn, Maradonan
				log.debug("-n");
				changed =  s.substring(0, s.length()-1);
			} else {
				log.debug("--> default");
				changed =  s.substring(0, s.length() - 2);
			}
		} else {
			log.debug("--> no match");
		}
		
		/* else if (s.matches(".*t")) { // 2. AKK PL, 1. NOM PL --> ei esiinny?
			return s.substring(0, s.length() - 2);
		} */
		if(changed != null) {
			log.debug("--> transformed to: '"+changed+"'");
			return changed;
		} else {
			return s;
		}
	}

	
}
