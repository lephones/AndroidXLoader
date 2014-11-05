package net.lephones.android;

import java.lang.reflect.Field;
import java.util.Iterator;

import dalvik.system.PathClassLoader;
import net.lephones.android.classLoader.BootstrapClassloader;
import net.lephones.android.classLoader.OriginClassLoader;
import net.lephones.android.classLoader.PluginClassloader;
import net.lephones.android.plugin.PlugManager;
import net.lephones.android.resources.ResourcesManager;
import net.lephones.android.utils.BaseUtil;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewStub;

/**
 * application 一切从这里开始
 * @author lephone
 *
 */
public class LaucherApplication extends Application {
	
	private static final String TAG = LaucherApplication.class.getName();

	public static LaucherApplication instance;
	
	private Field classLoaderField;
	private Object mPackageInfo;

	private BootstrapClassloader loader;

	private Field loadedApkField;
	
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		Context context = getBaseContext();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			Field parentLoader = ClassLoader.class.getDeclaredField("parent");
			parentLoader.setAccessible(true);
			parentLoader.set(classLoader, new OriginClassLoader());
			
			loadedApkField = context.getClass().getDeclaredField("mPackageInfo");
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

	/**
	 * 下面的代码仅用于测试
	 * @param pm
	 */
	@Deprecated
	private void initPlugLoader(PlugManager pm) {
		ResourcesManager rm = new ResourcesManager(getResources());
		Iterator<String> it = pm.getPluginAllNames().iterator();
		while(it.hasNext()){
			String name =it.next();
			loader.addPluginLoader(name, new PluginClassloader(pm.getPlugByName(name), pm.getPluginLibDir(), loader));
			rm.addResourcesMap(pm.getPlugByName(name));
		}
		pm.setRm(rm);
	}
	
	public void setResources(Resources rs) {
		Field field;
		try {
			field = mPackageInfo.getClass().getDeclaredField("mResources");

			field.setAccessible(true);
			field.set(mPackageInfo, rs);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
