/* Olivia Shanley
CSC 380 Artificial Intelligence
Project 1 - Search
Dr. Salgian, The College of New Jersey

This purpose of this program is to implement Depth-First, Breadth-First, and A*
search algorithms. The user is prompted to choose the algorithm and map for their
search (as well as from two heuristics for A* Search), and the coordinates to
the respective path are printed.

The main elements of the code are as follows:
1) User prompting and input parsing, as well as map display
2) The Space class, which holds information about a space in the grid
3) The three search algorithms
4) Helper methods for finding the starting position and all of the valid potential
moves one path could take next */

import java.util.*;
import java.io.*;
import java.util.concurrent.*;

public class SearchMap{

    ArrayList<String> searchAlgs;
    ArrayList<File> maps;
    ArrayList<String> heuristics;

    private class Space {
        private char symbol;
        private int cost;
        private int costFromStart;
        private boolean visited = false;
        private int[] coords;
        private Space parent;

        private Space (char symbol, int[] coords, int cost, boolean beenVisited){
            this.symbol = symbol;
            this.coords = coords;
            this.cost = cost;
            this.visited = beenVisited;
            this.costFromStart = 0;
            this.parent = null;
        }

        public void setParent(Space parent){
            this.parent = parent;
        }

        public void visited(boolean update){
            this.visited = update;
        }

        public int getRow(){
            return coords[0];
        }

        public int getCol(){
            return coords[1];
        }

        public boolean hasBeenVisited(){
            return visited;
        }

        public char symbol(){
            return symbol;
        }

        public int cost(){
            return cost;
        }

        public int costFromStart(){
            return costFromStart;
        }
        public void updatePathCost(int addCost){
            this.costFromStart += addCost;
        }

        public Space parent(){
            return parent;
        }

    }

    private int[] findSymbol(Space[][] map, char symbol){
        int[] startCoords = new int[2];
        for (int row = 0; row < map.length; row++) {
            for (int col=0; col < map[0].length; col++) {
                if (map[row][col].symbol() == symbol){
                    startCoords[0] = row;
                    startCoords[1] = col;
                }
            }
        }
        return startCoords;
    }

    private void printPath(Space goalSpace){
        Stack<Space> path = new Stack<Space>();
        int cost = 0;
        Space temp = goalSpace;

        while (temp.parent() != null){
            cost += temp.cost();
            path.push(temp);
            temp = temp.parent();

        }
        path.push(temp);
        cost+= temp.cost();

        System.out.println("Path to goal: \t");

        while(!path.empty()){
            temp = path.pop();
            System.out.println("(" + temp.getRow() + "," + temp.getCol() + ")\t");
        }
        System.out.println("Total cost of goal: " + cost);
    }

    private ArrayList<Space> validNeighbors(Space[][] map, Space parent, int curRow, int curCol){
        ArrayList<Space> validMoves = new ArrayList<Space>();

        //check bounds, then determine if a neighbor is valid
        //if so, set parent connecion to current space and set visited to true

        if (curRow > 0){
            Space up = map[curRow-1][curCol];
            if(!(up.symbol() == '#') && !up.hasBeenVisited()){
                up.setParent(parent);

                validMoves.add(up);
                up.visited = true;
            }
        }
        if(curRow < map.length-1){
            Space down = map[curRow+1][curCol];
            if(!(down.symbol() == '#') && !down.hasBeenVisited()){
                down.setParent(parent);

                validMoves.add(down);
                down.visited = true;
            }
        }
        if(curCol > 0){
            Space left = map[curRow][curCol-1];
            if(!(left.symbol() == '#') && !left.hasBeenVisited()){
                left.setParent(parent);

                validMoves.add(left);
                left.visited = true;
            }
        }
        if(curCol < map[0].length-1){
            Space right = map[curRow][curCol+1];
            if(!(right.symbol() == '#') && !right.hasBeenVisited()){
                right.setParent(parent);

                validMoves.add(right);
                right.visited = true;
            }
        }

        return validMoves;
    }

    private class costComparator implements Comparator<Space>{

        @Override
        public int compare(Space space1, Space space2){
            if((space1.costFromStart() + 1) < (space2.costFromStart() + 1)){
                return -1;
            }
            else if((space1.costFromStart() + 1) > (space2.costFromStart() + 1))
                return 1;
            else return 0;
        }
    }

    private void aStarPath(Space[][] map){

        //Initialize priority queue to order Spaces by cost
        Comparator<Space> comparator = new costComparator();
        PriorityQueue<Space> frontier = new PriorityQueue<Space>(11,comparator);

        //set start coordinates
        int [] start = findSymbol(map, 's');
        int row = start[0];
        int col = start[1];
        int nodesExpanded = 0;

        //add startSpace to frontier and mark visited
        Space startSpace = map[row][col];
        frontier.add(startSpace);
        int curCost = startSpace.cost();
        startSpace.visited = true;

        while(frontier.peek()!=null){
            Space curSpace = frontier.poll();
            nodesExpanded++;

            if(curSpace.symbol() == 'g'){
                printPath(curSpace);
                System.out.println("Nodes expanded: " + nodesExpanded);
                return;
            }

            curCost = curSpace.costFromStart();
            row = curSpace.getRow();
            col =  curSpace.getCol();

            //get list of valid adjacent moves
            ArrayList <Space> validMoves = validNeighbors(map, curSpace, row, col);

            //add to frontier
            for (Space sp : validMoves ) {

                //add one to total path length
                sp.updatePathCost(1);
                frontier.add(sp);
            }
        }
        System.out.println("Nodes expanded: " + nodesExpanded);
        System.out.println("This grid has no solution. Exiting.");
        return;
    }

    private void aStarCost(Space[][] map){

        //Initialize priority queue to order Spaces by cost
        Comparator<Space> comparator = new costComparator();
        PriorityQueue<Space> frontier = new PriorityQueue<Space>(11,comparator);

        //set start coordinates
        int [] start = findSymbol(map, 's');
        int row = start[0];
        int col = start[1];
        int nodesExpanded = 0;

        //add startSpace to frontier and mark visited
        Space startSpace = map[row][col];
        frontier.add(startSpace);
        int curCost = startSpace.cost();
        startSpace.visited = true;

        while(frontier.peek()!=null){
            Space curSpace = frontier.poll();
            nodesExpanded++;

            if(curSpace.symbol() == 'g'){
                printPath(curSpace);
                System.out.println("Nodes expanded: " + nodesExpanded);
                return;
            }

            curCost = curSpace.costFromStart();
            row = curSpace.getRow();
            col =  curSpace.getCol();

            //get list of valid adjacent moves
            ArrayList <Space> validMoves = validNeighbors(map, curSpace, row, col);

            //add to frontier
            for (Space sp : validMoves ) {

                //add one to total path length
                sp.updatePathCost(curSpace.cost());
                frontier.add(sp);
            }
        }
        System.out.println("Nodes expanded: " + nodesExpanded);
        System.out.println("This grid has no solution. Exiting.");
        return;
    }

    private void depthFirstSearch(Space[][] map){
        Stack<Space> frontier = new Stack<Space>();
        int nodesExpanded = 0;
        int [] start = findSymbol(map, 's');
        boolean goal = false;
        int row = start[0];
        int col = start[1];

        Space startSpace = map[row][col];
        startSpace.visited = true;
        //push start to frontier
        frontier.push(startSpace);

        while (!goal){
            //if frontier is empty, grid has no path to goal
            if(frontier.empty()){
                System.out.println("Nodes expanded: " + nodesExpanded);
                System.out.println("This grid has no solution. Exiting.");
                return;
            }

            //pop Space off the stack
            Space curSpace = frontier.pop();
            nodesExpanded++;
            row = curSpace.getRow();
            col =  curSpace.getCol();

            //check for goal
            if (curSpace.symbol() == 'g') {
                goal = true;
                printPath(curSpace);
            }
            else{

                //get list of valid adjacent moves
                ArrayList <Space> validMoves = validNeighbors(map, curSpace, row, col);

                //add to frontier
                for (Space sp : validMoves ) {
                    frontier.push(sp);
                }
            }
        }
        System.out.println("Nodes expanded: " + nodesExpanded);
    }

    private void breadthFirstSearch(Space[][] map){

        Queue <Space> frontier = new LinkedList<Space>();
        int [] start = findSymbol(map, 's');
        boolean goal = false;
        int row = start[0];
        int col = start[1];
        int nodesExpanded = 0;

        Space startSpace = map[row][col];
        startSpace.visited = true;
        //add start to front
        frontier.add(startSpace);

        while (!goal){
            Space curSpace = frontier.peek();
            row = curSpace.getRow();
            col =  curSpace.getCol();

            //get list of valid adjacent moves
            ArrayList <Space> validMoves = validNeighbors(map, curSpace, row, col);

            //add to frontier
            for (Space sp : validMoves ) {
                frontier.add(sp);
            }

            //remove oldest Space from frontier
            Space rm = frontier.remove();
            nodesExpanded++;

            //check if goals
            if (rm.symbol() == 'g') {
                goal = true;
                System.out.println("Nodes expanded: " + nodesExpanded);
                printPath(rm);
            }
            else{
                if (frontier.peek() == null){
                    System.out.println("Nodes expanded: " + nodesExpanded);
                    System.out.println("This grid has no solution. Exiting.");
                    return;
                }
            }
        }
    }

    private Space[][] parseMap(File file){
        Space [][] map = null;
        try{
            FileReader fr = new FileReader(file); //read file input
            BufferedReader br = new BufferedReader(fr); //buffer input
            String[] dimensions = br.readLine().split("\\s"); //read first line

            //read dimensions from first line
            int colSize = Integer.parseInt(dimensions[0]);
            int rowSize = Integer.parseInt(dimensions[1]);
            map = new Space[rowSize][colSize];

            int row = 0;
            int col = 0;
            String line = "";
            int i = 0;

            //display matrix
            for(i=0; i < colSize; i++)
            System.out.print("   " + i);

            System.out.println();

            //print one row at a time
            while((line = br.readLine()) != null){
                System.out.print(row + " ");
                for(char c : line.toCharArray()){
                    //set attributes of each space
                    int cost = 1;
                    if (c == '#') {
                        cost = 0;
                    }
                    else if (c == '.') {
                        cost = 1;
                    }
                    else if (c == ','){
                        cost = 2;
                    }
                    else if (c == 's') {
                        cost = 0;
                    }
                    int[] coords = new int[2];
                    coords[0] = row;
                    coords[1] = col;

                    map[row][col] = new Space(c, coords, cost, false);
                    System.out.print(map[row][col].symbol() + "   ");
                    col++;
                }
                row++;
                col = 0;
                System.out.println();
            }
            fr.close();
        }
        catch(IOException Error){
            System.out.println("File not found. Program exited.");
        }
        return map;
    }

    private String promptForAlg(){
        String userInput;
        System.out.println("\nPlease enter a search algorithm to use. Valid options are: \n");

        for(String a : searchAlgs){
            System.out.println(a + "\t");
        }

        System.out.println("\nChoice: ");
        userInput = System.console().readLine();
        System.out.println("\nYou selected: " + userInput + "\n");

        while (!searchAlgs.contains(userInput)){
            System.out.println("Invalid option. Please choose again: \n");

            userInput = System.console().readLine();
        }
        return userInput;
    }

    private File chooseMap(){
        String userInput;
        System.out.println("Choose a map to search from the following (i.e. 'map1'):");

        for(File m : maps){
            System.out.println(m + "\t");
        }
        System.out.println("\nChoice: ");
        userInput = System.console().readLine();

        userInput = "./maps/" + userInput + ".txt";

        while (!maps.contains(new File(userInput))){
            System.out.println("Invalid file. Please choose again: \n");

            userInput = "./maps/" + System.console().readLine() + ".txt";
        }
        System.out.println("\nYou selected: " + userInput + "\n");

        return new File(userInput);
    }

    private String getHeuristic(){
        String userInput;
        System.out.println("\nPlease enter a search heuristic for A*. Valid options are: \n");

        for(String a : heuristics){
            System.out.println(a + "\t");
        }

        System.out.println("\nChoice: ");
        userInput = System.console().readLine();
        System.out.println("\nYou selected: " + userInput + "\n");

        while (!heuristics.contains(userInput)){
            System.out.println("Invalid option. Please choose again: \n");

            userInput = System.console().readLine();
        }
        return userInput;
    }

    public SearchMap() {
        searchAlgs = new ArrayList<>(Arrays.asList("DFS", "BFS", "A*"));
        maps = new ArrayList<File>(Arrays.asList(new File("./maps").listFiles()));
        heuristics = new ArrayList<>(Arrays.asList("distance", "cost"));
    }

    public static void main(String[] args) {
        long startTime = 0;
        long elapsedTime = 0;
        SearchMap search = new SearchMap();
        String searchType = search.promptForAlg();
        String heuristic = " ";
        if (searchType.equals("A*")){
            heuristic = search.getHeuristic();
        }

        File mapFile = search.chooseMap();
        Space[][] map = search.parseMap(mapFile);

        switch (searchType) {
            case "DFS":
                startTime = System.nanoTime();
                search.depthFirstSearch(map);
                elapsedTime = System.nanoTime() - startTime;
                elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
                System.out.println("Elapsed time: " + elapsedTime + "ms");
                break;

            case "BFS":
                startTime = System.nanoTime();
                search.breadthFirstSearch(map);
                elapsedTime = System.nanoTime() - startTime;
                elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
                System.out.println("Elapsed time: " + elapsedTime + "ms");
                break;

            case "A*":
                startTime = System.nanoTime();

                if (heuristic.equals("cost"))
                    search.aStarCost(map);
                else if (heuristic.equals("distance"));
                    search.aStarPath(map);

                elapsedTime = System.nanoTime() - startTime;
                elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
                System.out.println("Elapsed time: " + elapsedTime + "ms");
                break;
        }
    }
}
