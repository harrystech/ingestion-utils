package com.harrys.orika


import ma.glasnost.orika.impl.DefaultMapperFactory
import ma.glasnost.orika.{Converter, MapperFactory}

/**
 * Created by chris on 10/14/15.
 */
object OrikaUtils {

  def defaultConverters : Seq[Converter[_, _]] = ScalaConverters.scalaConverters ++ DateTimeConverters.dateTimeConverters

  def createDefaultMapperFactory() : MapperFactory = {
    val factory = new DefaultMapperFactory.Builder().mapNulls(true).build()
    registerDefaultConverters(factory)
  }

  def registerDefaultConverters(factory: MapperFactory) : MapperFactory = {
    defaultConverters.foreach(c => factory.getConverterFactory.registerConverter(c))
    factory
  }
}
