import java.util.*;
import java.io.*;

public class Board{
    int remainingSpaces;
    Space[][] map;

    public Board(File boardChoice){
        this.map = parseMap(boardChoice);
        this.remainingSpaces = remainingMoves().size();
    }

    public Space[][] getBoard(){
        return this.map;
    }

    public Space[][] parseMap(File file) {
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

    public ArrayList<Space> remainingMoves(){
		int freeSpaces = 0;
		int row, col;
        ArrayList<Space> remainingSpaces = new ArrayList<>();

		for(row = 0; row < map.length; row++){
			for(col = 0; col < map[0].length; col++){
				if (!map[row][col].isOccupied()){
                    remainingSpaces.add(map[row][col]);
					freeSpaces++;
				}
			}

		}
		System.out.println("Remaining spaces: " + freeSpaces);
		return remainingSpaces;

	}
}
