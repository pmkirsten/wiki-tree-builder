package es.pmkirsten.parser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class XwikiTreeMacroPathParser {

	private int initialCount = 0;
	private int count = 0;

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

	public void walk(String myPath) {
		Path path = Paths.get(myPath);
		this.setInitialCount(path.getNameCount() - 1);
		StringBuilder builder = new StringBuilder();
		builder.append("{{wrapper}}\n|(((\n{{tree}}\n{{velocity}}\n{{html}}\n<ul>\n");
		builder.append(this.printElement(path));
		builder.append("</ul>\n{{/html}}\n{{/velocity}}\n{{/tree}}\n)))|(((\n)))\n{{/wrapper}}");
		System.out.println(builder.toString());
	}

	public boolean checkDirectory(Path p) {
		return p.toFile().isDirectory();
	}

	public String returnWhitespaces(int times) {
		return String.join("", Collections.nCopies(times, "  "));
	}

	public String printElement(Path p) {
		this.setCount(p.getNameCount());
		int whiteSpaces = this.getCount() - this.getInitialCount();
		StringBuilder builder = new StringBuilder();
		if (!this.checkDirectory(p)) {
			builder.append(this.returnWhitespaces(whiteSpaces));
			builder.append("<li data-jstree='{\"icon\":\"glyphicon glyphicon-file\"}'>");
			builder.append(p.getFileName());
			builder.append("</li>\n");
			return builder.toString();
		}

		if (this.checkDirectory(p) && (p.toFile().list().length == 0)) {
			builder.append(this.returnWhitespaces(whiteSpaces));
			builder.append("<li data-jstree='{\"icon\":\"glyphicon glyphicon-folder-open\"}'>");
			builder.append(p.getFileName());
			builder.append("</li>\n");
			return builder.toString();
		}

		builder.append(this.returnWhitespaces(whiteSpaces));
		builder.append("<li data-jstree='{");
		if ((this.getCount() - this.getInitialCount() - 1) == 0) {
			builder.append("\"opened\":true, ");
		}
		builder.append("\"icon\":\"glyphicon glyphicon-folder-open\"}'>\n");
		builder.append(this.returnWhitespaces(whiteSpaces));
		builder.append(p.getFileName() + "\n");
		builder.append(this.returnWhitespaces(whiteSpaces));
		builder.append("<ul>\n");
		for (File children : p.toFile().listFiles()) {
			builder.append(this.printElement(children.toPath()));
		}
		this.setCount(p.getNameCount());
		builder.append(this.returnWhitespaces(whiteSpaces));
		builder.append("</ul>\n");
		builder.append(this.returnWhitespaces(whiteSpaces));
		builder.append("</li>\n");

		return builder.toString();

	}

	public static void main(String[] args) {
		XwikiTreeMacroPathParser parser = new XwikiTreeMacroPathParser();
		String myPath = "F:\\workspace\\RaceControl\\src";
		parser.walk(myPath);
	}
}
