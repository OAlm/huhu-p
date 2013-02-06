/**
 * Konfiguroidaan nimi / ei-nimi -luettelot
 *  (NameExtractorissa name.list, notAName.list)
 */

package fi.metropolia.mediaworks.juju.extractor.person;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class NameConfig {
	private static final Logger log = Logger.getLogger(NameConfig.class);

	private HashSet<String> names;
	private HashSet<String> noNames;

	public NameConfig() {

		BufferedReader in;
		String temp;

		names = new HashSet<String>();
		try {
			in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/name/name.list")));

			while ((temp = in.readLine()) != null) {
				names.add(temp);
			}
		} catch (Exception e) {
			System.out.println("Error reading name file: " + e);
			System.exit(0);
		}
		log.debug("Firstnames loaded, size: " + names.size());

		noNames = new HashSet<String>();
		try {
			in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/name/notAName.list")));

			while ((temp = in.readLine()) != null) {
				noNames.add(temp);
			}
		} catch (Exception e) {
			System.out.println("Error reading name file: " + e);
			System.exit(0);
		}
	}

	public HashSet<String> getNamelist() {
		return names;
	}

	public HashSet<String> getNoNamelist() {
		return noNames;
	}
}
