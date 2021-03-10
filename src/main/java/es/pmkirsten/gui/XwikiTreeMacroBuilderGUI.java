package es.pmkirsten.gui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import es.pmkirsten.builder.XwikiTreeMacroPathBuilder;

public class XwikiTreeMacroBuilderGUI {

	protected JFrame frmXwikiTreeMacro;
	protected JFileChooser fileChooser = new JFileChooser();
	protected String path = null;
	protected JTextArea stringTree;
	protected JTextPane textPane;
	protected JButton btnFileChooser;
	protected JButton btnParseFolder;
	protected JButton btnCopyText;
	protected JLabel lblExclusions;
	protected JScrollPane scrollExclusionTextArea;
	protected JTextArea exclusionTextArea;
	protected XwikiTreeMacroPathBuilder builder = new XwikiTreeMacroPathBuilder();
	protected JSplitPane splitPane;
	protected JScrollPane scrollPane;
	protected JTree gTree;
	protected JMenuBar menuBar;
	protected JMenu aboutMenu;
	protected JMenuItem aboutMenuInfo;
	protected ButtonGroup iconGroup;
	protected JLabel lblIcons;
	protected JRadioButton glyphRadioBtn;
	protected JRadioButton fontRadioBtn;
	protected ActionListener fileChooserActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = XwikiTreeMacroBuilderGUI.this.path == null ? "." : XwikiTreeMacroBuilderGUI.this.path;
			XwikiTreeMacroBuilderGUI.this.fileChooser.setCurrentDirectory(new java.io.File(path));
			XwikiTreeMacroBuilderGUI.this.fileChooser.setDialogTitle("Seleccionar carpeta");
			XwikiTreeMacroBuilderGUI.this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			XwikiTreeMacroBuilderGUI.this.fileChooser.setAcceptAllFileFilterUsed(false);

			if (XwikiTreeMacroBuilderGUI.this.fileChooser
					.showOpenDialog(XwikiTreeMacroBuilderGUI.this.frmXwikiTreeMacro) == JFileChooser.APPROVE_OPTION) {
				XwikiTreeMacroBuilderGUI.this.path = XwikiTreeMacroBuilderGUI.this.fileChooser.getSelectedFile()
						.getAbsolutePath();
				XwikiTreeMacroBuilderGUI.this.textPane.setText(XwikiTreeMacroBuilderGUI.this.path);
				XwikiTreeMacroBuilderGUI.this.builder.setBasePath(Paths.get(XwikiTreeMacroBuilderGUI.this.path));
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().clear();
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElementsPathMatcher().clear();
				XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().add(".git/");
				XwikiTreeMacroBuilderGUI.this.builder.checkGitignores(Paths.get(XwikiTreeMacroBuilderGUI.this.path));
				StringBuilder builder = new StringBuilder();
				for (String ignore : XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements()) {
					builder.append(ignore + "\n");
				}
				XwikiTreeMacroBuilderGUI.this.exclusionTextArea.setText(builder.toString());
				XwikiTreeMacroBuilderGUI.this.btnParseFolder.doClick();
			}
		}

	};

	protected ActionListener parseTreeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			XwikiTreeMacroBuilderGUI.this.builder.getSelectedElements().clear();
			XwikiTreeMacroBuilderGUI.this.builder.getSelectedElementsPathMatcher().clear();
			XwikiTreeMacroBuilderGUI.this.reanalyzeTextTree();
			XwikiTreeMacroBuilderGUI.this.populateTree();

		}
	};

	public void reanalyzeTextTree() {
		XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().clear();
		XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElementsPathMatcher().clear();
		for (String line : XwikiTreeMacroBuilderGUI.this.exclusionTextArea.getText().split("\\n")) {
			XwikiTreeMacroBuilderGUI.this.builder.getIgnoreElements().add(line);
		}
		XwikiTreeMacroBuilderGUI.this.builder.convertExclusionsToPathMatcher();
		String stringTree = XwikiTreeMacroBuilderGUI.this.builder.walk(XwikiTreeMacroBuilderGUI.this.path);
		XwikiTreeMacroBuilderGUI.this.stringTree.setText(stringTree);
	}

	protected ActionListener copyActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringSelection stringSelection = new StringSelection(XwikiTreeMacroBuilderGUI.this.stringTree.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);

		}
	};

	protected ActionListener setGlyphIcons = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			XwikiTreeMacroBuilderGUI.this.builder.setGlyphIcons();
		}
	};

	protected ActionListener setFontAwesomeIcons = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			XwikiTreeMacroBuilderGUI.this.builder.setFontAwesomeIcons();
		}
	};

	protected KeyListener shortcutKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int[] selectionRows = XwikiTreeMacroBuilderGUI.this.gTree.getSelectionRows();
			TreePath[] selectionPaths = XwikiTreeMacroBuilderGUI.this.gTree.getSelectionPaths();

			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				for (TreePath tp : selectionPaths) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
					String itemName = ((TreeElement) node.getUserObject()).getName();
					XwikiTreeMacroBuilderGUI.this.exclusionTextArea.setText(XwikiTreeMacroBuilderGUI.this.exclusionTextArea.getText() + "\n" + itemName);
					DefaultTreeModel model = (DefaultTreeModel) XwikiTreeMacroBuilderGUI.this.gTree.getModel();
					model.removeNodeFromParent(node);
				}
				XwikiTreeMacroBuilderGUI.this.reanalyzeTextTree();

			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				for (TreePath tp : selectionPaths) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
					TreeElement element = (TreeElement) node.getUserObject();
					element.setSelected(!element.isSelected());
					if (element.isSelected()) {
						XwikiTreeMacroBuilderGUI.this.builder.getSelectedElements().add(tp);
					} else {
						XwikiTreeMacroBuilderGUI.this.builder.getSelectedElements().remove(tp);
					}

				}
				XwikiTreeMacroBuilderGUI.this.builder.convertSelectionsToPathMatcher();
				XwikiTreeMacroBuilderGUI.this.gTree.updateUI();
				XwikiTreeMacroBuilderGUI.this.reanalyzeTextTree();
			}


		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					XwikiTreeMacroBuilderGUI window = new XwikiTreeMacroBuilderGUI();
					window.frmXwikiTreeMacro.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public XwikiTreeMacroBuilderGUI() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmXwikiTreeMacro = new JFrame();
		this.frmXwikiTreeMacro.setTitle("XWiki Tree Macro Builder");
		this.frmXwikiTreeMacro.setBounds(100, 100, 1500, 735);
		this.frmXwikiTreeMacro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 129, 184, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		this.frmXwikiTreeMacro.getContentPane().setLayout(gridBagLayout);

		this.lblIcons = new JLabel("Iconos:");
		GridBagConstraints gbc_lblIcons = new GridBagConstraints();
		gbc_lblIcons.insets = new Insets(0, 0, 5, 5);
		gbc_lblIcons.gridx = 0;
		gbc_lblIcons.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.lblIcons, gbc_lblIcons);

		this.iconGroup = new ButtonGroup();
		this.glyphRadioBtn = new JRadioButton("Glyphicons");
		GridBagConstraints gbc_rdBtnGlypgRadioButton = new GridBagConstraints();
		gbc_rdBtnGlypgRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdBtnGlypgRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdBtnGlypgRadioButton.gridx = 1;
		gbc_rdBtnGlypgRadioButton.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.glyphRadioBtn, gbc_rdBtnGlypgRadioButton);
		this.glyphRadioBtn.addActionListener(this.setGlyphIcons);
		this.glyphRadioBtn.setSelected(true);

		this.fontRadioBtn = new JRadioButton("Font Awesome");
		GridBagConstraints gbc_rdbtnFontRadioButton = new GridBagConstraints();
		gbc_rdbtnFontRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnFontRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnFontRadioButton.gridx = 2;
		gbc_rdbtnFontRadioButton.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.fontRadioBtn, gbc_rdbtnFontRadioButton);
		this.fontRadioBtn.addActionListener(this.setFontAwesomeIcons);
		this.iconGroup.add(this.glyphRadioBtn);
		this.iconGroup.add(this.fontRadioBtn);

		JLabel lblCarpeta = new JLabel("Carpeta:");
		lblCarpeta.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCarpeta = new GridBagConstraints();
		gbc_lblCarpeta.gridwidth = 2;
		gbc_lblCarpeta.anchor = GridBagConstraints.WEST;
		gbc_lblCarpeta.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarpeta.gridx = 0;
		gbc_lblCarpeta.gridy = 1;
		this.frmXwikiTreeMacro.getContentPane().add(lblCarpeta, gbc_lblCarpeta);

		this.splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 7;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 4;
		gbc_splitPane.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.splitPane, gbc_splitPane);

		JScrollPane scrollStringTree = new JScrollPane();
		this.splitPane.setRightComponent(scrollStringTree);

		this.stringTree = new JTextArea();
		scrollStringTree.setViewportView(this.stringTree);

		this.scrollPane = new JScrollPane();
		this.splitPane.setLeftComponent(this.scrollPane);
		this.splitPane.setDividerLocation(315);

		this.gTree = new JTree();
		this.gTree.setModel(null);
		this.scrollPane.setViewportView(this.gTree);

		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 4;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 2;
		this.frmXwikiTreeMacro.getContentPane().add(this.textPane, gbc_textPane);

		this.btnFileChooser = new JButton("Selecciona carpeta...");
		GridBagConstraints gbc_btnFileChooser = new GridBagConstraints();
		gbc_btnFileChooser.anchor = GridBagConstraints.EAST;
		gbc_btnFileChooser.insets = new Insets(0, 0, 5, 5);
		gbc_btnFileChooser.gridx = 3;
		gbc_btnFileChooser.gridy = 3;
		this.frmXwikiTreeMacro.getContentPane().add(this.btnFileChooser, gbc_btnFileChooser);
		this.btnFileChooser.addActionListener(this.fileChooserActionListener);

		this.btnCopyText = new JButton("Copiar \u00E1rbol");
		GridBagConstraints gbc_btnCopyText = new GridBagConstraints();
		gbc_btnCopyText.anchor = GridBagConstraints.EAST;
		gbc_btnCopyText.insets = new Insets(0, 0, 5, 5);
		gbc_btnCopyText.gridx = 3;
		gbc_btnCopyText.gridy = 4;
		this.btnCopyText.addActionListener(this.copyActionListener);
		this.frmXwikiTreeMacro.getContentPane().add(this.btnCopyText, gbc_btnCopyText);

		this.lblExclusions = new JLabel("Exclusiones (incluye contenido .gitignore):");
		GridBagConstraints gbc_lblExclusions = new GridBagConstraints();
		gbc_lblExclusions.gridwidth = 3;
		gbc_lblExclusions.anchor = GridBagConstraints.WEST;
		gbc_lblExclusions.insets = new Insets(0, 0, 5, 5);
		gbc_lblExclusions.gridx = 0;
		gbc_lblExclusions.gridy = 5;
		this.frmXwikiTreeMacro.getContentPane().add(this.lblExclusions, gbc_lblExclusions);

		this.btnParseFolder = new JButton("Reanalizar \u00E1rbol");
		GridBagConstraints gbc_btnParseFolder = new GridBagConstraints();
		gbc_btnParseFolder.anchor = GridBagConstraints.EAST;
		gbc_btnParseFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnParseFolder.gridx = 3;
		gbc_btnParseFolder.gridy = 5;
		this.frmXwikiTreeMacro.getContentPane().add(this.btnParseFolder, gbc_btnParseFolder);
		this.btnParseFolder.addActionListener(this.parseTreeActionListener);

		this.scrollExclusionTextArea = new JScrollPane();
		GridBagConstraints gbc_scrollExclusionTextArea = new GridBagConstraints();
		gbc_scrollExclusionTextArea.gridwidth = 4;
		gbc_scrollExclusionTextArea.insets = new Insets(0, 0, 0, 5);
		gbc_scrollExclusionTextArea.fill = GridBagConstraints.BOTH;
		gbc_scrollExclusionTextArea.gridx = 0;
		gbc_scrollExclusionTextArea.gridy = 6;
		this.frmXwikiTreeMacro.getContentPane().add(this.scrollExclusionTextArea, gbc_scrollExclusionTextArea);

		this.exclusionTextArea = new JTextArea();
		this.scrollExclusionTextArea.setViewportView(this.exclusionTextArea);

		this.menuBar = new JMenuBar();
		this.frmXwikiTreeMacro.setJMenuBar(this.menuBar);

		this.aboutMenu = new JMenu("M\u00E1s informaci\u00F3n");
		this.aboutMenu.setHorizontalAlignment(SwingConstants.CENTER);
		this.menuBar.add(this.aboutMenu);

		this.aboutMenuInfo = new JMenuItem("Acerca de...");
		this.aboutMenuInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AboutInfoPanel panel = new AboutInfoPanel();
				panel.setModal(true);
				panel.setVisible(true);
			}
		});
		this.aboutMenu.add(this.aboutMenuInfo);
	}



	public void populateTree() {
		this.scrollPane.setViewportView(null);
		this.gTree = new JTree(this.builder.getModelTree());
		this.gTree.setCellRenderer(new ElementTreeCellRenderer());
		this.gTree.addKeyListener(this.shortcutKeyListener);
		this.scrollPane.setViewportView(this.gTree);
	}
}