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

import junit.framework.TestCase;

import org.junit.Test;

import de.jtem.halfedge.util.HalfEdgeUtils;

public class TestHalfEdgeDataStructure  extends TestCase {
	
	public static class MyVertex extends Vertex<MyVertex,MyEdge,MyFace> {}
	public static class MyEdge extends Edge<MyVertex,MyEdge,MyFace> {}
	public static class MyFace extends Face<MyVertex,MyEdge,MyFace> {}

//	@Test
//	public void testHalfEdgeDataStructure() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testCreateCombinatoriallyEquivalentCopy() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			h1 = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);
		HalfEdgeUtils.addIcosahedron(h1);
		assertTrue("problem in HalfEdgeUtils.addIcosahedron", HalfEdgeUtils.isValidSurface(h1));
		HalfEdgeDataStructure<MyVertex,MyEdge,MyFace> h2 = h1.createCombinatoriallyEquivalentCopy(MyVertex.class, MyEdge.class, MyFace.class);
		assertTrue(HalfEdgeUtils.isValidSurface(h2));
		for (Edge<?,?,?> e1 : h1.getEdges()) {
			Edge<?,?,?> e2 = h2.getEdge(e1.getIndex());
			assertTrue(e1.isPositive() == e2.isPositive());
		}
	}

//	@Test
//	public void testGetEdgeClass() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFaceClass() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetVertexClass() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNewVertex() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNewVertices() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNewEdge() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNewEdges() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNewFace() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNewFaces() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveFace() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveEdge() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveVertex() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetVertex() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetEdge() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFace() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testNumFaces() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testNumEdges() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testNumVertices() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFaces() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetVertices() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetEdges() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPositiveEdges() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetNegativeEdges() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testToString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsValidSurface() {
//		fail("Not yet implemented");
//	}

}
