package es.pmkirsten.gui;

public class TreeElement {

	public static String FOLDER_CLOSED_ICON = "/folder-solid.png";
	public static String FOLDER_OPEN_ICON = "/folder-open-solid.png";
	public static String FILE_ICON = "/file-solid.png";
	public static final int FOLDER_CLOSED = 1;
	public static final int FOLDER_OPEN = 2;
	public static final int FILE = 3;
	private String name;
	private String icon;
	private int type;

	public TreeElement(String name, int type) {
		this.name = name;
		this.type = type;
		this.setIcon(type);
	}

	private void setIcon(int type) {
		switch (type) {
		case FOLDER_OPEN:
			this.icon = TreeElement.getFolderOpenIcon();
			break;
		case FOLDER_CLOSED:
			this.icon = TreeElement.getFolderClosedIcon();
			break;
		case FILE:
			this.icon = TreeElement.getFileIcon();
			break;
		default:
			break;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return this.icon;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
		this.setIcon(type);
	}

	public static String getFolderClosedIcon() {
		return TreeElement.FOLDER_CLOSED_ICON;
	}

	public static void setFolderClosedIcon(String folderClosedIcon) {
		TreeElement.FOLDER_CLOSED_ICON = folderClosedIcon;
	}

	public static String getFolderOpenIcon() {
		return TreeElement.FOLDER_OPEN_ICON;
	}

	public static void setFolderOpenIcon(String folderOpenIcon) {
		TreeElement.FOLDER_OPEN_ICON = folderOpenIcon;
	}

	public static String getFileIcon() {
		return TreeElement.FILE_ICON;
	}

	public static void setFileIcon(String fileIcon) {
		TreeElement.FILE_ICON = fileIcon;
	}
}
