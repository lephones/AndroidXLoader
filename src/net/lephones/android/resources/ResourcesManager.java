package net.lephones.android.resources;

import java.lang.reflect.Method;
import java.util.HashMap;

import net.lephones.android.plugin.Plugin;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

public class ResourcesManager {
	
	private Resources onrignResources;
	
	private HashMap<String, Resources> resourcesMap = new HashMap<String, Resources>();
	
	public ResourcesManager(Resources onrignResources) {
		this.onrignResources = onrignResources;
	}

	public void addResourcesMap(Plugin plugin) {
		
		if(plugin ==null || TextUtils.isEmpty(plugin.getName()) || TextUtils.isEmpty(plugin.getPath())){
			return;
		}
		if(resourcesMap.containsKey(plugin.getName())){
			return;
		}
		try{
			AssetManager assets = AssetManager.class.newInstance();
			Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
			addAssetPathMethod.setAccessible(true);
			addAssetPathMethod.invoke(assets, plugin.getPath());
			Resources resources = new Resources(assets, onrignResources.getDisplayMetrics(), onrignResources.getConfiguration());
			resourcesMap.put(plugin.getName(), resources);
		}catch(Exception e){
			
			throw new RuntimeException(e);
		}
			
	}
	
	public HashMap<String, Resources> getResourcesMap() {
		return resourcesMap;
	}
	
	public Resources getResources(String pluginName) {
		return resourcesMap.get(pluginName);
	}

}
