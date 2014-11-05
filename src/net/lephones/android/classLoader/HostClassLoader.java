package net.lephones.android.classLoader;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import net.lephones.android.LaucherApplication;
import net.lephones.android.plugin.PlugManager;
import android.util.Log;
import dalvik.system.PathClassLoader;

public class HostClassLoader extends PathClassLoader {

	private static final String TAG = "HostClassLoader";

	private BootstrapClassloader bootLoader;
	private PathClassLoader defaultClassLoader;
	private PlugManager pm;

	public HostClassLoader(String dexPath, String libraryPath,
			ClassLoader parent, BootstrapClassloader bootLoader,
			PathClassLoader pathClassLoader, PlugManager pm) {
		super(dexPath, libraryPath, parent);

		this.bootLoader = bootLoader;
		this.defaultClassLoader = pathClassLoader;
		this.pm = pm;
	}

	//此处最好加上对包名的判断
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		Log.e(TAG, "loadClass " + className);
		Class clazz = null;
		if (clazz == null) {

			Set<String> set = pm.getPluginAllNames();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String plugName = it.next();
				PluginClassloader loader = bootLoader
						.getClassLoaderByName(plugName);
				try {
					clazz = loader.loadClassFromCurrent(className);
				} catch (ClassNotFoundException e) {
					continue;
				}
				if (clazz != null) {
					
					//remove
					LaucherApplication.instance.setResources(pm.getRm().getResources(plugName));
					
					return clazz;
				}
			}
			
		}
		
		if(clazz == null){
			try {
				clazz = defaultClassLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
	
			}
		}
		
		if(clazz == null){
			clazz = super.loadClass(className);
		}
		return clazz;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Log.e(TAG, "findClass " + name);
		return super.findClass(name);
	}
	
	public Class<?> loadClassFromCurrent(String className) throws ClassNotFoundException {
		Log.e(TAG, "loadClassFromCurrent " + className);
		return super.loadClass(className);
	}
	
}
