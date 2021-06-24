package com.johan.view.spider.processor;

import com.google.auto.service.AutoService;
import com.johan.view.spider.annotation.EnableViewSpider;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.johan.view.spider.annotation.EnableViewSpider"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ViewSpiderProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;
    private ElementParser elementParser;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(EnableViewSpider.class);
        if (elements.size() == 0) {
            return true;
        }
        Logger.getInstance().init(processingEnvironment);
        Logger.getInstance().logNote("+++++++++++ 开始爬取View +++++++++++");
        long startTime = System.currentTimeMillis();
        elementParser = new ElementParser(processingEnvironment);
        for (Element element : elements) {
            ElementResult elementResult = elementParser.parseElement(element);
            if (elementResult == null) {
                Logger.getInstance().logWarning(element + " 解析结果为 NULL");
                continue;
            }
            writeJavaFile(elementResult);
        }
        Logger.getInstance().logNote("+++++++++++ 结束爬取View，用时 " + (System.currentTimeMillis() - startTime) + "ms  +++++++++++");
        Logger.getInstance().destroy();
        return true;
    }

    private void writeJavaFile(ElementResult result) {
        String javaCode = CodeHelper.createCode(result);
        try {
            JavaFileObject source = processingEnvironment.getFiler()
                    .createSourceFile(result.packageName + "." + CodeHelper.toViewSpiderClassName(result.className));
            Writer writer = source.openWriter();
            writer.write(javaCode);
            writer.flush();
            writer.close();
            Logger.getInstance().logNote("写入文件成功：" + result.packageName + "." + CodeHelper.toViewSpiderClassName(result.className));
        } catch (IOException e) {
            Logger.getInstance().logError("写入文件失败：" + result.packageName + "." + CodeHelper.toViewSpiderClassName(result.className));
        }
    }

}
