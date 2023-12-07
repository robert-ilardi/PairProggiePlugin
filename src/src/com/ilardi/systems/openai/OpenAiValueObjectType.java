/**
 * Created Jun 22, 2023
 */
package com.ilardi.systems.openai;

import java.io.Serializable;

import com.ilardi.systems.chatai.ChatAiException;

/**
 * @author robert.ilardi
 *
 */

public enum OpenAiValueObjectType implements Serializable {
  BOT_ERROR_RESPONSE,

  BASIC_CHAT, BOT_BASIC_CHAT_SUCCESS,

  INLINE_GENERAL_CHAT, BOT_INLINE_GENERAL_CHAT_SUCCESS,

  ENGINEERED_PROMPT, BOT_ENGINEERED_PROMPT_SUCCESS,

  INLINE_ASK_COMMENTS, INLINE_ASK_COMMENTS_METHOD, INLINE_ASK_COMMENTS_CLASS, BOT_SUCCESS_COMMENTS, BOT_SUCCESS_COMMENTS_METHOD, BOT_SUCCESS_COMMENTS_CLASS,

  INLINE_ASK_CODE_REVIEW, INLINE_ASK_CODE_REVIEW_METHOD, INLINE_ASK_CODE_REVIEW_CLASS, BOT_SUCCESS_CODE_REVIEW, BOT_SUCCESS_CODE_REVIEW_METHOD, BOT_SUCCESS_CODE_REVIEW_CLASS,

  INLINE_ASK_CODE_GEN, INLINE_ASK_CODE_GEN_CLASS, INLINE_ASK_CODE_GEN_METHOD, BOT_SUCCESS_CODE_GEN, BOT_SUCCESS_CODE_GEN_CLASS, BOT_SUCCESS_CODE_GEN_METHOD,

  INLINE_ASK_OPTIMIZE, INLINE_ASK_OPTIMIZE_CLASS, INLINE_ASK_OPTIMIZE_METHOD, BOT_SUCCESS_OPTIMIZE, BOT_SUCCESS_OPTIMIZE_CLASS, BOT_SUCCESS_OPTIMIZE_METHOD;

  public static OpenAiValueObjectType mapReqTypeToResType(OpenAiValueObjectType reqType, boolean successRes) throws ChatAiException {
    OpenAiValueObjectType resType;

    //Map Request Type to Response Type
    //Add Cases as needed

    if (successRes) {
      //Success Types
      switch (reqType) {
        case BASIC_CHAT:
          resType = OpenAiValueObjectType.BOT_BASIC_CHAT_SUCCESS;
          break;
        case ENGINEERED_PROMPT:
          resType = OpenAiValueObjectType.BOT_ENGINEERED_PROMPT_SUCCESS;
          break;
        case INLINE_GENERAL_CHAT:
          resType = OpenAiValueObjectType.BOT_INLINE_GENERAL_CHAT_SUCCESS;
          break;
        case INLINE_ASK_COMMENTS:
          resType = OpenAiValueObjectType.BOT_SUCCESS_COMMENTS;
          break;
        case INLINE_ASK_COMMENTS_CLASS:
          resType = OpenAiValueObjectType.BOT_SUCCESS_COMMENTS_CLASS;
          break;
        case INLINE_ASK_COMMENTS_METHOD:
          resType = OpenAiValueObjectType.BOT_SUCCESS_COMMENTS_METHOD;
          break;
        case INLINE_ASK_CODE_GEN:
          resType = OpenAiValueObjectType.BOT_SUCCESS_CODE_GEN;
          break;
        case INLINE_ASK_CODE_GEN_CLASS:
          resType = OpenAiValueObjectType.BOT_SUCCESS_CODE_GEN_CLASS;
          break;
        case INLINE_ASK_CODE_GEN_METHOD:
          resType = OpenAiValueObjectType.BOT_SUCCESS_CODE_GEN_METHOD;
          break;
        case INLINE_ASK_OPTIMIZE:
          resType = OpenAiValueObjectType.BOT_SUCCESS_OPTIMIZE;
          break;
        case INLINE_ASK_OPTIMIZE_CLASS:
          resType = OpenAiValueObjectType.BOT_SUCCESS_OPTIMIZE_CLASS;
          break;
        case INLINE_ASK_OPTIMIZE_METHOD:
          resType = OpenAiValueObjectType.BOT_SUCCESS_OPTIMIZE_METHOD;
          break;
        case INLINE_ASK_CODE_REVIEW:
          resType = OpenAiValueObjectType.BOT_SUCCESS_CODE_REVIEW;
          break;
        case INLINE_ASK_CODE_REVIEW_CLASS:
          resType = OpenAiValueObjectType.BOT_SUCCESS_CODE_REVIEW_CLASS;
          break;
        case INLINE_ASK_CODE_REVIEW_METHOD:
          resType = OpenAiValueObjectType.BOT_SUCCESS_CODE_REVIEW_METHOD;
          break;
        default:
          //resType = OpenAiValueObjectType.BOT_GENERAL_SUCCESS;
          throw new ChatAiException("Request (" + reqType + ") to Response Mapping Does Not Exists!");
      }
    }
    else {
      //Error Types
      resType = OpenAiValueObjectType.BOT_ERROR_RESPONSE;
    }

    return resType;
  }

}
