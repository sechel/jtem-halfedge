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

package de.jtem.halfedge;

/**
 * Abstract base class for classes representing edges of a 
 * {@link HalfEdgeDataStructure}.
 * 
 * Any subclass that is to be used with a {@link HalfEdgeDataStructure} 
 * must provide a default constructor accessible to {@link HalfEdgeDataStructure}.
 * 
 * <p>
 * The following invariants are maintained: 
 * <ul>
 * <li>
 * 	<code>this.{@link #getNextEdge()} == null || this == {@link #getNextEdge()}.{@link #getPreviousEdge()}</code>
 * </li>
 * <li>
 * 	<code>this.{@link #getPreviousEdge()} == null || this == {@link #getPreviousEdge()}.{@link #getNextEdge()}</code>
 * </li>
 * <li>
 * 	<code>this.{@link #getOppositeEdge()} == null || this == this.{@link #getOppositeEdge()}.{@link #getOppositeEdge()}</code>
 * </li>
 * <li>
 * 	<code>this.{@link #getOppositeEdge()} == null || this.{@link #isPositive()} != {@link #getOppositeEdge()}.{@link #isPositive()}</code>
 * </li>
 * </ul>
 * 
 * @author Stefan Sechelmann
 * @author Boris Springborn
 * 
 * @param <V> the corresponding vertex class
 * @param <E> the corresponding edge class
 * @param <F> the corresponding face class
 */
abstract public class Edge <V extends Vertex<V, E, F>, 
							E extends Edge<V, E, F>, 
							F extends Face<V, E, F>> extends Node<V, E, F> {

	private E nextEdge = null;
	private E previousEdge = null;
	private E oppositeEdge = null;
	private V targetVertex = null;
	private F leftFace = null;
	private boolean isPositive = false;
	private E self = null;
	
	public static class Naked extends Edge<Vertex.Naked, Edge.Naked, Face.Naked> {}; 
	
	protected Edge() {}
	
	final protected void setSelf(E self) {
		assert this == self;
		this.self = self;
	}
	
	
	/**
	 * Returns the face on the left of this edge. May return <code>null</code>.
	 * @return the left Face.
	 */
	final public F getLeftFace() {
		if (leftFace != null && leftFace.getHalfEdgeDataStructure() == null) {
			// face has been removed.
			leftFace = null;
		}
		assert leftFace == null || leftFace.getHalfEdgeDataStructure() == this.getHalfEdgeDataStructure();
		return leftFace;
	}

	/**
	 * Returns the face on the right of this edge. Returns null if the opposite edge is null. 
	 * Otherwise returns the left face of the opposite edge.
	 * @return the left Face.
	 */	
	final public F getRightFace(){
		E oe = getOppositeEdge();
		if (oe == null) {
			return null;
		}
		return oe.getLeftFace();
	}

	/**
	 * Sets the left face of this edge. The parameter may be null. Otherwise it must belong to the same 
	 * {@link HalfEdgeDataStructure} as this edge.
	 * 
	 * @param f the left face, may be null.
	 */
	final public void setLeftFace(F f) {
		checkHalfEdgeDataStructure(f);
		this.leftFace = f;
		if (leftFace != null) {
			assert this == self;
			leftFace.setBoundaryEdge(self);
		}
	}

	/**
	 * Returns the next edge of the left face (or boundary component) of this edge. 
	 * @return the next edge
	 */
	final public E getNextEdge() {
		assert nextEdge == null || (this == this.nextEdge.previousEdge 
				&& this.getHalfEdgeDataStructure() == nextEdge.getHalfEdgeDataStructure());
		return nextEdge;
	}

	/**
	 * Links this edge with a new next edge. The parameter may be null. Otherwise it must belong to the same 
	 * {@link HalfEdgeDataStructure} as this edge. 
	 * @param nextEdge the new next edge, may be null
	 */
	final public void linkNextEdge(E nextEdge) {
		checkHalfEdgeDataStructure(nextEdge);
		if (this.nextEdge == nextEdge) {
			return;
		}
		if (this.nextEdge != null) {
			assert this == this.nextEdge.previousEdge;
			this.nextEdge.previousEdge = null;
		}
		if (nextEdge != null) {
			if (nextEdge.previousEdge != null) {
				nextEdge.previousEdge.nextEdge = null;
			}
			assert this == self;
			nextEdge.previousEdge = self;
		}
		this.nextEdge = nextEdge;
	}
	
	/**
	 * Links this edge with a new previous edge. The parameter may be null. Otherwise it must belong to the same 
	 * {@link HalfEdgeDataStructure} as this edge. 
	 * @param previousEdge the new previous edge, may be null
	 */
	final public void linkPreviousEdge(E previousEdge) {
		checkHalfEdgeDataStructure(previousEdge);
		if (this.previousEdge == previousEdge) {
			return;
		}
		if (this.previousEdge != null) {
			assert this == this.previousEdge.nextEdge;
			this.previousEdge.nextEdge = null;
		}
		if (previousEdge != null) {
			if (previousEdge.nextEdge != null) {
				previousEdge.nextEdge.previousEdge = null;
			}
			assert this == self;
			previousEdge.nextEdge = self;
		}
		this.previousEdge = previousEdge;
	}
	
	/**
	 * Returns the opposite edge. 
	 * @return the opposite edge, may be null
	 */
	final public E getOppositeEdge() {
		assert oppositeEdge == null || (oppositeEdge != this 
										&& this == oppositeEdge.oppositeEdge
										&& this.getHalfEdgeDataStructure() == oppositeEdge.getHalfEdgeDataStructure());
		return oppositeEdge;
	}

	/**
	 * Set opposite edge, and if <code>oppositeEdge!=null</code>
	 * adjust value of <code>oppositeEdge</code>.{@link #isPositive()}.
	 * @param oppositeEdge may be null.
	 */
	final public void linkOppositeEdge(E oppositeEdge) {
		checkHalfEdgeDataStructure(oppositeEdge);
		if (this.oppositeEdge == oppositeEdge) {
			return;
		}
		if (this == oppositeEdge) {
			throw new RuntimeException("Opposite edge cannot be this edge.");
		}
		if (this.oppositeEdge != null) {
			this.oppositeEdge.oppositeEdge = null;
		}
		if (oppositeEdge != null) {
			if (oppositeEdge.oppositeEdge != null) {
				oppositeEdge.oppositeEdge.oppositeEdge = null;
			}
			assert this == self;
			oppositeEdge.oppositeEdge = self;
			oppositeEdge.isPositive = ! isPositive;
		}
		this.oppositeEdge = oppositeEdge;
	}

	/**
	 * Returns the previous edge of the left face (or boundary component) of this edge. 
	 * @return the previous edge
	 */	
	final public E getPreviousEdge() {
		assert previousEdge == null || (this == previousEdge.nextEdge
				&& this.getHalfEdgeDataStructure() == previousEdge.getHalfEdgeDataStructure());
		return previousEdge;
	}

	/**
	 * Returns the target vertex of this edge. May return <code>null</code>.
	 * @return the target vertex
	 */
	final public V getTargetVertex() {
		if (targetVertex != null && targetVertex.getHalfEdgeDataStructure() == null) {
			// vertex has been removed
			targetVertex = null;
			return targetVertex;
		}
		assert targetVertex == null || this.getHalfEdgeDataStructure() == targetVertex.getHalfEdgeDataStructure();
		return targetVertex;
	}


	/**
	 * Sets the target vertex of this edge. The parameter may be null. Otherwise it must belong to the same 
	 * {@link HalfEdgeDataStructure} as this edge.
	 * 
	 * @param v the target vertex
	 */
	final public void setTargetVertex(V v) {
		checkHalfEdgeDataStructure(v);
		this.targetVertex = v;
		if (targetVertex != null) {
			assert this == self;
			targetVertex.setIncomingEdge(self);
		}
	}
	
	
	/**
	 * Returns the start vertex of this edge. Returns null if the opposite edge is null. 
	 * Otherwise returns the target vertex of the opposite edge.
	 * @return the left Face.
	 */	
	final public V getStartVertex(){
		E oe = getOppositeEdge();
		if (oe == null) {
			return null;
		}
		return oe.getTargetVertex();
	}
	
	/**
	 * Is this edge positive? 
	 * @return true if this edge is positive
	 */
	final public boolean isPositive() {
		assert oppositeEdge == null || this.isPositive != oppositeEdge.isPositive;
		return isPositive;
	}

	/**
	 * Set value returned by {@link #isPositive()}, and if {@link #getOppositeEdge()} 
	 * is not <code>null</code> adjust value returned by {@link #getOppositeEdge()}.{@link #isPositive()}.  
	 * @param signature true for positive, false for negative.
	 */	
	final public void setIsPositive(boolean signature) {
		this.isPositive = signature;
		if (oppositeEdge != null) {
			oppositeEdge.isPositive = !signature;
		}
	}

	
	/**
	 * Copies the data fields of the given edge into this edge
	 * @param v
	 */
	public void copyData(E e) {
		
	}
	
}
