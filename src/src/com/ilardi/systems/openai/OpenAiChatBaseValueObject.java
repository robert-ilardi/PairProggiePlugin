/**
 * Created Jun 14, 2023
 */
package com.ilardi.systems.openai;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author robert.ilardi
 *
 */

public abstract class OpenAiChatBaseValueObject implements Serializable, Comparable<OpenAiChatBaseValueObject> {

  @JsonIgnore
  protected OpenAiValueObjectType oaVoType;

  @JsonIgnore
  protected long createdTs;

  @JsonIgnore
  protected long lstModTs;

  @JsonIgnore
  protected long oaVoCreationIndex;

  public OpenAiChatBaseValueObject() {}

  public OpenAiValueObjectType getOaVoType() {
    return oaVoType;
  }

  public void setOaVoType(OpenAiValueObjectType oaVoType) {
    this.oaVoType = oaVoType;
  }

  public long getCreatedTs() {
    return createdTs;
  }

  public void setCreatedTs(long createdTs) {
    this.createdTs = createdTs;
  }

  public long getLstModTs() {
    return lstModTs;
  }

  public void setLstModTs(long lstModTs) {
    this.lstModTs = lstModTs;
  }

  public long getOaVoCreationIndex() {
    return oaVoCreationIndex;
  }

  public void setOaVoCreationIndex(long oaVoCreationIndex) {
    this.oaVoCreationIndex = oaVoCreationIndex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdTs, lstModTs, oaVoCreationIndex, oaVoType);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof OpenAiChatBaseValueObject)) {
      return false;
    }

    OpenAiChatBaseValueObject other = (OpenAiChatBaseValueObject) obj;

    return createdTs == other.createdTs && lstModTs == other.lstModTs && oaVoCreationIndex == other.oaVoCreationIndex && oaVoType == other.oaVoType;
  }

  @Override
  public int compareTo(OpenAiChatBaseValueObject other) {
    int comp = -1;

    if (other != null && other instanceof OpenAiChatBaseValueObject) {
      if (this.oaVoCreationIndex < other.oaVoCreationIndex) {
        comp = -1;
      }
      else if (this.oaVoCreationIndex == other.oaVoCreationIndex) {
        comp = 0;
      }
      else if (this.oaVoCreationIndex > other.oaVoCreationIndex) {
        comp = 1;
      }
    }

    return comp;
  }

  @Override
  public String toString() {
    return "OpenAiChatBaseValueObject [oaVoType=" + oaVoType + ", createdTs=" + createdTs + ", lstModTs=" + lstModTs + ", oaVoCreationIndex=" + oaVoCreationIndex + "]";
  }

}
