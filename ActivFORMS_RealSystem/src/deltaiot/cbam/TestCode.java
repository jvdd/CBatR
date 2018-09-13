package deltaiot.cbam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestCode {
	
	public static void main(String [] args) {
		String partialPath = String.format("C:\\Users\\Jeroen\\Documents\\BachelorProef\\Resultaten\\Project_v%s\\", 5);
		//String partialPath = String.format("C:\\Users\\Mama\\Desktop\\BachelorProef_Resultaten\\Project_v%s\\", projectVersion);
		 
		List<Integer> energyVersionsTest = new ArrayList<Integer>(Arrays.asList(0, 1));
		String extraInfoName = "";
		String extraInfoFile = "";
		RunStatisticResults.executeStatisticTest(partialPath, energyVersionsTest, extraInfoName, extraInfoFile, args);
	}
	
}
