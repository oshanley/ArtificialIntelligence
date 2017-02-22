import java.lang.Math;

public class RandomPlayer extends Player {

    public RandomPlayer(){
        super("random");
    }

    public void makeMove(Board board){
        double r = Math.random();

        //paradrop by random choice or if first to make move
        if (r > 0.5 || board.remainingMoves().size() == 25){ //paradrop
            Space maxSpace = board.highestVal();
            maxSpace.setOccupier(this);
            this.updateScore(maxSpace.value());

            System.out.println("Paradrop on: " + maxSpace.value());

        }
        else{ //blitz
            System.out.println("Random blitz");
        }

    }

}
