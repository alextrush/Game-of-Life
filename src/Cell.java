
public class Cell {
	protected int dieLow, dieHigh, birthLow, birthHigh, value;
	protected boolean alive, next;
	public Cell() {
		dieLow = 2;
		dieHigh = 4;
		birthLow = 3;
		birthHigh = 3;
		alive = true;
		next = true;
		value = 1;
	}

	public Cell( int birth, int die) {
		dieLow = die;
		dieHigh = die;
		birthLow = birth;
		birthHigh = birth;
		alive = true;
		next = true;
		value = 1;
	}

	public Cell(int birthLow, int birthHigh, int dieLow, int dieHigh ){
		this.birthLow = birthLow;
		this.birthHigh = birthHigh;
		this.dieLow = dieLow;
		this.dieHigh = dieHigh;
		alive = true;
		next = true;
	}
	
	public void nextStep( int adj ){
		if( alive ){
			if( adj < dieLow || adj > dieHigh ){
				next = false;
			}			
		} else {
			if( adj >= birthLow && adj <= birthHigh ){
				next = true;
			}
		}
	}
	
	public boolean step(){
		alive = next;
		return alive;
	}
	
	public boolean alive(){
		return alive;
	}
	
	public void set( boolean alive ){
		this.alive = alive;
		next = alive;
	}
	
	public void setLowBirth( int birthLow ){
		this.birthLow = birthLow;
	}

	public void setHighBirth( int birthHigh ){
		this.birthHigh = birthHigh;
	}
	
	public void overcrowd( int dieHigh ){
		this.dieHigh = dieHigh;
	}
	
	public void starve( int dieLow ){
		this.dieLow = dieLow;
	}

	public int value() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}