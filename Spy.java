import java.util.*;

/**
 * @author wan
 *
 */
public class Spy {
    private GraphNode location;
    private int move;
    private int reveal;
    
    /**
     * @param g Graph of current game
     * @param reveal - number of steps after which the spy reveals himself
     */
    public Spy(SpyGraph g, int reveal){
        this.location = g.getRandomNode();
        this.reveal = reveal;
        this.move = 0;
    }

    /**
     * Prints the location of the spy if specified by reveal
     * Return false if spy has no moves to make
     * @param playerLoc - current location of the player
     * @return true if spy moves.
     */
    public boolean move(String playerLoc){

        // FIND POSSIBLE MOVES
        List<GraphNode> possibleMoves = new ArrayList<GraphNode>();
        List<Neighbor> neighbors = location.getNeighbors();
        for ( Neighbor n : neighbors ) {
            GraphNode gn = n.getNeighborNode();
            // NO SPYCAM AND NO PLAYER HERE
            if ( ! gn.getSpycam() && ! gn.getNodeName().equals(playerLoc)) {
                possibleMoves.add(gn);
                //System.out.println("added node "+gn.getNodeName()+" to possible moves for spy");
            }    else {
               // System.out.println("not a neighbor of " + gn.getNodeName());
            }
        }
        
        if ( possibleMoves.size() <= 0 ) 
            return false;
                
        //move spy to a random neighbor that does not have player or spycam
        GraphNode newLoc = possibleMoves.get(Game.RNG.nextInt(possibleMoves.size()));
        
        //if the spy should reveal location, print location
        move = (move+1)%reveal;

        if (move==0) System.out.println("Spy's location is " + newLoc.getNodeName());
        location = newLoc;
        return true;
        
    }
    
    /**
     * @return location of spy
     */
    public String revealLocation() { 
        return location.getNodeName();
    }


    /**
     * @param locationName name of potential node
     * @return true if Spy is at location specified by location name
     */
    public boolean isAt(String locationName) {
        return location.getNodeName().equals(locationName);
    }
    
    /** @return the name of the node that the spy is at */
    public String getLocationName() {
        return location.getNodeName();
    }
    
    
}
