package fi.metropolia.mediaworks.juju.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;

import org.apache.tika.Tika;

public class EpubTest {

	public static void main(String[] args) throws Exception {
		Tika t = new Tika();
		File f = new File("/home/jonime/kirja3.epub");
		Reader r = t.parse(f);
		BufferedReader br = new BufferedReader(r);
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.trim().length() > 0) {
				System.out.println(line);
			}
		}
	}
}
