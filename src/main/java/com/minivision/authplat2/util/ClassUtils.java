package com.minivision.authplat2.util;

/**
 * class处理工具类
 * @author hughzhao
 * @2018年3月2日
 */
public final class ClassUtils {

	public static String getRepositoryName(Class<?> crudRepositoryClass) {
        for (Class<?> repositoryInterface : crudRepositoryClass.getInterfaces()) {
            if (repositoryInterface.getName().startsWith("com.minivision.authplat2.repository")) {
                return repositoryInterface.getSimpleName();
            }
        }
        return "UnknownRepository";
    }
	
}
