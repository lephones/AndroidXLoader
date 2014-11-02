package net.lephones.android.classLoader;

import java.util.Iterator;
import java.util.Set;

import android.util.Log;

import net.lephones.android.plugin.PlugManager;
import dalvik.system.PathClassLoader;

public class HostClassLoader extends PathClassLoader {
	
	private static final String TAG = "HostClassLoader";
	
	private BootstrapClassloader bootLoader;
	private PathClassLoader defaultClassLoader; 
	private PlugManager pm ;

	public HostClassLoader(String dexPath, String libraryPath,
			ClassLoader parent, BootstrapClassloader bootLoader,
			PathClassLoader pathClassLoader, PlugManager pm) {
		super(dexPath, libraryPath, parent);
		
		this.bootLoader = bootLoader;
		this.defaultClassLoader = pathClassLoader;
		this.pm = pm;
	}
	
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		Log.e(TAG, "loadClass " + className);
		Class clazz = null;
		try{
			clazz = defaultClassLoader.loadClass(className);
		}catch (ClassNotFoundException e) {
			
		}
		if(clazz == null){
			
			try{
			Set<String> set = pm.getPluginAllNames();
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				String plugName = it.next();
				PluginClassloader loader = bootLoader.getClassLoaderByName(plugName);
				clazz = loader.loadClassFromCurrent(className);
				if(clazz == null){
					continue;
				}else{
					return clazz;
				}
				
			}
			}catch (ClassNotFoundException e) {
				
			}
			if(clazz == null){
				clazz = getParent().loadClass(className);
				
			}
		}
		
		return clazz;
	}
	
}
