/**
 * Created Jun 5, 2023
 */
package com.ilardi.systems.chatai;

import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_BOT_CHARACTER_DESCRIPTION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_BOT_NAME;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_CHATS_CONFIGS_PROP_SUFFIX_LIST;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_MAX_DIALOG_MESSAGE_TOKENS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_MAX_REQUEST_HISTORY_OBJECTS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_MAX_RESPONSE_HISTORY_OBJECTS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_OPENAI_API_CHATGPT_MODEL;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_OPENAI_API_CHATGPT_ROLE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_OPENAI_API_KEY;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_OPENAI_API_URL;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PREFIX_CHATS_CONFIGS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PREFIX_RUNTIME_ADDITION_DEFAULT_CONFIGS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROGRAMMER_DESCRIPTION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROGRAMMER_NAME;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROGRAMMING_LANGUAGE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROGRAMMING_TECHNIQUE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROJECT_DESCRIPTION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROJECT_REQUIREMENTS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROJECT_TITLE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_CHAT_DESCRIPTION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_CHAT_KEY_NAME;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_CHAT_TOPIC;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_CONTEXT_MODE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_DISPLAY_NAME;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_ENABLE_EXTENDED_UI_PROCESSING;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_ENABLE_INIT_PROMPT_EXTENDED_UI_PROCESSING;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_GROUP_TYPE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_INIT_MESG;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_ORDER_INDEX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_PRECONFIGURED_CHAT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_SYSTEM_CHAT_TAG;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_TOOLTIP;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_SUFFIX_VISIBLE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_TOKEN_SAFETY_MARGIN_PERCENTAGE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ilardi.systems.openai.OpenAiChatRequest;
import com.ilardi.systems.openai.OpenAiChatResponse;
import com.ilardi.systems.openai.OpenAiValueObjectType;
import com.ilardi.systems.util.ApplicationContext;
import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class ChatSession implements Serializable {

  private static final Logger logger = LogManager.getLogger(ChatSession.class);

  private ApplicationContext appCntxt;

  private MultiChatRingBuffers ringBuffers;

  //Chat Session Config Objects-------------------------->
  private ArrayList<String> chatConfigPropertySuffixLst;

  private ArrayList<ChatKey> activeChatKeyLst;

  private ArrayList<ChatConfig> preconfiguredChatsConfigLst;

  private ArrayList<ChatKey> preconfiguredChatKeyLst;

  private HashMap<ChatKey, ChatConfig> chatsConfigMap;

  private HashMap<String, ChatKey> nameChatKeyMap;

  private HashMap<SystemChatTag, ArrayList<ChatKey>> sysTagChatKeyMap;

  private HashMap<ChatGroupType, ArrayList<ChatKey>> groupTypeChatKeyMap;

  private ChatConfig runtimeAdditionDefaultCfg;

  private HashMap<ChatKey, ChatSessionStates> chatSessionStatesMap;

  //----------------------------------------------------->

  private String openAiApiUrl;
  private String openAiApiKey;

  private String openAiApiChatGptModel;
  private String openAiApiChatGptRole;

  private String programmerName;
  private String programmerDescription;

  private String botName;
  private String botCharacterDescription;

  private String projectTitle;
  private String projectDescription;
  private String projectRequirements;

  private String programmingLanguage;
  private String programmingTechnique;

  private int maxDialogTokens;
  private int maxRequestHistoryObjs;
  private int maxResposneHistoryObjs;
  private int tokenSafetyMarginPercentage;

  public ChatSession() {}

  public synchronized void init() throws ChatAiException {
    try {
      logger.debug("Initializing Chat Session Object");

      appCntxt = ApplicationContext.getInstance();

      chatConfigPropertySuffixLst = new ArrayList<String>();
      activeChatKeyLst = new ArrayList<ChatKey>();
      chatsConfigMap = new HashMap<ChatKey, ChatConfig>();
      nameChatKeyMap = new HashMap<String, ChatKey>();
      sysTagChatKeyMap = new HashMap<SystemChatTag, ArrayList<ChatKey>>();
      groupTypeChatKeyMap = new HashMap<ChatGroupType, ArrayList<ChatKey>>();
      preconfiguredChatKeyLst = new ArrayList<ChatKey>();
      preconfiguredChatsConfigLst = new ArrayList<ChatConfig>();
      chatSessionStatesMap = new HashMap<ChatKey, ChatSessionStates>();

      //Read Configurations----------------------->

      loadChatSessionConfig();

      //Finish ALL Config Reading Above this line
      //------------------------------------------>
      //Create Needed Objects Below this line

      //Create Initial Ring Buffers
      ringBuffers = new MultiChatRingBuffers();

      createSystemChatRingBuffers();
    }
    catch (Exception e) {
      throw new ChatAiException("An error occurred while attempting to Initialize Chat Session! System Message: " + e.getMessage(), e);
    }
  }

  private void createSystemChatRingBuffers() {
    ChatConfig chatCfg;

    logger.debug("Creating System Chat Ring Buffers");

    for (int i = 0; i < preconfiguredChatsConfigLst.size(); i++) {
      chatCfg = preconfiguredChatsConfigLst.get(i);

      if (chatCfg.isSystemChat()) {
        logger.debug("Creating System Chat Ring Buffer: " + chatCfg);
        createChatRingBuffer(chatCfg);
      }
    }
  }

  private void loadChatSessionConfig() throws IlardiSystemsException {
    logger.debug("Loading and Initializating Chat Session Configuration Objects...");

    readChatSessionProperties();

    readChatCfgPropertySuffixes();

    readChatsConfigs();

    readRuntimeAdditionDefaultConfig();
  }

  private void readChatCfgPropertySuffixes() throws IlardiSystemsException {
    String tmp;
    String[] tokens;
    StringBuilder sb;

    logger.debug("Reading Chat Config Property Suffixes...");

    //Read Chat Config Property Suffix List ------------------------>

    tmp = appCntxt.getUserProperty(PROPS_CHATS_CONFIGS_PROP_SUFFIX_LIST);
    if (tmp != null) {
      tmp = tmp.trim();

      if (tmp.length() > 0) {
        tokens = tmp.split(",");

        if (tokens != null && tokens.length > 0) {
          chatConfigPropertySuffixLst.clear();

          for (int i = 0; i < tokens.length; i++) {
            tmp = tokens[i];

            if (tmp != null && !tmp.isBlank()) {
              tmp = tmp.trim();
              chatConfigPropertySuffixLst.add(tmp);
            }
          }
        }

        if (chatConfigPropertySuffixLst != null && !chatConfigPropertySuffixLst.isEmpty()) {
          sb = new StringBuilder();
          sb.append("Supported Chat Config Property Suffix List: ");

          for (int i = 0; i < chatConfigPropertySuffixLst.size(); i++) {
            if (i > 0) {
              sb.append(", ");
            }

            tmp = chatConfigPropertySuffixLst.get(i);

            sb.append(tmp);
          }

          tmp = sb.toString();

          logger.debug(tmp);
        }
      }

      sb = null;
      tokens = null;
      tmp = null;
    }

    //Add Property Reads Above This Line-------------------->

    sb = null;
    tmp = null;
    tokens = null;
  }

  private void readChatsConfigs() throws IlardiSystemsException {
    String name;
    Iterator<Object> iter;
    Properties chatsPropsSuperSet;
    HashSet<String> chatsConfigKeyNamePrefixSet;
    int lastDotSeparatorIdx;
    ChatConfig chatCfg;

    logger.debug("Reading Chats Configurations -->");

    logger.debug("Loading Chats Configurations Properties Super Set...");

    chatsPropsSuperSet = appCntxt.getUserPropertiesByPropPrefix(PROPS_PREFIX_CHATS_CONFIGS);

    if (chatsPropsSuperSet != null) {
      //Get All Chat Super-Prefixes (Chat Key Name part of full Property Name)
      logger.debug("Parsing All Chat Key Name Super-Prefixes");

      iter = chatsPropsSuperSet.keySet().iterator();

      chatsConfigKeyNamePrefixSet = new HashSet<String>();

      while (iter.hasNext()) {
        name = (String) iter.next();

        name = name.trim();
        lastDotSeparatorIdx = name.lastIndexOf('.');

        name = name.substring(0, lastDotSeparatorIdx);
        name = name.trim();

        if (!chatsConfigKeyNamePrefixSet.contains(name)) {
          chatsConfigKeyNamePrefixSet.add(name);
        }
      } //End while loop

      //Create All Chat Configuration Objects
      for (String keyNamePropPrefix : chatsConfigKeyNamePrefixSet) {
        //Create Chat Config
        chatCfg = createChatConfigFromProperties(chatsPropsSuperSet, keyNamePropPrefix);

        //Make sure we are tracking Chat Objects
        //storeChatObjectsForTracking(chatCfg);
        preconfiguredChatsConfigLst.add(chatCfg);
      } //End for loop

      chatsConfigKeyNamePrefixSet.clear();
      chatsConfigKeyNamePrefixSet = null;
    } //End null check
  }

  private ChatConfig createChatConfigFromProperties(Properties chatsPropsSuperSet, String keyNamePropPrefix) {
    String name, value;
    StringBuilder sb;
    ChatConfig chatCfg;

    //Creation of the Chat Config Object must happen first
    chatCfg = createBasicChatConfig(chatsPropsSuperSet, keyNamePropPrefix);

    //Other Properties go Below this Line---------------------------->

    //Chat Topic --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_CHAT_TOPIC);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_CHAT_TOPIC;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_CHAT_TOPIC, value);

    //Chat Description --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_CHAT_DESCRIPTION);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_CHAT_DESCRIPTION;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_CHAT_DESCRIPTION, value);

    //Init Pre-Configured Message ------------------------>
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_INIT_MESG);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_INIT_MESG;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_INIT_MESG, value);

    //Auto Send Init Prompt --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT, value);

    //Include Project Engineered Prompt --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT, value);

    //Enable Init Prompt Extended UI Processing ------------------>
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_ENABLE_INIT_PROMPT_EXTENDED_UI_PROCESSING);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_ENABLE_INIT_PROMPT_EXTENDED_UI_PROCESSING;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_ENABLE_INIT_PROMPT_EXTENDED_UI_PROCESSING, value);

    //Enable Extended UI Processing --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_ENABLE_EXTENDED_UI_PROCESSING);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_ENABLE_EXTENDED_UI_PROCESSING;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_ENABLE_EXTENDED_UI_PROCESSING, value);

    //Is Preconfigured Chat ----------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_PRECONFIGURED_CHAT);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_PRECONFIGURED_CHAT;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_PRECONFIGURED_CHAT, value);

    //Chat Order Index ----------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_ORDER_INDEX);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_ORDER_INDEX;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_ORDER_INDEX, value);

    //Chat Display Name ----------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_DISPLAY_NAME);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_DISPLAY_NAME;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_DISPLAY_NAME, value);

    //Is Visible ----------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_VISIBLE);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_VISIBLE;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_VISIBLE, value);

    //ToolTip ----------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_VISIBLE);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_VISIBLE;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_VISIBLE, value);

    //------------------------------------------------------------------->
    //Add More Chat Context Properties Before This Line

    //Copy All chatConfigPropertySuffixLst Properties into ChatCfg ----------->

    for (int i = 0; i < chatConfigPropertySuffixLst.size(); i++) {
      name = chatConfigPropertySuffixLst.get(i);

      if (keyNamePropPrefix != null) {
        sb = new StringBuilder();
        sb.append(keyNamePropPrefix);
        sb.append(".");
        sb.append(name);
        name = sb.toString();
      }

      value = chatsPropsSuperSet.getProperty(name);
      value = value.trim();

      chatCfg.setProperty(name, value);
    }

    //------------------------------------------------------------------------>    

    name = null;
    sb = null;
    value = null;

    return chatCfg;
  }

  private ChatConfig createBasicChatConfig(Properties chatsPropsSuperSet, String keyNamePropPrefix) {
    ChatConfig chatCfg;
    StringBuilder sb;
    String name, value;
    ChatKey chatKey;
    ChatGroupType chatGrp;
    ChatContextMode ctxMode;
    SystemChatTag sysTag;

    logger.debug("Creating New Chat Config Object using keyNamePropPrefix = " + keyNamePropPrefix);

    //Create new ChatConfig Object --------------------------------------->
    chatCfg = new ChatConfig();

    //Chat Key ----------------------------------------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_CHAT_KEY_NAME);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_CHAT_KEY_NAME;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    //Create ChatKey Object
    chatKey = createNewChatKey(value, chatsPropsSuperSet, keyNamePropPrefix);

    //Add the Chat Key to the Chat Config Object
    chatCfg.setChatKey(chatKey);

    //Store Chat Key Property
    chatCfg.setProperty(PROPS_SUFFIX_CHAT_KEY_NAME, value);

    //Other Chat Config Properties and Objects Go Below this Line ------->

    //Context Mode --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_CONTEXT_MODE);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_CONTEXT_MODE;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();
    value = value.toUpperCase();

    ctxMode = ChatContextMode.valueOf(value);
    chatCfg.setContextMode(ctxMode);

    chatCfg.setProperty(PROPS_SUFFIX_CONTEXT_MODE, value);

    //Chat Group Type --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_GROUP_TYPE);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_GROUP_TYPE;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();
    value = value.toUpperCase();

    chatGrp = ChatGroupType.valueOf(value);
    chatCfg.setGroupType(chatGrp);

    chatCfg.setProperty(PROPS_SUFFIX_GROUP_TYPE, value);

    //System Chat Tag --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_SYSTEM_CHAT_TAG);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_SYSTEM_CHAT_TAG;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();
    value = value.toUpperCase();

    sysTag = SystemChatTag.valueOf(value);
    chatCfg.setSystemTag(sysTag);

    chatCfg.setProperty(PROPS_SUFFIX_SYSTEM_CHAT_TAG, value);

    //Entity Match Expression ----------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatCfg.setProperty(PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION, value);

    //Add Additional Core Properties Above this Line--------------->

    sb = null;
    name = null;
    value = null;
    chatKey = null;
    chatGrp = null;
    ctxMode = null;
    sysTag = null;

    return chatCfg;
  }

  private ChatKey createNewChatKey(String chatKeyName, Properties chatsPropsSuperSet, String keyNamePropPrefix) {
    StringBuilder sb;
    ChatKey chatKey;
    String name, value;
    int iTmp;
    boolean bTmp;

    chatKey = new ChatKey(chatKeyName);

    //Order Index --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_ORDER_INDEX);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_ORDER_INDEX;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();
    iTmp = Integer.parseInt(value);

    chatKey.setOrderIndex(iTmp);

    //Display Name --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_DISPLAY_NAME);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_DISPLAY_NAME;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatKey.setDisplayName(value);

    //ToolTip --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_TOOLTIP);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_TOOLTIP;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatKey.setToolTip(value);

    //Visible --------------------------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_VISIBLE);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_VISIBLE;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();
    value = value.toUpperCase();
    bTmp = "TRUE".equalsIgnoreCase(value);

    chatKey.setVisible(bTmp);

    //Entity Match Expression ----------->
    if (keyNamePropPrefix != null) {
      sb = new StringBuilder();
      sb.append(keyNamePropPrefix);
      sb.append(".");
      sb.append(PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION);
      name = sb.toString();
    }
    else {
      name = PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION;
    }

    value = chatsPropsSuperSet.getProperty(name);
    value = value.trim();

    chatKey.setEntityMatchingExpression(value);

    //Add Additional Core Properties Above this Line--------------->

    sb = null;
    name = null;
    value = null;

    return chatKey;
  }

  private void addToGroupTypeChatKeyMap(ChatGroupType chatGrp, ChatKey chatKey) {
    ArrayList<ChatKey> ckLst;

    logger.debug("Adding Chat to GroupType-ChatKey Map - GroupType: " + chatGrp + "; ChatKey: " + chatKey);

    ckLst = groupTypeChatKeyMap.get(chatGrp);

    if (ckLst == null) {
      ckLst = new ArrayList<ChatKey>();
      groupTypeChatKeyMap.put(chatGrp, ckLst);
    }

    if (!ckLst.contains(chatKey)) {
      ckLst.add(chatKey);
    }
  }

  private synchronized void addToSysTagChatKeyMap(SystemChatTag sysTag, ChatKey chatKey) {
    ArrayList<ChatKey> ckLst;

    logger.debug("Adding System Chat to SysTag-ChatKey Map - SystemTag: " + sysTag + "; ChatKey: " + chatKey);

    ckLst = sysTagChatKeyMap.get(sysTag);

    if (ckLst == null) {
      ckLst = new ArrayList<ChatKey>();
      sysTagChatKeyMap.put(sysTag, ckLst);
    }

    if (!ckLst.contains(chatKey)) {
      ckLst.add(chatKey);
    }
  }

  public String engineerChatPrompt(ChatKey cKey) throws ChatAiException {
    StringBuilder sb;
    String engineeredPrompt, tmp;
    ChatConfig chatCfg;

    logger.debug("Engineering Chat Prompt for ChatKey: " + cKey);

    sb = new StringBuilder();

    //Project Details if Required--------------->

    if (includeProjectDetailsInPrompt(cKey)) {
      sb.append("Project Title: ");
      sb.append(this.projectTitle);
      sb.append("\n");

      sb.append("Project Description: ");
      sb.append(this.projectDescription);
      sb.append("\n");

      //sb.append("Project Requirements: ");
      //sb.append(this.projectRequirements);
      //sb.append("\n");

      sb.append("Programming Language: ");
      sb.append(this.programmingLanguage);
      sb.append("\n");

      sb.append("Programming Technique: ");
      sb.append(this.programmingTechnique);
      sb.append("\n");

      /*sb.append("My Name: ");
      sb.append(this.programmerName);
      sb.append("\n");
      
      sb.append("My Description: ");
      sb.append(this.programmerDescription);
      sb.append("\n");
      
      sb.append("Your Name: ");
      sb.append(this.botName);
      sb.append("\n");
      
      sb.append("Your Description: ");
      sb.append(this.botCharacterDescription);
      sb.append("\n");*/
    } //End includeProjectDetailsInPrompt Check

    //------------------------------------------------>

    //Chat Config Specific Prompt Details------------->
    chatCfg = chatsConfigMap.get(cKey);

    //Chat Topic-------------------------->
    tmp = chatCfg.getProperty(PROPS_SUFFIX_CHAT_TOPIC);

    if (tmp != null && !tmp.isBlank()) {
      tmp = tmp.trim();

      sb.append("Chat Topic: ");
      sb.append(tmp);
      sb.append("\n");
    }

    //Chat Specific Ready Message---------------------->
    tmp = chatCfg.getProperty(PROPS_SUFFIX_INIT_MESG);

    if (tmp != null && !tmp.isBlank()) {
      tmp = tmp.trim();

      sb.append("\n");
      sb.append(tmp);
      sb.append("\n");
    }

    //Add Additional Engineered Prompt Above This Line---------------->

    engineeredPrompt = sb.toString();

    chatCfg = null;
    sb = null;
    tmp = null;

    logger.debug("Engineered Prompt(" + cKey + "): " + engineeredPrompt);

    return engineeredPrompt;
  }

  private boolean includeProjectDetailsInPrompt(ChatKey chatKey) {
    ChatConfig chatCfg;
    boolean includeDetails = false;

    logger.debug("Checking if Project Detail are Required for Prompt Engineering - ChatKey: " + chatKey);

    chatCfg = chatsConfigMap.get(chatKey);

    if (chatCfg != null) {
      includeDetails = chatCfg.includeProjectEngineeredPrompt();
    }

    return includeDetails;
  }

  private void readChatSessionProperties() throws IlardiSystemsException {
    String sTmp;
    int iTmp;

    logger.debug("Reading Chat Session Properties...");

    sTmp = appCntxt.getUserProperty(PROPS_OPENAI_API_URL);
    sTmp = sTmp.trim();
    openAiApiUrl = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_OPENAI_API_KEY);
    sTmp = sTmp.trim();
    openAiApiKey = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_OPENAI_API_CHATGPT_MODEL);
    sTmp = sTmp.trim();
    openAiApiChatGptModel = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_OPENAI_API_CHATGPT_ROLE);
    sTmp = sTmp.trim();
    openAiApiChatGptRole = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_PROGRAMMER_NAME);
    if (sTmp != null) {
      sTmp = sTmp.trim();
      programmerName = sTmp;
    }

    sTmp = appCntxt.getUserProperty(PROPS_BOT_NAME);
    if (sTmp != null) {
      sTmp = sTmp.trim();
      botName = sTmp;
    }

    sTmp = appCntxt.getUserProperty(PROPS_PROJECT_TITLE);
    sTmp = sTmp.trim();
    projectTitle = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_PROJECT_DESCRIPTION);
    sTmp = sTmp.trim();
    projectDescription = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_PROGRAMMING_LANGUAGE);
    sTmp = sTmp.trim();
    programmingLanguage = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_MAX_DIALOG_MESSAGE_TOKENS);
    sTmp = sTmp.trim();
    iTmp = Integer.parseInt(sTmp);
    maxDialogTokens = iTmp;

    sTmp = appCntxt.getUserProperty(PROPS_MAX_REQUEST_HISTORY_OBJECTS);
    sTmp = sTmp.trim();
    iTmp = Integer.parseInt(sTmp);
    maxRequestHistoryObjs = iTmp;

    sTmp = appCntxt.getUserProperty(PROPS_MAX_RESPONSE_HISTORY_OBJECTS);
    sTmp = sTmp.trim();
    iTmp = Integer.parseInt(sTmp);
    maxResposneHistoryObjs = iTmp;

    sTmp = appCntxt.getUserProperty(PROPS_TOKEN_SAFETY_MARGIN_PERCENTAGE);
    sTmp = sTmp.trim();
    iTmp = Integer.parseInt(sTmp);
    tokenSafetyMarginPercentage = iTmp;

    sTmp = appCntxt.getUserProperty(PROPS_PROGRAMMING_TECHNIQUE);
    sTmp = sTmp.trim();
    programmingTechnique = sTmp;

    sTmp = appCntxt.getUserProperty(PROPS_PROGRAMMER_DESCRIPTION);
    if (sTmp != null) {
      sTmp = sTmp.trim();
      programmerDescription = sTmp;
    }

    sTmp = appCntxt.getUserProperty(PROPS_BOT_CHARACTER_DESCRIPTION);
    if (sTmp != null) {
      sTmp = sTmp.trim();
      botCharacterDescription = sTmp;
    }

    sTmp = appCntxt.getUserProperty(PROPS_PROJECT_REQUIREMENTS);
    if (sTmp != null) {
      sTmp = sTmp.trim();
      projectRequirements = sTmp;
    }
  }

  //public void removeChatRingBuffer(String entityName) {
  public void removeChatRingBuffer(ChatKey ck) {
    SmartChatRingBuffer ringBuffer;
    //ChatKey ck;

    //ck = findActiveChatKeyByMatchingExpression(entityName);

    if (ck != null) {
      if (!isSystemChat(ck)) {
        logger.debug("Removing Ring Buffer for ChatKey: " + ck);

        //ck = getChatKeyByName(chatKeyName);

        ringBuffer = ringBuffers.removeRingBuffer(ck);

        removeChatObjectsFromTracking(ck);

        ringBuffer.clearRingBuffer();

        ck = null;
        ringBuffer = null;
      }
      else {
        logger.debug("Cannot Remove System Chat: " + ck);
      }
    }
    /*else {
      logger.debug("Skipping RingBuffer Removal. Could not find ChatKey for EntityName: " + entityName);
    }*/
  }

  private boolean isSystemChat(ChatKey cKey) {
    ChatConfig chatCfg;
    SystemChatTag sysTag;
    boolean sysChat = false;

    if (cKey != null) {
      chatCfg = chatsConfigMap.get(cKey);

      if (chatCfg != null) {
        sysTag = chatCfg.getSystemTag();

        if (sysTag != null) {
          sysChat = SystemChatTag.isSystemChatTag(sysTag);
        }
      }
    }

    return sysChat;
  }

  public boolean isStatelessChat(ChatKey cKey) {
    ChatConfig chatCfg;
    ChatContextMode ctxMode;
    boolean statelessChat = false;

    if (cKey != null) {
      chatCfg = chatsConfigMap.get(cKey);

      if (chatCfg != null) {
        ctxMode = chatCfg.getContextMode();

        if (ctxMode != null) {
          statelessChat = ChatContextMode.STATELESS.equals(ctxMode);
        }
      }
    }

    return statelessChat;
  }

  private boolean checkPreconfiguredChatFlag(ChatKey cKey) {
    ChatConfig chatCfg;
    boolean preconf = false;

    logger.debug("Checking if Is Preconfigured Chat - ChatKey: " + cKey);

    chatCfg = chatsConfigMap.get(cKey);

    if (chatCfg != null) {
      preconf = chatCfg.isPreconfiguredChat();
    }

    return preconf;
  }

  public synchronized void createChatRingBuffer(String entityName) {
    ChatConfig chatCfg = null;

    logger.debug("Creating Chat Ring Buffer for EntityName: " + entityName);

    chatCfg = findChatConfigForActiveChatKByMatchingExpression(entityName);

    //First try to locate a Preconfigured but inactive Chat Config
    if (chatCfg == null) {
      //Additional Chat Config Required
      chatCfg = findPreconfiguredChatConfigByMatchingExpression(entityName);
    }

    //If not Preconfiguration is available, use Runtime Additional Defaulting
    if (chatCfg == null) {
      //Additional Chat Config Required
      chatCfg = createRuntimeAdditionalChatConfig(entityName);
    }

    if (chatCfg != null) {
      createChatRingBuffer(chatCfg);
    }
    else {
      logger.debug("Check Chats-Config Properties. Ring Buffer Cannot be Created. Could Not Locate or Create a Chat Configuration for EntityNanme: " + entityName);
    }
  }

  private ChatConfig findChatConfigForActiveChatKByMatchingExpression(String entityName) {
    ChatConfig chatCfg;
    ChatKey cKey;

    cKey = findActiveChatKeyByMatchingExpression(entityName);
    chatCfg = chatsConfigMap.get(cKey);
    cKey = null;

    return chatCfg;
  }

  private synchronized void createChatRingBuffer(ChatConfig chatCfg) {
    SmartChatRingBuffer ringBuffer;
    ChatKey cKey;

    logger.debug("Creating Chat Ring Buffer for ChatConfig: " + chatCfg);

    cKey = chatCfg.getChatKey();

    if (!ringBuffers.containsRingBuffer(cKey)) {
      ringBuffer = new SmartChatRingBuffer(cKey);

      ringBuffer.setChatGptModel(openAiApiChatGptModel);
      ringBuffer.setChatGptRole(openAiApiChatGptRole);

      ringBuffer.setMaxTokenCount(maxDialogTokens);
      ringBuffer.setMaxRequestCount(maxRequestHistoryObjs);
      ringBuffer.setMaxResponseCount(maxResposneHistoryObjs);
      ringBuffer.setTokenSafetyMarginPercentage(tokenSafetyMarginPercentage);

      ringBuffer.init();

      ringBuffers.addRingBuffer(ringBuffer);

      //Make sure we are tracking Chat Objects
      storeChatObjectsForTracking(chatCfg);
    }
    else {
      logger.debug("Chat Ring Buffer Already Exists: " + cKey);
    }
  }

  public void clearRingBuffer(ChatKey chatKey) {
    SmartChatRingBuffer ringBuffer;

    logger.debug("Clearing Ring Buffer for ChatKey: " + chatKey);

    ringBuffer = ringBuffers.getRingBuffer(chatKey);

    ringBuffer.clearRingBuffer();

    setSentInitialEngineeredPrompt(chatKey, false);
  }

  public void addNewRequestToDialog(String inputText, ChatKey chatKey, OpenAiValueObjectType oaVoType) {
    SmartChatRingBuffer ringBuffer;

    ringBuffer = ringBuffers.getRingBuffer(chatKey);

    ringBuffer.addNewRequestToDialog(inputText, oaVoType);
  }

  public boolean initialPromptSent(ChatKey cKey) throws ChatAiException {
    int sz;
    SmartChatRingBuffer ringBuffer;

    logger.debug("Checking if Intitial Prompt was Sent for Chat Key: " + cKey);

    ringBuffer = ringBuffers.getRingBuffer(cKey);

    if (ringBuffer == null) {
      throw new ChatAiException("Ring Buffer NOT Found for Chat Key: " + cKey);
    }

    sz = ringBuffer.requestHistorySize();

    return (sz > 0);
  }

  public OpenAiChatRequest getCompleteDialogAsRequest(ChatKey cKey, boolean trimDialogToMaxTokens) throws ChatAiException {
    SmartChatRingBuffer ringBuffer;

    logger.debug("Obtaining Complete Dialog for Context Chat Key: " + cKey);

    ringBuffer = ringBuffers.getRingBuffer(cKey);

    if (ringBuffer == null) {
      throw new ChatAiException("Ring Buffer NOT Found for Chat Key: " + cKey);
    }

    if (trimDialogToMaxTokens) {
      ringBuffer.smartlyRemoveDialogEntries();
    }

    return ringBuffer.getCompleteDialogAsRequest();
  }

  public void addResponseToDialog(OpenAiChatResponse oaRes, ChatKey chatKey) throws ChatAiException {
    SmartChatRingBuffer ringBuffer;

    ringBuffer = ringBuffers.getRingBuffer(chatKey);

    ringBuffer.addResponseToDialog(oaRes);
  }

  public void populateProBudFields(OpenAiChatResponse oaRes, boolean keeper, ChatKey chatKey, OpenAiValueObjectType reqType) throws ChatAiException {
    SmartChatRingBuffer ringBuffer;

    ringBuffer = ringBuffers.getRingBuffer(chatKey);

    ringBuffer.populateProBudFields(oaRes, keeper, reqType);
  }

  public ChatKey getChatKeyByName(String chatKeyName) {
    ChatKey cKey = null;

    if (chatKeyName != null) {
      cKey = nameChatKeyMap.get(chatKeyName);
    }

    return cKey;
  }

  public String getOpenAiApiUrl() {
    return openAiApiUrl;
  }

  public String getOpenAiApiKey() {
    return openAiApiKey;
  }

  public String getOpenAiApiChatGptModel() {
    return openAiApiChatGptModel;
  }

  public String getOpenAiApiChatGptRole() {
    return openAiApiChatGptRole;
  }

  public String getProgrammerName() {
    return programmerName;
  }

  public String getProgrammerDescription() {
    return programmerDescription;
  }

  public String getBotName() {
    return botName;
  }

  public String getBotCharacterDescription() {
    return botCharacterDescription;
  }

  public String getProjectTitle() {
    return projectTitle;
  }

  public String getProjectDescription() {
    return projectDescription;
  }

  public String getProjectRequirements() {
    return projectRequirements;
  }

  public String getProgrammingLanguage() {
    return programmingLanguage;
  }

  public String getProgrammingTechnique() {
    return programmingTechnique;
  }

  public int getMaxDialogTokens() {
    return maxDialogTokens;
  }

  public int getMaxRequestHistoryObjs() {
    return maxRequestHistoryObjs;
  }

  public int getMaxResposneHistoryObjs() {
    return maxResposneHistoryObjs;
  }

  public int getTokenSafetyMarginPercentage() {
    return tokenSafetyMarginPercentage;
  }

  public OpenAiChatResponse getLastResponse(ChatKey chatKey) {
    OpenAiChatResponse oaRes = null;
    SmartChatRingBuffer ringBuffer;

    ringBuffer = ringBuffers.getRingBuffer(chatKey);

    oaRes = ringBuffer.getLastResponseFromHistory();
    ringBuffer = null;

    return oaRes;
  }

  public int requestHistorySize(ChatKey cKey) {
    SmartChatRingBuffer ringBuffer;
    int sz;

    ringBuffer = ringBuffers.getRingBuffer(cKey);

    sz = ringBuffer.requestHistorySize();
    ringBuffer = null;

    return sz;
  }

  public int responseHistorySize(ChatKey cKey) {
    SmartChatRingBuffer ringBuffer;
    int sz;

    ringBuffer = ringBuffers.getRingBuffer(cKey);

    sz = ringBuffer.responseHistorySize();
    ringBuffer = null;

    return sz;
  }

  public int dialogSize(ChatKey cKey) {
    SmartChatRingBuffer ringBuffer;
    int sz;

    ringBuffer = ringBuffers.getRingBuffer(cKey);

    sz = ringBuffer.dialogSize();
    ringBuffer = null;

    return sz;
  }

  public List<OpenAiChatResponse> getUnprocessedResponses(ChatKey chatKey) {
    List<OpenAiChatResponse> osResLst;
    SmartChatRingBuffer ringBuffer;

    ringBuffer = ringBuffers.getRingBuffer(chatKey);

    osResLst = ringBuffer.getUnprocessedResponsesFromHistory();
    ringBuffer = null;

    return osResLst;
  }

  private synchronized void storeChatObjectsForTracking(ChatConfig chatCfg) {
    ChatKey chatKey;
    ChatGroupType chatGrp;
    SystemChatTag sysTag;
    ChatSessionStates csStates;

    logger.debug("Adding Chat Objects to Tracking In Memory Stores for ChatConfig: " + chatCfg);

    if (chatCfg != null) {
      chatKey = chatCfg.getChatKey();

      if (chatKey != null) {
        //Create Chat Session States
        csStates = new ChatSessionStates();
        chatSessionStatesMap.put(chatKey, csStates);

        //Add to Group Type Chat Key Map
        chatGrp = chatCfg.getGroupType();
        addToGroupTypeChatKeyMap(chatGrp, chatKey);

        //Add to System Tag Chat Key Map if is System Chat
        sysTag = chatCfg.getSystemTag();
        if (SystemChatTag.isSystemChatTag(sysTag)) {
          addToSysTagChatKeyMap(sysTag, chatKey);
        }

        //Adding Chat Key to Simple List
        if (!activeChatKeyLst.contains(chatKey)) {
          activeChatKeyLst.add(chatKey);
        }

        //Add Chat Key to the Name to ChatKey Map
        if (!nameChatKeyMap.containsKey(chatKey.getChatName())) {
          nameChatKeyMap.put(chatKey.getChatName(), chatKey);
        }

        //Store New Chat Config in Chat Config Map
        if (!chatsConfigMap.containsKey(chatKey)) {
          chatsConfigMap.put(chatKey, chatCfg);
        }

        if (checkPreconfiguredChatFlag(chatKey)) {
          if (!preconfiguredChatKeyLst.contains(chatKey)) {
            preconfiguredChatKeyLst.add(chatKey);
          }
        }
      } //End null chatKey check
    } //End null chatCfg check
  }

  private synchronized void removeChatObjectsFromTracking(ChatKey cKey) {
    List<ChatKey> ckLst;
    ChatConfig chatCfg;
    ChatGroupType grpTy;

    //if (!preconfiguredChatKeyLst.contains(cKey)) {
    if (!isSystemChat(cKey)) {
      logger.debug("Removing Chat Objects from In Memory Tracking for ChatKey: " + cKey);

      chatSessionStatesMap.remove(cKey);

      activeChatKeyLst.remove(cKey);

      nameChatKeyMap.remove(cKey.getChatName());

      chatCfg = chatsConfigMap.remove(cKey);

      if (chatCfg != null) {
        grpTy = chatCfg.getGroupType();

        ckLst = getChatKeysByGroup(grpTy);

        ckLst.remove(cKey);
      }

      ckLst = null;
      chatCfg = null;
      grpTy = null;
    }
    else {
      logger.debug("Skipping Removal of Chat Objects from Tracking for System Chat: " + cKey);
    }
  }

  public ArrayList<ChatKey> getChatKeysByGroup(ChatGroupType grpTy) {
    ArrayList<ChatKey> ckLst = null;

    if (grpTy != null) {
      ckLst = groupTypeChatKeyMap.get(grpTy);
    }

    return ckLst;
  }

  public ArrayList<ChatKey> getChatKeysBySystemTag(SystemChatTag sysTag) {
    ArrayList<ChatKey> ckLst = null;

    if (sysTag != null) {
      ckLst = sysTagChatKeyMap.get(sysTag);
    }

    return ckLst;
  }

  private void readRuntimeAdditionDefaultConfig() {
    String origName, newName, value;
    Iterator<Object> iter;
    Properties defaultChatsProps, refinedDefaultChatProps = null;
    int lastDotSeparatorIdx;

    logger.debug("Reading Chats Runtime Addition Default Configuration Template");

    defaultChatsProps = appCntxt.getUserPropertiesByPropPrefix(PROPS_PREFIX_RUNTIME_ADDITION_DEFAULT_CONFIGS);

    if (defaultChatsProps != null) {
      //Get All Chat Super-Prefixes (Chat Key Name part of full Property Name)
      logger.debug("Stripping Property Prefixes");

      iter = defaultChatsProps.keySet().iterator();

      refinedDefaultChatProps = new Properties();

      while (iter.hasNext()) {
        origName = (String) iter.next();

        newName = origName.trim();
        lastDotSeparatorIdx = newName.lastIndexOf('.');
        lastDotSeparatorIdx++;

        newName = newName.substring(lastDotSeparatorIdx);
        newName = newName.trim();

        value = defaultChatsProps.getProperty(origName);
        if (value != null) {
          value = value.trim();
        }

        refinedDefaultChatProps.setProperty(newName, value);
      } //End while loop
    } //End null check

    if (refinedDefaultChatProps != null) {
      runtimeAdditionDefaultCfg = createChatConfigFromProperties(refinedDefaultChatProps, null);
    }
  }

  private ChatConfig createRuntimeAdditionalChatConfig(String chatKeyName) {
    ChatConfig chatCfg;
    ChatKey chatKey;
    Properties rtAddProps;

    rtAddProps = runtimeAdditionDefaultCfg.getChatProps();

    chatKey = createNewChatKey(chatKeyName, rtAddProps, null);
    chatCfg = new ChatConfig(chatKey, runtimeAdditionDefaultCfg);

    //Make sure we are tracking Chat Objects
    //storeChatObjectsForTracking(chatCfg);

    return chatCfg;
  }

  public ArrayList<ChatKey> getSortedChatKeyListCopy() {
    ArrayList<ChatKey> sortedCkLst, tmp;

    //Note: each list needs to be cloned
    //since we do not know what the calling
    //client will do with them.
    //Cloning will prevent
    //corruption of main lists

    sortedCkLst = new ArrayList<ChatKey>();

    //Add Primary Chats First
    tmp = getChatKeysByGroup(ChatGroupType.PRIMARY);
    if (tmp != null && !tmp.isEmpty()) {
      tmp = new ArrayList<ChatKey>(tmp);
      Collections.sort(tmp);
      sortedCkLst.addAll(tmp);
      tmp = null;
    }

    //Add Secondary Chats Second
    tmp = getChatKeysByGroup(ChatGroupType.SECONDARY);
    if (tmp != null && !tmp.isEmpty()) {
      tmp = new ArrayList<ChatKey>(tmp);
      Collections.sort(tmp);
      sortedCkLst.addAll(tmp);
      tmp = null;
    }

    //Add Runtime Additional Chats Last
    tmp = getChatKeysByGroup(ChatGroupType.RUNTIME_ADDITION);
    if (tmp != null && !tmp.isEmpty()) {
      tmp = new ArrayList<ChatKey>(tmp);
      Collections.sort(tmp);
      sortedCkLst.addAll(tmp);
      tmp = null;
    }

    return sortedCkLst;
  }

  public boolean isAutoSendInitialPromptEnabled(ChatKey cKey) {
    boolean enabled = false;
    ChatConfig chatCfg;

    chatCfg = chatsConfigMap.get(cKey);

    enabled = chatCfg.isAutoSendInitialPromptEnabled();

    return enabled;
  }

  public ChatKey findActiveChatKeyByMatchingExpression(String entityName) {
    ChatKey cKey = null;

    for (int i = 0; i < activeChatKeyLst.size(); i++) {
      cKey = activeChatKeyLst.get(i);

      if (cKey.matchesEntityMatchingExpression(entityName)) {
        break;
      }
      else {
        cKey = null;
      }
    }

    return cKey;
  }

  public ChatConfig findPreconfiguredChatConfigByMatchingExpression(String entityName) {
    ChatConfig chatCfg = null;

    for (int i = 0; i < preconfiguredChatsConfigLst.size(); i++) {
      chatCfg = preconfiguredChatsConfigLst.get(i);

      if (chatCfg.matchesEntityMatchingExpression(entityName)) {
        break;
      }
      else {
        chatCfg = null;
      }
    }

    return chatCfg;
  }

  public void setSentInitialEngineeredPrompt(ChatKey cKey, boolean sent) {
    ChatSessionStates csStates;

    csStates = chatSessionStatesMap.get(cKey);

    csStates.setSentInitialEngineeredPrompt(sent);
  }

  public boolean isSentInitialEngineeredPrompt(ChatKey cKey) {
    ChatSessionStates csStates;
    boolean sent;

    csStates = chatSessionStatesMap.get(cKey);

    sent = csStates.isSentInitialEngineeredPrompt();

    return sent;
  }

  public int countCurrentTokens(ChatKey cKey) {
    SmartChatRingBuffer ringBuffer;
    int tokenCnt;

    ringBuffer = ringBuffers.getRingBuffer(cKey);

    tokenCnt = ringBuffer.countCurrentTokens();

    ringBuffer = null;

    return tokenCnt;
  }

}
