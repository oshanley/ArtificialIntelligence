import java.util.ArrayList;

public class Player {
    int score;
    String playMethod;
    int nodesExplored;

    public Player(String alg){
        this.score = 0;
        this.playMethod = alg;
    }

    public Player(Player copy){
        this.score = copy.score;
        this.playMethod = copy.playMethod;
        this.nodesExplored = copy.nodesExplored;
    }

    public void updateScore(int increment){
        this.score += increment;
    }

    public String playMethod(){
        return this.playMethod;
    }

    public int getScore(){
        return this.score;
    }

}
