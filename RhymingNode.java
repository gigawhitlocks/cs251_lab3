import java.util.LinkedList;
import java.util.ListIterator;

public class RhymingNode {

	private LinkedList<RhymingNode> children;
	private String contents;
	private boolean stem;

	public RhymingNode( String _contents ) {
		this.contents = _contents;
		this.children = new LinkedList<RhymingNode>();
	}

	public LinkedList<RhymingNode> getChildren() {
		return children;
	}

	public String getValue() {
		return contents;
	}

	public void addChild( String newChild ) {
		RhymingNode newNode = new RhymingNode(newChild);
		this.children.add(newNode);
	}

	public void addChild( RhymingNode node) {
		this.children.add(node);
	}

	public void rmChild ( RhymingNode node ) {
		this.children.remove(node);
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public boolean isStem() {
		return this.stem;
	}

	public void setStem( boolean stem ) {
		this.stem = stem;
	}

}
