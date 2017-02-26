/* Olivia Shanley
CSC 380 Artificial Intelligence
Project 2 - War
Dr. Salgian, The College of New Jersey

The purpose of this Game class is to serve as the driver for the program. It
initializes the board and its players; implements game play through the switching of players,
as well as calling the respective moves for each player; and determines when the game has ended. */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class Game {

	ArrayList<File> boards;
	ArrayList<String> agents;

	public void minimax(Board board, Player curPlayer, Player opponent){
        //create a new Minimax Object
		int maxDepth = 3;
		Minimax agent = new Minimax(board, maxDepth, curPlayer, opponent);
		Space attackSpace = agent.minimax();

		Move makeMove = new Move(board, curPlayer, opponent);
		System.out.println("Making move at: " + attackSpace.getCoords()[0] + "," + attackSpace.getCoords()[1]);
		makeMove.attack(attackSpace);
    }

	public void alphabeta(Board board, Player curPlayer, Player opponent){
        //create a new AlphaBeta Object
		int maxDepth = 6;
		AlphaBeta agent = new AlphaBeta(board, maxDepth,curPlayer, opponent);
		Space attackSpace = agent.minimax();

		Move makeMove = new Move(board, curPlayer, opponent);
		System.out.println("Making move at: " + attackSpace.getCoords()[0] + "," + attackSpace.getCoords()[1]);
		makeMove.attack(attackSpace);
    }

	private void playGame(Board board, Player p1, Player p2){
		boolean gameOver = false;
		Player curPlayer = p1;
		Player opponent = p2;
		Space[][] map = board.getBoard();

		while(!gameOver){
			if (board.remainingMoves().size() == 0){
				System.out.println("No remaining moves. Game over");
				System.out.println("Final scores: \t P1 - " + p1.getScore() + " | P2 - " + p2.getScore());
				if (p1.getScore() > p2.getScore())
					System.out.println("Player 1 wins");
				else if (p1.getScore() < p2.getScore())
					System.out.println("Player 2 wins");
				else
					System.out.println("It's a tie!");
					System.out.println();
				gameOver = true;
			}
			else{
				long startTime = 0;
				long elapsedTime = 0;
				startTime = System.nanoTime();

				curPlayer.updateTotalMoves();
				System.out.println("Remaining spaces: " + board.remainingMoves().size());
				//make move depending on play method
				switch (curPlayer.playMethod()) {
					case "minimax":
						minimax(board, curPlayer, opponent);

						elapsedTime = System.nanoTime() - startTime;
						elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
						System.out.println("Elapsed time: " + elapsedTime + "ms");

						curPlayer.updateTime(elapsedTime);
						System.out.println("Player new score: " + curPlayer.getScore());
						System.out.println();
						break;

					case "random":
						Move makeMove = new Move(board, curPlayer, opponent);
						makeMove.randomMove();

						elapsedTime = System.nanoTime() - startTime;
						elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
						System.out.println("Elapsed time: " + elapsedTime + "ms");
						curPlayer.updateTime(elapsedTime);

						System.out.println("Player new score: " + curPlayer.getScore());
						System.out.println();
						break;

					case "AB":
						alphabeta(board, curPlayer, opponent);
						elapsedTime = System.nanoTime() - startTime;
						elapsedTime = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
						System.out.println("Elapsed time: " + elapsedTime + "ms");
						curPlayer.updateTime(elapsedTime);

						System.out.println("Player new score: " + curPlayer.getScore());
						System.out.println();
						break;
				}

				//switch Player
				if (curPlayer.equals(p1)){
					System.out.println("Player 2's turn");
					curPlayer = p2;
					opponent = p1;
				}
				else if (curPlayer.equals(p2)){
					System.out.println("Player 1's turn");
					curPlayer = p1;
					opponent = p2;
				}
			}
		}

		//print statistics for player 1
		long totalTime = p1.getGameTime();
		int moves = p1.getTotalMoves();
		long avgTime = (long)(totalTime/moves);
		int totalNodes = p1.getExplored();
		int avgNodes = totalNodes/moves;
		ArrayList<Space> ownedSpaces = board.getOwnedSpaces(p1);

		//Display scores, average move time,
		System.out.println("Player 1 total play time: " + totalTime + "ms");
		System.out.println("Player 1 avg move time: " + avgTime + "ms");
		System.out.println("Player 1 nodes explored: " + totalNodes);
		System.out.println("Player 1 avg nodes explored per move: " + avgNodes);
		System.out.println("Player 1 owns the following spaces: ");
		for(Space sp : ownedSpaces)
			System.out.print("(" + sp.getCoords()[0] + "," + sp.getCoords()[1] + ") ");

		System.out.println("\n");

		//print statistics for player 1
		totalTime = p2.getGameTime();
		moves = p2.getTotalMoves();
		avgTime = (long)(totalTime/moves);
		totalNodes = p2.getExplored();
		avgNodes = totalNodes/moves;
		ownedSpaces = board.getOwnedSpaces(p2);

		System.out.println("Player 2 total play time: " + totalTime + "ms");
		System.out.println("Player 2 avg move time: " + avgTime + "ms");
		System.out.println("Player 2 nodes explored per move: " + totalNodes);
		System.out.println("Player 2 avg nodes explored: " + avgNodes);
		System.out.println("Player 2 owns the following spaces: ");
		for(Space sp : ownedSpaces)
			System.out.print("(" + sp.getCoords()[0] + "," + sp.getCoords()[1] + ") ");

		System.out.println();
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

		//Initialize players

		//Player 1
		System.out.println("----------------------------------------------------");
		System.out.println("Player One:");
		String playerOneType = war.choosePlayerType();

		p1 = new Player(playerOneType);

		System.out.println("----------------------------------------------------");

		//Player 2
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
