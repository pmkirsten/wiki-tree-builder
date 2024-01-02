package es.pmkirsten.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AboutInfoPanel extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutInfoPanel dialog = new AboutInfoPanel();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutInfoPanel() {
		this.setTitle("Acerca de");
		this.setBounds(100, 100, 452, 135);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			JLabel developText = new JLabel("Esta aplicación ha sido desarrollada por Pablo Martínez Kirsten.");
			this.contentPanel.add(developText);
		}
		{
			JLabel developLink = new JLabel("Github");
			this.goGithub(developLink, "https://github.com/pmkirsten", "Github");
			this.contentPanel.add(developLink);
		}
		{
			JLabel iconText = new JLabel("Los iconos son obtenidos de");
			this.contentPanel.add(iconText);
		}
		{

			JLabel iconLink = new JLabel("Fontawesome");
			this.goGithub(iconLink, "https://fontawesome.com/", "Fontawesome");
			this.contentPanel.add(iconLink);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						AboutInfoPanel.this.dispose();
					}
				});
				buttonPane.add(okButton);
				this.getRootPane().setDefaultButton(okButton);
			}
		}
	}

	private void goGithub(JLabel website, final String url, String text) {
		website.setText("<html><a href=\"\">" + text + "</a></html>");
		website.setCursor(new Cursor(Cursor.HAND_CURSOR));
		website.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException | IOException ex) {
				}
			}
		});
	}

	private void goFontawesone(JLabel website, final String url, String text) {
		website.setText("<html><a href=\"\">" + text + "</a></html>");
		website.setCursor(new Cursor(Cursor.HAND_CURSOR));
		website.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException | IOException ex) {
				}
			}
		});
	}

}
