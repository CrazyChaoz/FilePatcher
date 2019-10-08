public
class Change implements Comparable<Change> {

	public long position;
	public ChangeType type;
	public byte data;

	public Change(long position, ChangeType type, byte data) {
		this.position = position;
		this.type = type;
		this.data = data;
	}

	@Override
	public int compareTo(Change o) {
		return (int) ((position - o.position) % Integer.MAX_VALUE); //FIXME: error when add/delete on same position
	}

	@Override
	public String toString() {
		return "\nChange{" +
				"position=" + position +
				", type=" + type +
				", data=" + (char) data +
				"}\n";
	}
}
