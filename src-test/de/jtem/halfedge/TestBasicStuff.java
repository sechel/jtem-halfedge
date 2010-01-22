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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import de.jtem.halfedge.util.HalfEdgeUtils;

public class TestBasicStuff  extends TestCase {
	
	static public class MyVertex extends Vertex<MyVertex,MyEdge,MyFace> {}

	static public class MyEdge extends Edge<MyVertex,MyEdge,MyFace> {}
	
	static public class MyFace extends Face<MyVertex,MyEdge,MyFace> {}

	@Test
	public void testsInvolvingConstructionOfQuadrilateralFromScratch() {
		HalfEdgeDataStructure<MyVertex, MyEdge, MyFace> heds = 
			new HalfEdgeDataStructure<MyVertex,MyEdge,MyFace>(MyVertex.class,MyEdge.class,MyFace.class);
//		for (int i = 0; i < 8; i++) {
//			heds.addNewEdge();
//		}
		int n = heds.addNewEdges(8).size();
		assertTrue(n == 8);
//		for (int i = 0; i < 4; i++) {
//			heds.addNewVertex();
//		}
		n = heds.addNewVertices(4).size();
		assertTrue(n == 4);
		heds.addNewFace();
		heds.getEdge(0).linkOppositeEdge(heds.getEdge(4));
		heds.getEdge(1).linkOppositeEdge(heds.getEdge(5));
		heds.getEdge(2).linkOppositeEdge(heds.getEdge(6));
		heds.getEdge(3).linkOppositeEdge(heds.getEdge(7));

		heds.getEdge(0).linkNextEdge(heds.getEdge(1));
		heds.getEdge(1).linkNextEdge(heds.getEdge(2));
		heds.getEdge(2).linkNextEdge(heds.getEdge(3));
		heds.getEdge(3).linkNextEdge(heds.getEdge(0));

		heds.getEdge(4).linkNextEdge(heds.getEdge(7));
		heds.getEdge(5).linkNextEdge(heds.getEdge(4));
		heds.getEdge(6).linkNextEdge(heds.getEdge(5));
		heds.getEdge(7).linkNextEdge(heds.getEdge(6));

		heds.getEdge(0).setLeftFace(heds.getFace(0));
		heds.getEdge(1).setLeftFace(heds.getFace(0));
		heds.getEdge(2).setLeftFace(heds.getFace(0));
		heds.getEdge(3).setLeftFace(heds.getFace(0));

		heds.getEdge(0).setTargetVertex(heds.getVertex(0));
		heds.getEdge(1).setTargetVertex(heds.getVertex(1));
		heds.getEdge(2).setTargetVertex(heds.getVertex(2));
		heds.getEdge(3).setTargetVertex(heds.getVertex(3));
		heds.getEdge(4).setTargetVertex(heds.getVertex(3));
		heds.getEdge(5).setTargetVertex(heds.getVertex(0));
		heds.getEdge(6).setTargetVertex(heds.getVertex(1));
		heds.getEdge(7).setTargetVertex(heds.getVertex(2));
		
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		
		List<MyEdge> el = HalfEdgeUtils.boundaryEdges(heds.getFace(0)); 
		for (int i = 0; i < 4; i++) {
			assertTrue(el.contains(heds.getEdge(i)));
		}
		for (int i = 4; i < 7; i++) {
			assertFalse(el.contains(heds.getEdge(i)));
		}
		
		assertFalse(HalfEdgeUtils.isInteriorFace(heds.getFace(0)));
		
		heds.addNewFace();
		assertFalse(HalfEdgeUtils.isValidSurface(heds));
	
//		System.err.println(heds);

		for (Face<MyVertex,MyEdge,MyFace> f : heds.getFaces()) {
//			System.err.println(f);
			assertTrue(f == heds.getFace(f.getIndex()));
		}
		for (Edge<MyVertex,MyEdge,MyFace> e : heds.getEdges()) {
//			System.err.println(e);
			assertTrue(e == heds.getEdge(e.getIndex()));
		}
		for (Vertex<MyVertex,MyEdge,MyFace> v : heds.getVertices()) {
//			System.err.println(v);
			assertTrue(v == heds.getVertex(v.getIndex()));
		}
		
		
		MyFace f1 = heds.getFace(1);
		heds.removeFace(f1);
//		System.err.println(f1);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		
//		System.err.println(heds);
		
	}

}
