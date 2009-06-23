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
