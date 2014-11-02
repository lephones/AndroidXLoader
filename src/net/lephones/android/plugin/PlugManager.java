package net.lephones.android.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.lephones.android.R;

import android.content.Context;

public class PlugManager {
	
	private Context context;
	
	private String pluginDir;
	
	private String pluginOptDir;
	
	private String pluginLibDir;
	
	//保存所有plugin
	private Map<String, Plugin> pluginMap;

	public PlugManager(Context context) {
		this.context = context;
		this.pluginDir = context.getDir("plugins", Context.MODE_PRIVATE).getAbsolutePath();
		this.pluginOptDir = context.getDir("plugins_opt", Context.MODE_PRIVATE).getAbsolutePath();
		this.pluginLibDir = context.getDir("plugins_lib", Context.MODE_PRIVATE).getAbsolutePath();
		this.pluginMap = new ConcurrentHashMap<String, Plugin>();
		initPlugins();
	}
	
	/**
	 * 通过配置的组件类名 寻找对应的plugin
	 * @param classname
	 * @return
	 */
	public Plugin getPluginByComponentName(String classname){
		Collection<Plugin> collection =  pluginMap.values();
		Iterator<Plugin> it =  collection.iterator();
		while(it.hasNext()){
			Plugin plugin = it.next();
			//获得所有组件 activity service ....
			String[] components = plugin.getComponents();
			if(components !=null){
				for(String componentName:components){
					if(classname.equalsIgnoreCase(componentName)){
						return plugin;
					}
				}
			}
		}
		return null;
	}
	
	public Set<String> getPluginAllNames() {
		
		Set<String> set = pluginMap.keySet();
		
		return set;

	}
	
	private void initPlugins(){
		InputStream in = context.getResources().openRawResource(R.raw.plugins);
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader buffered = new BufferedReader(inReader);
		
		String s = null;
		try {
			while((s = buffered.readLine()) != null){
				String[] arrays = s.split("\\|");
				
				Plugin plugin = new Plugin(context.getApplicationInfo().dataDir + File.separator  + "lib" + File.separator + arrays[2]);
				this.pluginMap.put(arrays[0], plugin);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPluginLibDir() {
		return pluginLibDir;
	}
	
	public Plugin getPlugByName(String name) {
		return this.pluginMap.get(name);
	}

}
