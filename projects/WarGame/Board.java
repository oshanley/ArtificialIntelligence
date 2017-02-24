import java.util.*;
import java.io.*;

public class Board{
    private Space[][] map;

    public Board(File boardChoice){
        this.map = parseMap(boardChoice);
    }

    public Board(Board copy){
        int row;
        int col;
        final int colSize = copy.map[0].length;
        final int rowSize = copy.map.length;

        this.map = new Space[rowSize][colSize];
        for(row = 0; row < rowSize; row++){
            for (col = 0; col < colSize; col++){
                this.map[row][col] = new Space(copy.map[row][col]);
            }
        }
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
            String line = "";
            int i = 1;

			board = new Space[rowSize][colSize];

            //read in from file and initialize map Spaces
			while((line = br.readLine()) != null){
                String[] nums = line.split("\\s+");
                for(String c : nums){
                    int[] coords = new int[2];
    				coords[0] = row;
    				coords[1] = col;
    				board[row][col] = new Space(Integer.parseInt(c), coords);
                    System.out.print(Integer.parseInt(c) + " ");
                    col++;
                }
                i++;
                row++;
                col = 0;
            	System.out.println();
            }
			//place console prompt on new line
			System.out.println();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}

    public Space[][] getBoard(){
        return this.map;
    }

    public ArrayList<Space> remainingMoves(){
		int row, col;
        ArrayList<Space> remainingSpaces = new ArrayList<>();

		for(row = 0; row < map.length; row++){
			for(col = 0; col < map[0].length; col++){
				if (!(map[row][col].isOccupied())){
                    remainingSpaces.add(map[row][col]);
				}
			}

		}
		return remainingSpaces;
	}

    public ArrayList<Space> getOwnedSpaces(Player curPlayer){
        ArrayList<Space> owned = new ArrayList<>();
        int row, col;

		for(row = 0; row < map.length; row++){
			for(col = 0; col < map[0].length; col++){
				if (map[row][col].occupiedBy() == curPlayer){
                    owned.add(map[row][col]);
				}
			}
		}

        return owned;
    }
}
