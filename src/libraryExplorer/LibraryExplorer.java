package libraryExplorer;

import java.io.File;

public class LibraryExplorer {
	private String path;

	public LibraryExplorer(String path) {
		this.path = path;
	}

	public void explore() {
		explore(path);
	}

	public void explore(String path) {
		File current = new File(path);
		if (current.isFile()) {
			if(path.toLowerCase().endsWith(".mp3")){
				
			}
			else{
				System.out.print("Unrecognized file type :");
				System.out.println(path);
				System.out.println("");
			}
		} else {
			for (String f : current.list()) {
				if (!f.startsWith(".")) {
					explore(current.getAbsolutePath() + "/" + f);
				}
			}
		}
	}

	public static void main(String[] args) {
		LibraryExplorer explorer = new LibraryExplorer(
				"/Users/mariosangiorgio/Music/iTunes/iTunes Media/Music");
		explorer.explore();
	}
}
