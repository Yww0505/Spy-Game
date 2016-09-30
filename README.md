# Spy-Game
--generated a undirected, edge-weighted graph according to the input area file. Put a spy randomly at some node.
--player was assigned a limited number of spy camera and travel budget, that spy camera can be dropped at any points to detect spy and travel budget can move the player to another node in the graph.
--using dijkstra algorithm to provide the player the shortest path to travel.
--the spy will sometimes expose his location, but, of course he will keep moving after that.
--when the all the spy's current neighbors are either the player or spy camera that he has nowhere to move, game win. If the player spent all his budget and still have not detected the spy, game lose.
