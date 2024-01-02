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

import es.pmkirsten.builder.WikiTreePathBuilder;

public class WikiTreeBuilderGUI {

	protected JFrame frmXwikiTreeMacro;
	protected JFileChooser fileChooser = new JFileChooser();
	protected String path = null;
	protected JTextArea stringTree;
	protected JTextPane textPane;
	protected JTextPane textFolderIconsPane;
	protected JTextPane textFileIconsPane;
	protected JButton btnFileChooser;
	protected JButton btnParseFolder;
	protected JButton btnCopyText;
	protected JLabel lblExclusions;
	protected JScrollPane scrollExclusionTextArea;
	protected JTextArea exclusionTextArea;
	protected WikiTreePathBuilder builder = new WikiTreePathBuilder();
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
	protected JRadioButton customRadioBtn;
	protected ActionListener fileChooserActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = WikiTreeBuilderGUI.this.path == null ? "." : WikiTreeBuilderGUI.this.path;
			WikiTreeBuilderGUI.this.fileChooser.setCurrentDirectory(new java.io.File(path));
			WikiTreeBuilderGUI.this.fileChooser.setDialogTitle("Seleccionar carpeta");
			WikiTreeBuilderGUI.this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			WikiTreeBuilderGUI.this.fileChooser.setAcceptAllFileFilterUsed(false);

			if (WikiTreeBuilderGUI.this.fileChooser
					.showOpenDialog(WikiTreeBuilderGUI.this.frmXwikiTreeMacro) == JFileChooser.APPROVE_OPTION) {
				WikiTreeBuilderGUI.this.path = WikiTreeBuilderGUI.this.fileChooser.getSelectedFile()
						.getAbsolutePath();
				WikiTreeBuilderGUI.this.textPane.setText(WikiTreeBuilderGUI.this.path);
				WikiTreeBuilderGUI.this.builder.setBasePath(Paths.get(WikiTreeBuilderGUI.this.path));
				WikiTreeBuilderGUI.this.builder.getIgnoreElements().clear();
				WikiTreeBuilderGUI.this.builder.getIgnoreElementsPathMatcher().clear();
				WikiTreeBuilderGUI.this.builder.getIgnoreElements().add(".git/");
				WikiTreeBuilderGUI.this.builder.checkGitignores(Paths.get(WikiTreeBuilderGUI.this.path));
				StringBuilder builder = new StringBuilder();
				for (String ignore : WikiTreeBuilderGUI.this.builder.getIgnoreElements()) {
					builder.append(ignore + "\n");
				}
				WikiTreeBuilderGUI.this.exclusionTextArea.setText(builder.toString());
				WikiTreeBuilderGUI.this.btnParseFolder.doClick();
			}
		}

	};

	protected ActionListener parseTreeActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			WikiTreeBuilderGUI.this.builder.getSelectedElements().clear();
			WikiTreeBuilderGUI.this.builder.getSelectedElementsPathMatcher().clear();
			WikiTreeBuilderGUI.this.reanalyzeTextTree();
			WikiTreeBuilderGUI.this.populateTree();

		}
	};

	public void reanalyzeTextTree() {
		WikiTreeBuilderGUI.this.builder.getIgnoreElements().clear();
		WikiTreeBuilderGUI.this.builder.getIgnoreElementsPathMatcher().clear();
		for (String line : WikiTreeBuilderGUI.this.exclusionTextArea.getText().split("\\n")) {
			WikiTreeBuilderGUI.this.builder.getIgnoreElements().add(line);
		}
		WikiTreeBuilderGUI.this.builder.convertExclusionsToPathMatcher();
		String stringTree = WikiTreeBuilderGUI.this.builder.walk(WikiTreeBuilderGUI.this.path);
		WikiTreeBuilderGUI.this.stringTree.setText(stringTree);
	}

	protected ActionListener copyActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringSelection stringSelection = new StringSelection(WikiTreeBuilderGUI.this.stringTree.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);

		}
	};

	protected ActionListener setGlyphIcons = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			WikiTreeBuilderGUI.this.builder.setGlyphIcons();
			textFolderIconsPane.setEnabled(false);
			textFileIconsPane.setEnabled(false);
		}
	};

	protected ActionListener setFontAwesomeIcons = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			textFolderIconsPane.setEnabled(false);
			textFileIconsPane.setEnabled(false);
			WikiTreeBuilderGUI.this.builder.setFontAwesomeIcons();
		}
	};

	protected ActionListener setCustomIcons = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			textFolderIconsPane.setEnabled(true);
			textFileIconsPane.setEnabled(true);
			WikiTreeBuilderGUI.this.builder.setCustomIcons(textFolderIconsPane.getText(), textFileIconsPane.getText());
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
			int[] selectionRows = WikiTreeBuilderGUI.this.gTree.getSelectionRows();
			TreePath[] selectionPaths = WikiTreeBuilderGUI.this.gTree.getSelectionPaths();

			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				for (TreePath tp : selectionPaths) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
					String itemName = ((TreeElement) node.getUserObject()).getName();
					WikiTreeBuilderGUI.this.exclusionTextArea.setText(WikiTreeBuilderGUI.this.exclusionTextArea.getText() + "\n" + itemName);
					DefaultTreeModel model = (DefaultTreeModel) WikiTreeBuilderGUI.this.gTree.getModel();
					model.removeNodeFromParent(node);
				}
				WikiTreeBuilderGUI.this.reanalyzeTextTree();

			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				for (TreePath tp : selectionPaths) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
					TreeElement element = (TreeElement) node.getUserObject();
					element.setSelected(!element.isSelected());
					if (element.isSelected()) {
						WikiTreeBuilderGUI.this.builder.getSelectedElements().add(tp);
					} else {
						WikiTreeBuilderGUI.this.builder.getSelectedElements().remove(tp);
					}

				}
				WikiTreeBuilderGUI.this.builder.convertSelectionsToPathMatcher();
				WikiTreeBuilderGUI.this.gTree.updateUI();
				WikiTreeBuilderGUI.this.reanalyzeTextTree();
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
					WikiTreeBuilderGUI window = new WikiTreeBuilderGUI();
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
	public WikiTreeBuilderGUI() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmXwikiTreeMacro = new JFrame();
		this.frmXwikiTreeMacro.setTitle("Wiki Tree Builder");
		this.frmXwikiTreeMacro.setBounds(100, 100, 1500, 735);
		this.frmXwikiTreeMacro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 129, 184, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		this.frmXwikiTreeMacro.getContentPane().setLayout(gridBagLayout);

		this.lblIcons = new JLabel("Iconos:");
		GridBagConstraints gbc_lblIcons = new GridBagConstraints();
		gbc_lblIcons.insets = new Insets(5, 5, 5, 5);
		gbc_lblIcons.gridx = 0;
		gbc_lblIcons.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.lblIcons, gbc_lblIcons);

		this.iconGroup = new ButtonGroup();
		this.glyphRadioBtn = new JRadioButton("Glyphicons");
		GridBagConstraints gbc_rdBtnGlypgRadioButton = new GridBagConstraints();
		gbc_rdBtnGlypgRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdBtnGlypgRadioButton.insets = new Insets(5, 0, 5, 5);
		gbc_rdBtnGlypgRadioButton.gridx = 1;
		gbc_rdBtnGlypgRadioButton.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.glyphRadioBtn, gbc_rdBtnGlypgRadioButton);
		this.glyphRadioBtn.addActionListener(this.setGlyphIcons);
		this.glyphRadioBtn.setSelected(true);

		this.fontRadioBtn = new JRadioButton("Font Awesome");
		GridBagConstraints gbc_rdbtnFontRadioButton = new GridBagConstraints();
		gbc_rdbtnFontRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnFontRadioButton.insets = new Insets(5, 0, 5, 5);
		gbc_rdbtnFontRadioButton.gridx = 2;
		gbc_rdbtnFontRadioButton.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.fontRadioBtn, gbc_rdbtnFontRadioButton);
		this.fontRadioBtn.addActionListener(this.setFontAwesomeIcons);

		this.customRadioBtn = new JRadioButton("Personalizado");
		GridBagConstraints gbc_rdbtnCustomRadioButton = new GridBagConstraints();
		gbc_rdbtnCustomRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCustomRadioButton.insets = new Insets(5, 0, 5, 5);
		gbc_rdbtnCustomRadioButton.gridx = 3;
		gbc_rdbtnCustomRadioButton.gridy = 0;
		this.frmXwikiTreeMacro.getContentPane().add(this.customRadioBtn, gbc_rdbtnCustomRadioButton);
		this.customRadioBtn.addActionListener(this.setCustomIcons);

		this.iconGroup.add(this.glyphRadioBtn);
		this.iconGroup.add(this.fontRadioBtn);
		this.iconGroup.add(this.customRadioBtn);

		JLabel lblCustomFolder = new JLabel("Icono carpeta:");
		lblCustomFolder.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCustomFolder = new GridBagConstraints();
		gbc_lblCustomFolder.gridwidth = 2;
		gbc_lblCustomFolder.anchor = GridBagConstraints.EAST;
		gbc_lblCustomFolder.insets = new Insets(0, 5, 5, 5);
		gbc_lblCustomFolder.gridx = 0;
		gbc_lblCustomFolder.gridy = 1;
		this.frmXwikiTreeMacro.getContentPane().add(lblCustomFolder, gbc_lblCustomFolder);

		this.textFolderIconsPane = new JTextPane();
		this.textFolderIconsPane.setEditable(true);
		GridBagConstraints gbc_textFolderIconsPane = new GridBagConstraints();
		gbc_textFolderIconsPane.gridwidth = 2;
		gbc_textFolderIconsPane.insets = new Insets(0, 0, 5, 5);
		gbc_textFolderIconsPane.fill = GridBagConstraints.BOTH;
		gbc_textFolderIconsPane.gridx = 2;
		gbc_textFolderIconsPane.gridy = 1;
		this.frmXwikiTreeMacro.getContentPane().add(this.textFolderIconsPane, gbc_textFolderIconsPane);
		textFolderIconsPane.setText("{{ base_path }}/assets/jstree/fa-folder-open.svg");
		textFolderIconsPane.setEnabled(false);

		JLabel lblCustomFile = new JLabel("Icono fichero:");
		lblCustomFile.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCustomFile = new GridBagConstraints();
		gbc_lblCustomFile.gridwidth = 2;
		gbc_lblCustomFile.anchor = GridBagConstraints.EAST;
		gbc_lblCustomFile.insets = new Insets(0, 5, 5, 5);
		gbc_lblCustomFile.gridx = 0;
		gbc_lblCustomFile.gridy = 2;
		this.frmXwikiTreeMacro.getContentPane().add(lblCustomFile, gbc_lblCustomFile);

		this.textFileIconsPane = new JTextPane();
		this.textFileIconsPane.setEditable(true);
		GridBagConstraints gbc_textFileIconsPane = new GridBagConstraints();
		gbc_textFileIconsPane.gridwidth = 2;
		gbc_textFileIconsPane.insets = new Insets(0, 0, 5, 5);
		gbc_textFileIconsPane.fill = GridBagConstraints.BOTH;
		gbc_textFileIconsPane.gridx = 2;
		gbc_textFileIconsPane.gridy = 2;
		this.frmXwikiTreeMacro.getContentPane().add(this.textFileIconsPane, gbc_textFileIconsPane);
		textFileIconsPane.setText("{{ base_path }}/assets/jstree/fa-file.svg");
		textFileIconsPane.setEnabled(false);

		JLabel lblCarpeta = new JLabel("Carpeta:");
		lblCarpeta.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblCarpeta = new GridBagConstraints();
		gbc_lblCarpeta.gridwidth = 2;
		gbc_lblCarpeta.anchor = GridBagConstraints.WEST;
		gbc_lblCarpeta.insets = new Insets(0, 5, 5, 5);
		gbc_lblCarpeta.gridx = 0;
		gbc_lblCarpeta.gridy = 3;
		this.frmXwikiTreeMacro.getContentPane().add(lblCarpeta, gbc_lblCarpeta);

		this.splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 9;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.insets = new Insets(5, 0, 5, 5);
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
		for(KeyListener kl: gTree.getListeners(KeyListener.class)) {
			gTree.removeKeyListener(kl);
		}
		this.gTree.setModel(null);
		this.scrollPane.setViewportView(this.gTree);

		this.textPane = new JTextPane();
		this.textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.gridwidth = 4;
		gbc_textPane.insets = new Insets(0, 5, 5, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 4;
		this.frmXwikiTreeMacro.getContentPane().add(this.textPane, gbc_textPane);

		this.btnFileChooser = new JButton("Selecciona carpeta...");
		GridBagConstraints gbc_btnFileChooser = new GridBagConstraints();
		gbc_btnFileChooser.anchor = GridBagConstraints.EAST;
		gbc_btnFileChooser.insets = new Insets(0, 0, 5, 5);
		gbc_btnFileChooser.gridx = 3;
		gbc_btnFileChooser.gridy = 5;
		this.frmXwikiTreeMacro.getContentPane().add(this.btnFileChooser, gbc_btnFileChooser);
		this.btnFileChooser.addActionListener(this.fileChooserActionListener);

		this.btnCopyText = new JButton("Copiar \u00E1rbol");
		GridBagConstraints gbc_btnCopyText = new GridBagConstraints();
		gbc_btnCopyText.anchor = GridBagConstraints.EAST;
		gbc_btnCopyText.insets = new Insets(0, 0, 5, 5);
		gbc_btnCopyText.gridx = 3;
		gbc_btnCopyText.gridy = 6;
		this.btnCopyText.addActionListener(this.copyActionListener);
		this.frmXwikiTreeMacro.getContentPane().add(this.btnCopyText, gbc_btnCopyText);

		this.lblExclusions = new JLabel("Exclusiones (incluye contenido .gitignore):");
		GridBagConstraints gbc_lblExclusions = new GridBagConstraints();
		gbc_lblExclusions.gridwidth = 3;
		gbc_lblExclusions.anchor = GridBagConstraints.WEST;
		gbc_lblExclusions.insets = new Insets(0, 5, 5, 5);
		gbc_lblExclusions.gridx = 0;
		gbc_lblExclusions.gridy = 7;
		this.frmXwikiTreeMacro.getContentPane().add(this.lblExclusions, gbc_lblExclusions);

		this.btnParseFolder = new JButton("Reanalizar \u00E1rbol");
		GridBagConstraints gbc_btnParseFolder = new GridBagConstraints();
		gbc_btnParseFolder.anchor = GridBagConstraints.EAST;
		gbc_btnParseFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnParseFolder.gridx = 3;
		gbc_btnParseFolder.gridy = 7;
		this.frmXwikiTreeMacro.getContentPane().add(this.btnParseFolder, gbc_btnParseFolder);
		this.btnParseFolder.addActionListener(this.parseTreeActionListener);

		this.scrollExclusionTextArea = new JScrollPane();
		GridBagConstraints gbc_scrollExclusionTextArea = new GridBagConstraints();
		gbc_scrollExclusionTextArea.gridwidth = 4;
		gbc_scrollExclusionTextArea.insets = new Insets(0, 5, 5, 5);
		gbc_scrollExclusionTextArea.fill = GridBagConstraints.BOTH;
		gbc_scrollExclusionTextArea.gridx = 0;
		gbc_scrollExclusionTextArea.gridy = 8;
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