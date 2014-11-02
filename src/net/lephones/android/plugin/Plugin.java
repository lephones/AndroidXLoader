package net.lephones.android.plugin;

/**
 * 插件
 * @author lephone
 *
 */
public class Plugin {
	
	private String name;
	
	private String version;
	
	//保存的路径
	private String path;
	
	private String[] components ;

	public Plugin(String path) {
		this.name = "test";
		this.version = version;
		this.path = path;
	}

	public String[] getComponents() {
		return components;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getPath() {
		return path;
	}
	
	

}
