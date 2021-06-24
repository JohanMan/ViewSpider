package com.johan.view.spider.processor;


import java.util.List;

public class ElementResult {

    public String packageName;
    public String className;
    public List<ViewField> fieldList;

    public static class ViewField {
        public String type;
        public String name;
        public String id;
        List<ViewField> childFieldList;
    }

}
