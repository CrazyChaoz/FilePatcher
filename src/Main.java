import java.io.*;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) throws IOException {
//		ExecutorService threadies = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Set<ChangedFile> changeList = new TreeSet<>();


		File patchedDirectory = Paths.get("testdata0").toFile();
		File unPatchedDirectory = Paths.get("testdata1").toFile();

		System.out.println("Paths:");
		System.out.println(patchedDirectory.getAbsolutePath());
		System.out.println(unPatchedDirectory.getAbsolutePath());


		if (patchedDirectory.isDirectory() && unPatchedDirectory.isDirectory()) {
			File[] patchedFiles = patchedDirectory.listFiles();
			File[] unPatchedFiles = unPatchedDirectory.listFiles();

			for (int fileCounter = 0; patchedFiles.length > fileCounter; fileCounter++) {
//				if (!Arrays.equals(Files.readAllBytes(patchedFiles[fileCounter].toPath()), Files.readAllBytes(unPatchedFiles[fileCounter].toPath())))


				ChangedFile changedFile = new ChangedFile(patchedFiles[fileCounter].getName());

				InputStream file0In = new BufferedInputStream(new FileInputStream(patchedFiles[fileCounter]));
				InputStream file1In = new BufferedInputStream(new FileInputStream(unPatchedFiles[fileCounter]));


				int i0, i1;
				long position = 0;
				long differentSincePosition=0;

				while ((i0 = file0In.read()) != -1) {
					if ((i1 = file1In.read()) != -1) {
						if(i0!=i1){
							//replace
							changedFile.addChange(ChangeType.CHANGE, position, (byte) i0);
						}
					} else {
						changedFile.addChange(ChangeType.ADD, position, (byte) i0);
					}

					position++;
				}

				while ((i1 = file1In.read()) != -1) {
						changedFile.addChange(ChangeType.ADD, position, (byte) i1);
				}


				changeList.add(changedFile);
				file0In.close();
				file1In.close();


//				threadies.execute(() -> {});


			}


			changeList.forEach(System.out::println);
		}
	}
}

class ChangedFile implements Comparable<ChangedFile> {

	String fileName;
	private Set<Change> changes = new TreeSet<>();


	public ChangedFile(String fileName) {
		this.fileName = fileName;
	}

	public void addChange(ChangeType type,long position, byte data){
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
				", changes=" + changes +
				'}';
	}
}
enum ChangeType{
	ADD,DELETE,CHANGE
}
class Change implements Comparable<Change>{
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
		return (int)((position-o.position)%Integer.MAX_VALUE); //FIXME: error when add/delete on same position
	}

	@Override
	public String toString() {
		return "Change{" +
				"position=" + position +
				", type=" + type +
				", data=" + data +
				'}';
	}
}
