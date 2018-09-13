package prismWrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import prism.PrismLog;

public class PrismFileLog extends PrismLog {

	Path			filePath;
	BufferedWriter	bw;
	double time;
	
	public PrismFileLog(Path filePath) {
		this.filePath = filePath;
		createFile();
	}

	public void createFile() {
		File file = filePath.toFile();
		try {
			if (!file.exists()) // checks whether the file is Exist or not
				file.createNewFile();
			
			// creating fileWriter object with the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}
	
	@Override
	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void flush() {
		try {
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public long getFilePointer() {
		return 0;
	}
	
	@Override
	public void print(boolean arg0) {
		try {
			bw.write(Boolean.toString(arg0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(char arg0) {
		try {
			bw.write(arg0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(double arg0) {
		try {
			bw.write(Double.toString(arg0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(float arg0) {
		try {
			bw.write(Float.toString(arg0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(int arg0) {
		try {
			bw.write(Integer.toString(arg0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(long arg0) {
		try {
			bw.write(Long.toString(arg0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(Object arg0) {
		try {
			bw.write(arg0.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void print(String arg0) {
		try {
			//System.out.println(arg0);
			if (arg0 != null){
				if (arg0.startsWith("\nTime for model c") && arg0.contains(":")){
					String seconds = arg0.substring(arg0.indexOf(":") + 1, arg0.indexOf("seconds")).trim();
					time += Double.parseDouble(seconds);
					bw.write(arg0);
					bw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void println() {
//		try {
//			bw.newLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public boolean ready() {
		return true;
	}
	
	public void clearLog(){
		filePath.toFile().delete();
		createFile();
	}
	
	public double getTime(){
		return time;
	}
	
	public void resetTime(){
		time = 0.0;
	}
}
