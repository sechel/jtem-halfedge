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
