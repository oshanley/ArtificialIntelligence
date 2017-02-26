/* Olivia Shanley
CSC 380 Artificial Intelligence
Project 2 - War
Dr. Salgian, The College of New Jersey

The Player class keeps track of all of a player's properties throughout the game.
Such properties include the score, the algorithm that the player implements; the number of total moves
throughout the game, as well as the total time for gameplay; and the number of nodes
explored by the Minimax and Minimax with Alpha-beta pruning agents.*/

import java.util.ArrayList;

public class Player {
    int score;
    String playMethod;
    int totalMoves;
    int nodesExplored;
    long totalGameTime;

    public Player(String alg){
        this.score = 0;
        this.playMethod = alg;
        this.nodesExplored = 0;
        this.totalGameTime = 0;
        this.totalMoves = 0;
    }

    public Player(Player copy){
        this.score = copy.score;
        this.playMethod = copy.playMethod;
        this.nodesExplored = copy.nodesExplored;
    }

    public void updateScore(int increment){
        this.score += increment;
    }

    public void updateTime(long moveTime){
        this.totalGameTime+=moveTime;
    }

    public void incrementExplored(){
        this.nodesExplored++;
    }

    public void updateTotalMoves(){
        this.totalMoves++;
    }

    public String playMethod(){
        return this.playMethod;
    }

    public int getScore(){
        return this.score;
    }

    public long getGameTime(){
        return this.totalGameTime;
    }

    public int getTotalMoves(){
        return this.totalMoves;
    }

    public int getExplored(){
        return this.nodesExplored;
    }


}
