package deltaiot.cbam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ProcessError {

	private static PrintStream errorPS = null;
	
	public static PrintStream getErrorPrintStream() {
		String errorFilePath = RunStatisticResults.getPath().replaceAll(".txt", "ERROR FILE.txt");
		File file = new File(errorFilePath);

		try {
			errorPS = new PrintStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return errorPS;
	}

}
