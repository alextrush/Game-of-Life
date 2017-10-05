import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class Grid implements Iterable<Cell>{
	protected Cell[][] cells;
	public Grid(){
	}
	
	public Grid( int width, int height ) {
		cells = new Cell[height][width];
		for( int i =0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				cells[i][j] = new Cell(1, 3, 7, 8);
			}
		}
	}

	public Cell[][] cells(){
		return cells;
	}
	
	public void set( int x, int y, boolean alive ){
		if( cells != null ){
			cells[(y+cells.length)%cells.length][(x+cells[0].length)%cells[0].length].set(alive);
		}
	}
	
	public int value( int i, int j ){
		if( cells == null ){
			return 0;
		}
		return cells[i][j].value();
	}
	
	public boolean alive( int x, int y ){
		if( cells != null ){
			x = x % cells[0].length;
			y = y % cells.length;
			return cells[y][x].alive();
		} else { 
			return false;
		}
	}
	
	public void next(){
		if( cells != null ){
			for( int i = 0 ; i < cells.length ; i++ ){
				for( int j = 0 ; j < cells[i].length ; j++ ){
					cells[i][j].nextStep( adjTo(i,j) );
				}
			}
			for( int i = 0 ; i < cells.length ; i++ ){
				for( int j = 0 ; j < cells[i].length ; j++ ){
					cells[i][j].step();
				}
			}
		}
	}
	
	public int[] store(){
		if( cells == null || cells.length < 1 ){
			return null;
		}
		int width = cells[0].length;
		int height = cells.length;
		int i = 0;
		int j = 0;
		int[] temp = new int[cells.length*cells[0].length/32+1];
		System.out.println(temp.length);
		return temp;
	}
	
	protected int adjTo( int i, int j ){
		if ( cells != null ){
			int sum = 0;
			int left = (j - 1 + cells[0].length) % cells[0].length;
			int right = (j + 1) % cells[0].length;
			int up = (i - 1 + cells.length) % cells.length;
			int down = (i + 1) % cells.length;
			if(cells[up][left].alive()) sum++;
			if(cells[i][left].alive()) sum++;
			if(cells[down][left].alive()) sum++;
			if(cells[up][j].alive()) sum++;
			if(cells[down][j].alive()) sum++;					
			if(cells[up][right].alive()) sum++;
			if(cells[i][right].alive()) sum++;	
			if(cells[down][right].alive()) sum++;						
			return sum;
		} else {
			return 0;
		}
	}	
	
	public Iterable<Cell> adj( int i, int j ){
		if( cells == null ){
			return null;
		}
		ArrayList<Cell> adj = new ArrayList<Cell>();
		if( i > 0 ){
			adj.add( cells[i-1][j] );
			if( j > 0 ){
				adj.add( cells[i][j-1] );
				adj.add( cells[i-1][j-1] );
			}
			if( j < cells[i].length - 1 ){
				adj.add( cells[i-1][j+1] );
				adj.add( cells[i][j+1] );
			}
		}
		if( i < cells.length - 1 ){
			adj.add( cells[i+1][j] );
			if( j > 0 ){
				adj.add( cells[i+1][j-1] );
			}
			if( j < cells[i].length - 1 ){
				adj.add( cells[i+1][j+1] );
			}
		}
		return adj;
	}

	public Iterable<Cell> adjWrap( int i, int j ){
		if( cells == null ){
			return null;
		}
		ArrayList<Cell> adj = new ArrayList<Cell>();
		int left = (j - 1 + cells[0].length) % cells[0].length;
		int right = (j + 1) % cells[0].length;
		int up = (i - 1 + cells.length) % cells.length;
		int down = (i + 1) % cells.length;
		adj.add( cells[up][j] );
		adj.add( cells[i][left] );
		adj.add( cells[up][left] );
		adj.add( cells[up][right] );
		adj.add( cells[i][right] );
		adj.add( cells[down][j] );
		adj.add( cells[down][left] );
		adj.add( cells[down][right] );
		return adj;
	}
	
	public void clear() {
		for(int i = 0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				set( j, i, false );
			}
		}
	}

	public void fill() {
		for(int i = 0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				set( j, i, true );
			}
		}
	}

	public void randomize() {
		Random r = new Random();
		int num = 0;
		for(int i = 0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				num = r.nextInt(100);
				set( j, i, num % 2 == 0 );
			}
		}
	}

	public void randomize( int weighting ) {
		Random r = new Random();
		int num = 0;
		for(int i = 0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				num = r.nextInt(100 * weighting);
				set( j, i, num % weighting == 0 );
			}
		}
	}

	public Iterator<Cell> iterator() {
		ArrayList<Cell> list = new ArrayList<Cell>();
		for(int i = 0 ; i < cells.length ; i++ ){
			for( int j = 0 ; j < cells[i].length ; j++ ){
				list.add( cells[i][j] );
			}
		}
		return list.iterator();
	}

	public Point2D.Float find( Cell cell ){
		if( cells == null ){
			return null;
		}
		int i = 0, j = 0;
		while ( i < cells.length ){
			if( cells[i] == null ){
				return null;
			}
			j = 0;
			while( j < cells[i].length ){
				if( cells[i][j] == cell ){
					return new Point2D.Float(i, j);
				}
				j++;
			}
			i++;
		}
		return null;
	}
	
	public String toString(){
		String str = "";
		for( int i =0 ; i < cells.length ; i++ ){
			str += "[";
			for( int j = 0 ; j < cells[i].length ; j++ ){
				str += (cells[i][j].alive()) ? "1" : "0";
				if( j < cells[i].length -1 ){
					str += ", ";
				}
			}
			str += "]\n";
		}
		return str;
	}
	
	public static void main(String[] args) {
		Grid g = new Grid( 4, 3 );
		g.set(1, 1, true);
		int steps = 20;
		System.out.println(g);
		System.out.println();
		for( int i = 0 ; i < steps ; i++ ){
			g.next();
			System.out.println(g);
			System.out.println();
		}
	}
}