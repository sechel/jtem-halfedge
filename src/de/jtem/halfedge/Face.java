package de.jtem.halfedge;

/**
 * Abstract base class for classes representing faces of a 
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
public abstract class Face <V extends Vertex<V, E, F>, 
							E extends Edge<V, E, F>, 
							F extends Face<V, E, F>   > extends Node<V, E, F> {

	private E boundaryEdge = null;
		
	/**
	 * A face class with no further properties or functionality.
	 * @author Stefan Sechelmann, Boris Springborn
	 */
	public static class Naked extends Face<Vertex.Naked, Edge.Naked, Face.Naked> {}

	protected Face() {}
	
	/**
	 * Find an edge in the half-edge data structure that has this face 
	 * as left face.
	 * @return the edge, or null if no such edge exists.
	 * @throws RuntimeException if this face does not belong to a half-edge data structure
	 */
	final public E getBoundaryEdge() throws RuntimeException {
		checkHalfEdgeDataStructure();
		if (boundaryEdge == null || this == boundaryEdge.getLeftFace()) {
			return boundaryEdge;
		}
		// look for a boundary edge.
		for (E e : hds.getEdges()) {
			assert e != null;
			if (this == e.getLeftFace()) {
				boundaryEdge = e;
				return boundaryEdge;
			}
		}
		boundaryEdge = null;
		return boundaryEdge;
	}

	final void setBoundaryEdge(E e) {
		checkHalfEdgeDataStructure(e);
		this.boundaryEdge = e;
	};
	
	
	public void copyData(F f) {
		
	}
		
}
