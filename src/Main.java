import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
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

				ChangedFile changedFile = new ChangedFile(patchedFiles[fileCounter].getName());

				FileInputStream file0In = new FileInputStream(patchedFiles[fileCounter]);
				FileInputStream file1In = new FileInputStream(unPatchedFiles[fileCounter]);




				int i0, i1, lookahead0 = 0, lookahead1 = 0;
				int maxlookahead = 10;
				long position = 0;

				while ((i0 = file0In.read()) != -1) {
					if ((i1 = file1In.read()) != -1) {

						if (i0 != i1) {
							//replace
//							changedFile.addChange(ChangeType.CHANGE, position, (byte) i0);


							FileChannel fc0 = file0In.getChannel();
							FileChannel fc1 = file1In.getChannel();

							long startpos0=fc0.position();
							long startpos1=fc1.position();

							lookaheadloop:
							for (lookahead0=0; lookahead0 < maxlookahead; lookahead0++) {
								for (lookahead1=0; lookahead1 < maxlookahead; lookahead1++) {
									if (Channels.newInputStream(fc0).read() == Channels.newInputStream(fc1).read()) {
										break lookaheadloop;
									}
									fc1.position(fc1.position() + 1);
								}
								fc0.position(fc0.position() + 1);
							}

							System.out.println("file0 needs to move " + lookahead0);
							System.out.println("file1 needs to move " + lookahead1);


							fc0.position(startpos0);
							fc1.position(startpos1);


							lookaheadloop2:
							for (lookahead0=0; lookahead0 < maxlookahead; lookahead0++) {
								for (lookahead1=0; lookahead1 < maxlookahead; lookahead1++) {
									if (Channels.newInputStream(fc0).read() == Channels.newInputStream(fc1).read()) {
										break lookaheadloop2;
									}
									fc0.position(fc0.position() + 1);
								}
								fc1.position(fc1.position() + 1);
							}

							System.out.println("file0 needs to move " + lookahead0);
							System.out.println("file1 needs to move " + lookahead1);


							fc0.position(startpos0-1);
							fc1.position(startpos1-1);

							changedFile.addChange(lookahead0 < lookahead1 ? ChangeType.ADD:ChangeType.DELETE, position, (byte) (lookahead0 < lookahead1 ? Channels.newInputStream(fc0).read() : Channels.newInputStream(fc1).read()));


						}
					} else {
						changedFile.addChange(ChangeType.ADD, position, (byte) i0);
					}

					position++;
				}

				while ((i1 = file1In.read()) != -1) {
					changedFile.addChange(ChangeType.DELETE, position++, (byte) i1);
				}


				changeList.add(changedFile);
				file0In.close();
				file1In.close();


//				threadies.execute(() -> {});


				///////////////////////////////
				///////////////////////////////
				new ChangeApplier(changedFile);
				///////////////////////////////
				///////////////////////////////

			}


			changeList.forEach(System.out::println);
		}
	}
}











