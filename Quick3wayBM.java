import java.util.Random;
import java.util.LinkedList;
import java.util.ListIterator;
import java.lang.Comparable;
public class Quick3wayBM {


	public static void main(String[] args) {
		int size = StdIn.readInt();
		Integer[] inputAry = new Integer[size];
		int i = 0;
		int input;
		while ( ! StdIn.isEmpty() ) {
			input = StdIn.readInt();
			inputAry[i++] = input;
		}
		sort(inputAry);
//		for ( int x : inputAry ) System.out.print(x+" ");
//		System.out.println();
	}

	public static void sort(Comparable[] ary) {
		sort(ary, 0, ary.length-1);
	}

	private static void sort(Comparable[] ary, int lo, int hi) {
		boolean lr = false; //which side do we put the == on?

		if ((hi-lo) < 8) {
	//		System.out.println("(Insertion_Sort, "+lo+", "+hi+")");
			Comparable[] tmp = new Comparable[hi-lo+1];
			for ( int i = 0; i < tmp.length; i++ ) tmp[i] = ary[i+lo];
			insertionSort(tmp);
			for ( int i = 0; i < tmp.length; i++ ) ary[i+lo] = tmp[i];
		}
		else {
			LinkedList<Comparable> left = new LinkedList<Comparable>(),
								   right = new LinkedList<Comparable>(),
								   lmiddle = new LinkedList<Comparable>(),
								   rmiddle = new LinkedList<Comparable>();

			Comparable pivot;
			String improvementUsed;
			if ( hi-lo+1 <= 40 ) {
				//median-of-three partitioning
				pivot = medianOfThree(ary, lo, hi);
				improvementUsed = "median of 3";
			} else {
				//Tukey Ninther
				Comparable[] possiblePivots = new Comparable[3];
				for ( int i = 0; i < 3; i++ ) {
					possiblePivots[i] = medianOfThree(ary, lo, hi);
				}
				insertionSort(possiblePivots);
				pivot = possiblePivots[1];
				improvementUsed = "Tukey Ninether";
			}

			for ( int i = lo; i <= hi; i++ ){
				if ( ary[i].compareTo(pivot) > 0 ) right.add(ary[i]);
				else if (ary[i].compareTo(pivot) < 0 ) left.add(ary[i]);
				else {
					lr = !lr;
					if (lr) rmiddle.add(ary[i]);
					else lmiddle.add(ary[i]);
				}
			}

			int j = left.size();
			int k = right.size();

			// output
			// (improvement_used, lo, v, p, k/i, j, q, hi)

	/*		System.out.println("("+improvementUsed+
					", "+lo+
					", "+lmiddle.size()+ //v
					", "+k+ //p
					", "+j+
					", "+rmiddle.size()+
					", "+hi+")");*/
			left.addAll(lmiddle);
			left.addAll(rmiddle);
			left.addAll(right);
			ListIterator iter = left.listIterator(0);
			int i = 0;
			while ( iter.hasNext() ) ary[lo+i++] = (Comparable)iter.next();
			sort(ary, lo, lo+j);
			sort(ary, hi-k, hi);
		}
	}


	private static Comparable medianOfThree(Comparable[] ary, int lo, int hi) {
		Comparable[] temp = new Comparable[3];
		Random rnd = new Random();
		for ( int i = 0; i < temp.length; i++) temp[i] = ary[lo+rnd.nextInt(hi-lo)];
		insertionSort(temp);
		return temp[1];
	}

	private static void insertionSort(Comparable[] ary) {
		LinkedList temp = new LinkedList<Comparable>();
		for ( int i = 0; i < ary.length; i++ ) {
			if ( temp.isEmpty() ) temp.add(ary[i]);
			else temp = insert(temp, ary[i]);
		}
		Comparable[] tempAry = new Comparable[ary.length];
		tempAry = (Comparable[])temp.toArray(new Comparable[ary.length]);
		for ( int i = 0; i < tempAry.length; i++ ) ary[i] = tempAry[i];
	}

	private static LinkedList<Comparable> insert (LinkedList<Comparable> ary, Comparable item) {
		ListIterator iterator = ary.listIterator(0);
		Comparable current;
		while ( iterator.hasNext() ) {
			current = (Comparable)iterator.next();
			int compare = item.compareTo(current);
			if ( compare > 0 ) {
				compare = item.compareTo(current);
			} else if ( compare == 0 ) {
				iterator.add(item);
				return ary;
			} else {
				iterator.previous();
				iterator.add(item);
				return ary;
			}
		}
		ary.add(item);
		return ary;
	}
}
