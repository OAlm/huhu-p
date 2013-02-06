/**
 * Helper class for NameExtractor
 * 
 * @author alm
 *  
 */

package fi.metropolia.mediaworks.juju.extractor.person;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NameInfo {
	private static final Logger log = Logger.getLogger(NameInfo.class);
	
	private String nodeText;
	private String nodeLemma;
	private String nodeMorpho;
	private String nodeSyntax;
	private boolean upperType;
	
	public NameInfo(Node n, boolean isUpper) {
		this.upperType = isUpper; 
		this.setNodeInfo(n, isUpper);
	}
	
	private void setNodeInfo(Node n, boolean isUpper) {
		
		NodeList nl = n.getChildNodes(); //3 kpl: T,M,S
		
		
		try {
			
			this.nodeText = nl.item(0).getTextContent().trim();
		} catch(NullPointerException e) {
			e.printStackTrace();
			log.debug(nl.item(0)==null);
			log.debug(nl.item(0).getTextContent()==null);
			log.debug(nl.item(0).getTextContent().trim()==null);
		}
		
		if(isUpper) {
			this.nodeLemma = nl.item(1).getTextContent().trim();
			this.nodeMorpho = nl.item(2).getTextContent().replaceFirst("\\s?<\\?>\\s?","").trim(); //joskus mukana haamu <?>
			this.nodeSyntax = nl.item(3).getTextContent().trim();
		
		}
	}
	
	public String getText() {
		return this.nodeText;
	}

	public String getLemma() {
		if(upperType) {
			return this.nodeLemma;
		}
		return null;
	}
	
	public String getMorpho() {
		if(upperType) {
			return this.nodeMorpho;
		}
		return null;
	}
	
	public String getSyntax() {	
		if(upperType) {
			return this.nodeSyntax;		
		}
		return null;
	}
	
}

