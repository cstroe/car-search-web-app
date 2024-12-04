package com.example

import freemarker.template.{Configuration, TemplateExceptionHandler}

import java.util.TimeZone

object Freemarker {
  val freemarker: Configuration = {
    val cfg = new Configuration(Configuration.VERSION_2_3_33)
    cfg.setClassLoaderForTemplateLoading(getClass.getClassLoader, "/templates")
    cfg.setDefaultEncoding("UTF-8")
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER)
    cfg.setLogTemplateExceptions(false)
    cfg.setWrapUncheckedExceptions(true)
    cfg.setFallbackOnNullLoopVariable(false)
    cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault)
    cfg
  }
}
