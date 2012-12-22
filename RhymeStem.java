public class RhymeStem implements Comparable {

	private String value;

	public RhymeStem(String value) {
		this.value = value;
	}


	public String getValue () {
		return this.value;
	}
	public int compareTo ( Object o ) {
		String oval = ((RhymeStem)o).getValue();
		if ( value.length() == oval.length() ) {
			return value.compareTo(oval);
		} else {
			if ( value.length() == oval.length()) return 0;
			else if ( value.length() < oval.length() ) return -1;
			else if ( value.length() > oval.length() ) return 1;
		}
		return -1;
	}
}
