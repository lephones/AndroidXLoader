package net.lephones.android.classLoader;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.lephones.android.plugin.PlugManager;
import net.lephones.android.plugin.Plugin;
import net.lephones.android.utils.BaseUtil;
import android.content.Context;
import dalvik.system.PathClassLoader;

/**
 * 引导启动类
 * @author lephone
 *
 */
public class BootstrapClassloader extends PathClassLoader {
	
	private static final String TAG = BootstrapClassloader.class.getSimpleName();
	
	private ClassLoader hostClassLoader;
	private Context context;
	private PlugManager plugManager;
	private Map<String, PluginClassloader> pluginLoaderMap;
	
	public BootstrapClassloader(Context context, PathClassLoader originClassLoader ,PlugManager plugManager) {
		
		super(context.getApplicationInfo().sourceDir, getBootClassLoader(ClassLoader.getSystemClassLoader()));
		this.context = context;
		this.pluginLoaderMap = new ConcurrentHashMap<String, PluginClassloader>();
		this.plugManager = plugManager;
		this.hostClassLoader = new HostClassLoader(context.getApplicationInfo().sourceDir, getLibPath(), getBootClassLoader(ClassLoader.getSystemClassLoader()), this, originClassLoader, plugManager);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		BaseUtil.log(TAG, "findClass " + name);
		//先看是不是插件的组件
		Plugin plugin = plugManager.getPluginByComponentName(name);
		PluginClassloader loader = null;
		if(plugin != null){
			loader = getClassLoaderByName(plugin.getName());
			return loader.loadClassFromCurrent(name);
		}else{
			return hostClassLoader.loadClass(name);
		}
		
	}
	
	/**
	 * 添加一个插件加载器
	 * @param pluginName
	 * @param plugin
	 */
	public void addPluginLoader(String pluginName , PluginClassloader pluginLoader) {
		synchronized (pluginLoaderMap) {
			pluginLoaderMap.put(pluginName, pluginLoader);
		}
		
	}
	
	/**
	 * 删除一个插件加载器
	 * @param pluginName
	 * @param plugin
	 */
	public void removePluginLoader(String pluginName){
		synchronized (pluginLoaderMap) {
			pluginLoaderMap.remove(pluginName);
		}
	}
	
	public PluginClassloader getClassLoaderByName(String pluginName) {
		synchronized (pluginLoaderMap) {
			return pluginLoaderMap.get(pluginName);
		}
	}
	
	private String getLibPath() {
		return this.plugManager.getPluginLibDir() + File.pathSeparator + context.getApplicationInfo().dataDir + File.separator + "lib";
	}
	
	private static ClassLoader getBootClassLoader(ClassLoader classloader) {
		while (!classloader.getClass().getSimpleName().equalsIgnoreCase("BootClassLoader"))
			classloader = classloader.getParent();
		    return classloader;
	}
	
	
}