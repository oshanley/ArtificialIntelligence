/* Olivia Shanley
CSC 380 Artificial Intelligence
Project 2 - War
Dr. Salgian, The College of New Jersey

The AlphaBeta class creates an object which implements a Minimax with Alpha-Beta Pruning algorithm.
It keeps track of the initial state of the board, as well as performs the Alpha-beta pruning algorithm.
Once finished, it returns the Space to be attacked by the current player. */

public class AlphaBeta{

    Board initialState;
    Player maxPlayer;
    Player minPlayer;
    Space nextMove;
    int maxDepth;
    int alpha;
    int beta;

    public AlphaBeta(Board board, int maxDepth, Player curPlayer, Player opponent){
        this.initialState = new Board(board);
        this.maxDepth = maxDepth;
        this.maxPlayer = curPlayer;
        this.minPlayer = opponent;
        this.alpha = Integer.MIN_VALUE;;
        this.beta = Integer.MAX_VALUE;
    }

    public int max(Board curBoardState, int curDepth){
        int bestValue = Integer.MIN_VALUE;
        Board nextBoardState;

        //if end of game or max depth reached, return differences between player scores
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

            //loop through all possible moves
            for (Space sp : curBoardState.remainingMoves
            ()){

                //mock out an attack and save the state of the board
                Move mockAttack = new Move(mockAttackBoard, mockMaxPlayer, mockMinPlayer);

                mockAttack.attack(sp);
                nextBoardState = mockAttackBoard;

                //perform min on next state
                int v = min(nextBoardState, curDepth-1);
                if(v > bestValue){
                    bestValue = v;
                    nextMove = sp;
                }
                if (bestValue > alpha)
                    alpha = bestValue;

                //beta cutoff detected
                if(beta <= alpha){
                    break;
                }
            }
            return bestValue;
        }
    }

    public int min(Board curBoardState, int curDepth){
        int bestValue = Integer.MAX_VALUE;
        Board nextBoardState;

        //if end of game or max depth reached, return differences between player scores
        if(curBoardState.remainingMoves().size() == 0 || curDepth == 0){

            if (maxPlayer.getScore()>minPlayer.getScore())
                return 10;
            else return -10;
            //return maxPlayer.getScore()-minPlayer.getScore();
        }
        else{
            //increment the number of nodes explored
            maxPlayer.incrementExplored();

            //create separate state to mock out attack
            Board mockAttackBoard = new Board(curBoardState);
            Player mockMaxPlayer = new Player(maxPlayer);
            Player mockMinPlayer = new Player(minPlayer);

            //loop through all possible moves
            for (Space sp : curBoardState.remainingMoves()){

                //mock out an attack and save the state of the board
                Move mockAttack = new Move(mockAttackBoard, mockMaxPlayer, mockMinPlayer);
                mockAttack.attack(sp);
                nextBoardState = mockAttackBoard;

                //perform min on next state
                int v = max(nextBoardState, curDepth-1);
                if(v < bestValue){
                    bestValue = v;
                    nextMove = sp;
                }
                if (bestValue < alpha)
                    alpha = bestValue;

                //alpha cutoff detected
                if(beta <= alpha){
                    break;
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
