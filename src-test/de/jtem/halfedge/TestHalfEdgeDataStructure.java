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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import de.jtem.halfedge.util.HalfEdgeUtils;

public class TestHalfEdgeDataStructure  extends TestCase {
	
	
	public static class MyHDS extends HalfEdgeDataStructure<MyVertex,MyEdge,MyFace> {
		public MyHDS() {
			super(MyVertex.class, MyEdge.class, MyFace.class);
		}
	}
	public static class MyVertex extends Vertex<MyVertex,MyEdge,MyFace> {}
	public static class MyEdge extends Edge<MyVertex,MyEdge,MyFace> {}
	public static class MyFace extends Face<MyVertex,MyEdge,MyFace> {}

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

	@Test
	public void testReindexingVerticesGetIndex() throws Exception {
		MyHDS hds = new MyHDS();
		hds.addNewVertices(10);
		MyVertex v6 = hds.getVertex(6);
		hds.removeVertex(hds.getVertex(5));
		Assert.assertEquals(6, v6.index);
		Assert.assertEquals(5, v6.getIndex());
	}
	
	@Test
	public void testReindexingEdgesGetIndex() throws Exception {
		MyHDS hds = new MyHDS();
		hds.addNewEdges(10);
		MyEdge e6 = hds.getEdge(6);
		hds.removeEdge(hds.getEdge(5));
		Assert.assertEquals(6, e6.index);
		Assert.assertEquals(5, e6.getIndex());
	}
	
	@Test
	public void testReindexingFacesGetIndex() throws Exception {
		MyHDS hds = new MyHDS();
		hds.addNewFaces(10);
		MyFace f6 = hds.getFace(6);
		hds.removeFace(hds.getFace(5));
		Assert.assertEquals(6, f6.index);
		Assert.assertEquals(5, f6.getIndex());
	}
	
	@Test
	public void testReindexingVerticesGetVertex() throws Exception {
		MyHDS hds = new MyHDS();
		hds.addNewVertices(10);
		MyVertex v6 = hds.getVertex(6);
		hds.removeVertex(hds.getVertex(5));
		Assert.assertNotSame(hds.vertexList.get(6), v6);
		Assert.assertSame(hds.vertexList.get(5), v6);
		Assert.assertEquals(6, v6.index);
		Assert.assertEquals(5, hds.getVertex(5).index);
	}
	
	@Test
	public void testReindexingEdgesGetEdge() throws Exception {
		MyHDS hds = new MyHDS();
		hds.addNewEdges(10);
		MyEdge e6 = hds.getEdge(6);
		hds.removeEdge(hds.getEdge(5));
		Assert.assertNotSame(hds.edgeList.get(6), e6);
		Assert.assertSame(hds.edgeList.get(5), e6);
		Assert.assertEquals(6, e6.index);
		Assert.assertEquals(5, hds.getEdge(5).index);
	}
	
	@Test
	public void testReindexingFacesGetFace() throws Exception {
		MyHDS hds = new MyHDS();
		hds.addNewFaces(10);
		MyFace f6 = hds.getFace(6);
		hds.removeFace(hds.getFace(5));
		Assert.assertNotSame(hds.faceList.get(6), f6);
		Assert.assertSame(hds.faceList.get(5), f6);
		Assert.assertEquals(6, f6.index);
		Assert.assertEquals(5, hds.getFace(5).index);
	}

}
