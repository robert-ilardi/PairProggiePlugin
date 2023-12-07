/**
 * Created Oct 9, 2023
 */
package com.ilardi.systems.probud;

import java.io.Serializable;

import com.ilardi.systems.chatai.ChatKey;

/**
 * @author rober
 *
 */
public class ChatTexts implements Serializable {

  private ChatKey chatKey;

  private boolean chatContextChanged;

  private String globalChatTxt;

  private String chatContextTxt;

  public ChatTexts() {}

  public boolean isChatContextChanged() {
    return chatContextChanged;
  }

  public void setChatContextChanged(boolean chatContextChanged) {
    this.chatContextChanged = chatContextChanged;
  }

  public String getGlobalChatTxt() {
    return globalChatTxt;
  }

  public void setGlobalChatTxt(String globalChatTxt) {
    this.globalChatTxt = globalChatTxt;
  }

  public ChatKey getChatKey() {
    return chatKey;
  }

  public void setChatKey(ChatKey chatKey) {
    this.chatKey = chatKey;
  }

  public String getChatContextTxt() {
    return chatContextTxt;
  }

  public void setChatContextTxt(String chatContextTxt) {
    this.chatContextTxt = chatContextTxt;
  }

  @Override
  public String toString() {
    return "ChatTexts [globalChatTxt=" + globalChatTxt + ", chatKey=" + chatKey + ", chatContextTxt=" + chatContextTxt + "]";
  }

}
