/**
 * Created Jun 5, 2023
 */
package com.ilardi.systems.openai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author robert.ilardi
 *
 */

public class OpenAiChatSuccessResponse extends OpenAiChatResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("object")
  private String object;

  @JsonProperty("created")
  private long created;

  @JsonProperty("model")
  private String model;

  @JsonProperty("usage")
  private OpenAiUsage usage;

  @JsonProperty("choices")
  private List<OpenAiChoice> choices;

  public OpenAiChatSuccessResponse() {
    super();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public long getCreated() {
    return created;
  }

  public void setCreated(long created) {
    this.created = created;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public OpenAiUsage getUsage() {
    return usage;
  }

  public void setUsage(OpenAiUsage usage) {
    this.usage = usage;
  }

  public List<OpenAiChoice> getChoices() {
    return choices;
  }

  public void setChoices(List<OpenAiChoice> choices) {
    this.choices = choices;
  }

  public String getAllReplyText() {
    OpenAiChoice choice;
    OpenAiMessage mesg;
    StringBuilder sb;
    String tmp, replyTxt = null;

    if (choices != null && !choices.isEmpty()) {
      sb = new StringBuilder();

      for (int i = 0; i < choices.size(); i++) {
        choice = choices.get(i);

        if (choice != null) {
          mesg = choice.getMessage();

          if (mesg != null) {
            tmp = mesg.getContent();

            if (tmp != null) {
              tmp = tmp.trim();

              if (tmp.length() > 0) {
                if (i > 0) {
                  sb.append("\n");
                }

                sb.append(tmp);
              }
            }
          }
        }
      }

      if (sb.length() > 0) {
        replyTxt = sb.toString();
      }

      sb = null;
      choice = null;
      mesg = null;
      tmp = null;
    }

    return replyTxt;
  }

  @Override
  public String toString() {
    return "OpenAiChatSuccessResponse [id=" + id + ", object=" + object + ", created=" + created + ", model=" + model + ", usage=" + usage + ", choices=" + choices + ", responseProcessed="
        + responseProcessed + ", engineeredPrompt=" + engineeredPrompt + ", priority=" + priority + ", oaVoType=" + oaVoType + ", createdTs=" + createdTs + ", lstModTs=" + lstModTs
        + ", oaVoCreationIndex=" + oaVoCreationIndex + "]";
  }

}
