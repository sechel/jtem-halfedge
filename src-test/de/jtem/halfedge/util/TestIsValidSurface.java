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

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import de.jtem.halfedge.Edge;
import de.jtem.halfedge.Face;
import de.jtem.halfedge.HalfEdgeDataStructure;
import de.jtem.halfedge.Vertex;

public class TestIsValidSurface extends TestCase {

	// Further tests of isValidSurface are contained in TestBasicStuff.
	
	@Test
	public void testUniqueEdgeCycleForFaces() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> heds 
			= new HalfEdgeDataStructure<Vertex.Naked,Edge.Naked,Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);
		HalfEdgeUtils.addOctahedron(heds);
		HalfEdgeUtils.addDodecahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Face.Naked fx = heds.getFace(3);
		Face.Naked fy = heds.getFace(heds.numFaces()-5);
		List<Edge.Naked> boundaryOfFy = HalfEdgeUtils.boundaryEdges(fy);
		for (Edge.Naked e : boundaryOfFy) {
			e.setLeftFace(fx);
		}
		heds.removeFace(fy);
		assertFalse(HalfEdgeUtils.isValidSurface(heds));
		fy = heds.addNewFace();
		for (Edge.Naked e : boundaryOfFy) {
			e.setLeftFace(fy);
		}
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}
	
	@Test
	public void testUniqueEdgeCocycleForVertices() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> heds 
			= new HalfEdgeDataStructure<Vertex.Naked,Edge.Naked,Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);
		HalfEdgeUtils.addOctahedron(heds);
		HalfEdgeUtils.addDodecahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked vx = heds.getVertex(3);
		Vertex.Naked vy = heds.getVertex(heds.numVertices()-5);
		List<Edge.Naked> coBoundaryOfVy = HalfEdgeUtils.incomingEdges(vy);
		for (Edge.Naked e : coBoundaryOfVy) {
			e.setTargetVertex(vx);
		}
		heds.removeVertex(vy);
		assertFalse(HalfEdgeUtils.isValidSurface(heds));
		vy = heds.addNewVertex();
		for (Edge.Naked e : coBoundaryOfVy) {
			e.setTargetVertex(vy);
		}
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}
	
	@Test
	public void testFacesConnectedOnlyByOneVertex() {
		HalfEdgeDataStructure<Vertex.Naked, Edge.Naked, Face.Naked> heds 
			= new HalfEdgeDataStructure<Vertex.Naked,Edge.Naked,Face.Naked>(Vertex.Naked.class, Edge.Naked.class, Face.Naked.class);
		HalfEdgeUtils.addOctahedron(heds);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
		Vertex.Naked v2 = heds.getVertex(2);
		List<Edge.Naked> incoming = HalfEdgeUtils.incomingEdges(v2);
		assertTrue(incoming.size() == 4);
		Edge.Naked ex = incoming.get(0);
		Edge.Naked ey = incoming.get(2);
		Face.Naked fx = ex.getLeftFace();
		Face.Naked fy = ey.getLeftFace();
		heds.removeFace(fx);
		heds.removeFace(fy);
		assertFalse(HalfEdgeUtils.isValidSurface(heds));
		HalfEdgeUtils.fillHole(ex);
		assertTrue(HalfEdgeUtils.isValidSurface(heds));
	}

}
