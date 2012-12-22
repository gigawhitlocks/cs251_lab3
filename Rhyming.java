import java.util.LinkedList;
import java.lang.StringBuilder;
import java.util.ListIterator;
import java.util.Collections;

public class Rhyming {


	private static RhymingNode rhymingTree;
	public static void main( String[] args ) {
//		String[] testArray = { "clinic", "ethnic", "electrics", "metrics",
//			"coughed","laughed","friendly","rapidly","murky","lucky"};

		LinkedList<String> tempList = new LinkedList<String>();
		while (! StdIn.isEmpty() ) {
			tempList.add(StdIn.readString());
		}
		String[] testArray = new String[tempList.size()];
		testArray = tempList.toArray(testArray);



		rhymingTree = buildTree(testArray);
//		System.out.println(printTree(rhymingTree,0));
		RhymeStem[] rhymesarray = findRhymeStems(rhymingTree);
		Quick3wayBM.sort(rhymesarray);
		for (RhymeStem i : rhymesarray) {
			System.out.print("[ ");
			String[] current = allChildrenOf(findNodeWithValue(rhymingTree, i.getValue()), i.getValue());
			//output.add(tree.getValue().substring(0, tree.getValue().length()-str.length())+"|"+str);
			Quick3wayBM.sort(current);
			for ( int s = 0; s < current.length; s++)
				current[s] = current[s].substring(0, current[s].length()-i.getValue().length())+"|"+i.getValue();
			for ( int s = 0; s < current.length; s++) {
				if (s < current.length-1) System.out.print(current[s]+", ");
				else System.out.println(current[s]+" ]");
			}
		}

	}


	private static boolean doesRhyme( String str1, String str2 ) {
		if ( str1.equals("") || str2.equals("") ) return true;
		else return !findRhyme(str1, str2).equals("");
	}


	private static String findRhyme (String str1, String str2) {
		StringBuilder workingsb = new StringBuilder();
		char[] chary1, chary2;


		//assign shorter string to chary1
		//and longer string to chary2

		if ( str1.length() > str2.length() ) {
			chary1 = str2.toCharArray();
			chary2 = str1.toCharArray();
		} else {
			chary1 = str1.toCharArray();
			chary2 = str2.toCharArray();
		}


		// iterate through backwards looking for the rhyme

		int offset = chary2.length - chary1.length;
		for ( int i = chary1.length-1; i >= 0; i-- ) {
			if ( chary1[i] == chary2[i+offset] )
				workingsb.insert(0,chary1[i]);
			else break;
		}

		return workingsb.toString();
	}

	private static RhymingNode buildTree ( String[] inputs ) {
		rhymingTree = new RhymingNode("");
		for ( int i = 0; i < inputs.length; i++ ) {
			addNode(rhymingTree, inputs[i]);
		}
		Rhyming.rhymingTree = rhymingTree;
		return rhymingTree;
	}

	// by the time we're here, we know the input rhymes with the parent
	private static void addNode ( RhymingNode tree, String input ) {
//		System.out.println("ADDING "+input);
		LinkedList<RhymingNode> children = tree.getChildren();

//		System.out.println(printTree(tree,0)+"-----------------------------------\n");
		// if we're viewing a leaf, just add the child.
		if ( !tree.hasChildren() )  {
			tree.addChild(input);

		}

		else {
			ListIterator<RhymingNode> iter = children.listIterator();
			String bestrhyme = "";
			RhymingNode bestNode = new RhymingNode("");
			while ( iter.hasNext() ) {
				RhymingNode current = iter.next();
				String rhyme = findRhyme(input, current.getValue());
				if ( bestrhyme.length() < rhyme.length() ) {
					bestrhyme = rhyme;
					bestNode = current;
				}

			}

			if (!bestrhyme.equals("")) {
				if ( bestrhyme.equals(tree.getValue())) {
					tree.addChild(input);
					return;
				}
					//input rhymes with the current node
				if ( bestNode.hasChildren() && ( bestrhyme.equals(bestNode.getValue())) ) {
					//the node is a rhyming stem, not a leaf
					//recurse!
					addNode(bestNode, input);
					return;
				} else {
					//the node is a leaf
					//create a rhyming node
					RhymingNode rnode = new RhymingNode(bestrhyme);
					//add current and item to the rhyming node
					rnode.addChild(bestNode);
					rnode.addChild(input);
					//add the rhyming node to the tree
					tree.addChild(rnode);
					//remove current from the tree
					tree.rmChild(bestNode);
					return;
				}
			}
			// no rhyme found amongst children
			//
//			System.out.println("rhyme not found: "+input);
			tree.addChild(input);
		}



	}

	private static RhymeStem[] findRhymeStems (RhymingNode tree) {
		LinkedList<RhymeStem> outList = new LinkedList<RhymeStem>();

		if (tree.hasChildren()) {
			RhymingNode current;
			if (tree.getValue() != "" ) outList.add(new RhymeStem(tree.getValue()));
			LinkedList<RhymingNode> children = tree.getChildren();
			ListIterator<RhymingNode> iter = children.listIterator();
			while ( iter.hasNext() ) {
				current = iter.next();
				for ( RhymeStem i : findRhymeStems(current) )
					outList.add(i);
			}
		}

		RhymeStem[] output = new RhymeStem[outList.size()];
		output = outList.toArray(output);
		return output;

	}

	private static String printTree(RhymingNode tree, int depth) {
		String output = "";
		if ( !tree.hasChildren() ) {
				for ( int i = 1 ; i < depth; i++ ) output += "  ";
				return output+tree+" "+tree.getValue()+"\n";
		}
		else {
			LinkedList<RhymingNode> children = tree.getChildren();
			ListIterator<RhymingNode> iter = children.listIterator();
			depth++;
			while (iter.hasNext()) {
				for ( int i = 1 ; i < depth; i++ ) output += "  ";
				output = output+tree+" "+tree.getValue()+"\n"+
					printTree(iter.next(), depth+1);
			}
			return output;
		}

	}

	private static String[] allChildrenOf ( RhymingNode tree , String str ) {
		LinkedList<String> output = new LinkedList<String>();
		if ( ! tree.hasChildren() ) {
			output.add(tree.getValue());
			//output.add(tree.getValue().substring(0, tree.getValue().length()-str.length())+"|"+str);
		} else {
			LinkedList<RhymingNode> children = tree.getChildren();
			ListIterator<RhymingNode> iter = children.listIterator();
			while (iter.hasNext()) {
				for ( String s : allChildrenOf(iter.next(), str)) output.add(s);
			}

		}


		String[] retval = new String[output.size()];
		retval = output.toArray(retval);
		return retval;
	}

	private static RhymingNode findNodeWithValue ( RhymingNode tree, String str ) {
		if ( tree.getValue().equals(str) ) return tree;
		else if ( tree.hasChildren() ){
			LinkedList<RhymingNode> children = tree.getChildren();
			ListIterator<RhymingNode> iter = children.listIterator();
			RhymingNode out;
			while ( iter.hasNext() ) {
				out = findNodeWithValue( iter.next(), str );
				if (out != null) return out;
			}
		}
		return null;
	}

	private static void printRhymes(RhymeStem[] ary) {
		for ( int i = 0; i < ary.length; i++) {


		}
	}

}
