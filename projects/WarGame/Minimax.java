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

    public Space minimax(){
        max(initialState, maxDepth);

        return nextMove;
    }

    public int max(Board curBoard, int curDepth){
        int bestValue = Integer.MIN_VALUE;
        Board nextState;

        //if end of game or max depth reached, return differences between player scores
        if(curBoard.remainingMoves().size() == 0 || curDepth == 0){
            // if (maxPlayer.getScore()>minPlayer.getScore())
            //     return 10;
            // else return -10;
            return maxPlayer.getScore()-minPlayer.getScore();
        }
        else{

            //create separate state to mock out attack
            Board mockAttackBoard = new Board(curBoard);
            Player mockMaxPlayer = new Player(maxPlayer);
            Player mockMinPlayer = new Player(minPlayer);

            //loop through all possible moves
            for (Space sp : curBoard.remainingMoves()){

                //mock out an attack
                Move MockAttack = new Move(mockAttackBoard, mockMaxPlayer, mockMinPlayer);
                nextState = mockAttackBoard;

                //perform min on next state
                int v = min(nextState, curDepth-1);

                //if the current move is the best move, set as next move
                if(v>bestValue){
                    nextMove = sp;
                    bestValue = v;
                }
            }
            return bestValue;
        }
    }

    public int min(Board curBoard, int curDepth){
        int bestValue = Integer.MAX_VALUE;
        Board nextState;

        //if end of game, determine winner
        if(curBoard.remainingMoves().size() == 0 || curDepth == 0){
            /*if (maxPlayer.getScore()>minPlayer.getScore())
                return 10;
            else return -10;*/
            return maxPlayer.getScore()-minPlayer.getScore();
        }
        else{

            //create separate state to mock out attack
            Board mockAttackBoard = new Board(curBoard);
            Player mockMaxPlayer = new Player(maxPlayer);
            Player mockMinPlayer = new Player(minPlayer);

            //loop through all possible moves of the current board state
            for (Space sp : curBoard.remainingMoves()){

                Move MockAttack = new Move(mockAttackBoard, mockMaxPlayer, mockMinPlayer);
                nextState = mockAttackBoard;

                //perform max on next state
                int v = max(nextState, curDepth-1);

                //if the current move is the best move, set as next move
                if(v<bestValue){
                    bestValue = v;
                    nextMove = sp;
                }
            }
            return bestValue;
        }
    }
}
