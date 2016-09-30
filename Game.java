
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Game {

    
    public static final java.util.Random RNG = new java.util.Random(2); // seeded for deterministic behavior

    // One Scanner connected to keyboard input
    private static final Scanner scan = new Scanner(System.in);

    // Instance Fields
    private SpyGraph spyGraph; // until details read from file in main
    private Spy spy;
    private Player player;

    // INITIALIZED FROM CONFIG FILE
    private String unit = "dollar";
    private int reveal = 5;
    private int numsteps = 100;
    private int budget = 100;
    private int spycams = 5;
    private int check = 5;

    public void init(String configFilename, String areaFilename)  throws InvalidAreaFileException{
        
        //create a new graph from the area file
        spyGraph = createGraphFromAreaFile(areaFilename);

        //set configuration parameters from the config file
        readConfigFile(configFilename);    

        //create a spy and "hide" the spy
        spy = new Spy(spyGraph, reveal); 

        //get player name and create a player at a random node -- will always choose same node
        System.out.println("Enter the name of your player: ");
        String name = scan.nextLine();
        player = new Player(name, budget, spycams, spyGraph.getRandomNode()); 


    }

    /**
     * @param args - args[0] is a file containing various arguments, args[1] contains the graph
     * @throws FileNotFoundException if the input files do not exist
     * @throws InvalidAreaFileException if the input provided for the graph does not specify a graph
     */
    public static void main(String[] args) throws FileNotFoundException, InvalidAreaFileException{
        if (args.length!=2){ 
            System.out.println("Usage: java Game config.txt area.txt");
            System.exit(1);
        }

        String configFilename = args[0];
        String areaFilename = args[1];
        Game game = new Game();
        game.init(configFilename,areaFilename);
        game.startGame() ; 
    }

    private void startGame() {

        System.out.println("Your random starting location is " + player.getLocationName());
        System.out.println("Type 'h' at any time for a list of commands.");

        int stepCount = 0;
        boolean playerWins = false;  // not yet
        boolean playerQuits = false;

        // Keep playing until game is over (player wins or out of steps)
        while ( !playerQuits && !playerWins && stepCount < numsteps ) {
        	
          // move spy away from player if possible
          if( ! spy.move(player.getLocationName()) ) {
              // was not possible
        	  
              System.out.println("GAME OVER, spy is surrounded.");
              playerWins = true;
              break;
          }
         
          // let player choose a command
          boolean player_moved = false;
          while(!player_moved && !playerQuits){ 
            System.out.println("Enter command: ");
            char command = scan.next().toLowerCase().charAt(0);

            // handle player's command
            
            
            switch(command){
            case 'h': //help
                help();
                break;
            case 'q': //quit
                playerQuits = true;
                break;
            case 'p'://path NODE, cost of path from player to [next] node
                map(spyGraph,player.getLocationName(),scan.next());
                break;
            case 'm'://move NODE
                player_moved = player.move(scan.next()) ;
                break;
            case 'g'://get NODE - retrieves a spycam from NODE if NODE has a spycam
                String nodeName = scan.next();
                player.pickupSpycam(spyGraph.getNodeFromName(nodeName));
                break;

            case 'd'://drop (spycam - if available)
                player.dropSpycam();
                break;

            case 'c':// display clock
                System.out.println((numsteps-stepCount) + " moves remaining");
                break;

            case 's'://spycams - prints # of spycams
                System.out.println("You have " + player.getSpycams() + " spycams remaining.");
                System.out.println("Your spycams are located at nodes: ");
                player.printSpyCamLocations();
                break;

            case 'l'://display location
                System.out.println("Your location is " + player.getLocationName());
                break;

            case 'b'://display budget
                System.out.println("Your budget is " + player.getBudget() + " " + unit + "s");
                break;

            case 'n'://display neighbors
                GraphNode loc = player.getLocation();
                loc.printNeighborNames();
                break;

            case 'o'://onspy - are you on the spy? T or F
                boolean onspy = spy.isAt(player.getLocationName());  // if spy is where player is
                player.decreaseBudget(check); // there is a cost to checking
                if (onspy){
                    System.out.println("You are at the same location as the spy.");
                    playerWins = true;
                }
                else 
                    System.out.println("The spy is not at your location");
                break;
            default:
                System.out.println("Not a valid command.");
                break;
            }
          }
          // Increment steps and check for game over
          stepCount++;

        }

        if (playerWins)
            System.out.println(player.getName()+ " wins!");
        else
            System.out.println("GAME OVER, you did not find the spy, spy wins");

        System.out.println("Spy is at node " + spy.getLocationName());
        System.out.println("Player is at node " + player.getLocationName());

    }

    /**
     * @param play - player
     * @param end - node to move to
     * @return true if the player moves to end
     */
    public static boolean move(Player play, String end){
        return play.move(end);
    }

    /**
     * Display different paths from the start node to the selected "end" node.
     *
     * @param g The graph to search for a path
     * @param start The name of the first node in the path
     * @param end The name of the last node in the path
     */
    public static void map(SpyGraph g, String start, String end){

        // BFS 
        List<Neighbor> BFS = g.BFS(start, end);
        System.out.println("BFS");
        System.out.print(start);
        for (int i = 0; i < BFS.size(); i++){
            System.out.print(" --" + BFS.get(i).getCost() + "--> " + BFS.get(i).getNeighborNode().getNodeName());
        }
        System.out.println();

        // DFS 
        List<Neighbor> DFS = g.DFS(start, end);
        System.out.println("DFS");
        System.out.print(start);
        for (int i = 0; i < DFS.size(); i++){
            System.out.print(" --" + DFS.get(i).getCost() + "--> " + DFS.get(i).getNeighborNode().getNodeName());
        }
        System.out.println();

        // DIJKSTRA'S 
//        List<Neighbor> DIJ = g.Dijkstra(start, end);
//        System.out.println("Dijkstra's");
//        System.out.print(start);
//        for (int i = 0; i < DIJ.size(); i++){
//            System.out.print(" --" + DIJ.get(i).getCost() + "--> " + DIJ.get(i).getNeighborNode().getNodeName());
//        }
//        System.out.println();

    }

    /**
     * Prints the possible commands a player can enter
     */
    public static void help(){
        //print possible commands and a description
        System.out.println("All commands can be activated with the first character.");
        System.out.println("'budget' prints the money you have remaining to use on moves.");
        System.out.println("'clock' returns the number of moves remaining.");
        System.out.println("'drop' places a spycam at your current location if there is one available.");
        System.out.println("'get NODE' retrieves a spycam from NODE if NODE has a spycam");
        System.out.println("'location' prints your location");
        System.out.println("'move NODE' moves you to NODE if possible.");
        System.out.println("'neighbors' prints all neighbors of your location and the cost to get there.");
        System.out.println("'onspy' tells you if you are at the same location as the spy and decreases your budget by a prespecified amount");
        System.out.println("'path NODE' prints three possible paths from your location to NODE using DFS, BFS, and Dijkstra's");
        System.out.println("'quit' ends the game");
        System.out.println("'spycams' prints the nubmer of spycams remaining and the locations of placed spycams.");

    }

    /**
     * Reads the configuration file to set Game Parameters as desired by player.
     * @param configFilename Must be of the form:
     * <pre>REVEALSPY 2
     * MOVES 10
     * BUDGET 15
     * SPYCAMS 3
     * UNIT pound
     * COST 2</pre>
     * 
     * Ends program with error message if file not found
     */
    private void readConfigFile(String configFilename)  {
        Scanner arguments;
        try {
            arguments = new Scanner(new File(configFilename));
            while( arguments.hasNext() ){
                // Just use first character of config constant name
                String tagStr = arguments.next();
                char tag = tagStr.toLowerCase().charAt(0);
                try {            //read the desired units or integer value
                    switch(tag){
                    case 'u': unit = arguments.next(); break;
                    case 'm': numsteps = Integer.parseInt(arguments.next()); break;
                    case 's': spycams = Integer.parseInt(arguments.next()); break;
                    case 'r': reveal = Integer.parseInt(arguments.next());     break;
                    case 'b': budget = Integer.parseInt(arguments.next());     break;
                    case 'c': check = Integer.parseInt(arguments.next()); break;
                    default: System.out.println("Invalid configuration parameter: " + tagStr);
                    }
                } catch (NumberFormatException e ) {
                    System.out.println("Unable to interpret: "+tagStr+"'s value. Must be an integer.");
                }
            }
            arguments.close();
        } catch (FileNotFoundException e1) {
            System.out.println("CONFIG FILE: " + configFilename + " was not found.");
            System.exit(1);
        }

    }

    /**
     * Builds the Graph from the area data file.
     * @param areaFilename The name of a file that contains information 
     * for creating an undirected graph.  Vertex names and edge pairs.  
     * @return - A SpyGraph constructed according to the file input
     * @throws InvalidAreaFileException if the file does not correspond to a graph
     */
    @SuppressWarnings("resource")
    public static SpyGraph createGraphFromAreaFile(String areaFilename) throws InvalidAreaFileException{
        SpyGraph g = new SpyGraph();
        try {
            String node = null;
            Scanner scan = new Scanner(new File(areaFilename));
            scan.nextLine();
            while (!(node = scan.next()).equals("EDGES")){
                g.addGraphNode(node.toLowerCase());
            }
            while(scan.hasNext()){
                g.addEdge(scan.next().toLowerCase(), scan.next().toLowerCase(), Integer.parseInt(scan.next()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("AREA FILE: " + areaFilename + " was not found.");
            System.exit(1);
        }
        return g;
    }

}
