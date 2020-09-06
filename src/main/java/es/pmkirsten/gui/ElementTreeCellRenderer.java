package es.pmkirsten.gui;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

public class ElementTreeCellRenderer implements TreeCellRenderer {

	private final JLabel label;

	public ElementTreeCellRenderer() {
		this.label = new JLabel();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Object o = ((DefaultMutableTreeNode) value).getUserObject();
		if (o instanceof TreeElement) {
			TreeElement element = (TreeElement) o;
			URL imageUrl = this.getClass().getResource(element.getIcon());
			if (imageUrl != null) {
				this.label.setIcon(new ImageIcon(imageUrl));
			}
			this.label.setText(element.getName());
		} else {
			this.label.setIcon(null);
			this.label.setText("" + value);
		}
		return this.label;
	}

}
