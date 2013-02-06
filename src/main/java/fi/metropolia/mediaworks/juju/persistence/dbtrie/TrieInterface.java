package fi.metropolia.mediaworks.juju.persistence.dbtrie;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Trierajapinta
 * @author alm
 *
 */
public interface TrieInterface {

	public boolean contains(String label);  // are there matching keys for parameter? [EXACT MATCH] 
	public boolean containsPrefix(String label); //are there matching keys for the prefix? [PREFIX MATCH]
	public List<String> matchByPrefix(String label); //return matching keys
	
	public Set<String> getIds(String label); //return the ids (values) for given label [EXACT MATCH]
	public Collection<String> getLabels(String id);
	public String getLabel(String id);

	public int size();
	
}
