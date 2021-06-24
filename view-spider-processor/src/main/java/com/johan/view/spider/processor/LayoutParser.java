package com.johan.view.spider.processor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class LayoutParser {

    /**
     * 解析布局
     * @param layoutFile 布局名称
     * @param fileManager 文件管理器
     * @param origin 是否使用原名称
     * @return 解析出来的字段
     */
    public static List<ElementResult.ViewField> parseLayout(File layoutFile, FileManager fileManager, boolean origin) {
        List<ElementResult.ViewField> fieldList = new ArrayList<>();
        parseXml(layoutFile, fileManager, fieldList, origin);
        return fieldList;
    }

    /**
     * 解析 xml 内容
     * @param layoutFile 布局名称
     * @param fileManager 文件管理器
     * @param fieldList 保存解析出来的字段
     * @param origin 是否使用原名称
     */
    private static void parseXml(File layoutFile, FileManager fileManager, List<ElementResult.ViewField> fieldList, boolean origin) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParserHandler handler = new SAXParserHandler(fileManager, fieldList, origin);
            SAXParser parser = factory.newSAXParser();
            parser.parse(layoutFile, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * XML 解析器
     */
    private static class SAXParserHandler extends DefaultHandler {

        private FileManager fileManager;
        private List<ElementResult.ViewField> fieldList;
        private boolean origin;

        public SAXParserHandler(FileManager fileManager, List<ElementResult.ViewField> fieldList, boolean origin) {
            this.fileManager = fileManager;
            this.fieldList = fieldList;
            this.origin = origin;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            // 处理 include 标签
            if ("include".equals(qName)) {
                handleInclude(attributes);
                return;
            }
            List<ElementResult.ViewField> childFieldList = null;
            // 处理 ViewStub 标签
            if ("ViewStub".equals(qName)) {
                childFieldList = handleViewStub(attributes);
            }
            // 找到 id 属性
            String idValue = findAttribute(attributes, "android:id");
            if (idValue == null) return;
            String id = findLayout(idValue);
            ElementResult.ViewField viewField = new ElementResult.ViewField();
            viewField.type = qName;
            viewField.id = id;
            viewField.name = origin ? id : StringUtil.toUppercase(id);
            if (childFieldList != null) {
                viewField.childFieldList = childFieldList;
            }
            fieldList.add(viewField);
        }

        /**
         * 处理 include 标签
         * @param attributes 标签
         */
        private void handleInclude(Attributes attributes) {
            // 获取 layout 属性值
            String layoutValue = findAttribute(attributes, "layout");
            if (layoutValue == null) return;
            String layout = findLayout(layoutValue);
            File layoutFile = fileManager.findXmlFile(layout);
            if (layoutFile == null) return;
            // 解析 layout XML
            parseXml(layoutFile, fileManager, fieldList, origin);
        }

        /**
         * 处理 ViewStub 标签
         * @param attributes 标签
         * @return 子布局所有字段
         */
        private List<ElementResult.ViewField> handleViewStub(Attributes attributes) {
            List<ElementResult.ViewField> childFieldList = new ArrayList<>();
            // 获取 layout 属性值
            String layoutValue = findAttribute(attributes, "android:layout");
            if (layoutValue == null) {
                return childFieldList;
            }
            String layout = findLayout(layoutValue);
            File layoutFile = fileManager.findXmlFile(layout);
            if (layoutFile == null) {
                return childFieldList;
            }
            // 解析 layout XML
            parseXml(layoutFile, fileManager, childFieldList, origin);
            return childFieldList;
        }

        /**
         * 查找属性值
         * @param attributes 标签
         * @param attribute 目标属性标签
         * @return 目标属性值
         */
        private String findAttribute(Attributes attributes, String attribute) {
            int count = attributes.getLength();
            for (int i = 0; i < count; i++) {
                String attributeName = attributes.getQName(i);
                if (attribute.equals(attributeName)) {
                    return attributes.getValue(i);
                }
            }
            return null;
        }

        /**
         * 查找布局名称
         * @param attribute 属性
         * @return 布局名称
         */
        private String findLayout(String attribute) {
            int index = attribute.indexOf("/");
            if (index == -1) return attribute;
            return attribute.substring(index + 1);
        }

    }

}
