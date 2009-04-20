package ar.com.feedactionizer.gui;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.plaf.metal.MetalIconFactory;

import ar.com.feedactionizer.core.DownloadFeeder;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class SysTray {

	protected static TrayIcon icon = null;
	
	protected Image getImage() throws HeadlessException {

		Icon defaultIcon = MetalIconFactory.getTreeComputerIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(),
				defaultIcon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
		return img;
	}

	protected PopupMenu createPopupMenu() throws HeadlessException {
		PopupMenu menu = new PopupMenu();
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DownloadFeeder.INSTANCE.stop();
				System.exit(0);
			}
		});
		menu.add(exit);
		return menu;
	}

	public SysTray(final JFrame parent,String name) {
		parent.setVisible(false);
		icon = new TrayIcon(getImage(),name, createPopupMenu());
		icon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setVisible(true);
				SystemTray.getSystemTray().remove(icon);
			}
		});
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
}