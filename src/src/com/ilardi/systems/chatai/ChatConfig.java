/**
 * Created Sep 10, 2023
 */
package com.ilardi.systems.chatai;

import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_PRECONFIGURED_CHAT;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author robert.ilardi
 *
 */

public class ChatConfig implements Serializable {

  private ChatKey chatKey;

  private ChatContextMode contextMode;
  private ChatGroupType groupType;
  private SystemChatTag systemTag;

  private Properties chatProps;

  public ChatConfig() {}

  public ChatConfig(ChatKey chatKey, ChatConfig runtimeAdditionDefaultCfg) {
    this.chatKey = chatKey;

    this.contextMode = runtimeAdditionDefaultCfg.contextMode;
    this.groupType = runtimeAdditionDefaultCfg.groupType;
    this.systemTag = runtimeAdditionDefaultCfg.systemTag;

    this.chatProps = (Properties) runtimeAdditionDefaultCfg.chatProps.clone();
  }

  public ChatKey getChatKey() {
    return chatKey;
  }

  public void setChatKey(ChatKey chatKey) {
    this.chatKey = chatKey;
  }

  public ChatContextMode getContextMode() {
    return contextMode;
  }

  public void setContextMode(ChatContextMode contextMode) {
    this.contextMode = contextMode;
  }

  public ChatGroupType getGroupType() {
    return groupType;
  }

  public void setGroupType(ChatGroupType groupType) {
    this.groupType = groupType;
  }

  public synchronized void setProperty(String name, String value) {
    if (chatProps == null) {
      chatProps = new Properties();
    }

    chatProps.setProperty(name, value);
  }

  public synchronized String getProperty(String name) {
    String value = null;

    if (chatProps != null) {
      value = chatProps.getProperty(name);
    }

    return value;
  }

  public SystemChatTag getSystemTag() {
    return systemTag;
  }

  public void setSystemTag(SystemChatTag systemTag) {
    this.systemTag = systemTag;
  }

  public Properties getChatProps() {
    return chatProps;
  }

  public void setChatProps(Properties chatProps) {
    this.chatProps = chatProps;
  }

  @Override
  public String toString() {
    return "ChatConfig [chatKey=" + chatKey + ", contextMode=" + contextMode + ", groupType=" + groupType + ", systemTag=" + systemTag + ", chatProps=" + chatProps + "]";
  }

  public boolean getPropertyAsBoolean(String name) {
    boolean b = false;
    String value;

    value = getProperty(name);

    if (value != null) {
      value = value.trim();
      b = "TRUE".equalsIgnoreCase(value);
    }

    return b;
  }

  public boolean isAutoSendInitialPromptEnabled() {
    return getPropertyAsBoolean(PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT);
  }

  public boolean includeProjectEngineeredPrompt() {
    return getPropertyAsBoolean(PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT);
  }

  public boolean isPreconfiguredChat() {
    return getPropertyAsBoolean(PROPS_SUFFIX_PRECONFIGURED_CHAT);
  }

  public boolean matchesEntityMatchingExpression(String entityName) {
    return (chatKey != null ? chatKey.matchesEntityMatchingExpression(entityName) : false);
  }

  public boolean isSystemChat() {
    return SystemChatTag.isSystemChatTag(systemTag);
  }

  public boolean isStatelessChat() {
    return ChatContextMode.STATELESS.equals(contextMode);
  }

}
