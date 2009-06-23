package de.jtem.halfedge.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
