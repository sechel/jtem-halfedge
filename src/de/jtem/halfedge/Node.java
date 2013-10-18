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
 * The abstract base class for any vertex, edge and face class.
 * 
 * @author Stefan Sechelmann
 * @author Boris Springborn
 */
abstract public class Node <
	V extends Vertex<V, E, F>,
	E extends Edge<V, E, F>,
	F extends Face<V, E, F>	   
> implements Comparable<Node<V, E, F>> {

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
	
	@Override
	public int compareTo(Node<V, E, F> o) {
		return getIndex() - o.getIndex();
	}

}
