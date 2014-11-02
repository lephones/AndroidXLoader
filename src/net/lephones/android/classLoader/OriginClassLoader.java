package net.lephones.android.classLoader;

import android.util.Log;
import net.lephones.android.LaucherApplication;

/**
 * 此类用于替换原生APK的pathclassloader的parent 重写findclass 
 * 备用 目前啥也不做
 * 
 * @author lephone
 * 
 */
public class OriginClassLoader extends ClassLoader {

	private static final String TAG = OriginClassLoader.class.getSimpleName();

	public OriginClassLoader() {
		super();
	}

	@Override
	protected Class<?> findClass(String className)
			throws ClassNotFoundException {
		Log.e(TAG, "find " + className);
		return super.findClass(className);
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		Log.e(TAG, "load " + className);
		return super.loadClass(className);
	}

}
