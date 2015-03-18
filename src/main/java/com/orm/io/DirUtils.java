package com.orm.io;

import java.io.File;

import com.orm.constants.ORMConstants;

public final class DirUtils {
    
    private DirUtils() {
        
    }

    public static void mkdir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();
        System.out.println("mkdir: " + dirName);
    }
    
    public static String getPackageName(String basePackageName, String packageName) {
        StringBuilder sb = new StringBuilder();
        sb.append(basePackageName.replace(".", ORMConstants.SLASH));
        sb.append(ORMConstants.SLASH);
        sb.append(packageName.replace(".", ORMConstants.SLASH));
        return sb.toString();
    }
}
