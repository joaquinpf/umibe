package ar.com.umibe.gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.media.jmc.JMediaPlayer;

public class PreviewWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 282907187125213176L;

	public PreviewWindow() {
		super("Reflection frame");
		try {
			File file = new File(
					"h:\\copy.avi");
			if(file.exists()){
			JMediaPlayer mp = new JMediaPlayer();
			mp.setSource(file.toURI());
			this.add(mp, BorderLayout.CENTER);
			mp.play();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(480, 272);
		this.setLocationRelativeTo(null);
	}

	public static void main(String[] args) throws Exception {
		//JFrame.setDefaultLookAndFeelDecorated(true);
		//UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame w = new PreviewWindow();
				w.setVisible(true);
			}
		});
	}

}
