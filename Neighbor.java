
/**
 * The class Neighbor represents a neighbor object and its characters
 * @author Wan
 *
 */
public class Neighbor {

	/**
	 * A neighbor is added to an existing GraphNode by 
	 * creating an instance of Neighbor that stores the neighbor 
	 * and the cost to reach that neighbor.
	 * @param cost- The cost to reach this neighbor.
	 * @param neighbor- The neighbor node being reached by this edge.
	 */
	private int cost;
	private GraphNode neighbor;
	private Neighbor precessor;
	/**
	 * constructor of neighbor, initialize a new neighbor object
	 * A neighbor is added to an existing GraphNode by creating an instance of 
	 * Neighbor that stores the neighbor and the cost to reach that neighbor.
	 * @param cost
	 * @param neighbor
	 */
	public Neighbor(int cost,GraphNode neighbor){
		this.cost = cost;
		this.neighbor = neighbor;
		precessor = null;
	}
	/**
	 * Returns the cost of traveling this edge to get to the Neighbor 
	 * at the other end of this edge.
	 * @return the cost of the edge to get to this neighbor
	 */
	public int getCost(){
		return cost;
	}
	/**
	 * Returns the Neighbor (node) that is at the other end of "this" node's edge.
	 * @return the neighbor node itself.
	 */
	public GraphNode getNeighborNode(){
		return neighbor;
	}
	public void setPrecessor(Neighbor nei){
		precessor = nei;
	}
	public Neighbor getPrecessor(){
		return precessor;
	}
	/**
	 * Compares the node names of this node and the otherNode.
	 * Returns the results of comparing this node's name to the otherNode's name.
	 * Allows Lists of Neighbors to be sorted using built-in sort methods.
	 * Specified by: compareTo in interface java.lang.Comparable<Neighbor>
	 * @param otherNode- neighbor to be compared
	 * @return compareTo the node names of two neighbors
	 */
	public int compareTo(Neighbor otherNode){
		return this.neighbor.compareTo(otherNode.getNeighborNode());
	}
	/**
	 * Returns a String representation of this Neighbor. 
	 * The String that is returned shows an arrow (with the cost in the middle) and then the Neighbor node's name. 
	 * Example:--1--> b 
	 *indicates a cost of 1 to get to node b
	 *@return a String with the cost and destination node:
	 */
	public String toString(){
		return ("--" + cost + "--> " + neighbor.getNodeName());
	}


}
