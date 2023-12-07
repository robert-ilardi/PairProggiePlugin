/**
 * Created Oct 10, 2023
 */
package com.ilardi.systems.chatai;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class ChatSessionStates implements Serializable {

  private boolean sentInitialEngineeredPrompt = false;

  public ChatSessionStates() {}

  public boolean isSentInitialEngineeredPrompt() {
    return sentInitialEngineeredPrompt;
  }

  public void setSentInitialEngineeredPrompt(boolean sentInitialEngineeredPrompt) {
    this.sentInitialEngineeredPrompt = sentInitialEngineeredPrompt;
  }

}
