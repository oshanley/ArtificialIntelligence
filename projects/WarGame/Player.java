public class Player {
    int score;
    String playMethod;
    int nodesExplored;

    public Player(String alg){
        this.score = 0;
        this.playMethod = alg;
    }

    public void updateScore(int incrementScore){
        this.score += incrementScore;
    }

    public String playMethod(){
        return this.playMethod;
    }
}
