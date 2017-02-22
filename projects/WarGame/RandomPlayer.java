import java.lang.Math;

public class RandomPlayer extends Player {

    public RandomPlayer(){
        super("random");
    }

    public void makeMove(){
        double r = Math.random();

        if (r > 0.5){
            System.out.println("Random paradrop");
        }
        else{
            System.out.println("Random blitz");
        }

    }

}
