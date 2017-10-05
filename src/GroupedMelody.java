import java.util.ArrayList;
import java.util.Random;

public class GroupedMelody<E> {
	protected ArrayList<E> seed;
	protected ArrayList<E> group;
	protected static Random gen;
	
	public GroupedMelody(){
		seed = new ArrayList<E>();
		group = new ArrayList<E>();
		gen = new Random();
	}
	
	public GroupedMelody( Iterable<E> seed ){
		this();
		for( E item : seed ){
			this.seed.add( item );
		}
	}
	
	public void expandSeed( E item ){
		this.seed.add( item );
	}
	
	public void expandSeed( Iterable<E> items ){
		for( E item : items ){
			this.seed.add( item );
		}
	}
	
	
	
	public Iterable<E> generate( int length, int groupSize ){
		ArrayList<E> temp = new ArrayList<E>();
		if( groupSize < 1 || length < 1 ){
			return temp;
		}
		if( group.size() != groupSize ){
			group = newGroup( groupSize );
		}
		int initial = Math.min( length, group.size() );
		for( int i = 0 ; i < initial ; i++ ){
			temp.add( group.get( i ) );
		}
		while( temp.size() < length ){
			temp.add( next() );
		}
		return temp;
	}
	
	protected ArrayList<E> newGroup( int groupSize ) {
		ArrayList<E> list = new ArrayList<E>();
		if ( seed.size() <= 0 ) {
			return list;
		}
		int index = gen.nextInt( seed.size() );
		while( list.size() < groupSize ){
			list.add( seed.get( index ) );
			index = ( index + 1 ) % seed.size();
		}
		return list;
	}

	protected E next(){
		if( group.size() == 0 || seed.size() == 0 ){
			return null;
		}
		int index = gen.nextInt( seed.size() );
		int start = index, matches = 0;
		boolean looped = false;
		System.out.print( "Queue: " );
		for( E item : group ){
			System.out.print( item + "\t" );
		}
		//walk down until we find a matching first element
		do{
			//loop until all elements match
			while ( matches < group.size() ) {
				//we don't have a match so stop trying
				if( !seed.get( ( index + matches ) % seed.size() ).equals( group.get( matches ) ) ){
					break;
				} else {
					if( index > ( index + 1 ) % seed.size() ){
						looped = true;
					}
					matches++;
				}
			}
			if( matches >= group.size() ){
				//add to the group
				System.out.println( "Position " + index + ": Add " + seed.get( index ) );
				E temp = seed.get( index );
				addToGroup( temp );
				return temp;
			}
			if( index > ( index + 1 ) % seed.size() ){
				looped = true;
			}
			index = ( index + 1 ) % seed.size();
		} while( index <= start || !looped || matches > 0 );
		return null;
	}
	
	protected void addToGroup( E elem ){
		if( group.size() == 0 || seed.size() == 0 ){
			return;
		}
		group.add( elem );
		group.remove( 0 );
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> test = new ArrayList<Integer>();
		Random gen = new Random();
		int temp;
		for( int i = 0 ; i < 10 ; i++ ){
			temp = gen.nextInt( 12 ) + 1;
			test.add( temp );
			System.out.print( temp + "  " );
		}
		System.out.println();
		GroupedMelody<Integer> melGen = new GroupedMelody<Integer>();
		GroupedMelody<Integer> melGen2 = new GroupedMelody<Integer>(test);
		test = melGen2.newGroup( 5 );
		for( Integer item : melGen2.generate( 15, 3 ) ){
			System.out.print( item + "\t" );
		}
	}
}