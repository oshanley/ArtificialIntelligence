import java.util.*;
import java.io.*;//FileInputStream;
/*public enum SpaceType { COMMA, HASHTAG, PERIOD };



public void DFS (){
    Queue<String> Frontier;

}

public void BFS (){
    Queue<String> Frontier;

}

public void A_STAR(){
    Queue <String> Frontier = new PriorityQueue();

}

public void initmap(int col, int row){


}*/
public class SearchMap{

    ArrayList<String> searchAlgs;
    ArrayList<File> maps;

    public enum SpaceType {
         COMMA, HASH, PERIOD
     }

    private class Space {
        SpaceType spacetype;
        int cost;
        boolean visited = false;

        private Space (SpaceType type, int cost, boolean beenVisited){
            spacetype = type;
            cost = cost;
            visited = beenVisited;
        }

        private boolean hasBeenVisited(){
            return visited;
        }
        private SpaceType type(){
            return spacetype;
        }
        private int cost(){
            return cost;
        }

    }

    private Space[][] parseMap(File file){
        Space map[][] = null;
        try{
            FileReader fr = new FileReader(file); //read file input
            BufferedReader br = new BufferedReader(fr); //buffer input
            String[] dimensions = br.readLine().split("\\s"); //read first line

            //read dimensions
            int rowSize = Integer.parseInt(dimensions[0]);
            int colSize = Integer.parseInt(dimensions[1]);
            map = new Space[rowSize][colSize];

            System.out.println("Rows: " + rowSize + " Cols: " + colSize + "\n");

            int row = 0;
            int col = 0;
            String line;
            while((line = br.readLine()) != null){
                for(char c : line.toCharArray()){
                    SpaceType type = null;
                    int cost = 0;
                    if (c == '#') {
                        type = SpaceType.HASH;
                        cost = 0;
                    }
                    else if (c == '.') {
                        type = SpaceType.PERIOD;
                        cost = 1;
                    }
                    else if (c == ','){
                        type = SpaceType.COMMA;
                        cost = 2;
                    }
                    map[row][col] = new Space(type, cost, false);
                    System.out.print(c + "   ");
                }
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

    private void search(){
        String algorithm = promptForAlg();
        File map = chooseMap();
    }

    public SearchMap() {
        searchAlgs = new ArrayList<>(Arrays.asList("DFS", "BFS", "A*"));
        maps = new ArrayList<File>(Arrays.asList(new File("./maps").listFiles()));
    }

    public static void main(String[] args) {
        SearchMap search = new SearchMap();
        search.promptForAlg();
        File mapFile = search.chooseMap();
        search.parseMap(mapFile);
    }
}
