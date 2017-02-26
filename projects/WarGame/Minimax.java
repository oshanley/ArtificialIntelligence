/* Olivia Shanley
CSC 380 Artificial Intelligence
Project 2 - War
Dr. Salgian, The College of New Jersey

The Minimax class creates the minimax agent object. It keeps track of the initial
state of the board, as well as performs the minimax algorithm. Once finished, it
returns the Space to be attacked by the current player. */
import java.lang.Math;
import java.util.ArrayList;

public class Minimax{
    Board initialState;
    Player maxPlayer;
    Player minPlayer;
    Space nextMove;
    int maxDepth;

    public Minimax(Board board, int maxDepth, Player curPlayer, Player opponent){
        this.initialState = new Board(board);
        this.maxDepth = maxDepth;
        this.maxPlayer = curPlayer;
        this.minPlayer = opponent;
    }

    public Board getInitialState(){
        return this.initialState;
    }

    public int max(Board curBoardState, int curDepth){
        int bestValue = Integer.MIN_VALUE;
        Board nextBoardState;

        //if end of game or max depth reached, return differences between player scores
        if(curBoardState.remainingMoves().size() == 0 || curDepth == 0){

            if (maxPlayer.getScore()>minPlayer.getScore())
                return 10;
            else return -10;
            //return maxPlayer.getScore()-minPlayer.getScore();
        }
        else{
            //increment explored nodes
            maxPlayer.incrementExplored();

            //create separate state to mock out attack
            Board mockAttackBoard = new Board(curBoardState);
            Player mockMaxPlayer = new Player(maxPlayer);
            Player mockMinPlayer = new Player(minPlayer);

            //loop through all possible moves
            for (Space sp : curBoardState.remainingMoves
            ()){

                //mock out an attack and save the state of the board
                Move mockAttack = new Move(mockAttackBoard, mockMaxPlayer, mockMinPlayer);

                mockAttack.attack(sp);
                nextBoardState = mockAttackBoard;

                //perform min on next state
                int v = min(nextBoardState, curDepth-1);

                //if the current move is the best move, set as next move
                if(v>bestValue){
                    nextMove = sp;
                    bestValue = v;
                }
            }
            return bestValue;
        }
    }

    public int min(Board curBoardState, int curDepth){
        int bestValue = Integer.MAX_VALUE;
        Board nextBoardState;

        //if end of game, determine winner
        if(curBoardState.remainingMoves().size() == 0 || curDepth == 0){

            if (maxPlayer.getScore()>minPlayer.getScore())
                return 10;
            else return -10;
        }
        else{
            //increment explored nodes
            maxPlayer.incrementExplored();

            //create separate state to mock out attack
            Board mockAttackBoard = new Board(curBoardState);
            Player mockMaxPlayer = new Player(maxPlayer);
            Player mockMinPlayer = new Player(minPlayer);

            //loop through all possible moves of the current board state
            for (Space sp : curBoardState.remainingMoves()){

                //mock out an attack and save the state of the board
                Move mockAttack = new Move(mockAttackBoard, mockMaxPlayer, mockMinPlayer);

                mockAttack.attack(sp);

                nextBoardState = mockAttackBoard;
                //perform max on next state
                int v = max(nextBoardState, curDepth-1);

                //if the current move is the best move, set as next move
                if(v<bestValue){
                    bestValue = v;
                    nextMove = sp;
                }
            }
            return bestValue;
        }
    }

    public Space minimax(){
        System.out.println("Looking ahead at the following attacks: ");
        System.out.println("----------------");
        max(initialState, maxDepth);
        System.out.println("----------------");

        return nextMove;
    }
}
