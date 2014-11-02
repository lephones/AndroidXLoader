package net.lephones.android.utils;

import android.util.Log;

public class BaseUtil {
	
	public static void log(String tag , String msg){
		Log.e(tag, msg);
	}
	
	public static String optPath(String soPath , String optdir) {
		 	String str;
		    if ((soPath == null) || (optdir == null))
		    {
		      str = null;
		    }
		    StringBuilder sb = new StringBuilder(80);
		    sb.append(optdir);
		    if (!optdir.endsWith("/"))
		      sb.append("/");
		    int i = soPath.lastIndexOf("/");
		    if (i >= 0)
		      soPath = soPath.substring(i + 1);
		    int j = soPath.lastIndexOf(".");
		    if (j < 0){
		    	sb.append(soPath);
		    }else{
		    	sb.append(soPath, 0, j);
		    }
		    sb.append(".dex");
		    str = sb.toString();
		      
		      return str;
		}

}
