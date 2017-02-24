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

    //conquer a space on the board
    public int paradrop(Space chosenSpace){
            chosenSpace.setOccupier(curPlayer);
            curPlayer.updateScore(chosenSpace.value());
            return chosenSpace.value();
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

        //take current space
        chosenSpace.setOccupier(curPlayer);
        map[coords[0]][coords[1]] = new Space(chosenSpace);

        earnedPoints+=chosenSpace.value();

        //if the space can be used as a blitz, conquer any neighboring enemies
        if(canBlitz(chosenSpace)){
            ArrayList <Space> enemies = chosenSpace.neighborsOwnedBy(board.getBoard(), opponent);
            System.out.println("Space being blitzed has " + enemies.size() + " enemies to conquer");

            if(enemies.size()!= 0){
                for (Space enemy : enemies){
                    coords = enemy.getCoords();


                    System.out.println("Conquering: " + enemy.value());
    				System.out.println("Opponent: -" + enemy.value());

                    earnedPoints+=enemy.value();

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
		System.out.println("There are " + possibleMoves.size() + " moves left");

        double choice = Math.random()*possibleMoves.size();
        Space chosenSpace = possibleMoves.get((int)choice);

        attack(chosenSpace);


        /*//paradrop by random choice or if player owns no pieces
        if (r > 0.5 || possibleMoves.size() == 25){

            //randomly choose a Space to paradrop onto


            attack(chosenSpace);
            int [] coords = chosenSpace.getCoords();
            System.out.println("Paradrop on: (" + coords[0] + "," + coords[1] + ")");
			System.out.println("+" + chosenSpace.value());

        }
        //Blitz
        else{
			System.out.println("attempting to blitz");
            //get all spots owned by player on the board
            ArrayList<Space> ownedSpaces = board.getOwnedSpaces(curPlayer);
			System.out.println("Player owns " + ownedSpaces.size() + " spaces");
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
				//System.out.println("Accessing index: " + (int)choice);

                Space chosenSpace = ownedSpaces.get((int)choice);

				//System.out.println("Chose space " + chosenSpace.getCoords()[0] + "," + chosenSpace.getCoords()[1]);
                //get its free neighbors
                ArrayList<Space> neighbors = chosenSpace.freeNeighbors(board.getBoard());
				//System.out.println("Space has " + neighbors.size() + " neighbors");

                //if current space has no unoccupied neighbors, try another owned space
                //continue through all owned spaces until a valid space is found or all are evaluated
                while (neighbors.size() == 0 && ownedSpaces.size()>0){

                    //remove space from list of owned spaces since it can't be used
                    ownedSpaces.remove(chosenSpace);
                    if (ownedSpaces.size() > 0){
						choice = Math.random()*ownedSpaces.size();
					//	System.out.println("Accessing index: " + (int)choice);
                    	chosenSpace = ownedSpaces.get((int)choice);

						//System.out.println("Chose space " + chosenSpace.getCoords()[0] + "," + chosenSpace.getCoords()[1]);
						//System.out.println("Space has " + neighbors.size() + " neighbors");
						//get free spaces
	                    neighbors = chosenSpace.freeNeighbors(board.getBoard());
					}

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
				////	System.out.println("Found a space with at least 1 empty neighbor");
					System.out.println("Num free neighbors: " + neighbors.size());
                    //choose one of the open neighbors to blitz
                    choice = Math.random()*neighbors.size();
				//	System.out.println("Accessing index: " + (int)choice);
                    toBlitz = neighbors.get((int)choice);

                    blitz(toBlitz);
                }

            }
        }*/
    }


}
