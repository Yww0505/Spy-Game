
import java.util.*;
/**
 * The player class defines a player object which collects information
 * from the user and take corresponding operations
 * @author Wan
 *
 */
public class Player {
	private String name;
	private int budget;
	private int spycams;
	private GraphNode currNode;
	private ArrayList<String> spycamNames;

	public Player(String name, int budget,int spycams,GraphNode startnode){
		this.name = name;
		this.budget = budget;
		this.spycams = spycams;
		this.currNode = startnode;
		spycamNames = new ArrayList<String>();
	}
	/**
	 * get the name of the player
	 * @return name of player
	 */
	public String getName(){
		return name;
	}
	/**
	 * get the current budget
	 * @return remaining budget
	 */
	public int getBudget(){
		return budget;
	}
	/**
	 * decrease the budget is the parameter is larger than one
	 * @param dec
	 */
	public void decreaseBudget(int dec){
		if(dec > 1 ){
			this.budget = budget - dec;
		}

	}
	/**
	 * If there are no remaining spycams to drop, display "Not enough spycams" 
	 * and return false. 
	 * Otherwise: If there is not a spycam at the player's current location: 
	 * drop a spy cam (here) D decrement the remaining spycam count 
	 * if there was not already a spycam
	 * @return true if a spycam is dropped
	 */
	public boolean dropSpycam(){
		if(spycams == 0){
			System.out.println("Not enough spycams");
			return false;
		}
		if(!currNode.getSpycam()){
			currNode.setSpycam(true);
			spycams --;
			spycamNames.add(currNode.getNodeName());
			System.out.println("Dropped a Spy Cam at "+ currNode.getNodeName());
			return true;
		}
		else{
			System.out.println("Already a Spy Cam there");
		}

		return false;
	}
	/**
	 * Check the node to see if there is a spy cam. 
	 * If there is a spy cam at that node, remove the spy cam from that node. 
	 * Also, remove the spy cam name from the Player's list of spy cam names. 
	 * Otherwise, return false.
	 * @param node- The node the player asked to remove a spy cam from.
	 * @return true if a spycam is retrieved
	 */
	public boolean pickupSpycam(GraphNode node){
		if(node.getSpycam()){
			node.setSpycam(false);
			for(int i=0; i< spycamNames.size(); i++){
				if(spycamNames.get(i).equals(node.getNodeName())){
					spycamNames.remove(i);
					break;
				}
			}
			getSpycamBack(true);
		}
		return false;
	}
	/**
	 * 
	 * @return number of spycams remaining
	 */
	public int getSpycams(){
		return spycams;
	}
	/**
	 * 
	 * @param name- - Neighboring node to move to
	 * @return true if the player successfully moves to this node 
	 * if the cost is greater than 1, decrement budget by that amount
	 */
	public boolean move(String name){
		try{
			// if the cost is less than or equal to one, do nothing to the budget
			if(currNode.getCostTo(name)<=1){
				currNode = currNode.getNeighbor(name);
				return true;
			}
			// else, decrease the budget
			if(currNode.getCostTo(name)>1){
				if((budget - currNode.getCostTo(name))>=0){
					budget = budget - currNode.getCostTo(name);
					currNode = currNode.getNeighbor(name);
					return true;
				}

				else System.out.println("Not enough money cost is "+
						currNode.getCostTo(name)+ " budget is " + budget);
			}

		}
		catch(NotNeighborException e){
			System.out.println(name + " is not a neighbor of your current location");
		}
		return false;
	}
	/**
	 * 
	 * @return node lable for the current location of the player
	 */
	public String getLocationName(){
		return currNode.getNodeName();
	}
	/**
	 * Returns the node where the player is currently located.
	 * @return player's current node
	 */
	public GraphNode getLocation(){
		return currNode;
	}
	/**
	 * If pickupSpyCam is true, increment the number of spy cams remaining.
	 * @param pickupSpyCam- true if a spy cam was picked up. 
	 * False means there was no spy cam
	 */
	public void getSpycamBack(boolean pickupSpyCam){
		if(pickupSpyCam == true)
			spycams ++;

	}
	/**
	 * Display the names of the locations where Spy Cams were dropped 
	 * (and are still there).
	 */
	public void printSpyCamLocations(){

		for(int i =0; i<spycamNames.size(); i++){
			System.out.println("Spy cam at "+ spycamNames.get(i));
		}
	}
}







