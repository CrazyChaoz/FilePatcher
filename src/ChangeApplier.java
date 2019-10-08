import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ChangeApplier {

	public ChangeApplier(ChangedFile file) throws IOException {
		FileOutputStream outStream = new FileOutputStream("copy_of_" + file.fileName);
		FileInputStream fileInputStream = new FileInputStream("testdata1/" + file.fileName);

		Iterator<Change> iterator = file.changes.iterator();

		Change currChange = iterator.next();

		int currBitRead = fileInputStream.read();
		boolean hasNext = iterator.hasNext();

		if (file.changes.size() > 0) {
			for (int pos = 0; (currBitRead) != -1 || hasNext; pos++) {
				if (currChange.position == pos) {
					if (currChange.type == ChangeType.ADD) {
						outStream.write(currChange.data);
					} else if (currChange.type == ChangeType.CHANGE) {
						outStream.write(currChange.data);
						fileInputStream.read();
					} else if (currChange.type == ChangeType.DELETE) {
						//Write nothing
					}

					if (hasNext = iterator.hasNext()) {
						currChange = iterator.next();
					}

					if (currChange.position != pos + 1) {
						outStream.write(currBitRead);
						currBitRead = fileInputStream.read();
					}
					System.out.print((char) currBitRead);
				} else {
					outStream.write(currBitRead);
					currBitRead = fileInputStream.read();
				}
			}
		}
	}
}
