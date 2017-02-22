import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

	//minimax looks at all possible moves

public class Game {

	ArrayList<File> boards;
	ArrayList<String> agents;

	//valid neighbors to be used for blitz
	// private ArrayList<Space> validNeighbors(Space[][] map, Space curSpace, int curRow, int curCol){
    //     ArrayList<Space> validMoves = new ArrayList<Space>();
	//
    //     //check bounds, then determine if a neighbor is valid
    //     //if so, set previousMove connecion to current space and set visited to true
	//
    //     if (curRow > 0){
    //         Space up = map[curRow-1][curCol];
    //         if(!(up.isOccupied())){
    //             up.setPreviousMove(curSpace);
	//
    //             validMoves.add(up);
    //         }
    //     }
    //     if(curRow < map.length-1){
    //         Space down = map[curRow+1][curCol];
    //         if(!(down.isOccupied())){
    //             down.setPreviousMove(curSpace);
	//
    //             validMoves.add(down);
    //         }
    //     }
    //     if(curCol > 0){
    //         Space left = map[curRow][curCol-1];
    //         if(!(left.isOccupied())){
    //             left.setPreviousMove(curSpace);
	//
    //             validMoves.add(left);
    //         }
    //     }
    //     if(curCol < map[0].length-1){
    //         Space right = map[curRow][curCol+1];
    //         if(!(right.isOccupied())){
    //             right.setPreviousMove(curSpace);
	//
    //             validMoves.add(right);
    //         }
    //     }
	//
    //     return validMoves;
    // }

	private void playGame(Board board, Player greenPlayer, Player bluePlayer){
		boolean gameOver = false;
		Player curPlayer = greenPlayer;
		Space[][] map = board.getBoard();

		while(!gameOver){
			if (board.remainingMoves().size() == 0){
				System.out.println("No remaining moves. Game over");
				gameOver = true;
			}
			else{
				//make move depending on play method
				switch (curPlayer.playMethod()) {
					case "minimax":
						break;

					case "random":
						RandomPlayer randP = new RandomPlayer();
						randP.makeMove();
						break;

					case "AB":
						break;
				}

				//switch Player
				if (curPlayer.equals(greenPlayer)){
					System.out.println("Player 2's turn");
					curPlayer = bluePlayer;
				}
				else if (curPlayer.equals(bluePlayer)){
					System.out.println("Player 1's turn");
					curPlayer = greenPlayer;
				}
			}
		}
	}

	private String choosePlayerType(){
		String userInput;
	    System.out.println("Choose the algorithm for the agent to implement. The options are: \n");

	    for(String t : agents){
			switch (t) {
				case "random":
				System.out.format("1. Random. (Type: %s)\n", t);
				break;

				case "minimax":
				System.out.format("2. Minimax. (Type: %s)\n", t);
				break;

				case "AB":
				System.out.format("3. Minimax with Alpha-Beta Pruning. (Type: %s)\n", t);
				break;

			}

	    }
	    System.out.println("\nChoice: ");
	    userInput = System.console().readLine();

	    while (!agents.contains(userInput)){
	        System.out.println("Invalid algorithm. Please choose again: \n");

			userInput = System.console().readLine();
	    }
	    System.out.print("\nYou selected: ");

		switch (userInput) {
			case "random":
			System.out.format("Random\n");
			break;

			case "minimax":
			System.out.format("Minimax\n");
			break;

			case "AB":
			System.out.format("Minimax with Alpha-Beta Pruning\n");
			break;

	    }

	    return userInput;

	}

	public File chooseBoard(){
		String userInput;
	    System.out.println("Choose a board to play on (i.e. Kalamazoo):");

	    for(File b : boards){
	        System.out.println(b);
	    }
	    System.out.println("\nChoice: ");
	    userInput = System.console().readLine();

	    userInput ="./GameBoards/" + userInput + ".txt";

	    while (!boards.contains(new File(userInput))){
	        System.out.println("Invalid file. Please choose again: \n");
			userInput = System.console().readLine();

	        userInput = "./GameBoards/" + userInput + ".txt";
	    }
	    System.out.println("\nYou selected: " + userInput + "\n");

	    return new File(userInput);
	}

	public Game(){
		boards = new ArrayList<File>(Arrays.asList(new File("./GameBoards").listFiles()));
		agents = new ArrayList<>(Arrays.asList("random", "minimax", "AB"));
	}

	public static void main(String[] args) {
		Game war = new Game();

		File boardChoice = war.chooseBoard();
		Board board = new Board(boardChoice);


		//Intiialize players
		System.out.println("----------------------------------------------------");
		System.out.println("Player One:");
		String playerOneType = war.choosePlayerType();
		Player greenPlayer = new Player(playerOneType);

		System.out.println("----------------------------------------------------");
		System.out.println("Player Two:");
		String playerTwoType = war.choosePlayerType();
		Player bluePlayer = new Player(playerTwoType);
		System.out.println("----------------------------------------------------");
		System.out.println();

		//Initialize board
		Space[][] map = board.getBoard();
		war.playGame(board, greenPlayer, bluePlayer);
	}

}
