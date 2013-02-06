package fi.metropolia.mediaworks.juju.extractor.person;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 *  Container class for text-based name extracting task
 * 
 *  @author alm
 */

public class NameInfoText {
	private static final Logger log = Logger.getLogger(NameInfoText.class);
	
	private String nodeText;
	private String nodeLemma;
	
	public NameInfoText(Node n) { 
		//log.info("solmu: " +n.getNodeName());
		
		try {
			
//			System.out.println("N null: "+n==null);
			
			
			
			NodeList nl = n.getChildNodes(); //3 kpl: T,M,S
			for(int i=0; i<nl.getLength();i++) {
				String nodename = nl.item(i).getNodeName();
				if(nodename.equals("T")) {
					this.nodeText = nl.item(i).getTextContent().trim();
				} else if(nodename.equals("L")) {
					this.nodeLemma = nl.item(i).getTextContent().trim();
				}
			}
			
		} catch(NullPointerException e) {
			log.error(e.toString());
			this.nodeText = ""; //TODO: onko jees?
			e.printStackTrace();
		}
	}
	
	public String getText() {
		return this.nodeText;
	}
	/**
	 * Lowercase
	 * @return
	 */
	public String getLC() {
		//return this.nodeText.toLowerCase();
		return this.nodeLemma;
	}

}