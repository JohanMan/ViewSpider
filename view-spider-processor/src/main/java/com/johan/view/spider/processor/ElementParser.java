package com.johan.view.spider.processor;

import com.johan.view.spider.annotation.EnableViewSpider;

import java.io.File;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class ElementParser {

    private ProcessingEnvironment processingEnvironment;
    private FileManager fileManager;

    public ElementParser(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
        fileManager = new FileManager();
    }

    /**
     * 解析 Element
     * @param element 类信息
     * @return 解析结果
     */
    public ElementResult parseElement(Element element) {
        ElementResult result = new ElementResult();
        // 类名
        result.className = getClassName(element);
        // 包名
        result.packageName = getPackageName(element);
        // 布局文件名
        String layout = StringUtil.toLowercase(result.className);
        if (result.className.contains("Activity")) {
            layout = layout.replace("_activity", "");
            layout = "activity_" + layout;
        } else if (result.className.contains("Fragment")) {
            layout = layout.replace("_fragment", "");
            layout = "fragment_" + layout;
        } else if (result.className.contains("Window")) {
            layout = layout.replace("_window", "");
            layout = "window_" + layout;
        } else if (result.className.contains("Dialog")) {
            layout = layout.replace("_dialog", "");
            layout = "dialog_" + layout;
        }
        // 解析注解
        EnableViewSpider annotation = element.getAnnotation(EnableViewSpider.class);
        String value = annotation.value();
        boolean origin = annotation.origin();
        if (!value.equals("")) {
            layout = value;
        }
        // 查找布局文件
        File layoutFile = fileManager.findXmlFile(layout);
        if (layoutFile == null) {
            Logger.getInstance().logWarning("没找到" + layout + "布局文件");
            return null;
        }
        // 解析布局文件 xml
        List<ElementResult.ViewField> fieldList = LayoutParser.parseLayout(layoutFile, fileManager, origin);
        result.fieldList = fieldList;
        return result;
    }

    /**
     * 获取类名
     * @param element 类信息
     * @return 类名
     */
    private String getClassName(Element element) {
        return element.getSimpleName().toString();
    }

    /**
     * 获取包名
     * @param element 类信息
     * @return 包名
     */
    private String getPackageName(Element element) {
        return processingEnvironment.getElementUtils().getPackageOf(element.getEnclosingElement()).asType().toString();
    }

}
