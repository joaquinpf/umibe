package ar.com.umibe.commons.regex.reaction;

import javax.swing.JFrame;
import javax.swing.JLabel;

import ar.com.umibe.commons.util.GuiUtils;


public class NagReaction implements Reaction {
	
	@Override
	public void react(String file) {
		JFrame frame = new JFrame();
		frame.setSize(800, 600);
		frame.setAlwaysOnTop(true);
		JLabel label = new JLabel("Sentite molestado");
		frame.getContentPane().add(label);
		GuiUtils.centerOnScreen(frame);
		GuiUtils.centerOnScreen(label);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		NagReaction nag = new NagReaction();
		nag.react(null);
	}
	
}
