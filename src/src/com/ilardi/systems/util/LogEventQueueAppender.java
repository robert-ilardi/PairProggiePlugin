/**
 * Created Jul 18, 2023
 */
package com.ilardi.systems.util;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * @author robert.ilardi
 *
 */

@Plugin(name = "LogEventQueueAppender", category = "Core", elementType = "appender", printObject = true)
public class LogEventQueueAppender extends AbstractAppender {

  private LogEventQueue leQueue;

  public LogEventQueueAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
    super(name, filter, layout, ignoreExceptions, properties);

    leQueue = LogEventQueue.getInstance();
  }

  @Override
  public void append(LogEvent event) {
    byte[] bArr;
    String logMesgTxt;

    bArr = getLayout().toByteArray(event);

    logMesgTxt = new String(bArr);

    leQueue.enqueueLogMessage(logMesgTxt);
  }

  @PluginFactory
  public static LogEventQueueAppender createAppender(@PluginAttribute("name")
  String name, @PluginElement("Layout")
  Layout<? extends Serializable> layout, @PluginElement("Filter")
  Filter filter, @PluginAttribute("ignoreExceptions")
  boolean ignoreExceptions, @PluginElement("properties")
  Property[] properties) {
    if (name == null) {
      LOGGER.error("No name provided for " + LogEventQueueAppender.class.getName());
      return null;
    }

    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }

    return new LogEventQueueAppender(name, filter, layout, ignoreExceptions, properties);
  }

}
