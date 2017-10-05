import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Iterator;

public class LevelRandomizer extends Grid{
	public LevelRandomizer(int width, int height) {
		cells = new SpaceAwareCell[height][width];
		for( int i =0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				cells[i][j] = new SpaceAwareCell(3, 5, 3, 4 );
			}
		}
	}

	protected int adjNum( int i, int j ){
		if ( cells != null ){
			int num = 0;
			int left = (j - 1 + cells[0].length) % cells[0].length;
			int right = (j + 1) % cells[0].length;
			int up = (i - 1 + cells.length) % cells.length;
			int down = (i + 1) % cells.length;
			if(cells[up][left].alive()) num += 1;
			if(cells[up][j].alive()) num += 2;
			if(cells[up][right].alive()) num += 4;
			if(cells[i][left].alive()) num += 8;
			if(cells[i][right].alive()) num += 16;					
			if(cells[down][left].alive()) num += 32;
			if(cells[down][j].alive()) num += 64;	
			if(cells[down][right].alive()) num += 128;						
			return num;
		} else {
			return 0;
		}
	}

	public int adjNum( Cell cell ){
		Float point = find( cell );
		return adjNum( (int)point.x, (int)point.y );
	}
	
	public void set( int x, int y, boolean alive ){
		super.set(x, y, alive);
		y %= cells.length;
		x %= cells[y].length;
		if( cells != null ){
			((SpaceAwareCell) cells[y][x]).beAware( adjNum( y, x ) );
			for( Cell cell : adjWrap( y, x ) ){
				if( cell instanceof SpaceAwareCell ){
					((SpaceAwareCell) cell).beAware( adjNum( cell ) );
				}				
			}
		}
	}
	
	public void next(){
		super.next();
		if( cells != null ){
			for( int i = 0 ; i < cells.length ; i++ ){
				for( int j = 0 ; j < cells[i].length ; j++ ){
					if( cells[i][j] instanceof SpaceAwareCell ){
						((SpaceAwareCell)cells[i][j]).beAware( adjNum( i, j ) );
					}
				}
			}
		}
	}
	
	public Iterator<Cell> iterator() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		for(int i = 0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				if( cells[i][j] instanceof Cell ){
					list.add( (Cell)cells[i][j] );
				}
			}
		}
		return list.iterator();
	}
	
	public int showFrame( int i, int j ){
		if( cells[i][j] instanceof SpaceAwareCell ){
			return ((SpaceAwareCell)cells[i][j]).frame();
		} else { 
			return 0;
		}
	}
	
	public int showRotation( int i, int j ){
		if( cells[i][j] instanceof SpaceAwareCell ){
			return ((SpaceAwareCell)cells[i][j]).rotation();
		} else { 
			return 0;
		}
	}
}