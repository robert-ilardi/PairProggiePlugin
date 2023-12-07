/**
 * Created Jun 8, 2023
 */
package com.ilardi.systems.openai;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilardi.systems.chatai.SmartChatRingBufferValueObject;

/**
 * @author robert.ilardi
 *
 */

public class OpenAiChatRequest extends SmartChatRingBufferValueObject {

  @JsonProperty("model")
  private String model;

  @JsonProperty("messages")
  private List<OpenAiMessage> messages;

  public OpenAiChatRequest() {
    super();
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<OpenAiMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<OpenAiMessage> messages) {
    this.messages = messages;
  }

  public void addMessage(OpenAiMessage mesg) {
    if (messages == null) {
      messages = new ArrayList<>();
    }

    messages.add(mesg);
  }

  public void addMessage(String role, String txtMesg, OpenAiValueObjectType oaVoType) {
    OpenAiMessage mesg;

    mesg = new OpenAiMessage(role, txtMesg, oaVoType);

    addMessage(mesg);
  }

  @Override
  public String toString() {
    return "OpenAiChatRequest [model=" + model + ", messages=" + messages + ", engineeredPrompt=" + engineeredPrompt + ", priority=" + priority + ", oaVoType=" + oaVoType + ", createdTs=" + createdTs
        + ", lstModTs=" + lstModTs + ", oaVoCreationIndex=" + oaVoCreationIndex + "]";
  }

}
