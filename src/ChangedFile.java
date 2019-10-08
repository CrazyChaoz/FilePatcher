import java.util.Set;
import java.util.TreeSet;

public class ChangedFile implements Comparable<ChangedFile> {

	String fileName;
	Set<Change> changes = new TreeSet<>();


	public ChangedFile(String fileName) {
		this.fileName = fileName;
	}

	public void addChange(ChangeType type, long position, byte data) {
		changes.add(new Change(position, type, data));
	}

	@Override
	public int compareTo(ChangedFile o) {
		return fileName.compareTo(o.fileName);
	}

	@Override
	public String toString() {
		return "ChangedFile{" +
				"fileName='" + fileName + '\'' +
				"changes=" + changes +
				'}';
	}
}