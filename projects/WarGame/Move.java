import java.util.ArrayList;

public class Move{
    Board board;
    Player curPlayer;
    Player opponent;

    public Move(Board board, Player curPlayer, Player opponent){
        this.board = board;
        this.curPlayer = curPlayer;
        this.opponent = opponent;
    }

    public boolean canBlitz(Space curSpace){
        //if there are owned pieces neighboring the empty space, space can be blitzed
        ArrayList<Space> ownedNeighbors = curSpace.neighborsOwnedBy(board.getBoard(), curPlayer);

        if (ownedNeighbors.size() > 0)
            return true;
        else
            return false;

    }

    //given a space, check if you can conquer its neighbors
    public int attack(Space chosenSpace){
        Space[][] map = board.getBoard();
        int earnedPoints = 0;
        curPlayer.updateScore(chosenSpace.value());
        int[] coords = chosenSpace.getCoords();
        System.out.println("Blitz on: (" + coords[0] + "," + coords[1] + ")");
		System.out.println("Player: +" + chosenSpace.value());

        //take current space and update the map
        chosenSpace.setOccupier(curPlayer);
        map[coords[0]][coords[1]] = new Space(chosenSpace);

        earnedPoints+=chosenSpace.value();

        //if the space can be used as a blitz, conquer any neighboring enemies
        if(canBlitz(chosenSpace)){
            ArrayList <Space> enemies = chosenSpace.neighborsOwnedBy(board.getBoard(), opponent);

            if(enemies.size()!= 0){
                System.out.println("Space being blitzed has " + enemies.size() + " enemies to conquer");
                for (Space enemy : enemies){
                    coords = enemy.getCoords();


                    System.out.println("Player: +" + enemy.value());
    				System.out.println("Opponent: -" + enemy.value());

                    earnedPoints+=enemy.value();

                    //take enemy space and update the map
                    enemy.setOccupier(curPlayer);
                    map[coords[0]][coords[1]] = new Space(enemy);
                    curPlayer.updateScore(enemy.value());
    				opponent.updateScore(-enemy.value());
                }
            }
        }
        return earnedPoints;
    }

	public void randomMove(){
        double r = Math.random();
        ArrayList<Space> possibleMoves = board.remainingMoves();

        //choose a random space to attack
        double choice = Math.random()*possibleMoves.size();
        Space chosenSpace = possibleMoves.get((int)choice);

        attack(chosenSpace);
    }


}
