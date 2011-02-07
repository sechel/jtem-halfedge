/**
This file is part of a jTEM project.
All jTEM projects are licensed under the FreeBSD license 
or 2-clause BSD license (see http://www.opensource.org/licenses/bsd-license.php). 

Copyright (c) 2006-2010, Technische Universität Berlin, jTEM
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

-	Redistributions of source code must retain the above copyright notice, 
	this list of conditions and the following disclaimer.

-	Redistributions in binary form must reproduce the above copyright notice, 
	this list of conditions and the following disclaimer in the documentation 
	and/or other materials provided with the distribution.
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
OF SUCH DAMAGE.
**/

package de.jtem.halfedge.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.jtem.halfedge.Edge;
import de.jtem.halfedge.Face;
import de.jtem.halfedge.HalfEdgeDataStructure;
import de.jtem.halfedge.Vertex;


/**
 * Class providing static utility methods for half-edge data structures.
 * 
 * @author Boris Springborn
 */
/**
 * @author springb
 *
 */
public final class HalfEdgeUtils {
	
	// Don't instatiate.
	private HalfEdgeUtils() {}
	
	/**
	 * Test whether the half-edge data structure represents a valid surface.
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
	 * <b>11.</b> Among all edges {@code e} with {@code v} as target vertex, there is at most one for which {@link Edge#getLeftFace() e.getLeftFace()} returns {@code null}.
	 * <br>
	 * <br>
	 * Together with the invariants that are maintained by the classes in this package, this guarantees that the half-edge data structure encodes a cell
	 * decomposition of a surface.
	 * 
	 * @return {@code true} if the half-edge data structure represents a valid surface, {@code false} otherwise
	 */
	static public boolean isValidSurface(HalfEdgeDataStructure<?,?,?> heds) {
		return isValidSurface(heds, false);
	}
	/**
	 * Test whether the half-edge data structure represents a valid surface and, optionally, give a reason if it fails the test.
	 * If the parameter is {@code false}, this method behaves exatly as {@link #isValidSurface(HalfEdgeDataStructure)}. If the parameter is {@code true},
	 * and the half-edge data structure fails to represent a valid surface, the method outputs a brief explanation to {@link System#err}.
	 * This feature is intended for debugging.
	 * @param printReasonForFailureToSystemErr {@code true} if you want output to {@link System#err}.
	 * @return {@code true} if the half-edge data structure represents a valid surface, {@code false} otherwise
	 */
	static public boolean isValidSurface(HalfEdgeDataStructure<?,?,?> heds, boolean printReasonForFailureToSystemErr) {

		// must have at least one edge
		if (heds.getEdges().isEmpty()) {
			if (printReasonForFailureToSystemErr) System.err.println("getEdges().isEmpty == true");
			return false;
		}

		// check for null references
		for (Edge<?,?,?> e : heds.getEdges()) {
			if (e.getNextEdge() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("getNextEdge() returns null for edge " + e.getIndex());
				return false;
			}
			if (e.getPreviousEdge() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("getPreviousEdge() returns null for edge " + e.getIndex());
				return false;
			}
			if (e.getOppositeEdge() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("getOppositeEdge() returns null for edge " + e.getIndex());
				return false;
			}
			if (e.getTargetVertex() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("getTargetVertex() returns null for edge " + e.getIndex());
				return false;
			}
			// either left face or right face may be null but not both
			if (e.getLeftFace() == null && e.getRightFace() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("Left face and right face are null for edge " + e.getIndex());
				return false;
			}
		}

		// Check if edges in a cycle have same left face and edges in a cocycle have same target vertex
		for (Edge<?,?,?> e : heds.getEdges()) {
			if (e.getLeftFace() != e.getNextEdge().getLeftFace()) {
				if (printReasonForFailureToSystemErr) System.err.println("e.getLeftFace() != e.getNextEdge().getLeftFace() for edge " + e.getIndex());
				return false;
			}
			if (e.getTargetVertex() != e.getNextEdge().getOppositeEdge().getTargetVertex()) {
				if (printReasonForFailureToSystemErr) System.err.println("e.getTargetVertex() != e.getNextEdge().getOppositeEdge().getTargetVertex() for edge " + e.getIndex());
				return false;
			}
		}		

		// check if every face has a boundary edge
		for (Face<?,?,?> f : heds.getFaces()) {
			if (f.getBoundaryEdge() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("Face " + f.getIndex() + " has no boundary edge.");
				return false;
			}
		}
		
		// check if every vertex has an incoming edge
		for (Vertex<?,?,?> v : heds.getVertices()) {
			if (v.getIncomingEdge() == null) {
				if (printReasonForFailureToSystemErr) System.err.println("Vertex " + v.getIndex() + " has no incoming edge.");
			}
		}
		
		// check if each face corresponds to a unique edge cycle, 
		// each vertex corresponds to a unique edge cocycle,
		// and that there is at most one edge in a vertex cocyle with left face == null 
		int ne = heds.numEdges();
		int nf = heds.numFaces();
		int nv = heds.numVertices();
		assert (nv > 0 && ne > 0 && nf > 0);
		boolean[] vertexMark = new boolean[nv];
		boolean[] edgeMark = new boolean[ne];
		boolean[] faceMark = new boolean[nf];
		assert (false == (edgeMark[0])); // false should be the default value
		for (Edge<?,?,?> e : heds.getEdges()) {
			if (edgeMark[e.getIndex()]) {
				// the cycle of this edge has already been treated
				continue;
			}
			Face<?,?,?> f = e.getLeftFace();
			if (f != null) {
				// has this edge's left face already occurred in another cycle?
				if (faceMark[f.getIndex()]) {
					if (printReasonForFailureToSystemErr) System.err.println("Face " + f.getIndex() + " is contained in more than one edge cycle. (Found out while looking at " + e + ")");
					return false;
				}
				// mark the left face
				faceMark[f.getIndex()] = true;
			}
			// mark all edges in the edge cycle
			edgeMark[e.getIndex()] = true;
			for (Edge<?,?,?> e1 = e.getNextEdge(); e1 != e; e1 = e1.getNextEdge()) {
//				System.err.println(e1);
				edgeMark[e1.getIndex()] = true;
			}
		}
		edgeMark = new boolean[ne]; // initialize to false again
		assert (false == (edgeMark[0])); // false should be the default value
		for (Edge<?,?,?> e : heds.getEdges()) {
			if (edgeMark[e.getIndex()]) {
				// the cycle of this edge has already been treated
				continue;
			}
			Vertex<?,?,?> v = e.getTargetVertex();
			// has this edge's target vertex already occurred in another cycle?
			if (vertexMark[v.getIndex()]) {
				if (printReasonForFailureToSystemErr) System.err.println("Vertex " + v.getIndex() + " is contained in more than one edge cocycle.");
				return false;
			}
			// mark the left face
			vertexMark[v.getIndex()] = true;
			// mark all edges in the edge cocycle and check whether there's more than one with left face == null.
			edgeMark[e.getIndex()] = true;
			boolean leftFaceNull = e.getLeftFace() == null;
			for (Edge<?,?,?> e1 = e.getNextEdge().getOppositeEdge(); e1 != e; e1 = e1.getNextEdge().getOppositeEdge()) {
//				System.err.println(e1);
				edgeMark[e1.getIndex()] = true;
				if (e1.getLeftFace() == null) {
					if (leftFaceNull) {
						if (printReasonForFailureToSystemErr) System.err.println("There is more than one edge with target vertex " + v.getIndex() + " and left face null.");
						return false;
					}
					leftFaceNull = true;
				}
			}
		}

		// passed all tests
		return true;
	}
		
	
	
	/**
	 * Returns a list containing all vertices that are 
	 * target vertices of the boundary of the given face
	 * @see {@link HalfEdgeUtils#boundaryEdges(Face)}
	 * @param <V>
	 * @param <E>
	 * @param <F>
	 * @param face the face
	 * @return
	 */
	public static <
		V extends Vertex<V, E, F>,
		E extends Edge<V, E, F>,
		F extends Face<V, E, F>
	> List<V> boundaryVertices(F face) {
		Collection<E> b = boundaryEdges(face);
		LinkedList<V> vList = new LinkedList<V>();
		for (E e : b) {
			vList.add(e.getTargetVertex());
		}
		return vList;
	}
	
	
	/**
	 * Return a list of the edges which have a given face as left face. 
	 * <p>
	 * This method expects that if there is at least one such edge {@code e}, then all such edges, and only those, can be reached by repeatedly
	 * executing {@code e = e.}{@link de.jtem.halfedge.Edge#getNextEdge() getNextEdge()}, and that in the process
	 * {@code e} never becomes {@code null}.
	 * <p>
	 * The returned list contains the boundary edges in the correct cyclic order.
	 * 
	 * @param <E> the edge type
	 * @param <F> the face type
	 * @param face the face
	 * @return the list of boundary edges in the correct cyclic order
	 */
	static public <E extends Edge<?,E,F>, F extends Face<?,E,F>> List<E> boundaryEdges(F face) {
		final E e0 = face.getBoundaryEdge();
		if (e0 == null) {
			return Collections.emptyList();
		}
		LinkedList<E> result = new LinkedList<E>();
		E e = e0;
		do {
			if (face != e.getLeftFace()) {
				throw new RuntimeException("Edge " + e + " does not have face " + face + " as left face, " +
						"although it is the next edge of an edge which does.");
			}
			result.add(e);
			e = e.getNextEdge();
			if (e == null) {
				throw new RuntimeException("Some edge has null as next edge.");
			}
		} while (e != e0);
		return result;
	}
	
	/**
	 * Returns a collection containing all edges of {@code heds} with left face equal to null.
	 * @param <E> the edge type
	 * @param heds the surface
	 * @return the collection of boundary edges
	 */
	static public <E extends Edge<?,E,?>> Collection<E> boundaryEdges(HalfEdgeDataStructure<?,E,?> heds) {
		Collection <E>result = new ArrayList<E>();
		for (E e : heds.getEdges()) {
			if (e.getLeftFace() == null) {
				result.add(e);
			}
		}
		return result;
	}
	
	/**
	 * Returns a list containing lists for all boundary components 
	 * of {@code hds} with left face equal to null.
	 * @param <E> the edge type
	 * @param hds the surface
	 * @return the collection of boundary components
	 * @see boundaryEdges
	 */
	static public <E extends Edge<?,E,?>> List<List<E>> boundaryComponents(HalfEdgeDataStructure<?,E,?> hds) {
		List<List<E>> result = new ArrayList<List<E>>();
		Set<E> b = new HashSet<E>(boundaryEdges(hds));
		while (!b.isEmpty()) {
			List<E> c = new LinkedList<E>();
			E first = b.iterator().next();
			E e = first;
			do {
				assert b.contains(e);
				c.add(e);
				b.remove(e);
				e = e.getNextEdge();
			} while (e != first);
			result.add(c);
		}
		return result;
	}
	
	
	/**
	 * Returns a collection of all boundary vertices of {@code surf}. Assumes that {@code surf} represents a valid surface.
	 * @param <V> the vertex type
	 * @param <E> the edge type
	 * @param surf
	 * @return collection of boundary vertices
	 */
	static public <V extends Vertex<V,E,?>, E extends Edge<V,E,?>> Collection<V> boundaryVertices(HalfEdgeDataStructure<V,E,?> surf) {
		Collection<V> result = new ArrayList<V>();
		for (E e : surf.getEdges()) {
			if (e.getLeftFace() == null) {
				result.add(e.getTargetVertex());
			}
		}
		return result;
	}
	

	/**
	 * Return a list of the edges which have a given vertex as target vertex. 
	 * <p>
	 * This method expects that if there is one such edge {@code e}, then all such edges, and only those, can be reached by repeatedly
	 * executing {@code e = e.}{@link de.jtem.halfedge.Edge#getNextEdge() getNextEdge()}{@code .}{@link de.jtem.halfedge.Edge#getOppositeEdge() getOppositeEdge()},
	 * and that in the process {@code e} never becomes {@code null}.
	 * <p>
	 * The returned list contains the incoming edges in clockwise order.
	 *	
	 * @param <E> the edge type
	 * @param <V> the vertex type
	 * @param vertex the vertex
	 * @return the list of incoming edges, in clockwise order
	 */
	static public <E extends Edge<V,E,?>, V extends Vertex<V,E,?>> List<E> incomingEdges(V vertex){
		final E e0 = vertex.getIncomingEdge();
		if (e0 == null) {
			return Collections.emptyList();
		}
		LinkedList<E> result = new LinkedList<E>();
		E e = e0;
		do {
			if (vertex != e.getTargetVertex()) {
				throw new RuntimeException("Edge " + e + " does not have vertex " + vertex + " as target vertex, " +
				"although it is the opposite of the next edge of an edge which does.");
			}
			result.add(e);
			e = e.getNextEdge();
			if (e == null) {
				throw new RuntimeException("Some edge has null as next edge.");
			}
			e = e.getOppositeEdge();
			if (e == null) {
				throw new RuntimeException("Some edge has null as opposite edge.");
			}
		} while (e != e0);
		return result;
	}

	
	/**
	 * Return a list of those vertices that are connected to a given {@code vertex} by an edge.
	 * <p>
	 * The {@code i}th element of in this list is simply<br>
	 * {@code vertex.}{@link #incomingEdges(Vertex) getIncomingEdges(vertex)}{@code .get(i).}{@link de.jtem.halfedge.Edge#getStartVertex() getStartVertex() }.<br>
	 * Thus, the preconditions of {@link #incomingEdges(Vertex)} apply. 
	 * <p>
	 * The list contains the neighboring vertices in clockwise order, and it may contain the same vertex multiple times.
	 * (It may also contain {@code null}s.)
	 * 
	 * @param <E> the edge type
	 * @param <V> the vertex type
	 * @param vertex the list of neighboring vertices.
	 * @return the list of neighbor vertices, in clockwise order
	 */
	static public <E extends Edge<V,E,?>, V extends Vertex<V,E,?>> List<V> neighboringVertices(V vertex) {
		List<E> incoming = incomingEdges(vertex);
		LinkedList<V> result = new LinkedList<V>();
		for (E e : incoming) {
			result.add(e.getStartVertex());
		}
		return result;
	}
	
	
	/**
	 * Return a list of faces that are incident with a given vertex.
	 * <p>
	 * Uses {@link #incomingEdges(Vertex)}, so the preconditions explained there apply.
	 * <p>
	 * The returned list contains the incident faces in clockwise order. It does not contain {@code null}, 
	 * even if the vertex is on the boundary.
	 * @param <V> the vertex type
	 * @param <E> the edge type
	 * @param <F> the face type 
	 * @param vertex the vertex
	 * @return the incident faces in clockwise order
	 */
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> List<F> facesIncidentWithVertex(V vertex) {
		List<E> incoming = incomingEdges(vertex);
		List<F> result = new LinkedList<F>();
		for (E e : incoming) {
			F f = e.getLeftFace();
			if (f != null) {
				result.add(f);
			}
		}
		return result;
	}
	
	
	/**
	 * Test if a given vertex is on the boundary.
	 * <p>
	 * Uses {@link #incomingEdges(Vertex)}, so the preconditions explained there apply.
	 * 
	 * @param vertex
	 * @return {@code true} if there is an incoming edge with {@code null} as {@linkplain de.jtem.halfedge.Edge#getLeftFace() left face}, otherwise {@code false}.
	 */
	static public <V extends Vertex<V,E,?>,E extends Edge<V,E,?>> boolean isBoundaryVertex(V vertex) {
		List<E> incoming = incomingEdges(vertex);
		for (E e : incoming) {
			if (e.getLeftFace() == null) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Test if a given edge is on the boundary.
	 * 
	 * @param e
	 * @return {@code true} if either e.getLeftFace() == null or e.getRightFace() == null.
	 */
	static public <E extends Edge<?, E, ?>> boolean isBoundaryEdge(E e) {
		return e.getLeftFace() == null || e.getRightFace() == null;
	}
	
	
	/**
	 * Test if a given face is an inerior face (that is, not on the boundary).
	 * <p>
	 * Uses {@link #boundaryEdges(Face)}, so the preconditions explained there apply.
	 * @param <E> the edge type
	 * @param <F> the face type
	 * @param face the face
	 * @return {@code false} if there is a boundary edge with {@code null} as {@linkplain de.jtem.halfedge.Edge#getRightFace() right face}, otherwise {@code true}
	 */
	static public <F extends Face<?,E,F>, E extends Edge<?,E,F>> boolean isInteriorFace(F face){
		for (E e : boundaryEdges(face))
			if (e.getRightFace() == null) {
				return false;
			}
		return true;
	}

	
	/**
	 * Test if a given edge is an interior edge (that is, not on the boundary).
	 * @param e the edge
	 * @return {@code e.getLeftFace() != null && e.getRightFace() != null} 
	 */
	static public boolean isInteriorEdge(Edge<?,?,?> e){
		return e.getLeftFace() != null && e.getRightFace() != null;
	}
	
	/**
	 * Insert a new face into an edge cycle with no left face.
	 * <p>
	 * An edge cycle is a cycle of edges obtained by repeatedly applying {@link de.jtem.halfedge.Edge#getNextEdge()}.
	 * This method expects that one does not encounter {@code null} when doing so, and that all edges in the cycle
	 * have {@code null} as left face.
	 * 
	 * @param <V> the vertex type
	 * @param <E> the edge type
	 * @param <F> the face type
	 * @param edge an edge of the edge cycle
	 * @return the new face
	 * @throws RuntimeException if it is called with {@code null} as parameter, of if {@link de.jtem.halfedge.Edge#getNextEdge()} 
	 * returns {@code null} for some edge in the edge cycle, or if {@link de.jtem.halfedge.Edge#getLeftFace} is not {@code null} for some
	 * edge in the cycle.
	 */
	static public <V extends Vertex<V,E,F>,E extends Edge<V,E,F>, F extends Face<V,E,F>> F fillHole(E edge) throws RuntimeException {
//		System.err.println("fillHole(" + edge + ")");
		List<E> edgeList = new ArrayList<E>();
		E e = edge;
		do {
			if (e == null) {
					throw new RuntimeException("Method must not be called with null as parameter.");
			}
			if (e.getLeftFace() != null) {
				throw new RuntimeException(edge + " has a non-null left face. Null expected.");
			}
			edgeList.add(e);
			e = e.getNextEdge();
		} while (e != edge);
		F f = edge.getHalfEdgeDataStructure().addNewFace();
//		System.err.print("\tgluing " + f + " to ");
		for (E ee : edgeList) {
//			System.err.print(ee + " ");
			ee.setLeftFace(f);
		}
//		System.err.println();
		return f;
	}

	
	/**
	 * Fill all holes by adding faces.
	 * <p>
	 * Simply calls {@link #fillHole(Edge)} on edges with no left face until all edges have a left face.
	 * Hence, the preconditions described there apply.
	 *  
	 * @param heds
	 */
	static public <V extends Vertex<V,E,F>,
	               E extends Edge<V,E,F>,
	               F extends Face<V,E,F>> void fillAllHoles(HalfEdgeDataStructure<V,E,F> heds) {
		for (E e : heds.getEdges()) {
			if (e.getLeftFace() == null) {
				fillHole(e);
			}
		}
	}
	
	/**
	 * Add a new n-gon.
	 * <p>
	 * Increases the number of faces by 1, the number of edges by 2*n, and the number of vertices by n.
	 * @param <V> the vertex type
	 * @param <E> the edge type
	 * @param <F> the face type
	 * @param heds the half-edge data structure
	 * @param n the number of vertices 
	 * @return the new face
	 */
	static public <V extends Vertex<V,E,F>,E extends Edge<V,E,F>, F extends Face<V,E,F>> F addNGon(HalfEdgeDataStructure<V,E,F> heds, int n) {
		List<E> edges = heds.addNewEdges(n);
		List<E> oppositeEdges = heds.addNewEdges(n);
		List<V> vertices = heds.addNewVertices(n);
		F face = heds.addNewFace();
		for (int i = 0; i < n; i++) {
			int iPlus1 = (i + 1) % n;
			E eI = edges.get(i);
			E eIPlus1 = edges.get(iPlus1);
			E eOppI = oppositeEdges.get(i);
			E eOppIPlus1 = oppositeEdges.get(iPlus1);
			V vI = vertices.get(i);
			eI.linkOppositeEdge(eOppI);
			eI.linkNextEdge(eIPlus1);
			eOppI.linkPreviousEdge(eOppIPlus1);
			eI.setLeftFace(face);
			eI.setTargetVertex(vI);
			eOppIPlus1.setTargetVertex(vI);
		}
		return face;
	}
	
	/**
	 * Find an edge with given start and target vertices.
	 * <p>
	 * Uses {@link #incomingEdges(Vertex)}, so the preconditions explained there apply.
	 * @param <V> the vertex type
	 * @param <E> the edge type
	 * @param startVertex the start vertex
	 * @param targetVertex the target vertex
	 * @return an edge with those vertices as start and target vertices, or {@code null} if no such edge exists.
	 */
	static public <V extends Vertex<V,E,?>,E extends Edge<V,E,?>> E findEdgeBetweenVertices(V startVertex, V targetVertex) {
		for (E e : incomingEdges(targetVertex)) {
			if (startVertex == e.getStartVertex()) {
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Find an edge with given left and right faces.
	 * <p>
	 * Uses {@link #boundaryEdges(Face)}, so the preconditions explained there apply.
	 * @param <E> the edge type
	 * @param <F> the face type
	 * @param leftFace the left face
	 * @param rightFace the right face
	 * @return an edge with those faces as left and right face, or {@code null} if no such edge exists.
	 */
	static public <E extends Edge<?,E,F>, F extends Face<?,E,F>> E findEdgeBetweenFaces(F leftFace, F rightFace) {
		for (E e : boundaryEdges(leftFace)) {
			if (rightFace == e.getRightFace()) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Construct a new face by giving its vertices in cyclic order. 
	 * <p>
	 * This method assumes:<br>
	 * <ul>
	 * <li>At least three non-null vertices are passed as parameters.</li>
	 * <li>They belong to the {@link de.jtem.halfedge.HalfEdgeDataStructure HalfEdgeDataStructure} {@code heds}.</li>
	 * <li>Except for isolated vertices and missing faces, {@code heds} is a valid surface or empty. More precisely: If all vertices {@code v} with
	 * 		{@code v.}{@link de.jtem.halfedge.Vertex#getIncomingEdge() getIncomingEdge()}{@code == null}
	 * 		were removed from {@code heds}, and after executing {@link #fillAllHoles(HalfEdgeDataStructure) fillAllHoles(heds)}, {@code heds} would be empty or a 
	 *      {@linkplain de.jtem.halfedge.HalfEdgeDataStructure#isValidSurface() valid surface}.</li>
	 * <li>There are no loops or double edges in {@code heds}. More precisely: If {@code v0} and {@code v1} are vertices of {@code heds} 
	 * 		(where {@code v0 == v1} is allowed) then there is at most one {@link de.jtem.halfedge.Edge Edge} with start vertex {@code v0}
	 * 		and target vertex {@code v1}.</li>  
	 * <li>Adding a face with the given vertices will result in a {@link de.jtem.halfedge.HalfEdgeDataStructure HalfEdgeDataStructure}
	 * 						  without loops or double edges, which is again valid surface except for isolated vertices and missing faces. </li>  
	 * </ul>
	 * <p>
	 * Failure of this method is not atomic: If it throws an exception, the half-edge data structure may be left in some intermediate state.
	 * @param <V> the vertex type
	 * @param <E> the edge type
	 * @param <F> the face type
	 * @param heds the half-edge data structure
	 * @param v the vertices 
	 * @return an edge with the new face as left face
	 * @throws RuntimeException in some cases when something goes wrong. Of course, this should not happen if the above preconditions are fulfilled.
	 */
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> 
					E constructFaceByVertices(HalfEdgeDataStructure<V,E,F> heds, Vertex<?,?,?>... v) throws RuntimeException {
//		System.err.print("constructFaceByVertices(");
//		for (int i = 0; i < v.length; i ++) {
//			System.err.print(v[i] + " ");
//		}
//		System.err.println(")");
		if (heds == null) {
			throw new RuntimeException("Null may not be passed as HalfEdgeDataStructure argument.");
		}
		if (v.length < 3) {
			throw new RuntimeException("At least three vertices must be passed as arguments.");
		}		
//		if (v[0] == null) {
//			throw new RuntimeException("Null elements are not allowed as vertex arguments.");
//		}
//		HalfEdgeDataStructure<V,E,F> heds = v[0].getHalfEdgeDataStructure();
		for (int i = 0; i < v.length; i++) {
			if (v[i] == null) {
				throw new RuntimeException("Null elements are not allowed in the vertex argument list.");
			}
			if (v[i].getHalfEdgeDataStructure() != heds) {
				throw new RuntimeException(v[i] + " doesn't belong to " + heds + ".");
			}
		}
		Class<V> vClass = heds.getVertexClass();
		int numOldEdges = heds.numEdges();
		List<E> edgeCycle = new ArrayList<E>();
		for (int i = 0; i < v.length; i++) {
			E e = findEdgeBetweenVertices(vClass.cast(v[i]), vClass.cast(v[(i+1) % v.length]));
			if (e == null) {
				// make a new pair of half-edges
//				System.err.println("\tCreating edge pair from " + v[i] + " to " + v[(i+1) % v.length]);
				e = heds.addNewEdge();
				E eOpp = heds.addNewEdge();
				e.linkOppositeEdge(eOpp);
			} else {
				if (e.getOppositeEdge() == null) {
					throw new RuntimeException(e + " has no opposite edge.");
				}
				if (e.getLeftFace() != null) {
					throw new RuntimeException("There already is an edge from " + e.getStartVertex() + " to " + e.getTargetVertex() 
												+ " with " + e.getLeftFace() + " as left face.");
				}
			}
			edgeCycle.add(e);
		}
		int n = edgeCycle.size();
		for (int i0 = 0; i0 < n; i0++) {
			int i1 = (i0 + 1) % n;
			E e0 = edgeCycle.get(i0);
			E e1 = edgeCycle.get(i1);
//			V v0 = v[i0];
			V v1 = vClass.cast(v[i1]);
			if (e0.getIndex() >= numOldEdges) {
				if (e1.getIndex() >= numOldEdges) {
					// e0 and e1 are new
					e0.linkNextEdge(e1);
					e1.getOppositeEdge().linkNextEdge(e0.getOppositeEdge());
					e0.setTargetVertex(v1);
					e1.getOppositeEdge().setTargetVertex(v1);
				} else {
					// e0 is new, e1 is old
					E oldPrevEdge = e1.getPreviousEdge();
					e0.linkNextEdge(e1);
					oldPrevEdge.linkNextEdge(e0.getOppositeEdge());
					e0.setTargetVertex(v1);
				}
			} else {
				if (e1.getIndex() >= numOldEdges) {
					// e0 is old, e1 is new
					E oldNextEdge = e0.getNextEdge();
					e0.linkNextEdge(e1);
					e1.getOppositeEdge().linkNextEdge(oldNextEdge);
					e1.getOppositeEdge().setTargetVertex(v1);
				} // else e0 and e1 are old: nothing to do
			}
		}
		fillHole(edgeCycle.get(0));
		return edgeCycle.get(0);
	}
	
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> E addTetrahedron(HalfEdgeDataStructure<V,E,F> heds) {
		V v0 = heds.addNewVertex();
		V v1 = heds.addNewVertex();
		V v2 = heds.addNewVertex();
		V v3 = heds.addNewVertex();
		E eResult = constructFaceByVertices(heds, v0, v1, v2);
		constructFaceByVertices(heds, v1, v0, v3);
		constructFaceByVertices(heds, v2, v1, v3);
		constructFaceByVertices(heds, v0, v2, v3);
		return eResult;
	}
	
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> E addCube(HalfEdgeDataStructure<V,E,F> heds) {
		V v0 = heds.addNewVertex();
		V v1 = heds.addNewVertex();
		V v2 = heds.addNewVertex();
		V v3 = heds.addNewVertex();
		V v4 = heds.addNewVertex();
		V v5 = heds.addNewVertex();
		V v6 = heds.addNewVertex();
		V v7 = heds.addNewVertex();
		E eResult = constructFaceByVertices(heds, v0, v1, v2, v3);
		constructFaceByVertices(heds, v0, v4, v5, v1);
		constructFaceByVertices(heds, v1, v5, v6, v2);
		constructFaceByVertices(heds, v2, v6, v7, v3);
		constructFaceByVertices(heds, v3, v7, v4, v0);
		constructFaceByVertices(heds, v7, v6, v5, v4);		
		return eResult;
	}
	
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> E addOctahedron(HalfEdgeDataStructure<V,E,F> heds) {
		V v0 = heds.addNewVertex();
		V v1 = heds.addNewVertex();
		V v2 = heds.addNewVertex();
		V v3 = heds.addNewVertex();
		V v4 = heds.addNewVertex();
		V v5 = heds.addNewVertex();
		E eResult = constructFaceByVertices(heds, v0, v1, v2);
		constructFaceByVertices(heds, v0, v2, v3);		
		constructFaceByVertices(heds, v0, v3, v4);		
		constructFaceByVertices(heds, v0, v4, v1);		
		constructFaceByVertices(heds, v2, v1, v5);		
		constructFaceByVertices(heds, v3, v2, v5);		
		constructFaceByVertices(heds, v4, v3, v5);		
		constructFaceByVertices(heds, v1, v4, v5);		
		return eResult;
	}
	
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> E addDodecahedron(HalfEdgeDataStructure<V,E,F> heds) {
		V v0 = heds.addNewVertex();
		V v1 = heds.addNewVertex();
		V v2 = heds.addNewVertex();
		V v3 = heds.addNewVertex();
		V v4 = heds.addNewVertex();
		V v5 = heds.addNewVertex();
		V v6 = heds.addNewVertex();
		V v7 = heds.addNewVertex();
		V v8 = heds.addNewVertex();
		V v9 = heds.addNewVertex();
		V v10 = heds.addNewVertex();
		V v11 = heds.addNewVertex();
		V v12 = heds.addNewVertex();
		V v13 = heds.addNewVertex();
		V v14 = heds.addNewVertex();
		V v15 = heds.addNewVertex();
		V v16 = heds.addNewVertex();
		V v17 = heds.addNewVertex();
		V v18 = heds.addNewVertex();
		V v19 = heds.addNewVertex();
		E eResult = constructFaceByVertices(heds, v0, v1, v2, v3, v4);
		constructFaceByVertices(heds, v0, v5, v10, v6, v1);
		constructFaceByVertices(heds, v1, v6, v11, v7, v2);
		constructFaceByVertices(heds, v2, v7, v12, v8, v3);
		constructFaceByVertices(heds, v3, v8, v13, v9, v4);
		constructFaceByVertices(heds, v4, v9, v14, v5, v0);
		constructFaceByVertices(heds, v14, v15, v16, v10, v5);
		constructFaceByVertices(heds, v10, v16, v17, v11, v6);
		constructFaceByVertices(heds, v11, v17, v18, v12, v7);
		constructFaceByVertices(heds, v12, v18, v19, v13, v8);
		constructFaceByVertices(heds, v13, v19, v15, v14, v9);
		constructFaceByVertices(heds, v19, v18, v17, v16, v15);
		return eResult;
	}
	
	static public <V extends Vertex<V,E,F>, E extends Edge<V,E,F>, F extends Face<V,E,F>> E addIcosahedron(HalfEdgeDataStructure<V,E,F> heds) {
		V v0 = heds.addNewVertex();		
		V v1 = heds.addNewVertex();		
		V v2 = heds.addNewVertex();		
		V v3 = heds.addNewVertex();		
		V v4 = heds.addNewVertex();		
		V v5 = heds.addNewVertex();		
		V v6 = heds.addNewVertex();		
		V v7 = heds.addNewVertex();		
		V v8 = heds.addNewVertex();		
		V v9 = heds.addNewVertex();		
		V v10 = heds.addNewVertex();		
		V v11 = heds.addNewVertex();
		E eResult = constructFaceByVertices(heds, v0, v1, v2);
		constructFaceByVertices(heds, v0, v2, v3);
		constructFaceByVertices(heds, v0, v3, v4);
		constructFaceByVertices(heds, v0, v4, v5);
		constructFaceByVertices(heds, v0, v5, v1);
		constructFaceByVertices(heds, v2, v1, v7);
		constructFaceByVertices(heds, v3, v2, v6);
		constructFaceByVertices(heds, v4, v3, v10);
		constructFaceByVertices(heds, v5, v4, v9);
		constructFaceByVertices(heds, v1, v5, v8);
		constructFaceByVertices(heds, v1, v8, v7);
		constructFaceByVertices(heds, v2, v7, v6);
		constructFaceByVertices(heds, v3, v6, v10);
		constructFaceByVertices(heds, v4, v10, v9);
		constructFaceByVertices(heds, v5, v9, v8);
		constructFaceByVertices(heds, v7, v8, v11);
		constructFaceByVertices(heds, v6, v7, v11);
		constructFaceByVertices(heds, v10, v6, v11);
		constructFaceByVertices(heds, v9, v10, v11);
		constructFaceByVertices(heds, v8, v9, v11);
		return eResult;
	}
	
	
	/**
	 * Calculates the genus of the 2-manifold represented
	 * by the given HalfedgeDataDtructure by evaluating
	 * X = hds.numVertices() - hds.numEdges() / 2 + hds.numFaces()
	 * r = number of boundary components
	 * g = (2 - X - r) / 2
	 * @param hds a 2-manifold
	 * @return g
	 */
	public static int getGenus(HalfEdgeDataStructure<?, ?, ?> hds) {
		int r = boundaryComponents(hds).size();
		int X = hds.numVertices() - hds.numEdges() / 2 + hds.numFaces();
		return (2 - X - r) / 2;
	}
	
	
	
	/**
	 * Returns true if the neighborhood of this vertex is homeomorphic
	 * either to R2 or to a half-space 
	 * @param v the vertex
	 * @return 
	 */
	public static <
		V extends Vertex<V,E,F>,
	    E extends Edge<V,E,F>,
	    F extends Face<V,E,F>
	> boolean isManifoldVertex(V v) {
		int bc = 0;
		for (E e : incomingEdges(v)) {
			if (e.getLeftFace() == null) {
				bc++;
			}
		}
		return bc <= 1;
	}
	
	
	/**
	 * Inserts the nodes of src into dst
	 * @param src
	 * @param dst
	 * @return The vertex offset of the new vertices in dst
	 */
	public static <
		V extends Vertex<V,E,F>,
	    E extends Edge<V,E,F>,
	    F extends Face<V,E,F>,
	    HDSSRC extends HalfEdgeDataStructure<V, E, F>,
		VV extends Vertex<VV,EE,FF>,
	    EE extends Edge<VV,EE,FF>,
	    FF extends Face<VV,EE,FF>,
	    HDSDST extends HalfEdgeDataStructure<VV, EE, FF>
	> int copy(HDSSRC src, HDSDST dst) {
		Set<E> srcEdges = new HashSet<E>(src.getEdges());
		int vOffset = dst.numVertices();
		int eOffset = dst.numEdges();
		int fOffset = dst.numFaces();
		dst.addNewVertices(src.numVertices());
		dst.addNewEdges(src.numEdges());
		dst.addNewFaces(src.numFaces());
		for (E e : srcEdges) {
			E eNext = e.getNextEdge();
			E eOpp = e.getOppositeEdge();
			F f = e.getLeftFace();
			V v = e.getTargetVertex();
			EE ee = dst.getEdge(eOffset + e.getIndex());
			ee.setIsPositive(e.isPositive());
			if (eNext != null) {
				ee.linkNextEdge(dst.getEdge(eOffset + eNext.getIndex()));
			}
			if (eOpp != null) {
				ee.linkOppositeEdge(dst.getEdge(eOffset + eOpp.getIndex()));
			}
			if (f != null) {
				ee.setLeftFace(dst.getFace(fOffset + f.getIndex()));
			}
			if (v != null) {
				ee.setTargetVertex(dst.getVertex(vOffset + v.getIndex()));
			}
		}
		return vOffset;
	}
	
}
