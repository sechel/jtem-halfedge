package de.jtem.halfedge;




/**
 * The abstract base class for any vertex, edge and face class.
 * 
 * @author Stefan Sechelmann
 * @author Boris Springborn
 */
abstract public class Node <
	V extends Vertex<V, E, F>,
	E extends Edge<V, E, F>,
	F extends Face<V, E, F>	   
> {

	int 
		index = -1;
	HalfEdgeDataStructure<V, E, F> 
		hds = null;
	

	public final int getIndex() {
		return index;
	}
	
	
	final void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		Class<?> c = getClass();
		Class<?> ec = c.getEnclosingClass();
		return (ec == null ? "" : (ec.getSimpleName() + "."))
				+ c.getSimpleName()
				+ "[" 
				+ (hds == null ? "removed" : this.getIndex())
				+ "]";
	}

	final void checkHalfEdgeDataStructure() throws RuntimeException {
		if (hds == null) {
			throw new RuntimeException(this + " does not belong to any half-edge data structure.");
		}
	}
	
	final void checkHalfEdgeDataStructure(Node<?,?,?> n) throws RuntimeException {
		checkHalfEdgeDataStructure();
		if (n != null && this.hds != n.hds) {
			throw new RuntimeException(this + " and " + n + " do not belong to the same half-edge data structure.");
		}
	}
	
	/**
	 * Check whether this node still belongs to a half-edge data structure.
	 * @return <code>true</code> if this node belongs to a half-edge data structure, <code>false</code> if it has been removed.
	 */
	final public boolean isValid(){
		return hds != null;
	}
	
	final void setHalfEdgeDataStructure(HalfEdgeDataStructure<V, E, F> halfEdgeDataStructure) {
		this.hds = halfEdgeDataStructure;
	}

	/**
	 * Return the half-edge data structure to which this node belongs, or <code>null</code> if it has been removed.
	 * @return the half-edge data structure, or <code>null</code>.
	 */
	final public HalfEdgeDataStructure<V, E, F> getHalfEdgeDataStructure() {
		return hds;
	}

}
