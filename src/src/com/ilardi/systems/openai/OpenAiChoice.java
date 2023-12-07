/**
 * Created Jun 5, 2023
 */
package com.ilardi.systems.openai;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author robert.ilardi
 *
 */

public class OpenAiChoice extends OpenAiChatBaseValueObject {

  @JsonProperty("message")
  private OpenAiMessage message;

  @JsonProperty("finish_reason")
  private String finishReason;

  @JsonProperty("index")
  private int index;

  public OpenAiChoice() {}

  public OpenAiMessage getMessage() {
    return message;
  }

  public void setMessage(OpenAiMessage message) {
    this.message = message;
  }

  public String getFinishReason() {
    return finishReason;
  }

  public void setFinishReason(String finishReason) {
    this.finishReason = finishReason;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  @Override
  public String toString() {
    return "OpenAiChoice [message=" + message + ", finishReason=" + finishReason + ", index=" + index + ", oaVoType=" + oaVoType + ", createdTs=" + createdTs + ", lstModTs=" + lstModTs
        + ", oaVoCreationIndex=" + oaVoCreationIndex + "]";
  }

}
