public class SpaceAwareCell extends Cell {
	protected int rotation;
	protected int frame;

	public SpaceAwareCell() {
		super();
	}

	public SpaceAwareCell(int birthLow, int birthHigh, int dieLow, int dieHigh) {
		super(birthLow, birthHigh, dieLow, dieHigh);
	}

	public SpaceAwareCell(int birth, int die) {
		super(birth, die);
	}

	public int rotation() {
		return rotation;
	}

	public int frame() {
		return frame;
	}

	 public void beAware( int adjNum ){
		frame = setFrame( adjNum );
		rotation = setOrientation( adjNum );
	}
	
	protected int setFrame( int numAdj ) {
		int adjNum = numAdj & 90;
		//flat grass surface
		if( adjNum == 88 || adjNum == 74 || adjNum == 26 || adjNum == 82 ){
			return 1;
		//flat on both sides grass
		} else if( adjNum == 24 || adjNum == 66 ){
			return 2;
		//outer corner
		} else if( adjNum == 72 || adjNum == 10 || adjNum == 18 || adjNum == 80 ){
			return 3;
		//bump
		} else if( adjNum == 64 || adjNum == 8 || adjNum == 2 || adjNum == 16 ){
			return 4;
		//connected from all directions
		} else if( adjNum == 90 ){
			int nonAdj = (numAdj  ^ 255) & 165;
			//1 corner
			if( nonAdj == 1 || nonAdj == 4 || nonAdj == 128 || nonAdj == 32 ){
				return 6;
			//2 corners
			} else if( nonAdj == 5 || nonAdj == 132 || nonAdj == 160 || nonAdj == 33 ){
				return 7;
			//3 corners
			} else if( nonAdj == 133 || nonAdj == 164 || nonAdj == 161 || nonAdj == 37 ){
				return 8;
			//4 corners
			} else if( nonAdj == 165 ){
				return 9;
			//entirely surrounded
			} else {
				return 0;
			}
		//nothing surrounding it
		} else {
			return 5;
		}
	}
	
	protected int setOrientation( int adjNum ){
		int numAdj = adjNum & 90;
		int nonAdj = (adjNum  ^ 255) & 165;
		if( numAdj == 74 || numAdj == 66 || numAdj == 10 || numAdj == 8 || (numAdj == 90 && nonAdj == 4) || (numAdj == 90 && nonAdj == 132) || (numAdj == 90 && nonAdj == 164) ){
			return 90;
		} else if ( numAdj == 26 || numAdj == 18 || numAdj == 2 || (numAdj == 90 && nonAdj == 128) || (numAdj == 90 && nonAdj == 160) || (numAdj == 90 && nonAdj == 161) ){
			return 180;
		} else if ( numAdj == 82 || numAdj == 80 || numAdj == 16 || (numAdj == 90 && nonAdj == 32) || (numAdj == 90 && nonAdj == 33) || (numAdj == 90 && nonAdj == 37) ){
			return 270;
		} else {
			return 0;
		}
	}
}