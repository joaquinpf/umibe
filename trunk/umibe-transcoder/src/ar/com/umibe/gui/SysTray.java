package ar.com.umibe.gui;
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
import javax.swing.plaf.metal.MetalIconFactory;

import ar.com.umibe.core.DataModel;


/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class SysTray {

	private static TrayIcon icon = null;
	
	private Image getImage() throws HeadlessException {

		Icon defaultIcon = MetalIconFactory.getTreeComputerIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(),
				defaultIcon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
		return img;
	}

	private PopupMenu createPopupMenu() throws HeadlessException {
		PopupMenu menu = new PopupMenu();
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataModel.INSTANCE.stopWorkers();
				DataModel.INSTANCE.saveQueue();
				System.exit(0);
			}
		});
		menu.add(exit);
		return menu;
	}

	public SysTray() {
		icon = new TrayIcon(getImage(),
				DataModel.INSTANCE.getBuildID(), createPopupMenu());
		icon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((MainWindow)DataModel.INSTANCE.getUi()).setVisible(true);
				((MainWindow)DataModel.INSTANCE.getUi()).setExtendedState(0);
				SystemTray.getSystemTray().remove(icon);
			}
		});
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void showMessage(String message) {
		icon.displayMessage("Encoded", message,
				TrayIcon.MessageType.INFO);
	}
	
	/*public static void main(String[] args) throws Exception {

		TrayIcon icon = new TrayIcon(getImage(),
				"This is a Java Tray Icon", createPopupMenu());
		icon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((MainWindow)DataModel.INSTANCE.getUi()).setVisible(true);
			}
		});
		/*SystemTray.getSystemTray().add(icon);
		while(true) {
			Thread.sleep(10000);
			icon.displayMessage("Warning", "Click me! =)",
					TrayIcon.MessageType.WARNING);
		}
	}*/
}