/**
 * Created Jul 18, 2023
 */
package com.ilardi.systems.chatai;

import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_VALUE_VARIABLE_TOKEN_EXACT_MATCH;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_VALUE_VARIABLE_TOKEN_MATCH_KEY_NAME;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author robert.ilardi
 *
 */

public class ChatKey implements Serializable, Comparable<ChatKey> {

  private String chatName;

  private int orderIndex;

  private String displayName;

  private String toolTip;

  private boolean visible;

  private String entityMatchingExpression;

  protected ChatKey(String chatName) {
    this.chatName = chatName;
  }

  public String getChatName() {
    return chatName;
  }

  public int getOrderIndex() {
    return orderIndex;
  }

  public void setOrderIndex(int orderIndex) {
    this.orderIndex = orderIndex;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getToolTip() {
    return toolTip;
  }

  public void setToolTip(String toolTip) {
    this.toolTip = toolTip;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getEntityMatchingExpression() {
    return entityMatchingExpression;
  }

  public void setEntityMatchingExpression(String entityMatchingExpression) {
    this.entityMatchingExpression = entityMatchingExpression;
  }

  @Override
  public int hashCode() {
    return Objects.hash(chatName);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ChatKey)) {
      return false;
    }
    ChatKey other = (ChatKey) obj;
    return Objects.equals(chatName, other.chatName);
  }

  @Override
  public String toString() {
    return "ChatKey [chatName=" + chatName + ", orderIndex=" + orderIndex + ", displayName=" + displayName + ", toolTip=" + toolTip + ", visible=" + visible + "]";
  }

  @Override
  public int compareTo(ChatKey other) {
    if (other == null) {
      return 1;
    }
    else if (orderIndex > other.orderIndex) {
      return 1;
    }
    else if (orderIndex < other.orderIndex) {
      return -1;
    }
    else {
      return 0;
    }
  }

  public boolean matchesEntityMatchingExpression(String entityName) {
    boolean matched;
    String[] tokens;
    String tmp;

    if (entityName == null) {
      matched = false;
    }
    if (PROPS_VALUE_VARIABLE_TOKEN_MATCH_KEY_NAME.equalsIgnoreCase(entityMatchingExpression)) {
      matched = chatName.equals(entityName);
    }
    else if (entityMatchingExpression.startsWith(PROPS_VALUE_VARIABLE_TOKEN_EXACT_MATCH)) {
      tokens = entityMatchingExpression.split(":", 2);

      if (tokens != null && tokens.length == 2) {
        tmp = tokens[1];

        if (tmp != null) {
          tmp = tmp.trim();
          matched = entityName.equals(tmp);
        }
        else {
          matched = false;
        }
      }
      else {
        matched = false;
      }
    }
    else {
      matched = false;
    }

    return matched;
  }

}
