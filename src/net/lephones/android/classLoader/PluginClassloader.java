package net.lephones.android.classLoader;

import java.io.IOException;

import android.util.Log;
import net.lephones.android.plugin.Plugin;
import net.lephones.android.utils.BaseUtil;
import dalvik.system.DexFile;

/**
 * 插件加载类
 * @author lephone
 *
 */
public class PluginClassloader extends ClassLoader{
	
	private static final String TAG = PluginClassloader.class.getSimpleName();
	
	private DexFile dexFile;
	
	public PluginClassloader(Plugin plugin , String optDir ,ClassLoader classloader ) {
		super(classloader);
		try {
			this.dexFile = DexFile.loadDex(plugin.getPath(), getOptPath(plugin.getPath(), optDir), 0) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private String getOptPath(String path, String optDir) {
		return BaseUtil.optPath(path, optDir);
	}


	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException  {
		
		BaseUtil.log(TAG, "loadClass " + className);
		
		Class clazz = dexFile.loadClass(className, this);
		if(clazz == null){
			clazz = super.loadClass(className);
		}
		return clazz;
	}
	
	public Class<?> loadClassFromCurrent(String className) throws ClassNotFoundException {
		Log.e(TAG, dexFile.getName() + "loadClassFromCurrent " + className);
		Class clazz = dexFile.loadClass(className, this);
		if(clazz == null){
			throw new ClassNotFoundException(this + " can,t find class: " + className);
		}
		return clazz;
	}
}
