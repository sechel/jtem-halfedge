/**
 * <!-- teaser start -->
 * Provides the classes for a half-edge data structure. 
 * <!-- teaser end -->
 * 
 * <h3>Cell decompositions of surfaces</h3>
 * 
 * Half-edge data structures are used primarily to represent cell decompositions of oriented surfaces.
 * <p>
 * <small>We say "primarily" because half-edge data structures can be used to represent somewhat more 
 * general combinatorial structures, such as, for example, a checker board surface with white squares removed.
 * </small>
 * <p>
 * Here, <i>surface</i> means <i>two-dimensional manifold, possibly with boundary</i>; 
 * and a <i>cell decomposition of a surface</i> is a graph embedded in the surface such that the 
 * complement of the graph is (topologically) a disjoint union of open disks.
 * The term <i>map on a surface</i> means the same. 
 * Thus, a cell decomposition decomposes a surface into <i>vertices</i>, <i>edges</i>, and <i>faces</i>.
 * 
 * <h3>Regular and strongly regular</h3>
 * 
 * A cell decomposition of a surface is called <i>regular</i>, if it has no loops (edges with the same vertex 
 * on both ends), and if the boundary of a face contains an edge or vertex at most once. It is called
 * <i>strongly regular</i> if two edges have at most one vertex in common, and if two faces have at most 
 * one edge or one vertex in common. A strongly regular cell decomposition is usually called a <i>mesh</i>.
 * 
 * <h3>This half-edge data structure implementation</h3>
 * 
 * This half-edge data structure implementation consists of different types of objects representing vertices, 
 * half-edges, and faces.
 * The term <i>half-edge</i> can and should be thought of as synonymous with <i>oriented edge</i> 
 * or <i>directed edge</i>. 
 * <p>
 * Every half-edge object holds references to: <br>
 * <br>
 * &bull; its oppositely oriented companion edge<br>
 * &bull; the next edge in the boundary of the face on its left hand side<br>
 * &bull; the previous edge in the boundary of the face on its left hand side<br>
 * &bull; the face on its left hand side<br>
 * &bull; the vertex it points to.<br>
 * <br>
 * The face and vertex objects hold back references to a half-edge referencing them. 
 * 
 * <p>
 * Finally, there is the class {@link de.jtem.halfedge.HalfEdgeDataStructure} representing a whole half-edge 
 * data structure. It acts as a container for (and sort of factory of) its vertices, edges, and faces. 
 * 
 * <h3>Use of generics</h3>
 * 
 * Typically, one wants to equip vertices, edges, and faces with additional properties or functionality.
 * For example, vertices may have coordinates associated with them, edges may have weights, and
 * faces may have colors.
 * <p>
 * Our half-edge data structure facilitates this by using generic classes as abstract base classes 
 * for vertex, edge, and face types: The classes {@link de.jtem.halfedge.Vertex}, {@link de.jtem.halfedge.Edge}, 
 * {@link de.jtem.halfedge.Face} are all parameterized with the associated vertex, edge, and face types.
 * <p>
 * <b>Example.</b> To create a half-edge data structure with vertices that have 2D coordinates, proceed as follows.<br>
 * <br>
 * &bull; Step 1. Define appropriate subclasses of {@link de.jtem.halfedge.Vertex}, {@link de.jtem.halfedge.Edge}, 
 * {@link de.jtem.halfedge.Face}, for example: <br>
 * <br>
 * {@code public class MyVertex extends Vertex<MyVertex, MyEdge, MyFace> {public Point2D p;}}<br>
 * {@code public class MyEdge extends Edge<MyVertex, MyEdge, MyFace> {} }<br>
 * {@code public class MyFace extends Face<MyVertex, MyEdge, MyFace> {} }<br>
 * <br>
 * (Of course you might make the property <code>p</code> of <code>MyEdge</code> private and provide getter and setter methods, etc.)
 * Note that you always have to subclass  {@link de.jtem.halfedge.Vertex}, {@link de.jtem.halfedge.Edge}, 
 * and {@link de.jtem.halfedge.Face}, even if you do not define any additional functionality or properties. <br>
 * <br>	
 * &bull; Step 2. Instantiate a {@link de.jtem.halfedge.HalfEdgeDataStructure}:<br>
 * <br>
 * {@code HalfEdgeDataStructure<MyVertex,MyEdge,MyFace> heds = new  HalfEdgeDataStructure<MyVertex,MyEdge,MyFace>(MyVertex.class, MyEdge.class, MyFace.class);}<br>
 * <br> 
 * The parameters of the constructor serve as run time type tokens. <br>
 * <br>
 * &bull; Step 3. Instantiate vertices, edges, and faces using {@link de.jtem.halfedge.HalfEdgeDataStructure#addNewVertex() addNewVertex()}, 
 * {@link de.jtem.halfedge.HalfEdgeDataStructure#addNewEdge() addNewEdge()}, and {@link de.jtem.halfedge.HalfEdgeDataStructure#addNewFace() addNewFace()},
 * like this:<br>
 * <br>
 * {@code MyVertex v = heds.addNewVertex();}<br>
 * {@code MyEdge e = heds.addNewEdge();}<br>
 * {@code MyFace f = heds.addNewFace();}<br>
 */
package de.jtem.halfedge;