import java.util.*;
import java.io.*;//FileInputStream;
/*public enum SpaceType { COMMA, HASHTAG, PERIOD };

public class Space {
    SpaceType spacetype;
    int[][] coords;
    int cost;
    bool visited = false;
}

public void DFS (){
    Queue<String> Frontier;

}

public void BFS (){
    Queue<String> Frontier;

}

public void A_STAR(){
    Queue <String> Frontier = new PriorityQueue();

}

public void initgrid(int col, int row){


}*/
public class SearchGrid{
    public static void main(String[] args) {
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            while(line = br.readLine( != null)){
                ArrayList<Character> lineOfChars = new ArrayList<>();
                for(char c : line.toCharArray())
                    lineOfChars.add(c);
                grid.add(lineOfChars);
            }
            fr.close();
        }
        catch(FileNotFoundException Error){
            System.out.println("File not found.");
        }
        return grid;
    }
}
