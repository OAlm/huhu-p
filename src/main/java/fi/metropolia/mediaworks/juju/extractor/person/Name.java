package fi.metropolia.mediaworks.juju.extractor.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import fi.metropolia.mediaworks.juju.util.WordLocationContainer;
/**
 * Each identified name is saved to a Name-object.
 * Name-instances are associated in TreeMap-structure
 * of NameIndex.
 *
 */ 
	public class Name implements Comparable<Name>, Serializable {
				
/**
	 * 
	 */
	private static final long serialVersionUID = 6122906310812579820L;
/**
 * Name can have multiple forms of current surname, for example Achté and
 * Ackté. Each surname is stored in this linked list.
 * Primary surname is the shortest form of surname which 
 * is added last.
 */
		private LinkedList<String> surNames = new LinkedList<String>();
/**
 * Name can have multiple first names, for example Martti Olavi Ahtisaari
 * Each first name is stored in this linked list.
 */
		private LinkedList<String> firstNames = new LinkedList<String>();

		private int nameID; //nimen id-numero aineistossa
		private int nameFrequency; //yhden dokumentin sisällä
		
		//sijaintitiedot, nimien sijainti tekstissä
		private ArrayList<WordLocationContainer> location;
		
		//regex pienellä kirjoitettaville sukunimien aluille:
		//FIXME: lisää nämä sukunimiparsijaan! (16.10.06)
//		private String lowercaseSurnames = "af |von ";
/**
 * New name-object is created if NameIndex doesn't have name
 * with current ID. So, constructor is used to add a new 
 * instance of name at the first time. Later, when ID exist,
 * addName-method is used to add name to a current ID.
 * 
 * @param id identification number of the name
 * @param name the actual name 
 * @see #addName(String,boolean)
 */
		public Name(Vector<String> firstnames, String surname, int counter) {
			this.addName(firstnames, surname, true);
			this.location = new ArrayList <WordLocationContainer>();

			this.nameFrequency = 1;
			this.nameID = counter;
		}

		public Name(Vector<String> firstnames, String surname) {
			this.addName(firstnames, surname, true);
			this.location = new ArrayList <WordLocationContainer>();

			this.nameFrequency = 1;
		}
		
		public void setId(int id) {
			this.nameID = id;
		}

/**
 * When Name-instance with current ID exist, this 
 * method is used to update possible new form of
 * the name. (A. Aalto --> Aino Aalto)
 *
 * @param name String-rep of name
 * @param isFirst constructor uses true, when adding name for the first time, 
 *  later false is used. 
 */		
		public void addName(Vector <String> firstnamesNeu, String surname, boolean isFirst) {
					
			if(isFirst) {

				for(String s:firstnamesNeu) {
					firstNames.add(s);
				} 
				
				surNames.add(SurnameFixer.doFix(surname));

			} else {
				
				this.nameFrequency++;
				
				String temp = "", fn;

				int nameI = 0;
				
				for(String s: firstnamesNeu) {

					temp = s;
					
					//jos lisättävä sana, temp, on pidempi 
                    //tee catch-osuus (=lisää sana suoraan)
					try {
				    	fn = firstNames.get(nameI);
						if(!temp.equals(fn)	) {
//							System.out.println("sanat '" +fn+"' ja '" +temp+ "' erit.");
							if(fn.contains(".")) {
//								System.out.println("'"+fn +
//									 "' sis. pisteen, korvataan sanalla '" +
//                                     temp + "'.");
								firstNames.remove(nameI);
								firstNames.add(nameI, temp);
							}
						}
					} catch(IndexOutOfBoundsException e) {
//						System.out.println("(CATCH)-----> TOkenisssa enemmän tavaraa kuin ETUnimessä.");
						firstNames.add(nameI, temp);		
					}	
					nameI++;
				}

				temp = surname;

				// verrataan sukunimet,
                // ensisijainen sukunimi on lyhin muoto!
				if(!temp.equals(surNames.getFirst())) {
					if(temp.length() < surNames.getFirst().length()) {
						surNames.addFirst(temp);
					} else {
						surNames.add(temp);
					}
				}
//				System.out.println("Nimessä jälkeen:\n" + this.toString());
			}
			
		}		

		
		/**
		 * Lisätään yksittäisen nimen esiintymä frekvenssiin
		 * (lisätty 25.1.)
		 */
		public void countIndividual() {
			this.nameFrequency++;
		}
		
		public int getFrequency() {
			return this.nameFrequency;
		}
		public int getID() {
			return this.nameID;
		}
		/*
		 *when individual surname occurence is found from document, add it  
		 */
		public void addSurname(String surname) {
			surname = SurnameFixer.doFix(surname);
			if(!this.surNames.contains(surname)) {
				this.surNames.add(surname);
			}
		}
/**
 * Returns surname as a String
 * 
 * @return String representation of primary surname
 */
		public String getSur() {
			return this.upperCase(surNames.getFirst());
		}
		
		public String getSurLowerCase() {
			return surNames.getFirst();
		}
		
/**
 * DEBUG: 
 * @return
 */		
		public String getSurs() {
			HashSet<String> hs = new HashSet<String>();
			for(String s: surNames) {
				hs.add(this.upperCase(s));
			}
			return hs.toString();
		}

/**
 * Returns first name as a String
 * 
 * @return String representation of first name
 */
		public String getFirst() {
			return this.upperCase(firstNames.getFirst());
		}
		
		public String getFirstLowerCase() {
			return firstNames.getFirst();
		}
/**
 * Palauta kaikki etunimet
 */
		public String getFirsts() {
			String result = "";
			for(String s:firstNames) {
				result += this.upperCase(s)+" ";
			}
			return result.trim();
		}
		
		private String upperCase(String text) {
			if (text == null || text.length() == 0) {
				return text;
			}
			
			text = text.replaceAll("#", ""); //lisätty 14.6.06
						
			text = text.substring(0, 1).toUpperCase()+
						text.substring(1, text.length());

//TODO: useita tavuviivoja (=Gallen-Kallela-Korhonen)!
//Voipio-juvas muotoon Voipio-Juvas
			int hyphenIndex;
			int startIndex = 0;
			while((hyphenIndex = text.indexOf('-', startIndex)) != -1) {
			
				if(hyphenIndex != -1 &&
						text.length() > hyphenIndex+2) {
//					log.debug("HYPH: "+text);
					text = text.substring(0,hyphenIndex+1)+
					text.substring(hyphenIndex+1, hyphenIndex+2).toUpperCase()+
						text.substring(hyphenIndex+2, text.length());
					startIndex = hyphenIndex+1;
				} else {
					break;
				}
			
			}
			return text;
		}
		/**
		 * 9.9.2011: changed similarity to sur / firstname comparisons
		 * 
		 * --> TODO, this is quite heavy operation, should there
		 * be trie-structure for comparisoon
		 * 
		 * --> not really for ordering, -1 if not match, 0 if equal
		 * 
		 */
		@Override
		public int compareTo(Name another) throws ClassCastException {
		    boolean firstMatch = false;
		    boolean surMatch = false;
		    
// if any of the surnames matches
		    outer:
		    for(String sur1: another.surNames) {
		    	for(String sur2: this.surNames) {
		    		if(sur1.equals(sur2)) {
		    			surMatch = true;
		    			break outer;
		    		}
		    	}
		    }
		 
// if any of the firstnames matches
		    outer:
	    	for(String first1: another.firstNames) {
	    		for(String first2: this.firstNames) {
	    			if(first1.equals(first2)) {
	    				firstMatch = true;
	    				break outer;
	    			}
	    		}
	    	}
		    
		    if(firstMatch&&surMatch) {
		    	return 0; //EQUALS
 		    } else {
		    	return this.toString().compareTo(another.toString());
		    }    
		}
		
		
		@Override
		public String toString() { //FIXME 
			String result = ""; 
			Iterator<String> i = this.firstNames.iterator();
			while(i.hasNext()) {
				result += this.upperCase(i.next())+ " ";
			}

			i = this.surNames.iterator();          
			result += this.upperCase(i.next());
	
			return result;
		}

		public void addLocation(int start, int end) {
			this.location.add(new WordLocationContainer(start, end));		
		}

		public ArrayList<WordLocationContainer> getLocations() {
			return this.location;
		}
	}