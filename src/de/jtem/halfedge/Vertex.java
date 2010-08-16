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
	
	/**
	 * Copies the data fields of the given vertex into this vertex
	 * @param v
	 */
	public void copyData(V v) {
		
	}
	
}
