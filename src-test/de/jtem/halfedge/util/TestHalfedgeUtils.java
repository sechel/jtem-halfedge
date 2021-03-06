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

import static de.jtem.halfedge.util.HalfEdgeUtils.addCube;
import static de.jtem.halfedge.util.HalfEdgeUtils.addDodecahedron;
import static de.jtem.halfedge.util.HalfEdgeUtils.addIcosahedron;
import static de.jtem.halfedge.util.HalfEdgeUtils.addNGon;
import static de.jtem.halfedge.util.HalfEdgeUtils.addOctahedron;
import static de.jtem.halfedge.util.HalfEdgeUtils.addTetrahedron;
import static de.jtem.halfedge.util.HalfEdgeUtils.boundaryEdges;
import static de.jtem.halfedge.util.HalfEdgeUtils.constructFaceByVertices;
import static de.jtem.halfedge.util.HalfEdgeUtils.fillAllHoles;
import static de.jtem.halfedge.util.HalfEdgeUtils.fillHole;
import static de.jtem.halfedge.util.HalfEdgeUtils.incomingEdges;
import junit.framework.TestCase;

import org.junit.Test;

import de.jtem.halfedge.Edge;
import de.jtem.halfedge.Face;
import de.jtem.halfedge.HalfEdgeDataStructure;
import de.jtem.halfedge.Vertex;

public class TestHalfedgeUtils  extends TestCase{

//	@Test
//	public void testNeighboringVertices() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testConstructFaceByVertices() {
		// Build a 3x3 square grid by constructing the faces in a weird but permissible order.
        // The 16 vertex variables v0 ... v15 are numbered according to this scheme:
		// 12__3__14_15
		//  |  |  |  |
		//  8__9__10_11
		//  |  |  |  |
		//  4__5__6__7
		//	|  |  |  |
		//  0__1__2__3   
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
		   heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);
		Vertex.Naked v4 = heds.addNewVertex();
		Vertex.Naked v0 = heds.addNewVertex();
		Vertex.Naked v1 = heds.addNewVertex();
		Vertex.Naked v5 = heds.addNewVertex();
		constructFaceByVertices(heds, v4, v0, v1, v5);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked v2 = heds.addNewVertex();
		Vertex.Naked v6 = heds.addNewVertex();
		constructFaceByVertices(heds, v1, v2, v6, v5);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked v9 = heds.addNewVertex();
		Vertex.Naked v13 = heds.addNewVertex();
		Vertex.Naked v12 = heds.addNewVertex();		
		Vertex.Naked v8 = heds.addNewVertex();
		constructFaceByVertices(heds, v9, v13, v12, v8);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		constructFaceByVertices(heds, v4, v5, v9, v8);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked v10 = heds.addNewVertex();
		Vertex.Naked v14 = heds.addNewVertex();
		constructFaceByVertices(heds, v9, v10, v14, v13);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked v7 = heds.addNewVertex();
		Vertex.Naked v3 = heds.addNewVertex();
		constructFaceByVertices(heds, v7, v6, v2, v3);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked v11 = heds.addNewVertex();
		Vertex.Naked v15 = heds.addNewVertex();
		constructFaceByVertices(heds, v10, v11, v15, v14);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		constructFaceByVertices(heds, v6, v7, v11, v10);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		constructFaceByVertices(heds, v10, v9, v5, v6);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		// Finally, add one more face to close the surface.
		constructFaceByVertices(heds, v2, v1,v0,v4,v8,v12,v13,v14,v15,v11,v7,v3);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}
	
	@Test
	public void testAddNewNGonAndFillHole() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);
		final int n = 7;
		addNGon(heds, n);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		assertTrue(heds.numFaces() == 1);
		assertTrue(heds.numEdges() == 2 * n);
		assertTrue(heds.numVertices() == n);
		Face.Naked f0 = heds.getFace(0);
		Edge.Naked e = f0.getBoundaryEdge();
		e = e.getOppositeEdge();
		Face.Naked f1 = fillHole(e);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		assertTrue(heds.numFaces() == 2);
		assertTrue(heds.numEdges() == 2 * n);
		assertTrue(heds.numVertices() == n);
		// While we're at it, let's test Face's getIndex method.
		heds.removeFace(f0);
		assertTrue(f1.getIndex() == 0);
		heds.removeFace(f1);
		assertFalse(HalfEdgeUtils.isValidSurface(heds));
		fillHole(e);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}
	
	@Test
	public void testAddTetrahedron() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addTetrahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
//		System.err.println(heds.numVertices());
		assertTrue(heds.numVertices() == 4);
		assertTrue(heds.numEdges() == 12);
		assertTrue(heds.numFaces() == 4);
		for (Face.Naked f : heds.getFaces()) {
			assertTrue(boundaryEdges(f).size() == 3);
		}
		for (Vertex.Naked v : heds.getVertices()) {
			assertTrue(incomingEdges(v).size() == 3);
		}
	}

	@Test
	public void testAddCube() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addCube(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		assertTrue(heds.numVertices() == 8);
		assertTrue(heds.numEdges() == 24);
		assertTrue(heds.numFaces() == 6);
		for (Face.Naked f : heds.getFaces()) {
			assertTrue(boundaryEdges(f).size() == 4);
		}
		for (Vertex.Naked v : heds.getVertices()) {
			assertTrue(incomingEdges(v).size() == 3);
		}
	}
	@Test
	public void testAddOctahedron() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addOctahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		assertTrue(heds.numVertices() == 6);
		assertTrue(heds.numEdges() == 24);
		assertTrue(heds.numFaces() == 8);
		for (Face.Naked f : heds.getFaces()) {
			assertTrue(boundaryEdges(f).size() == 3);
		}
		for (Vertex.Naked v : heds.getVertices()) {
			assertTrue(incomingEdges(v).size() == 4);
		}
	}
	@Test
	public void testAddDodecahedron() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addDodecahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		assertTrue(heds.numVertices() == 20);
		assertTrue(heds.numEdges() == 60);
		assertTrue(heds.numFaces() == 12);
		for (Face.Naked f : heds.getFaces()) {
			assertTrue(boundaryEdges(f).size() == 5);
		}
		for (Vertex.Naked v : heds.getVertices()) {
			assertTrue(incomingEdges(v).size() == 3);
		}
	}
	@Test
	public void testAddIcosahedron() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addIcosahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		assertTrue(heds.numVertices() == 12);
		assertTrue(heds.numEdges() == 60);
		assertTrue(heds.numFaces() == 20);
		for (Face.Naked f : heds.getFaces()) {
			assertTrue(boundaryEdges(f).size() == 3);
		}
		for (Vertex.Naked v : heds.getVertices()) {
			assertTrue(incomingEdges(v).size() == 5);
		}
	}
	
	@Test
	public void testSeveralAddMethodsTogether() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
			heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addNGon(heds, 8);
		addTetrahedron(heds);
		addCube(heds);
		addOctahedron(heds);
		addDodecahedron(heds);
		addIcosahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}
	
	@Test
	public void testFillHoles() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> 
		heds = new HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);	
		addIcosahedron(heds);
		while (heds.numFaces() > 0) {
			heds.removeFace(heds.getFace(0));
		}
		assertFalse(HalfEdgeUtils.isValidSurface(heds));
		fillAllHoles(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}


}
