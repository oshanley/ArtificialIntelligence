public class Space {
        private int points;
        private boolean occupied;
        private int[] coords;
        private Space previousMove;


        public Space (int points, int[] coords){
          this.points = points;
          this.coords = coords;
        }

        /* Setters */

        public void setOccupied(boolean occupied){
            this.occupied = occupied;
        }

        public void setCoords(int[] coords){
            this.coords = coords;
        }

        public void setPreviousMove(Space previousMove){
            this.previousMove = previousMove;
        }

        /* Getters */
        
        public int getPoints() {
			return this.points;
		}

        public boolean isOccupied(){
            return this.occupied;
        }

        public int[] getCoords() {
			return this.coords;
		}

        public Space getPreviousMove(){
            return this.previousMove;
        }

    }
