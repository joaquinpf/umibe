package ar.com.umibe.gui;

import ar.com.umibe.core.DataModel;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class EntryPoint {
	public static void main(String args[]) {
		String flatArgs = " ";
		for(int i=0;i<args.length;i++){
			flatArgs += args[i];
		}
		
		if(flatArgs.contains("--distributed")){
			DataModel.INSTANCE.loadQueue(false);
		}
		else {
			DataModel.INSTANCE.loadQueue(true);
		}
		DataModel.INSTANCE.addWorker();
		DataModel.INSTANCE.loadWatchedFolders();
		if(flatArgs.contains("--cli")){
			new CommandLineInterface();
		}
		else {
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new MainWindow().setVisible(true);
				}
			});
		}
	}
}
