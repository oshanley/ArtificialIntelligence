import java.util.ArrayList;

public class Space {
        private int val;
        private boolean occupied;
        private int[] coords;
        private Space previousMove;
        private Player occupiedBy;


        public Space (int value, int[] coords){
          this.val = value;
          this.coords = coords;
        }

        public Space (Space toCopy){
            this.val = toCopy.val;
            this.occupied = toCopy.occupied;
            this.coords = toCopy.coords;
            this.previousMove = toCopy.previousMove;
            this.occupiedBy = toCopy.occupiedBy;
        }

        /* Setters */

        //public void isOccupied(boolean bool){
        //    this.occupied = bool;
        //}

        public void setOccupier(Player player){
            this.occupiedBy = player;
            this.occupied = true;
        }

        public void setCoords(int[] coords){
            this.coords = coords;
        }

        public void setPreviousMove(Space previousMove){
            this.previousMove = previousMove;
        }

        /* Getters */

        public int value() {
			return this.val;
		}

        public boolean isOccupied(){
            return this.occupied;
        }

        public Player occupiedBy(){
            return this.occupiedBy;
        }

        public int[] getCoords() {
			return this.coords;
		}

        public Space getPreviousMove(){
            return this.previousMove;
        }

        /* methods */
        public ArrayList<Space> freeNeighbors(Space[][] map){
            ArrayList<Space> validMoves = new ArrayList<Space>();

            //check bounds, then determine if a neighbor is valid
            int curRow = this.coords[0];
            int curCol = this.coords[1];

            if (curRow > 0){
                Space up = map[curRow-1][curCol];
                if(!(up.isOccupied())){
                    up.setPreviousMove(this);

                    validMoves.add(up);
                }
            }
            if(curRow < map.length-1){
                Space down = map[curRow+1][curCol];
                if(!(down.isOccupied())){
                    down.setPreviousMove(this);

                    validMoves.add(down);
                }
            }
            if(curCol > 0){
                Space left = map[curRow][curCol-1];
                if(!(left.isOccupied())){
                    left.setPreviousMove(this);

                    validMoves.add(left);
                }
            }
            if(curCol < map[0].length-1){
                Space right = map[curRow][curCol+1];
                if(!(right.isOccupied())){
                    right.setPreviousMove(this);

                    validMoves.add(right);
                }
            }

            return validMoves;
        }

        public ArrayList<Space> neighborsOwnedBy(Space[][] map, Player curPlayer){
            ArrayList<Space> owned = new ArrayList<Space>();

            //check bounds, then determine if a neighbor is valid
            int curRow = this.coords[0];
            int curCol = this.coords[1];

            if (curRow > 0){
                Space up = map[curRow-1][curCol];
                if((up.isOccupied()) && (up.occupiedBy()==curPlayer)){
                    owned.add(up);
                }
            }
            if(curRow < map.length-1){
                Space down = map[curRow+1][curCol];
                if((down.isOccupied()) && (down.occupiedBy()==curPlayer)){
                    owned.add(down);
                }
            }
            if(curCol > 0){
                Space left = map[curRow][curCol-1];
                if((left.isOccupied()) && (left.occupiedBy()==curPlayer)){
                    owned.add(left);
                }
            }
            if(curCol < map[0].length-1){
                Space right = map[curRow][curCol+1];
                if((right.isOccupied()) && (right.occupiedBy()==curPlayer)){
                    owned.add(right);
                }
            }

            return owned;
        }

    }
