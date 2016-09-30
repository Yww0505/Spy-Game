
import java.util.*;
/**
 * This class is to define a GraphNode object
 * @author Wan
 *
 */
public class GraphNode implements java.lang.Comparable<GraphNode>{
	/*
	 * A value that can be used if a cost is needed even if the 
	 * GraphNode is not a neighbor of a node.
	 */
	public int dijPath = 0;
	private String name;
	private boolean spycam;
	private ArrayList<Neighbor> neighborList;
	private ArrayList<String> neighborNames;
	/**
	 * Represents a valid location in the game graph.
	 * There can be a player, a spycam, or a spy at any graph node.
	 * @param name - The label that uniquely identifies this graph node.
	 */
	public GraphNode(String name){
		this.name = name;
		neighborList = new ArrayList<Neighbor>();
		neighborNames = new ArrayList<String>(); 
		spycam =false;
	}
	/**
	 * Return the name of this GraphNode.
	 * @return name of node
	 */
	public String getNodeName(){
		return name;
	}

	/**
	 * Returns a list of the neighbors of this GraphNode instance. 
	 * This instance of GraphNode is not included as it is not a neighbor of itself.
	 * @return a list of neighbors of this GraphNode.
	 */
	public List<Neighbor> getNeighbors(){
		return neighborList;
	}

	/**
	 * Returns true if this node name is a neighbor of current node.
	 * @param neighborName - neighbor to look for
	 * @return true if the node is an adjacent neighbor.
	 */
	public boolean isNeighbor(String neighborName){
		if(neighborName == null) throw new IllegalArgumentException();
		for(int i = 0; i < neighborNames.size(); i++){
			if(neighborNames.get(i).equals(neighborName))
				return true;
		}
		return false;
	}
	/**
	 * Maintains sorted order of neighbors by neighbor name.
	 * @param neighbor- An adjacent node (a neighbor)
	 * @param cost- The cost to move to that node (from this node)
	 */
	public void addNeighbor(GraphNode neighbor,int cost){


		if(neighbor == null) throw new IllegalArgumentException();

		Neighbor newNeighbor = new Neighbor(cost, neighbor);
		if(neighborList.size() ==0){
			neighborList.add(newNeighbor);
			neighborNames.add(neighbor.getNodeName());
			return;
		}
		boolean hasInserted = false;
		for(int i=0; i< neighborList.size(); i++){

			if(neighbor.compareTo(neighborList.get(i).getNeighborNode())<0){

				neighborList.add(i, newNeighbor);
				neighborNames.add(i,neighbor.getNodeName());
				hasInserted = true;
				break;
			}
		}
		if(!hasInserted){
			neighborList.add(newNeighbor);
			neighborNames.add(neighbor.getNodeName());
		}
	}
	/**
	 * Returns and iterator that can be used to find neighbors of this GraphNode.
	 * @return An iterator of String node labels
	 */
	public Iterator<String> getNeighborNames(){

		return neighborNames.iterator();
	}
	/**
	 * 
	 * @return true if the GraphNode has a spycam
	 */
	public boolean getSpycam(){
		return spycam;
	}
	/**
	 * 
	 * @param cam- indicates whether the node now has a spycam
	 */
	public void setSpycam(boolean cam){
		spycam = cam;
	}
	/**
	 * 
	 * @param neighborName- name of potential neighbor
	 * @return cost to neighborName
	 * @throws NotNeighborException- if neighborName is not a neighbor
	 */
	public int getCostTo(String neighborName)throws NotNeighborException{
		if(neighborName == null) throw new IllegalArgumentException();
		for(int i =0; i< neighborList.size();i++){
			if(neighborName.equals(neighborList.get(i).getNeighborNode().getNodeName())){
				return neighborList.get(i).getCost();

			}
		}
		throw new NotNeighborException();
	}
	/**
	 * 
	 * @param name- name of potential neighbor
	 * @return the GraphNode associated with name that is a neighbor of the current node
	 * @throws NotNeighborException- if name is not a neighbor of the GraphNode
	 */
	public GraphNode getNeighbor(String name)throws NotNeighborException{
		if(name == null) throw new IllegalArgumentException();
		for(int i =0; i< neighborList.size();i++){
			if(name.equals(neighborList.get(i).getNeighborNode().getNodeName())){
				return neighborList.get(i).getNeighborNode();

			}
		}
		throw new NotNeighborException();
	}
	/**
	 * Prints a list of neighbors of this GraphNode and the cost of the edge to them
	 1 b
	 4 c
	 20 d
	 1 f
	 */
	public void displayCostToEachNeighbor(){
		for(int i =0; i< neighborList.size();i++){
			System.out.println(neighborList.get(i).getCost() + " "
					+ neighborList.get(i).getNeighborNode().getNodeName());
		}
	}

	/**
	 * Return the results of comparing this node's name to the other node's name. 
	 * Allows collections of nodes to be sorted using the built-in sort methods.
	 *Specified by:
	 *compareTo in interface java.lang.Comparable<GraphNode>
	 * @param otherNode- Another node to compare names with this node.
	 * @return the result of compareTo on the node names only.
	 */
	public int compareTo(GraphNode otherNode){
		return this.name.compareTo(otherNode.getNodeName());
	}

	/**
	 * Overrides: toString in class java.lang.Object
	 * @return name of node
	 */
	public String toString(){
		return this.name;
	}


	/**
	 * Display's the node name followed by a list of neighbors to this node. Example:
	 1 b
	 4 c
	 20 d
	 1 f
	 */
	public void printNeighborNames(){
		for(int i =0; i< neighborList.size();i++){
			System.out.println(neighborList.get(i).getCost() + " "
					+ neighborList.get(i).getNeighborNode().getNodeName());
		}
	}

}
