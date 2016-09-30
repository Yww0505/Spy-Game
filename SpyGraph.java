
import java.util.*;

/**
 * Stores all vertexes as a list of GraphNodes. Provides necessary graph
 * operations as need by the SpyGame class.
 * 
 * @author wan
 *
 */
public class SpyGraph implements Iterable<GraphNode> {

	private List<GraphNode> vlist;

	/**
	 * Initializes an empty list of GraphNode objects
	 */
	public SpyGraph() {
		vlist = new ArrayList<GraphNode>();
	}

	/**
	 * Add a vertex with this label to the list of vertexes. No duplicate vertex
	 * names are allowed.
	 * 
	 * @param name
	 *            The name of the new GraphNode to create and add to the list.
	 */
	public void addGraphNode(String name) {
		if (name == null)
			throw new IllegalArgumentException();
		if (getNodeFromName(name) == null) {
			GraphNode newNode = new GraphNode(name);
			vlist.add(newNode);

		} else
			System.out.println("Duplicate nodes");
	}

	/**
	 * Adds v2 as a neighbor of v1 and adds v1 as a neighbor of v2. Also sets
	 * the cost for each neighbor pair.
	 * 
	 * @param v1name
	 *            The name of the first vertex of this edge
	 * @param v2name
	 *            The name of second vertex of this edge
	 * @param cost
	 *            The cost of traveling to this edge
	 * @throws IllegalArgumentException
	 *             if the names are the same
	 */
	public void addEdge(String v1name, String v2name, int cost) throws IllegalArgumentException {

		if (v1name == null || v2name == null || v1name.equals(v2name))
			throw new IllegalArgumentException();
		GraphNode v1 = getNodeFromName(v1name);
		GraphNode v2 = getNodeFromName(v2name);

		if (v1 != null && v2 != null && cost >= 0) {
			v1.addNeighbor(v2, cost);
			v2.addNeighbor(v1, cost);

		} else
			System.out.println("invalid request");
	}

	/**
	 * Return an iterator through all nodes in the SpyGraph
	 * 
	 * @return iterator through all nodes in alphabetical order.
	 */
	public Iterator<GraphNode> iterator() {
		return vlist.iterator();
	}

	/**
	 * Return Breadth First Search list of nodes on path from one Node to
	 * another.
	 * 
	 * @param start
	 *            First node in BFS traversal
	 * @param end
	 *            Last node (match node) in BFS traversal
	 * @return The BFS traversal from start to end node.
	 * 
	 * 
	 */

	public List<Neighbor> BFS(String start, String end) {
		
		if (start == null || end == null || start.equals(end))
			throw new IllegalArgumentException();
		GraphNode startNode = getNodeFromName(start);
		GraphNode endNode = getNodeFromName(end);
		if (startNode != null && endNode != null) {

			Queue<Neighbor> queue = new LinkedList<Neighbor>();
			// two corresponding arrayList to record successor and predecessor
			ArrayList<Neighbor> successor = new ArrayList<Neighbor>();
			ArrayList<Neighbor> predecessor = new ArrayList<Neighbor>();
			ArrayList<GraphNode> visited = new ArrayList<GraphNode>();
			Neighbor startNei = new Neighbor(0, startNode);
			visited.add(startNode);
			queue.add(startNei);
			boolean hasFound = false;
			while (!hasFound && !queue.isEmpty()) {

				Neighbor curr = queue.remove();

				for (Neighbor neighbor : curr.getNeighborNode().getNeighbors()) {
					String name = neighbor.getNeighborNode().getNodeName();
					// if found the destination
					if (name.equals(end)) {

						successor.add(neighbor);
						predecessor.add(curr);
						hasFound = true;
						break;
					}
					if (!visited.contains(neighbor.getNeighborNode())) {
						visited.add(neighbor.getNeighborNode());
						successor.add(neighbor);
						predecessor.add(curr);
						queue.add(neighbor);
					}
				}
			}
			if (hasFound) {
				ArrayList<Neighbor> list = new ArrayList<Neighbor>();
				int index = successor.size() - 1;
				// add items in successor to list accordingly
				while (index != -1) {
					list.add(0, successor.get(index));
					index = successor.indexOf(predecessor.get(index));
				}
				return list;
			}

		}
		System.out.println("invalid request, cannot found the path");
		return null;

	}

	/**
	 * @param name
	 *            Name corresponding to node to be returned
	 * @return GraphNode associated with name, null if no such node exists
	 */
	public GraphNode getNodeFromName(String name) {
		if (name == null)
			throw new IllegalArgumentException();
		for (GraphNode n : vlist) {
			if (n.getNodeName().equalsIgnoreCase(name))
				return n;
		}
		return null;
	}

	/**
	 * Return Depth First Search list of nodes on path from one Node to another.
	 * 
	 * @param start
	 *            First node in DFS traversal
	 * @param end
	 *            Last node (match node) in DFS traversal
	 * @return The DFS traversal from start to end node.
	 */

	public List<Neighbor> DFS(String start, String end) {
	
		if (start == null || end == null || start.equals(end))
			throw new IllegalArgumentException();
		GraphNode startNode = getNodeFromName(start);
		GraphNode endNode = getNodeFromName(end);
		if (startNode != null && endNode != null) {
			ArrayList<GraphNode> visited = new ArrayList<GraphNode>();
			visited.add(startNode);
			List<Neighbor> list = dfs(startNode, endNode, visited);
			return list;
		}
		System.out.println("invalid request");
		return null;
	}

	private List<Neighbor> dfs(GraphNode start, GraphNode end, ArrayList<GraphNode> visited) {

		for (Neighbor neighbor : start.getNeighbors()) {
			if (!visited.contains(neighbor.getNeighborNode())) {
				List<Neighbor> list = new ArrayList<Neighbor>();
				// if find the destination
				if (neighbor.getNeighborNode().equals(end)) {
					visited.add(end);
					list.add(neighbor);
					return list;
				}
				visited.add(neighbor.getNeighborNode());
				List<Neighbor> tmp = dfs(neighbor.getNeighborNode(), end, visited);
				// if tmp is not null, means destination is on this path
				if (tmp != null) {
					list.add(neighbor);
					list.addAll(tmp);
					return list;
				}

			}
		}
		// if for all neighbors there are neither destination nor unvisited
		// neighbors, return null indicating this path is useless
		return null;
	}

	/**
	 * 
	 * Return Dijkstra's shortest path list of nodes on path from one Node to
	 * another.
	 * 
	 * @param start
	 *            First node in path
	 * @param end
	 *            Last node (match node) in path
	 * @return The shortest cost path from start to end node.
	 */

	public List<Neighbor> Dijkstra(String start, String end) {

		ArrayList<Neighbor> shortest = new ArrayList<Neighbor>();
		ArrayList<Neighbor> visited = new ArrayList<Neighbor>();
		ArrayList<GraphNode> node = new ArrayList<GraphNode>();
		GraphNode startNode = getNodeFromName(start);
		GraphNode endNode = getNodeFromName(end);
		boolean found = false;
		startNode.dijPath = 0;
		
		Neighbor first = new Neighbor(0, startNode);
		first.setPrecessor(null);
		shortest.add(first);
		node.add(startNode);
		while (!found) {
			Neighbor curr = shortest.get(shortest.size() - 1);
			for (Neighbor nei : curr.getNeighborNode().getNeighbors()) {
				if(!node.contains(nei.getNeighborNode())){
					boolean duplicate = false;
					int tmp = nei.getCost();
					for (int i = 0; i < visited.size(); i++) {

						if (visited.get(i).getNeighborNode().equals(nei.getNeighborNode())) {
							duplicate = true;

							if (nei.getNeighborNode().dijPath > (tmp + curr.getNeighborNode().dijPath)) {
								nei.getNeighborNode().dijPath = tmp + curr.getNeighborNode().dijPath;
								nei.setPrecessor(curr);
								visited.remove(i);
								visited.add(nei);
							}

						}

					}
					if (!duplicate) {
						visited.add(nei);
						nei.getNeighborNode().dijPath = tmp + curr.getNeighborNode().dijPath;
						nei.setPrecessor(curr);
					}
				}
				
				
			}
			Neighbor min = visited.get(0);
			int minIndex = 0;
			for (int i = 1; i < visited.size(); i++) {
				if (visited.get(i).getNeighborNode().dijPath < min.getNeighborNode().dijPath) {
					min = visited.get(i);
					minIndex = i;
				}
			}
			shortest.add(min);
			visited.remove(minIndex);
			node.add(min.getNeighborNode());
			if(min.getNeighborNode().equals(endNode))
				found = true;

		}
		
		List<Neighbor> dijkstra = new ArrayList<Neighbor>();
		Neighbor curr = shortest.get(shortest.size()-1);
		
		while(curr!=null){
			dijkstra.add(0, curr);
			curr = curr.getPrecessor();
			
		}
		
		dijkstra.remove(0);
		return dijkstra;

		
	}

	
	public GraphNode getRandomNode() {
		if (vlist.size() <= 0) {
			System.out.println("Must have nodes in the graph before randomly choosing one.");
			return null;
		}
		int randomNodeIndex = Game.RNG.nextInt(vlist.size());
		return vlist.get(randomNodeIndex);
	}

}
