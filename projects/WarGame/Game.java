import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {

	ArrayList<File> boards;
	ArrayList<String> agents;

	private ArrayList<Space> validMoves(Space[][] board, Space previousMove, int curRow, int curCol){
        ArrayList<Space> validMoves = new ArrayList<Space>();

        //check bounds, then determine if a neighbor is valid
        //if so, set previousMove connecion to current space and set visited to true

        if (curRow > 0){
            Space up = board[curRow-1][curCol];
            if(!(up.isOccupied())){
                up.setPreviousMove(previousMove);

                validMoves.add(up);
            }
        }
        if(curRow < board.length-1){
            Space down = board[curRow+1][curCol];
            if(!(down.isOccupied())){
                down.setPreviousMove(previousMove);

                validMoves.add(down);
            }
        }
        if(curCol > 0){
            Space left = board[curRow][curCol-1];
            if(!(left.isOccupied())){
                left.setPreviousMove(previousMove);

                validMoves.add(left);
            }
        }
        if(curCol < board[0].length-1){
            Space right = board[curRow][curCol+1];
            if(!(right.isOccupied())){
                right.setPreviousMove(previousMove);

                validMoves.add(right);
            }
        }

        return validMoves;
    }

	// private Space makeMove(){
	//
	// }
	private void playGame(Space [][] board){
		boolean gameOver = false;

		Player p1 = new Player();
		Player p2 = new Player();
		Player curPlayer = p1;

		while(!gameOver){

			//make moves


				//switch Player
				if (curPlayer.equals(p1))
					curPlayer = p2;
				else if (curPlayer.equals(p2))
					curPlayer = p1;

		}
	}

	private Space[][] parseMap(File file) {
		FileReader fr;
		Space[][] board = null;

		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr); // buffer input

			// dimensions of grid
			final int colSize = 5;
		  	final int rowSize = 5;
			int row = 0;
			int col = 0;


			board = new Space[rowSize][colSize];

			int spaceVal = br.read();

			while(br.ready()){

				//initialize space on board
				int[] coords = new int[2];
				coords[0] = row;
				coords[1] = col;

				board[row][col] = new Space(spaceVal, coords);

				System.out.print(spaceVal + "\t");

				//go to next column
				col++;

				//if column out of bounds
				if (col > colSize - 1){
					//go to beginning of next row
					row++;
					col = 0;
					//if row out of bounds too (aka end of grid), exit
					if(row > rowSize - 1)
						break;

					System.out.println();
				}

				//get next value from file
				spaceVal = br.read();

            }
			//place console prompt on new line
			System.out.println();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
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
		System.out.println();

	    return userInput;

	}
	private File chooseBoard(){
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
		String playerOneType = war.choosePlayerType();
		Space[][] board = war.parseMap(boardChoice);
		//war.playGame(board);
	}

}
