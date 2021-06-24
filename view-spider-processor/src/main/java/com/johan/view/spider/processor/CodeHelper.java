package com.johan.view.spider.processor;

import java.util.ArrayList;
import java.util.List;

public class CodeHelper {

    /**
     * 生成代码
     * @param result 解析结果
     * @return 代码
     */
    public static String createCode(ElementResult result) {
        StringBuilder builder = new StringBuilder();
        // 包名
        builder.append("package ").append(result.packageName).append(";").append("\n\n");
        // 导入包
        builder.append("import android.widget.*;").append("\n");
        builder.append("import android.webkit.*;").append("\n");
        builder.append("import com.johan.view.spider.ViewSpider;").append("\n");
        builder.append("import com.johan.view.spider.ViewStubSpider;").append("\n");
        builder.append("import android.content.Context;").append("\n");
        builder.append("import android.app.Activity;").append("\n");
        builder.append("import android.view.*;").append("\n\n");
        // 开始
        builder.append("public class ").append(toViewSpiderClassName(result.className)).append(" implements ViewSpider {").append("\n\n");
        // 字段
        // 顺便遍历出来 ViewStub 减少一次遍历
        List<ElementResult.ViewField> viewStubFieldList = new ArrayList<>();
        for (ElementResult.ViewField viewField : result.fieldList) {
            builder.append("\t").append("public ").append(viewField.type).append(" ").append(viewField.name).append(";").append("\n");
            // 保存 ViewStub
            if (viewField.childFieldList != null && viewField.childFieldList.size() > 0) {
                viewStubFieldList.add(viewField);
            }
        }
        builder.append("\n");
        // find activity 方法
        builder.append("\t").append("@Override").append("\n");
        builder.append("\t").append("public void find(Activity activity) {").append("\n");
        for (ElementResult.ViewField viewField : result.fieldList) {
            builder.append("\t\t").append(viewField.name).append(" = (").append(viewField.type).append(") activity.findViewById(activity.getResources().getIdentifier(\"").append(viewField.id).append("\", \"id\", activity.getPackageName()));").append("\n");
            if (viewField.childFieldList != null && viewField.childFieldList.size() > 0) {
                builder.append("\t\t").append(createViewStubFieldName(viewField.name)).append(" = new ").append(createViewStubClassName(viewField.name)).append("(").append(viewField.name).append(");").append("\n");
            }
        }
        builder.append("\t").append("}").append("\n\n");
        // find view 方法
        builder.append("\t").append("@Override").append("\n");
        builder.append("\t").append("public void find(View view) {").append("\n");
        builder.append("\t\t").append("Context context = view.getContext();").append("\n");
        for (ElementResult.ViewField viewField : result.fieldList) {
            builder.append("\t\t").append(viewField.name).append(" = (").append(viewField.type).append(") view.findViewById(context.getResources().getIdentifier(\"").append(viewField.id).append("\", \"id\", context.getPackageName()));").append("\n");
            if (viewField.childFieldList != null && viewField.childFieldList.size() > 0) {
                builder.append("\t\t").append(createViewStubFieldName(viewField.name)).append(" = new ").append(createViewStubClassName(viewField.name)).append("(").append(viewField.name).append(");").append("\n");
            }
        }
        builder.append("\t").append("}").append("\n\n");
        // ViewStub
        for (ElementResult.ViewField viewField : viewStubFieldList) {
            builder.append("\t").append("public ").append(createViewStubClassName(viewField.name)).append(" ").append(createViewStubFieldName(viewField.name)).append(";").append("\n");
            builder.append("\t").append("public static class ").append(createViewStubClassName(viewField.name)).append(" extends ViewStubSpider {").append("\n");
            builder.append("\t\t").append("public ").append(createViewStubClassName(viewField.name)).append("(ViewStub viewStub) {").append("\n");
            builder.append("\t\t\t").append("super(viewStub);").append("\n");
            builder.append("\t\t").append("}").append("\n");
            for (ElementResult.ViewField childViewField : viewField.childFieldList) {
                builder.append("\t\t").append("public ").append(childViewField.type).append(" ").append(childViewField.name).append(";").append("\n");
            }
            builder.append("\t\t").append("@Override").append("\n");
            builder.append("\t\t").append("public void inflate() {").append("\n");
            builder.append("\t\t\t").append("if (inflated) return;").append("\n");
            builder.append("\t\t\t").append("inflated = true;").append("\n");
            builder.append("\t\t\t").append("View view = viewStub.inflate();").append("\n");
            builder.append("\t\t\t").append("Context context = view.getContext();").append("\n");
            for (ElementResult.ViewField childViewField : viewField.childFieldList) {
                builder.append("\t\t\t").append(childViewField.name).append(" = (").append(childViewField.type).append(") view.findViewById(context.getResources().getIdentifier(\"").append(childViewField.id).append("\", \"id\", context.getPackageName()));").append("\n");
            }
            builder.append("\t\t").append("}").append("\n");
            builder.append("\t").append("}").append("\n\n");
        }
        // 结束
        builder.append("}");
        return builder.toString();
    }

    /**
     * 创建 ViewStub 类名
     * @param name 字段名
     * @return 结果
     */
    private static String createViewStubClassName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1) + "Spider";
    }

    /**
     * 创建 ViewStub 字段名
     * @param name 字段名
     * @return 结果
     */
    private static String createViewStubFieldName(String name) {
        return name + "Spider";
    }

    /**
     * 转成 ViewSpider 类名
     * @param className 类名
     * @return 结果
     */
    public static String toViewSpiderClassName(String className) {
        if (className.endsWith("Activity")) {
            className = className.replace("Activity", "");
        } else if (className.endsWith("Fragment")) {
            className = className.replace("Fragment", "");
        } else if (className.endsWith("Window")) {
            className = className.replace("Window", "");
        } else if (className.endsWith("Dialog")) {
            className = className.replace("Dialog", "");
        } else if (className.endsWith("Adapter")) {
            className = className.replace("Adapter", "");
        }
        className += "ViewSpider";
        return className;
    }

}
