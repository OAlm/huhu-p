package fi.metropolia.mediaworks.juju.weighting;


abstract public class ItemWeighter<A> {
	abstract double process(Weighter<A> weighter, A object, double weight);
	
	/**
	 * @param weighter
	 */
	void beforeProcess(Weighter<A> weighter) {
		/**
		 * Extending class could implement this
		 */
	}
	
	/**
	 * @param weighter
	 */
	void afterProcess(Weighter<A> weighter) {
		/**
		 * Extending class could implement this
		 */
	}
}
