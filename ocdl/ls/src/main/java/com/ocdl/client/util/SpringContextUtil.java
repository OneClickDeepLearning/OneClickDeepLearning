package com.ocdl.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class SpringContextUtil {

    private static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);
    static Properties prop = new Properties();
    static String path;

    /**
     * Be used to get the properties of the project
     * @param name key to get the property value
     * @return value
     */
    public static String getPropertiesValue(String name) {
        // 文件输出流
        try {
            return prop.getProperty(name);
        } catch (Exception e) {
            logger.error(String.format("当前环境变量中没有{%s}的配置", name));
            // 获取失败则返回null
            return null;
        }
    }

    /**
     * Be used to change the property in the property instance
     * @param key key value of the property pair
     * @param value value value of the property pair
     * @return True: change successful  False: change failed
     */
    public synchronized static Boolean changeProp(String key, String value) {
        prop.setProperty(key, value);
        // 文件输出流
        FileOutputStream fos=null;
        try {
             fos = new FileOutputStream(path);
            // 将Properties集合保存到流中
            prop.store(fos, key);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                fos.close();// 关闭流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Be used to update the property in the memory
     * @param newPath the property file path
     */
    public synchronized static void updateProp(String newPath) {
        String decodedPath = "";
        try {
            decodedPath = java.net.URLDecoder.decode(newPath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            decodedPath = newPath;
        }
        if (path == null || "".equals(path)) {
            path = decodedPath;
        }

        prop = new Properties();// 属性集合对象
        FileInputStream fis;
        try {
            fis = new FileInputStream(decodedPath);
            prop.load(fis);// 将属性文件流装载到Properties对象中
            fis.close();// 关闭流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }


}