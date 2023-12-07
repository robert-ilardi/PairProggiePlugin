/**
 * Created Jun 8, 2023
 */
package com.ilardi.systems.openai;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author robert.ilardi
 *
 */

public class OpenAiError extends OpenAiChatBaseValueObject {

  @JsonProperty("message")
  private String message;

  @JsonProperty("type")
  private String type;

  @JsonProperty("param")
  private String param;

  @JsonProperty("code")
  private String code;

  public OpenAiError() {
    super();
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the param
   */
  public String getParam() {
    return param;
  }

  /**
   * @param param the param to set
   */
  public void setParam(String param) {
    this.param = param;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "OpenAiError [message=" + message + ", type=" + type + ", param=" + param + ", code=" + code + ", oaVoType=" + oaVoType + ", createdTs=" + createdTs + ", lstModTs=" + lstModTs
        + ", oaVoCreationIndex=" + oaVoCreationIndex + "]";
  }

}
