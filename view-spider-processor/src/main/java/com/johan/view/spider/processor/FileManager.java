package com.johan.view.spider.processor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private String layoutPattern = ".*res\\\\layout.*\\.xml";
    private Map<String, File> fileMap = new HashMap<>();

    public FileManager() {
        File projectFile = new File("");
        File parent = new File(projectFile.getAbsolutePath());
        long startTime = System.currentTimeMillis();
        Logger.getInstance().logNote("===== 开始查找布局文件 =====");
        findXml(parent);
        Logger.getInstance().logNote("===== 开始查找布局文件，用时 " + (System.currentTimeMillis() - startTime) + "ms =====");
    }

    /**
     * 查找所有 xml 布局文件
     * @param parent 父文件夹
     */
    private void findXml(File parent) {
        File[] files = parent.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findXml(file);
            }  else {
                if (file.getAbsolutePath().matches(layoutPattern)) {
                    String fileName = file.getName();
                    int index = fileName.lastIndexOf(".");
                    fileName = fileName.substring(0, index);
                    Logger.getInstance().logNote(fileName + " >> " + file.getAbsolutePath());
                    fileMap.put(fileName, file);
                }
            }
        }
    }

    /**
     * 查找布局文件
     * @param name 布局名称
     * @return 布局文件
     */
    public File findXmlFile(String name) {
        return fileMap.get(name);
    }

}
