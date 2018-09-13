package mapek;

public class Configuration {
	ManagedSystem system;
	Environment environment;
	Qualities qualities;
	
	public Configuration(){
		system = new ManagedSystem();
		environment = new Environment();
		qualities = new Qualities();
	}
	
	protected Configuration getCopy() {
		Configuration newConfiguration = new Configuration();
		newConfiguration.system = system.getCopy();
		newConfiguration.environment = environment.getCopy();
		newConfiguration.qualities = qualities.getCopy();
		return newConfiguration;
	}
}
