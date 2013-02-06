package fi.metropolia.mediaworks.juju.syntax.fi.omorfi;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import net.sf.hfst.NoTokenizationException;
import net.sf.hfst.UnweightedTransducer;
import net.sf.hfst.WeightedTransducer;

import org.apache.log4j.Logger;

public class Hyphenator {
	private static Logger log = Logger.getLogger(Hyphenator.class);
//	public interface transducer {
//		Collection<String> analyze(String str);
//	}
	
	public static void runTransducer(net.sf.hfst.Transducer t, String str)
	{
		System.out.println("Ready for input.");
		
		Scanner s = new Scanner(str);
		
		
		while (s.hasNext())
		{
			try {
				System.out.println(t.analyze(s.next()));
			} catch(NoTokenizationException e ) {
				log.error("no tokenization exception");
			}
		}
		
		s.close();
	}
	public static void main(String[] args) throws IOException {

		String str = "Halosen Niinistön Wahlströmin";
		
		FileInputStream transducerfile = null;
		String path = "C://data/workspace/omorfi3/src/transducer/hyphenation.dict.hfstol";
		try
		{ 

			transducerfile = new FileInputStream(path); 

		}
		catch (java.io.FileNotFoundException e)
		{
			System.err.println("File not found: couldn't read transducer file " + path + ".");
			System.exit(1);
		}
		
		System.out.println("Reading header...");
		net.sf.hfst.TransducerHeader h = new net.sf.hfst.TransducerHeader(transducerfile);
		DataInputStream charstream = new DataInputStream(transducerfile);
		System.out.println("Reading alphabet...");
		net.sf.hfst.TransducerAlphabet a = new net.sf.hfst.TransducerAlphabet(charstream, h.getSymbolCount());
		System.out.println("Reading transition and index tables...");
		if (h.isWeighted()) {
			WeightedTransducer transducer = new WeightedTransducer(transducerfile, h, a);
			runTransducer(transducer,str);
		} else {
			UnweightedTransducer transducer = new UnweightedTransducer(transducerfile, h, a);
			runTransducer(transducer,str);
		}
    }
}
