package ar.com.umibe.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Observable;

import ar.com.umibe.core.DataModel;
import ar.com.umibe.core.VideoFile;

public class CommandLineInterface implements UserIterface {
	
	private boolean usingCli = true;
	
	public CommandLineInterface(){
		DataModel.INSTANCE.setUi(this);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (usingCli) {
			System.out.print("KireNcoder[" + DataModel.INSTANCE.getHostname() + "] > ");
			try {
				final String command = in.readLine();
				handleCommand(command);

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private void handleCommand(String command) {
		String[] split = command.split(" ");
		if(split.length>=2){
			if(split[0].equals("addfile")){
				File file = new File(split[1]);
				if (file.exists() && file.isFile()) {
					System.out.println("GOT FILE: "
							+ split[1]);
					DataModel.INSTANCE.addToQueue(new VideoFile(split[1]));
				}
			}
			if(split[0].equals("addfolder")){
				File file = new File(split[1]);
				if (file.exists() && file.isDirectory()) {
					System.out.println("GOT FOLDER: "
							+ split[1]);
					DataModel.INSTANCE.addWatchedFolder(split[1]);
				}
			}
		} else if(split.length == 1){
			if(split[0].equals("start")){
				DataModel.INSTANCE.startWorkers();				
			}
			if(split[0].equals("stop")){
				DataModel.INSTANCE.stopWorkers();				
			}
			if(split[0].equals("listjobs")){
				list(DataModel.INSTANCE.getAllJobs());				
			}			
			if(split[0].equals("exit")){
				DataModel.INSTANCE.stopWorkers();
				DataModel.INSTANCE.saveQueue();		
				usingCli = false;
			}			
		}
	}

	private void list(ArrayList<VideoFile> allJobs) {
		for(int i=0; i<allJobs.size();i++){
			System.out.println(allJobs.get(i).getRoute());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
}
