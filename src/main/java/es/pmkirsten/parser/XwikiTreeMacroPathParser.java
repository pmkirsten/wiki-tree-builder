package es.pmkirsten.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class XwikiTreeMacroPathParser {

	private int initialCount = 0;
	private int count = 0;
	private int previousCount = 0;

	public int getInitialCount() {
		return this.initialCount;
	}

	public void setInitialCount(int initialCount) {
		this.initialCount = initialCount;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPreviousCount() {
		return this.previousCount;
	}

	public void setPreviousCount(int previousCount) {
		this.previousCount = previousCount;
	}

	public void printPath(Path p) {
		System.out.print(String.join("", Collections.nCopies(this.getCount() - this.getInitialCount(), "  ")));
		boolean directory = this.checkDirectory(p);
		System.out.print(this.printPreffix(directory));
		System.out.print(p.getFileName());
		System.out.println(this.printSuffix(directory));
	}

	public boolean checkDirectory(Path p) {
		return p.toFile().isDirectory();
	}

	public String printPreffix(boolean directory) {
		StringBuilder builder = new StringBuilder();
		builder.append("<li data-jstree='{\"icon\":\"glyphicon ");
		builder.append(directory == true ? "glyphicon-folder-open" : "glyphicon-file");
		builder.append("\"}'>");
		return builder.toString();
	}

	public String printSuffix(boolean directory) {
		if (directory != true) {
			return "</li>";
		}
		return "";
	}

	public void showPath(String myPath) {
		Path path1 = Paths.get(myPath);
		this.setInitialCount(path1.getNameCount());

		try {
			Files.walk(Paths.get(path1.toUri())).forEach(o -> {
				this.setCount(o.getNameCount());
				this.printPath(o);
				this.setPreviousCount(this.getCount());
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		XwikiTreeMacroPathParser parser = new XwikiTreeMacroPathParser();
		String myPath = "F:\\workspace\\RaceControl\\src";
		parser.showPath(myPath);

	}
}

