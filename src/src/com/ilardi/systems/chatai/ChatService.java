/**
 * Created May 23, 2023
 */
package com.ilardi.systems.chatai;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilardi.systems.openai.OpenAiChatErrorResponse;
import com.ilardi.systems.openai.OpenAiChatRequest;
import com.ilardi.systems.openai.OpenAiChatResponse;
import com.ilardi.systems.openai.OpenAiChatSuccessResponse;
import com.ilardi.systems.openai.OpenAiValueObjectType;

/**
 * @author robert.ilardi
 *
 */

public class ChatService {

  private static final Logger logger = LogManager.getLogger(ChatService.class);

  private ChatSession chatSession;

  private CloseableHttpClient httpClient;

  private ObjectMapper objMapper;

  public ChatService() {
    objMapper = new ObjectMapper();
  }

  public ChatSession getChatSession() {
    return chatSession;
  }

  public void setChatSession(ChatSession chatSession) {
    this.chatSession = chatSession;
  }

  private synchronized CloseableHttpClient getChatGptConnection() {
    if (!validHttpClient()) {
      closeHttpClient();
    }

    if (httpClient == null) {
      logger.debug("Creating New HTTP Client...");
      httpClient = HttpClients.createDefault();
    }
    else {
      logger.debug("Reusing HTTP Client...");
    }

    return httpClient;
  }

  private synchronized void closeHttpClient() {
    if (httpClient != null) {
      logger.debug("HTTP Client NOT Null. Closing HTTP Client...");
      httpClient = null;
      //TODO Do Proper Closing of HTTP Connections
    }
    else {
      logger.debug("HTTP Client is Null. Skipping HTTP Client Closure.");
    }
  }

  private synchronized boolean validHttpClient() {
    boolean valid;

    if (httpClient == null) {
      logger.debug("HTTP Client NOT Valid!");
      valid = false;
    }
    else {
      logger.debug("HTTP Client Is Valid!");
      valid = true;
    }

    return valid;
  }

  private synchronized HttpPost getHttpPost(String jsonStr) throws UnsupportedEncodingException {
    HttpPost httpPost;
    String openAiApiUrl, openAiApiKey;

    logger.debug("Creating New HTTP Post Object...");

    openAiApiUrl = chatSession.getOpenAiApiUrl();
    openAiApiKey = chatSession.getOpenAiApiKey();

    httpPost = new HttpPost(openAiApiUrl);
    httpPost.setHeader("Authorization", "Bearer " + openAiApiKey);
    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setEntity(new StringEntity(jsonStr));

    return httpPost;
  }

  public synchronized OpenAiChatResponse send(OpenAiChatRequest oaReq, ChatKey chatKey) throws ChatAiException {
    OpenAiChatResponse oaRes = null;
    CloseableHttpClient httpClient = null;
    HttpPost httpPost = null;
    HttpResponse httpRes = null;
    HttpEntity httpEntity = null;
    String jsonStr = null;
    int httpStatusCode;
    OpenAiValueObjectType reqType;

    try {
      httpClient = getChatGptConnection();

      jsonStr = objMapper.writeValueAsString(oaReq);

      httpPost = getHttpPost(jsonStr);

      logger.debug("Sending: " + jsonStr);

      httpRes = httpClient.execute(httpPost);

      logger.debug("HTTP Returned Status: " + httpRes.getStatusLine());

      httpStatusCode = httpRes.getStatusLine().getStatusCode();

      logger.debug("HTTP Status Code: " + httpStatusCode);

      httpEntity = httpRes.getEntity();

      if (httpEntity != null) {
        jsonStr = EntityUtils.toString(httpEntity);

        logger.debug("Received: " + jsonStr);

        switch (httpStatusCode) {
          case HttpStatus.SC_OK:
            oaRes = objMapper.readValue(jsonStr, OpenAiChatSuccessResponse.class);
            break;
          default:
            oaRes = objMapper.readValue(jsonStr, OpenAiChatErrorResponse.class);
        }

        reqType = oaReq.getOaVoType();

        chatSession.populateProBudFields(oaRes, true, chatKey, reqType);
      }
      else {
        logger.debug("NULL Entity Returned by Server!");
      }

      return oaRes;
    } //End try block
    catch (Exception e) {
      throw new ChatAiException("An error occurred while attempting to send OpenAI Chat Request. System Message: " + e.getMessage(), e);
    }
    finally {
      jsonStr = null;
      httpEntity = null;
      httpRes = null;

      if (httpPost != null) {
        httpPost.releaseConnection();
        httpPost = null;
      }

      httpClient = null;
    }
  }

}
