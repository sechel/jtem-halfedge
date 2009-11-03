package de.jtem.halfedge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.jtem.halfedge.util.HalfEdgeUtils;

/**
 * Class representing a half-edge data structure.
 * 
 * @author Stefan Sechelmann
 * @author Boris Springborn
 *
 * @param <E> the edge class of this half-edge data structure
 * @param <F> the face class of this half-edge data structure
 * @param <V> the vertex class of this half-edge data structure
 */
public class HalfEdgeDataStructure <V extends Vertex<V, E, F>,
										  E extends Edge<V, E, F>, 
									      F extends Face<V, E, F> > {

	private Class<V> vClass = null;
	private Class<E> eClass = null;	
	private Class<F> fClass = null;
	
	private List<V> vertexList = new ArrayList<V>();
	private List<F> faceList = new ArrayList<F>();
	private List<E> edgeList = new ArrayList<E>();	
	
	/**
	 * Instantiate a new half-edge data structure with given 
	 * vertex, edge, and face classes.
	 * 
	 * The parameters serve as runtime type tokens.
	 * 
	 * @param vClass the half-edge data structure's vertex class
	 * @param eClass the half-edge data structure's edge class
	 * @param fClass the half-edge data structure's face class
	 */
	public HalfEdgeDataStructure(Class<V> vClass, Class<E> eClass, Class<F> fClass) {
		if (vClass == null || fClass == null || eClass == null) {
			throw new RuntimeException("Parameters must not be null.");
		}
		this.vClass = vClass;
		this.eClass = eClass;
		this.fClass = fClass;
	}

	/**
	 * Create a combinatorially equivalent copy of this half-edge data structure.
	 * 
	 * @param <VV> the vertex type of the copy
	 * @param <EE> the edge type of the copy
	 * @param <FF> the face type of the copy
	 * @param vC the vertex class, used as runtime type token
	 * @param eC the edge class, used as runtime type token
	 * @param fC the face class, used as runtime type token
	 * @return a combinatorially equivalent copy
	 */
	public final <VV extends Vertex<VV,EE,FF>, EE extends Edge<VV,EE,FF>, FF extends Face<VV,EE,FF>> 
	HalfEdgeDataStructure<VV,EE,FF> createCombinatoriallyEquivalentCopy(Class<VV> vC, Class<EE> eC, Class<FF> fC) {
		return createCombinatoriallyEquivalentCopy(new HalfEdgeDataStructure<VV,EE,FF>(vC, eC, fC));
	}
	
	
	/**
	 * Create a combinatorially equivalent copy of this half-edge data structure. This method 
	 * takes a half-edge data structure object and overwrites its content.
	 * 
	 * @param heds The half-edge data structure to fill.
	 * @return heds
	 */
	public final <VV extends Vertex<VV,EE,FF>, 
				  EE extends Edge<VV,EE,FF>, 
				  FF extends Face<VV,EE,FF>, 
				  HEDS extends HalfEdgeDataStructure<VV,EE,FF>>
	HEDS createCombinatoriallyEquivalentCopy(HEDS heds) {
		heds.vertexList.clear();
		heds.edgeList.clear();
		heds.faceList.clear();
		
		heds.addNewVertices(numVertices());
		heds.addNewEdges(numEdges());
		heds.addNewFaces(numFaces());
		for (E e : edgeList) {
			E eNext = e.getNextEdge();
			E eOpp = e.getOppositeEdge();
			F f = e.getLeftFace();
			V v = e.getTargetVertex();
			EE ee = heds.getEdge(e.getIndex());
			ee.setIsPositive(e.isPositive());
			if (eNext != null) {
				ee.linkNextEdge(heds.getEdge(eNext.getIndex()));
			}
			if (eOpp != null) {
				ee.linkOppositeEdge(heds.getEdge(eOpp.getIndex()));
			}
			if (f != null) {
				ee.setLeftFace(heds.getFace(f.getIndex()));
			}
			if (v != null) {
				ee.setTargetVertex(heds.getVertex(v.getIndex()));
			}
		}
		return heds;
	}
	
	
	
	/**
	 * Return edge class of this half-edge data structure.
	 * @return the edge class
	 */
	public final Class<E> getEdgeClass() {
		return eClass;
	}
	
	/**
	 * Return face class of this half-edge data structure.
	 * @return the face class
	 */
	public final Class<F> getFaceClass() {
		return fClass;
	}

	/**
	 * Return vertex class of this half-edge data structure.
	 * @return the vertex class
	 */
	public final Class<V> getVertexClass() {
		return vClass;
	}

	
	/**
	 * Add a new vertex to this half-edge data structure.
	 * @return the newly created vertex
	 * @throws RuntimeException if an instance of the vertex class cannot be instantiated
	 */
	public final V addNewVertex() throws RuntimeException{
		V vertex = null;
		try {
			vertex = getVertexClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		vertex.setIndex(vertexList.size());
		vertexList.add(vertex);
		vertex.setHalfEdgeDataStructure(this);
		return vertex;
	}
	
	/**
	 * Add <code>n</code> new vertices to this half-edge data structure.
	 * @param n the number of vertices to create and add.
	 * @return the list (with size n) of new vertices
	 * @throws RuntimeException if instances of the vertex class cannot be instantiated
	 */
	public final List<V> addNewVertices(int n) throws RuntimeException {
		List<V> l= new ArrayList<V>(n);
		for (int i = 0; i < n; i++) {
			l.add(this.addNewVertex());
		}
		return l;
	}
	
	/**
	 * Add a new edge to this half-edge data structure.
	 * @return the newly created edge
	 * @throws RuntimeException if an instance of the edge class cannot be instantiated
	 */
	public final E addNewEdge(){
		E edge = null;
		try {
			edge = getEdgeClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		edge.setSelf(edge);
		edge.setIndex(edgeList.size());
		edgeList.add(edge);
		edge.setHalfEdgeDataStructure(this);
		return edge;
	}
	
	/**
	 * Add <code>n</code> new edges to this half-edge data structure.
	 * @param n the number of edges to create and add.
	 * @return the list (with size n) of new edges
	 * @throws RuntimeException if instances of the edge class cannot be instantiated
	 */
	public final List<E> addNewEdges(int n) throws RuntimeException {
		List<E> l= new ArrayList<E>(n);
		for (int i = 0; i < n; i++) {
			l.add(this.addNewEdge());
		}
		return l;
	}
	
	/**
	 * Add a new face to this half-edge data structure.
	 * @return the newly created face
	 * @throws RuntimeException if an instance of the face class cannot be instantiated
	 */
	public final F addNewFace(){
		F face = null;
		try {
			face = getFaceClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		face.setIndex(faceList.size());
		faceList.add(face);
		face.setHalfEdgeDataStructure(this);
		return face;
	}

	/**
	 * Add <code>n</code> new faces to this half-edge data structure.
	 * @param n the number of faces to create and add.
	 * @return the list (with size n) of new faces
	 * @throws RuntimeException if instances of the face class cannot be instantiated
	 */
	public final List<F> addNewFaces(int n) throws RuntimeException {
		List<F> l= new ArrayList<F>(n);
		for (int i = 0; i < n; i++) {
			l.add(this.addNewFace());
		}
		return l;
	}
	
	/**
	 * Remove a face from this halfedge data structure. 
	 * Does nothing if <code>face</code> is <code>null</code>. Otherwise it is 
	 * removed from the face list and invalidated.
	 * This means {@link Node#getHalfEdgeDataStructure() getHalfEdgeDataStructure()} will return 
	 * <code>null</code>, {@link Node#isValid() isValid()} will return <code>false</code>, 
	 * and many other methods will throw an exception.
	 * Edges which had <code>face</code> as left face will now return 
	 * <code>null</code>.
	 * @param face the face to remove
	 * @throws RuntimeException if <code>face</code> does not belong 
	 * to this half-edge data structure
	 */
	public final void removeFace(Face<?,?,?> face) throws RuntimeException {
		if (face == null) {
			return;
		}
		if (this != face.getHalfEdgeDataStructure()) {
			throw new RuntimeException(face + " is null or does not belong to " + this + ".");
		}
		if (faceList.remove(face)) {
			face.setBoundaryEdge(null);
			face.setHalfEdgeDataStructure(null);
			reindexFaces(face.index);
			return;
		}
		assert false;
	}
	
	/**
	 * Remove an edge from this halfedge data structure.
	 * 
	 * Does nothing if <code>edge</code>  is <code>null</code>. Otherwise, 
	 * it is removed from the edge list and invalidated.
	 * This means {@link Node#getHalfEdgeDataStructure() getHalfEdgeDataStructure()} will return 
	 * <code>null</code>, {@link Node#isValid() isValid} will return <code>false</code>, 
	 * and many other methods will throw an exception.
	 * <code>edge</code> will not be linked with any edges, vertices, faces after
	 * execution of this method.
	 * @param edge the edge to remove.
	 * @throws RuntimeException if <code>edge</code> does not belong 
	 * to this half-edge data structure
	 */
	public final void removeEdge(Edge<?,?,?> edge) throws RuntimeException {
		if (edge == null) {
			return;
		}
		if (this != edge.getHalfEdgeDataStructure()) {
			throw new RuntimeException(edge + " does not belong to " + this + ".");
		}
		if (edgeList.remove(edge)) {
			edge.setLeftFace(null);
			edge.setTargetVertex(null);
			edge.linkOppositeEdge(null);
			edge.linkNextEdge(null);
			edge.linkPreviousEdge(null);
			edge.setHalfEdgeDataStructure(null);
			reindexEdges(edge.index);
			return;
		}
		assert false;
	}
	

	
	/**
	 * Remove a vertex from this half-edge data structure. 
	 * 
	 * Does nothing if <code>vertex</code> is <code>null</code>. 
	 * Otherwise, it is removed from the vertex list and invalidated.
	 * This means {@link Node#getHalfEdgeDataStructure() getHalfEdgeDataStructure()} will return 
	 * <code>null</code>, {@link Node#isValid() isValid()} will return <code>false</code>, 
	 * and many other methods will throw an exception.
	 * Edges which had <code>vertex</code> as target vertex will now return 
	 * <code>null</code>.
	 * @param vertex the vertex to remove.
	 * @throws RuntimeException if <code>vertex</code> does not belong 
	 * to this half-edge data structure
	 */
	public final void removeVertex(Vertex<?,?,?> vertex){
		if (vertex == null)
			return;
		if (this != vertex.getHalfEdgeDataStructure()) {
			throw new RuntimeException(vertex + " does not belong to " + this + ".");
		}
		if (vertexList.remove(vertex)){
			vertex.setIncomingEdge(null);
			vertex.setHalfEdgeDataStructure(null);
			reindexVertices(vertex.index);
			return;
		}
		assert false;
	}
	
	
	/**
	 * Return vertex with given index from vertex list.
	 * @param index the index
	 * @return the vertex
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 */
	public final V getVertex(int index) throws IndexOutOfBoundsException {
		return getNode(vertexList, index);
	}
	
	/**
	 * Return edge with given index from edge list.
	 * @param index the index
	 * @return the edge
	 */
	public final E getEdge(int index){
		return getNode(edgeList, index);
	}

	/**
	 * Return face with given index from face list.
	 * @param index the index
	 * @return the face
	 */
	public final F getFace(int index){
		return getNode(faceList, index);
	}
	
	private final <N extends Node<V,E,F>> N getNode(List<N> nodeList, int index) throws IndexOutOfBoundsException {
		N n = nodeList.get(index);
		assert n != null && this == n.getHalfEdgeDataStructure();
		return n;
	}
	
	
	void reindexVertices(int start) {
		Iterator<V> it = vertexList.listIterator(start);
		while (it.hasNext()) {
			it.next().setIndex(start++);
		}
	}
	
	void reindexEdges(int start) {
		Iterator<E> it = edgeList.listIterator(start);
		while (it.hasNext()) {
			it.next().setIndex(start++);
		}
	}
	
	void reindexFaces(int start) {
		Iterator<F> it = faceList.listIterator(start);
		while (it.hasNext()) {
			it.next().setIndex(start++);
		}
	}
	
	
	/**
	 * Return number of faces.
	 * @return size of the face list
	 */
	public final int numFaces(){
		return faceList.size();
	}
	
	/**
	 * Return number of edge.
	 * @return size of the edge list
	 */
	public final int numEdges(){
		return edgeList.size();
	}
	
	/**
	 * Return number of vertices.
	 * @return size of the vertex list
	 */
	public final int numVertices(){
		return vertexList.size();
	}
	
	/**
	 * Return the face list.
	 * @return an unmodifiable view of the face list
	 */
	public final List<F> getFaces(){
		return Collections.unmodifiableList(faceList);
	}
	
	/**
	 * Return the vertex list.
	 * @return an unmodifiable view of the vertex list
	 */
	public final List<V> getVertices(){
		return Collections.unmodifiableList(vertexList);
	}
	
	/**
	 * Return the edge list.
	 * @return an unmodifiable view of the edge list
	 */
	public final List<E> getEdges(){
		return Collections.unmodifiableList(edgeList);
	}

	private final class SignatureEdgeIterator implements Iterator<E>{

		private boolean
			signature = false;
		private int
			actIntex = -1;
		private E
			nextEdge = null;
				
		public SignatureEdgeIterator(boolean signature) {
			this.signature = signature;
			nextEdge = getNextEdge();
		}
		
		private E getNextEdge(){
			while (++actIntex < edgeList.size()){
				E edge = edgeList.get(actIntex);
				if (edge.isPositive() == signature)
					return edge;
			}
			return null;
		}
		
		public boolean hasNext() {
			return nextEdge != null;
		}

		public E next() {
			E result = nextEdge;
			nextEdge = getNextEdge();
			return result;
		}

		public void remove() {
		}
		
	}
	
	
	/**
	 * Return an {@link Iterable} for iterating over the positive edges.
	 * If you want to loop over those edges in the edge list for which 
	 * {@link Edge#isPositive} returns {@code true}, you may do like this:<br>
	 * <br>
	 * {@code for (E e : mySuface.getPositiveEdges()) { ... }}
	 * @return the {@link Iterable}
	 */
	public final Iterable<E> getPositiveEdges(){
		return new Iterable<E>(){
			public Iterator<E> iterator() {
				return new SignatureEdgeIterator(true);
			}
		};
	}
	
	/**
	 * Return an {@link Iterable} for iterating over the positive edges.
	 * 
	 * @see #getPositiveEdges()
	 * @return the {@link Iterable}
	 */
	public final Iterable<E> getNegativeEdges(){
		return new Iterable<E>(){
			public Iterator<E> iterator() {
				return new SignatureEdgeIterator(false);
			}
		};
	}
	
	
	@Override
	public String toString() {
		return getClass().getSimpleName() 
			+ "<" + vClass.getSimpleName() 
			+ "," + eClass.getSimpleName() 
			+ "," + fClass.getSimpleName() + ">"
			+ "[vertices: " + numVertices() + ", oriented edges: "+ numEdges() + ", faces: " + numFaces() + "]";
	}

	/**
	 * Test whether this half-edge data structure represents a valid surface.
	 * 
	 * Checks whether the following conditions are satisfied.<br>
	 * <br>
	 * <b>1.</b> The edge list is not empty.<br>
	 * <br>
	 * For all edges {@code e} in the edge list: <br>
	 * <br>
	 * <b>2.</b> Neither {@link Edge#getNextEdge() e.getNextEdge()}, nor {@link Edge#getPreviousEdge() e.getPreviousEdge()},
	 * 			 nor  {@link Edge#getOppositeEdge() e.getOppositeEdge()} returns {@code null}.<br>
	 * <b>3.</b> {@link Edge#getTargetVertex() e.getTargetVertex()} does not return {@code null}.<br>
	 * <b>4.</b> {@link Edge#getLeftFace() e.getLeftFace()} and {@link Edge#getRightFace() e.getRightFace()} do not <i>both</i> return {@code null}. <br>
	 * <b>5.</b> {@link Edge#getLeftFace() getLeftFace()} returns the same face for {@code e} and {@code e.}{@link Edge#getNextEdge() getNextEdge()}.<br>
	 * <b>6.</b> {@link Edge#getTargetVertex() getTargetVertex()} returns the same vertex for {@code e} and 
	 *           {@code e.}{@link Edge#getNextEdge() getNextEdge().}{@link Edge#getOppositeEdge() getOppositeEdge()}.<br>
	 * <br>
	 * For all faces {@code f} in the face list: <br>
	 * <br>
	 * <b>7.</b> There is at least one edge {@code e} in the edge list that has {@code f} as left face. That is, there are no isolated faces.<br>
	 * <b>8.</b> If you repeatedly perform {@code e = e.}{@link Edge#getNextEdge() getNextEdge()}, you can reach all edges with {@code f} as left face.<br>
	 * <br>
	 * For all vertices {@code v} in the vertex list:<br>
	 * <br> 
	 * <b>9.</b> There is at least one edge {@code e} in the edge list that has {@code v} as target vertex. That is, there are no isolated vertices.<br>
	 * <b>10.</b> If you repeatedly perform {@code e = e.}{@link Edge#getNextEdge() getNextEdge().}{@link Edge#getOppositeEdge() getOppositeEdge()}, 
	 * you can reach all edges with {@code v} as target vertex. <br>
	 * <br>
	 * Together with the invariants that are maintained by the classes in this package, this guarantees that the half-edge data structure encodes a cell
	 * decomposition of a surface.
	 * 
	 * @return {@code true} if this half-edge data structure represents a valid surface, {@code false} otherwise
	 * @deprecated use {@link de.jtem.halfedge.util.HalfEdgeUtils#isValidSurface(HalfEdgeDataStructure)}
	 */
	@Deprecated
	public final boolean isValidSurface() {
		return HalfEdgeUtils.isValidSurface(this);
	}
	
	
	/**
	 * Test whether this half-edge data structure represents a valid surface and, optionally, give a reason if it fails the test.
	 * If the parameter is {@code false}, this method behaves exactly as {@link #isValidSurface()}. If the parameter is {@code true},
	 * and the half-edge data structure fails to represent a valid surface, the method outputs a brief explanation to {@link System#err}.
	 * This feature is intended for debugging.
	 * @param printReasonForFailureToSystemErr {@code true} if you want output to {@link System#err}.
	 * @return {@code true} if this half-edge data structure represents a valid surface, {@code false} otherwise
	 * @deprecated use {@link de.jtem.halfedge.util.HalfEdgeUtils#isValidSurface(HalfEdgeDataStructure, boolean)}. 
	 */
	@Deprecated
	public final boolean isValidSurface(boolean printReasonForFailureToSystemErr) {
		return HalfEdgeUtils.isValidSurface(this, printReasonForFailureToSystemErr);
	}
	
	public void clear() {
		vertexList.clear();
		edgeList.clear();
		faceList.clear();
	}
	
	
}
