package deltaiot.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class Configurations {

	public static URL url;
	public static String token;

	public static void loadConfigurations() {

		if (url == null && token == null) {
			Properties prop = new Properties();
			InputStream input = null;

			try {
				String path = System.getProperty("user.dir") + "/config.properties";

				input = new FileInputStream(path);

				// load a properties file
				prop.load(input);

				// get the property value and print it out

				token = prop.getProperty("token").trim();
				String urlString = prop.getProperty("url");
				url = new URL(urlString);
				// System.out.println(EMAIL);
				// System.out.println(URL);

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
		}
	}
}
