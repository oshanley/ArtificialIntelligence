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

	private void playGame(Board board, Player p1, Player p2){
		boolean gameOver = false;
		Player curPlayer = p1;
		Space[][] map = board.getBoard();

		while(!gameOver){
			if (board.remainingMoves().size() == 0){
				System.out.println("No remaining moves. Game over");
				if (p1.getScore() > p2.getScore())
					System.out.println("Player 1 wins");
				else if (p1.getScore() < p2.getScore())
					System.out.println("Player 2 wins");
				else
					System.out.println("It's a tie!");

				gameOver = true;
			}
			else{

				System.out.println("Remaining spaces: " + board.remainingMoves().size());
				//make move depending on play method
				switch (curPlayer.playMethod()) {
					case "minimax":
						break;

					case "random":
						curPlayer.makeRandomMove(board);
						System.out.println("Player new score: " + curPlayer.getScore());
						System.out.println();
						break;

					case "AB":
						break;
				}

				//switch Player
				if (curPlayer.equals(p1)){
					System.out.println("Player 2's turn");
					curPlayer = p2;
				}
				else if (curPlayer.equals(p2)){
					System.out.println("Player 1's turn");
					curPlayer = p1;
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
		Player p1, p2;

		//Intiialize players

		//Player 1 (green)
		System.out.println("----------------------------------------------------");
		System.out.println("Player One:");
		String playerOneType = war.choosePlayerType();

		p1 = new Player(playerOneType);

		System.out.println("----------------------------------------------------");

		//Player 2 (Blue)
		System.out.println("Player Two:");
		String playerTwoType = war.choosePlayerType();
		p2 = new Player(playerTwoType);

		System.out.println("----------------------------------------------------");
		System.out.println();

		//Initialize board
		Space[][] map = board.getBoard();
		war.playGame(board, p1, p2);
	}

}
