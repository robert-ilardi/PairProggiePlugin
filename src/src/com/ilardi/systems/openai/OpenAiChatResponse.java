/**
 * Created Jun 5, 2023
 */
package com.ilardi.systems.openai;

import com.ilardi.systems.chatai.SmartChatRingBufferValueObject;

/**
 * @author robert.ilardi
 *
 */

public abstract class OpenAiChatResponse extends SmartChatRingBufferValueObject {

  protected boolean responseProcessed;

  public OpenAiChatResponse() {
    super();
    responseProcessed = false;
  }

  public boolean isResponseProcessed() {
    return responseProcessed;
  }

  public void setResponseProcessed(boolean responseProcessed) {
    this.responseProcessed = responseProcessed;
  }

  @Override
  public String toString() {
    return "OpenAiChatResponse [responseProcessed=" + responseProcessed + ", engineeredPrompt=" + engineeredPrompt + ", priority=" + priority + ", oaVoType=" + oaVoType + ", createdTs=" + createdTs
        + ", lstModTs=" + lstModTs + ", oaVoCreationIndex=" + oaVoCreationIndex + "]";
  }

}
