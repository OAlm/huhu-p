package fi.metropolia.mediaworks.juju.util;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;


public class PropertyUtil {
	private static final Logger log = Logger.getLogger(PropertyUtil.class);
	
	public static Map<String, String[]> parseProperties(Properties p) {
		Map<String, String[]> m = new TreeMap<String, String[]>();
		
		for (Map.Entry<Object, Object> e : p.entrySet()) {
			String k = ((String)e.getKey()).trim();
			String v = ((String)e.getValue()).trim();
			String[] parts = v.trim().split("[ ]*,[ ]*");
			if (parts.length == 1 && parts[0].length() == 0) {
				log.debug("Empty key: " + k);
			} else {
				m.put(k, parts);	
			}
		}
		
		return m;
	}
	
	public static Properties loadProperties(File file) throws Exception {
		Properties p = new Properties();

		p.load(Tools.openFile(file));
		
		return p;
	}
	
	public static Properties loadProperties(URL url) {
		Properties props = new Properties();
		
		try {
			props.load(Tools.openStream(url.openStream()));
		} catch (Exception e) {
			log.error("Fail", e);
		}
		
		return props;
	}
	
	public static Properties loadProperties(String filename) {
		Properties props = new Properties();
		
		URL u = Loader.getResource(filename);
		if (u != null) {
			props.putAll(loadProperties(u));
		}
		
		File f = new File(filename);
		if (f.exists()) {
			try {
				props.load(Tools.openFile(f));
			} catch (Exception e) {
				log.error("Fail", e);
			}
		}
		
		return props;
	}
	
	public static void main(String[] args) {
		System.out.println(PropertyUtil.loadProperties("taikalaatikko.properties"));
	}
}
