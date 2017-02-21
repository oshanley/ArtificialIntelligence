public class Player {
    int score;

    public Player(){
        this.score = 0;
    }

    private void updateScore(int incrementScore){
        this.score += incrementScore;
    }
}
