import java.util.ArrayList;

public class Player {
    int score;
    String playMethod;
    int nodesExplored;

    public Player(String alg){
        this.score = 0;
        this.playMethod = alg;
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

    /* methods */

    public void paradrop(Space chosenSpace){
        chosenSpace.setOccupier(this);
        this.updateScore(chosenSpace.value());
    }

    public void blitz(Board board, Space chosenSpace){
        this.updateScore(chosenSpace.value());
        int[] coords = chosenSpace.getCoords();
        System.out.println("Blitz on: (" + coords[0] + "," + coords[1] + ")");

        //take space
        chosenSpace.setOccupier(this);

        //check for neighboring enemies
        ArrayList <Space> enemies = chosenSpace.neighboringEnemies(board.getBoard(), this);
        System.out.println("Space being blitzed has " + enemies.size() + " enemies to conquer");

        if(enemies.size()!= 0){
            for (Space enemy : enemies){
                System.out.println("Conquering: " + enemy.value());
                enemy.setOccupier(this);
                this.updateScore(enemy.value());
            }
        }

    }

    public void makeRandomMove(Board board){
        double r = Math.random();
        ArrayList<Space> possibleMoves = board.remainingMoves();

        //paradrop by random choice or if player owns no pieces
        if (r > 0.5 || possibleMoves.size() == 25){

            //randomly choose a Space to paradrop onto
            double choice = Math.random()*possibleMoves.size();
            Space chosenSpace = possibleMoves.get((int)choice);

            paradrop(chosenSpace);
            int [] coords = chosenSpace.getCoords();
            System.out.println("Paradrop on: (" + coords[0] + "," + coords[1] + ")");

        }
        //Blitz
        else{
            //get all spots owned by player on the board
            ArrayList<Space> ownedSpaces = board.getOwnedSpaces(this);

            //if player owns none, paradrop instead
            if (ownedSpaces.size() == 0){
                System.out.println("Player owns no spaces. Paradrop instead.");

                //choose space to paradrop
                double choice = Math.random()*possibleMoves.size();
                Space chosenSpace = possibleMoves.get((int)choice);

                paradrop(chosenSpace);

                System.out.println("Paradropped instead on: " + chosenSpace.value());
            }
            else {
                //choose owned space to use for blitz
                double choice = Math.random()*ownedSpaces.size();
                Space chosenSpace = ownedSpaces.get((int)choice);

                //get its free neighbors
                ArrayList<Space> neighbors = chosenSpace.freeNeighbors(board.getBoard());

                //if current space has no unoccupied neighbors, try another owned space
                //continue through all owned spaces until a valid space is found or all are evaluated
                while (neighbors.size() == 0 && ownedSpaces.size()>0){

                    //remove space from list of owned spaces since it can't be used
                    ownedSpaces.remove(chosenSpace);
                    choice = Math.random()*ownedSpaces.size();
                    chosenSpace = ownedSpaces.get((int)choice);

                    //get free spaces
                    neighbors = chosenSpace.freeNeighbors(board.getBoard());
                }

                //if there are no owned spaces with an empty neighboring spot, paradrop instead
                if (ownedSpaces.size() == 0){
                    System.out.println("No available spots to blitz.");

                    //choose space to paradrop
                    choice = Math.random()*possibleMoves.size();
                    chosenSpace = possibleMoves.get((int)choice);

                    paradrop(chosenSpace);

                    System.out.println("Paradropped instead on: " + chosenSpace.value());
                }
                //otherwise blitz
                else{
                    Space toBlitz;

                    //choose one of the open neighbors to blitz
                    choice = Math.random()*neighbors.size();
                    toBlitz = neighbors.get((int)choice);

                    blitz(board, toBlitz);
                }

            }
        }

    }
}
