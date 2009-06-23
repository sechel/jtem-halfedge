package de.jtem.halfedge;

/**
 * Abstract base class for classes representing vertices of a 
 * {@link HalfEdgeDataStructure}.
 * 
 * Any subclass that is to be used with a {@link HalfEdgeDataStructure} 
 * must provide a default constructor accessible to {@link HalfEdgeDataStructure}.
 * 
 * @author Stefan Sechelmann
 * @author Boris Springborn
 *
 * @param <V> the corresponding vertex class
 * @param <E> the corresponding edge class
 * @param <F> the corresponding face class
 */
public abstract class Vertex <V extends Vertex<V, E, F>,
	    					  E extends Edge<V, E, F>,
	    					  F extends Face<V, E, F>> extends Node<V, E, F> {

    private E incomingEdge = null;

	/**
	 * A vertex class with no further properties or functionality.
	 * @author Stefan Sechelmann, Boris Springborn
	 */
    public static class Naked extends Vertex<Vertex.Naked, Edge.Naked, Face.Naked> {}
    	
    protected Vertex() {}
    
	/**
	 * Find an edge in the half-edge data structure that has this vertex 
	 * as target vertex.
	 * @return the edge, or null if no such edge exists.
	 * @throws RuntimeException if this face does not belong to a half-edge data structure
	 */
	final public E getIncomingEdge() throws RuntimeException {
		checkHalfEdgeDataStructure();
		if (incomingEdge == null || this == incomingEdge.getTargetVertex()) {
			return incomingEdge;
		}
		// look for an incoming edge.
		for (E e : hds.getEdges()) {
			assert e != null;
			if (this == e.getTargetVertex()) {
				incomingEdge = e;
				return incomingEdge;
			}
		}
		incomingEdge = null;
		return incomingEdge;
    }

	final void setIncomingEdge(E e) {
		checkHalfEdgeDataStructure(e);
		this.incomingEdge = e;
	}
	
	public void copyData(V v) {
		
	}
	
}
