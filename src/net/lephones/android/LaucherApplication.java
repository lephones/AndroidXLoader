package net.lephones.android;

import java.lang.reflect.Field;
import java.util.Iterator;

import dalvik.system.PathClassLoader;

import net.lephones.android.classLoader.BootstrapClassloader;
import net.lephones.android.classLoader.OriginClassLoader;
import net.lephones.android.classLoader.PluginClassloader;
import net.lephones.android.plugin.PlugManager;
import net.lephones.android.utils.BaseUtil;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * application 一切从这里开始
 * @author lephone
 *
 */
public class LaucherApplication extends Application {
	
	private static final String TAG = LaucherApplication.class.getName();
	
	private Field classLoaderField;
	private Object mPackageInfo;

	private BootstrapClassloader loader;
	
	@Override
	public void onCreate() {
	
		super.onCreate();
		Context context = getBaseContext();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			Field parentLoader = ClassLoader.class.getDeclaredField("parent");
			parentLoader.setAccessible(true);
			parentLoader.set(classLoader, new OriginClassLoader());
			
			Field loadedApkField = context.getClass().getDeclaredField("mPackageInfo");
			loadedApkField.setAccessible(true);
			mPackageInfo = loadedApkField.get(context);
			classLoaderField = mPackageInfo.getClass().getDeclaredField("mClassLoader");
			classLoaderField.setAccessible(true);
			
			PathClassLoader pathClassloader = (PathClassLoader) classLoaderField.get(mPackageInfo);
			
			PlugManager pm = new PlugManager(this);
			loader = new BootstrapClassloader(this,	pathClassloader, pm );
			
			classLoaderField.set(mPackageInfo, loader);
			
			initPlugLoader(pm);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void initPlugLoader(PlugManager pm) {
		Iterator<String> it = pm.getPluginAllNames().iterator();
		while(it.hasNext()){
			String name =it.next();
			loader.addPluginLoader(name, new PluginClassloader(pm.getPlugByName(name), pm.getPluginLibDir(), loader));
		}
	}
}
