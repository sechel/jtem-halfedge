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

	final public void setBoundaryEdge(E e) {
		checkHalfEdgeDataStructure(e);
		if (!hds.getEdges().contains(e))
			throw new IllegalStateException("Not an edge of this face");
		this.boundaryEdge = e;
	};
	
	
	/**
	 * Copies the data fields of the given face into this face
	 * @param f
	 */
	public void copyData(F f) {
		
	}
		
}
