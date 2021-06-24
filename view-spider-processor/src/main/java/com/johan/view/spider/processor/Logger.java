package com.johan.view.spider.processor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

public class Logger {

    private static Logger instance;

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    private Logger() {

    }

    private ProcessingEnvironment processingEnvironment;

    /**
     * 初始化
     * @param processingEnvironment 环境
     */
    public void init(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    /**
     * 销毁
     */
    public void destroy() {
        this.processingEnvironment = null;
    }

    /**
     * Note Log
     * @param info 信息
     */
    public void logNote(String info) {
        if (processingEnvironment == null) {
            return;
        }
        Messager messager = processingEnvironment.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "** Note ** \n" + info);
    }

    /**
     * Warning Log
     * @param info 信息
     */
    public void logWarning(String info) {
        if (processingEnvironment == null) {
            return;
        }
        Messager messager = processingEnvironment.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, "** Warning ** \n" + info);
    }

    /**
     * Error Log
     * @param info 信息
     */
    public void logError(String info) {
        if (processingEnvironment == null) {
            return;
        }
        Messager messager = processingEnvironment.getMessager();
        messager.printMessage(Diagnostic.Kind.ERROR, "** Error ** \n" + info);
    }

}
