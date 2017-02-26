Olivia Shanley
CSC 380: Artificial Intelligence
Project II - War

List of Files:

./GameBoards/*.txt - The possible boards on which the game can be played
Game.java - The driver for game
Player.java - Class which creates Player objects. At any time, there are exactly two players.
Board.java - The board for the game, made up of Spaces. Keeps an omniscient view of which spaces are empty and which are owned by either Player.
Space.java - Class that holds properties for each space on the board. Able to see its surrounding neighbors.
Move.java - Allows a Player to attack a square. Implements random moves for a random player.
Minimax.java - Class to be used by a Player implementing the minimax algorithm.
AlphaBeta.java - Class to be used by a Player implementing the minimax with alpha-beta pruning algorithm.
Makefile - compiles and executes the program


To use:

    run 'make'
    Follow the prompts to select the game board to be used and the search algorithms for each player.

To clean up generated class files:

    run 'make clean'
