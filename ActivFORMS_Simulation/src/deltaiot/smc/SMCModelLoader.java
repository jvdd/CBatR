package deltaiot.smc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class SMCModelLoader {
	
	List<SMCModel> models;
	long lastModified;
	String configPath;
	public SMCModelLoader(String configPath){
		this.configPath = configPath;
	}
	
	public List<SMCModel> loadModels(){
		
		Properties prop = new Properties();
		InputStream input = null;

		try {
			
			File configFile = new File(configPath);
			if (!configFile.exists()){
				throw new RuntimeException("SMCConfig.properties file not found at following path:" + configPath);
			}
			else{
				long lastModified = configFile.lastModified();
				if (this.lastModified == lastModified){
					return models;
				}
				this.lastModified = lastModified;
			}

			models = new LinkedList<>();
			
			input = new FileInputStream(configPath);

			// load a properties file
			prop.load(input);

			// get the property value
			String reqs[] = prop.getProperty("requirements").split(",");
			String modelsFolderName = prop.getProperty("modelsFolderName");
			String modelsFolderPath = System.getProperty("user.dir") + "/" + modelsFolderName;
			for(String req: reqs){
				String key, path, simulations = "25", alpha = "0.05", epsilon = "0.05", model;
				ModelType type = null;
				key = req.trim();
				path = modelsFolderPath + "/" + prop.getProperty(key+".modelFileName");
				String modelType = prop.getProperty(key + ".type");
				if (modelType.equalsIgnoreCase("simulation")){
					type = ModelType.SIMULATION;
					simulations = prop.getProperty(key + ".totalSimulations");
				}else if (modelType.equalsIgnoreCase("probability")){
					type = ModelType.PROBABILITY;
					alpha = prop.getProperty(key+".alpha");
					epsilon = prop.getProperty(key+".epsilon");
				}
				model = new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
				SMCModel smcModel = new SMCModel(key, path, type, Integer.parseInt(simulations), Double.parseDouble(alpha), Double.parseDouble(epsilon), model);
				models.add(smcModel);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return models;
	}
}
