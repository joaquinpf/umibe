package ar.com.umibe.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class GuiUtils {
	public static void centerOnScreen(final Component target) {
		if (target != null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension dialogSize = target.getSize();
			if (dialogSize.height > screenSize.height) {
				dialogSize.height = screenSize.height;
			}
			if (dialogSize.width > screenSize.width) {
				dialogSize.width = screenSize.width;
			}
			target.setLocation((screenSize.width - dialogSize.width) / 2,
					(screenSize.height - dialogSize.height) / 2);
		}
	}
	public static void setIcon(final JFrame target){
        target.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	public static void setIcon(final JDialog target){
        target.setIconImage(new ImageIcon("resources/icon.png").getImage());
	}
	
	public static boolean contains(JComboBox j, String s){
		DefaultComboBoxModel model = (DefaultComboBoxModel) j.getModel();
		for(int i=0; i<model.getSize(); i++){
			if(model.getElementAt(i).equals(s)){
				return true;
			}
		}
		return false;
	}
}
