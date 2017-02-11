import java.util.*;
import java.io.*;

public class SearchMap{

    ArrayList<String> searchAlgs;
    ArrayList<File> maps;
    ArrayList<String> heuristics;


    private class Space {
        private char symbol;
        private int cost;
        private boolean visited = false;
        private int[] coords;

        private Space (char symbol, int[] coords, int cost, boolean beenVisited){
            this.symbol = symbol;
            this.coords = coords;
            this.cost = cost;
            this.visited = beenVisited;
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

    }

    private int[] findStart(Space[][] map){
        int[] startCoords = new int[2];
        for (int row = 0; row < map.length; row++) {
            for (int col=0; col < map[0].length; col++) {
                if (map[row][col].symbol() == 's'){
                    startCoords[0] = row;
                    startCoords[1] = col;
                }
            }
        }
        return startCoords;
    }

    private ArrayList<Space> validNeighbors(Space[][] map, int curRow, int curCol){
        ArrayList<Space> validMoves = new ArrayList<Space>();

        if (curRow > 0){
            Space up = map[curRow-1][curCol];
            if(!(up.symbol() == '#') && !up.hasBeenVisited()){

                validMoves.add(up);
                up.visited = true;
            }
        }
        if(curRow < map.length-1){
            Space down = map[curRow+1][curCol];
            if(!(down.symbol() == '#') && !down.hasBeenVisited()){

                validMoves.add(down);
                down.visited = true;
            }
        }
        if(curCol > 0){
            Space left = map[curRow][curCol-1];
            if(!(left.symbol() == '#') && !left.hasBeenVisited()){

                validMoves.add(left);
                left.visited = true;
            }
        }
        if(curCol < map[0].length-1){
            Space right = map[curRow][curCol+1];
            if(!(right.symbol() == '#') && !right.hasBeenVisited()){

                validMoves.add(right);
                right.visited = true;
            }
        }

        return validMoves;
    }

    private void depthFirstSearch(Space[][] map){
        Deque<Space> frontier = new ArrayDeque<Space>();
        int [] start = findStart(map);

        boolean goal = false;
        int row = start[0];
        int col = start[1];

        Space startSpace = map[row][col];

        frontier.add(startSpace);
        startSpace.visited = true;

        while (!goal){
            Space curSpace = frontier.peek();
            row = curSpace.getRow();
            col =  curSpace.getCol();

            ArrayList <Space> validMoves = validNeighbors(map, row, col);

            for (Space sp : validMoves ) {
                frontier.add(sp);
            }

            Space rm = frontier.remove();

            if (rm.symbol() == 'g') {
                goal = true;
                System.out.println("GOAL FOUND AT: (" + row + "," + col + ")");
            }
            else{
                if (frontier.peek() == null){
                    System.out.println("This grid has no solution. Exiting.");
                    return;
                }
            }
        }

    }

    private void breadthFirstSearch(Space[][] map){
        Queue <Space> frontier = new LinkedList<Space>();
        int [] start = findStart(map);

        boolean goal = false;
        int row = start[0];
        int col = start[1];

        Space startSpace = map[row][col];

        frontier.add(startSpace);
        startSpace.visited = true;

        while (!goal){
            Space curSpace = frontier.peek();
            row = curSpace.getRow();
            col =  curSpace.getCol();

            ArrayList <Space> validMoves = validNeighbors(map, row, col);

            for (Space sp : validMoves ) {
                frontier.add(sp);
            }

            Space rm = frontier.remove();

            if (rm.symbol() == 'g') {
                goal = true;
                System.out.println("GOAL FOUND AT: (" + row + "," + col + ")");
            }
            else{
                if (frontier.peek() == null){
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

            //read dimensions
            int colSize = Integer.parseInt(dimensions[0]);
            int rowSize = Integer.parseInt(dimensions[1]);
            map = new Space[rowSize][colSize];

            int row = 0;
            int col = 0;
            String line;

            while((line = br.readLine()) != null){
                for(char c : line.toCharArray()){
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
        SearchMap search = new SearchMap();
        String searchType = search.promptForAlg();

        if (searchType.equals("A*")){
            String heuristic = search.getHeuristic();
        }

        File mapFile = search.chooseMap();
        Space[][] map = search.parseMap(mapFile);

        switch (searchType) {
            case "DFS":
            search.depthFirstSearch(map);
                break;
            case "BFS":
                search.breadthFirstSearch(map);
                break;
            case "A*":
                break;
        }
    }
}
