/**
 * Created Jul 2, 2023
 */
package com.ilardi.systems.probud;

import static com.ilardi.systems.probud.ProggieBuddieConstants.DEFAULT_PROPERTY_VALUE_MASK_STRING;
import static com.ilardi.systems.probud.ProggieBuddieConstants.DEFAULT_PROP_TABLE_HEIGHT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.DEFAULT_PROP_TABLE_WIDTH;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PLUGIN_DESCRIPTION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PLUGIN_ICON_PATH;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PLUGIN_SHORT_DESCRIPTION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PLUGIN_TITLE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_CODE_GEN_CLASS_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_CODE_GEN_METHOD_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_CODE_GEN_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_CODE_REVIEW_CLASS_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_CODE_REVIEW_METHOD_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_CODE_REVIEW_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_COMMENT_CLASS_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_COMMENT_METHOD_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_COMMENT_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_OPTIMIZE_CLASS_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_OPTIMIZE_METHOD_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ASK_OPTIMIZE_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_CODE_SNIPPET_END_MARKER;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_CODE_SNIPPET_START_MARKER;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_EDITOR_POPUP_MESSAGE_DURATION;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ENABLE_EDITOR_POPUP_MESSAGES;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ENABLE_TEXT_TO_SPEECH;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_FORCE_DISABLE_AUTO_SEND_INIT_PROMPT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CHAT_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CODE_GEN_CLASS_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CODE_GEN_METHOD_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CODE_GEN_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CODE_REVIEW_CLASS_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CODE_REVIEW_METHOD_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_CODE_REVIEW_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_COMMENT_CLASS_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_COMMENT_METHOD_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_COMMENT_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_OPTIMIZE_CLASS_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_OPTIMIZE_METHOD_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_INLINE_OPTIMIZE_PROMPT_PREFIX;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_PROPERTIES_TO_MASK_LIST;
import static com.ilardi.systems.probud.ProggieBuddieConstants.UI_CHAT_RING_BUFFER_LABEL_PREFIX;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.ilardi.systems.chatai.ChatAiException;
import com.ilardi.systems.chatai.ChatGroupType;
import com.ilardi.systems.chatai.ChatKey;
import com.ilardi.systems.chatai.ChatService;
import com.ilardi.systems.chatai.ChatSession;
import com.ilardi.systems.chatai.SystemChatTag;
import com.ilardi.systems.eclipse.EclipseUtils;
import com.ilardi.systems.eclipse.IlardiSysEclipseException;
import com.ilardi.systems.eclipse.NotificationWidget;
import com.ilardi.systems.openai.OpenAiChatErrorResponse;
import com.ilardi.systems.openai.OpenAiChatRequest;
import com.ilardi.systems.openai.OpenAiChatResponse;
import com.ilardi.systems.openai.OpenAiChatSuccessResponse;
import com.ilardi.systems.openai.OpenAiChoice;
import com.ilardi.systems.openai.OpenAiMessage;
import com.ilardi.systems.openai.OpenAiUsage;
import com.ilardi.systems.openai.OpenAiValueObjectType;
import com.ilardi.systems.speech.FreeTtsAdapter;
import com.ilardi.systems.util.ApplicationContext;
import com.ilardi.systems.util.IlardiSystemsException;
import com.ilardi.systems.util.LogEventQueue;
import com.ilardi.systems.util.StringUtils;
import com.ilardi.systems.util.SystemUtils;

/**
 * @author robert.ilardi
 *
 */

public class ProggieBuddieUiCore {

  private static final Logger logger = LogManager.getLogger(ProggieBuddieUiCore.class);

  private static ProggieBuddieUiCore instance = null;

  private ProggieBuddiePlugin plugin;

  private ProggieBuddieUi pbUi;

  private Composite container;

  private ChatService chatSrv;

  private ChatSession chatSession;

  private final Object pbLock;
  private final Object chatDisplayLock;
  private final Object consoleDisplayLock;

  private boolean uiSendingMessage;
  private boolean sendingMessage;
  private boolean asyncSendingMessageStarted;

  private boolean textToSpeechEnabled;

  private boolean editorPopupMesgsEnabled;
  private int editorPopupMessageDuration;

  private EclipseUtils eUtils;

  private CodeObserver codeObserver;

  private ApplicationContext appCntxt;

  private Thread consoleDisplayThread;

  private Thread aiPeerProgrammerThread;

  private String[] maskPropNamesArr;
  private HashSet<String> maskPropNamesSet;

  private String inlineChatPromptPrefix;

  private String inlineCommentPromptPrefix;
  private String inlineCommentClassPromptPrefix;
  private String inlineCommentMethodPromptPrefix;

  private String inlineCodeReviewPromptPrefix;
  private String inlineCodeReviewClassPromptPrefix;
  private String inlineCodeReviewMethodPromptPrefix;

  private String inlineCodeGenPromptPrefix;
  private String inlineCodeGenClassPromptPrefix;
  private String inlineCodeGenMethodPromptPrefix;

  private String inlineOptimizePromptPrefix;
  private String inlineOptimizeClassPromptPrefix;
  private String inlineOptimizeMethodPromptPrefix;

  private String askCommentsPrompt;
  private String askCommentsClassPrompt;
  private String askCommentsMethodPrompt;

  private String askCodeReviewPrompt;
  private String askCodeReviewClassPrompt;
  private String askCodeReviewMethodPrompt;

  private String askCodeGenPrompt;
  private String askCodeGenClassPrompt;
  private String askCodeGenMethodPrompt;

  private String askOptimizePrompt;
  private String askOptimizeClassPrompt;
  private String askOptimizeMethodPrompt;

  private ChatKey _currentSelectedChatContextChatKey;
  private ChatKey _previousSelectedChatContextChatKey;

  private ChatKey _mainContextChatKey;
  private ChatKey _dynamicContextChatKey;

  private boolean forceDisableAutoSendInitPrompt;

  private String codeSnippetStartMarker;
  private String codeSnippetEndMarker;

  private Stack<ChatKey> chatKeySendStack;
  private Stack<ChatKey> chatKeyReceivedStack;

  private HashMap<ChatKey, StringBuilder> chatsTextMap;

  private StringBuilder globalChatText;

  private ProggieBuddieUiCore() {
    pbLock = new Object();

    chatDisplayLock = new Object();
    consoleDisplayLock = new Object();

    chatKeySendStack = new Stack<ChatKey>();
    chatKeyReceivedStack = new Stack<ChatKey>();

    globalChatText = new StringBuilder();

    chatsTextMap = new HashMap<ChatKey, StringBuilder>();
  }

  public synchronized static ProggieBuddieUiCore getInstance() {
    if (instance == null) {
      instance = new ProggieBuddieUiCore();
    }

    return instance;
  }

  public void init(ProggieBuddiePlugin plugin, Composite container) throws ProggieBuddieException {
    try {
      ChatKey mainCk;
      ArrayList<ChatKey> ckLst;

      logger.debug("Starting Initialization of UI Core at: " + StringUtils.GetTimeStamp());

      this.plugin = plugin;
      this.container = container;

      sendingMessage = false;
      uiSendingMessage = false;
      asyncSendingMessageStarted = false;

      appCntxt = ApplicationContext.getInstance();

      eUtils = EclipseUtils.getInstance();

      readUserProperties();

      plugin.setPartName(PLUGIN_TITLE);
      plugin.setContentDescription(PLUGIN_SHORT_DESCRIPTION);
      plugin.setTitleToolTip(PLUGIN_DESCRIPTION);

      Image titleImg = loadTitleImg();
      plugin.setTitleImage(titleImg);

      chatSession = initNewChatSession();

      //appCntxt.keyValueStorePut(APP_CNTXT_KEY_CHAT_SESSION, chatGptSession);
      //chatGptSession = (ChatSession) appCntxt.keyValueStoreGet(APP_CNTXT_KEY_CHAT_SESSION);

      logger.debug("Creating Chat Service using Chat Session: " + chatSession);
      chatSrv = new ChatService();
      chatSrv.setChatSession(chatSession);

      pbUi = createUi(container);

      if (pbUi == null) {
        throw new ProggieBuddieException("Create UI Function Return a NULL UI Reference! UI Builder may have Failed! Please Check Logs!");
      }
      else {
        logger.debug("Plugin UI Built by UI Builder Successfully!");
      }

      pbUi.titleImg = titleImg;

      loadPropertiesTab(null);

      setChatInputFocus();

      //addMainChatContextToUi();
      addPrimaryChatContextsToUi();

      //Init Current Chat Key Info to Main Chat
      ckLst = chatSession.getChatKeysBySystemTag(SystemChatTag.SYS_MAIN);
      //mainCk = chatSession.getMainChatKey();
      mainCk = ckLst.get(0);
      ckLst = null;

      changeCurrentSelectedChatContextChatKeyTo(mainCk);

      consoleDisplayThread = new Thread(consoleDisplayRunnable);
      consoleDisplayThread.start();

      codeObserver = CodeObserver.getInstance();
      codeObserver.startObservation();

      aiPeerProgrammerThread = new Thread(aiPeerProgrammerRunnable);
      aiPeerProgrammerThread.start();

      if (forceDisableAutoSendInitPrompt) {
        logger.debug("Auto Send Init Prompt is Forcefully Disabled!");
      }
      else {
        autoSendInitialEngineeredPrompt(mainCk);
      }

      logger.debug("Completed Initialization of UI Core at: " + StringUtils.GetTimeStamp());
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Initialize the Proggie Buddie Plugin UI. System Message: " + e.getMessage(), e);
    }
  }

  private ChatKey changeCurrentSelectedChatContextChatKeyTo(ChatKey cKey) {
    _previousSelectedChatContextChatKey = _currentSelectedChatContextChatKey;

    _currentSelectedChatContextChatKey = cKey;

    return _currentSelectedChatContextChatKey;
  }

  protected ChatKey getPreviousChatKey() {
    return _previousSelectedChatContextChatKey;
  }

  private void addPrimaryChatContextsToUi() {
    ArrayList<ChatKey> primaryChatKeys;
    //ChatKey cKey;

    logger.debug("Adding Primary Chat Contexts to UI");

    primaryChatKeys = chatSession.getChatKeysByGroup(ChatGroupType.PRIMARY);

    if (primaryChatKeys != null) {
      primaryChatKeys = new ArrayList<ChatKey>(primaryChatKeys); //Create Copy of List So Not to Corrupt
      Collections.sort(primaryChatKeys);

      /*for (int i = 0; i < primaryChatKeys.size(); i++) {
        cKey = primaryChatKeys.get(i);
      
        logger.debug("Adding Main Chat Context to UI: " + cKey);
      
        addChatContextToUiCombo(cKey);
      }*/

      updateChatContextToUiCombo(primaryChatKeys);
    }
  }

  private ChatSession initNewChatSession() throws ProggieBuddieException, ChatAiException {
    ChatSession cSession;

    logger.debug("Proggie Buddie is Creating New Chat Session Object...");

    cSession = new ChatSession();
    cSession.init();

    return cSession;
  }

  private void autoSendInitialEngineeredPrompt(ChatKey cKey) {
    Display.getDefault().asyncExec(() -> {
      try {
        String engineeredPrompt;

        if (chatSession.isAutoSendInitialPromptEnabled(cKey)) {
          logger.debug("Auto Send Initial Engineered Prompt Enabled for Chat Key: " + cKey);

          if (!chatSession.isSentInitialEngineeredPrompt(cKey)) {

            logger.debug("Sending Initial Engineered Prompt for Chat Key: " + cKey);

            engineeredPrompt = chatSession.engineerChatPrompt(cKey);

            handleSendMessage(engineeredPrompt, cKey, OpenAiValueObjectType.ENGINEERED_PROMPT);

            processLatestResponses(cKey, true);

            chatSession.setSentInitialEngineeredPrompt(cKey, true);
          } //End isSentInitialEngineeredPrompt check
          else {
            logger.debug("Initial Engineered Prompt Already Sent for Chat Key: " + cKey);
          }
        } //End isAutoSendInitialPromptEnabled check
        else {
          logger.debug("Auto Send Initial Engineered Prompt Disabled for Chat Key: " + cKey);
        }
      } //End try block
      catch (Exception e) {
        logger.throwing(e);
      }
    });
  }

  private Image loadTitleImg() {
    Image img;

    img = ImageDescriptor.createFromFile(getClass(), PLUGIN_ICON_PATH).createImage();

    return img;
  }

  private void readUserProperties() throws IlardiSystemsException {
    String tmp;

    tmp = appCntxt.getUserProperty(PROPS_ENABLE_TEXT_TO_SPEECH);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    textToSpeechEnabled = "TRUE".equalsIgnoreCase(tmp);

    tmp = appCntxt.getUserProperty(PROPS_ENABLE_EDITOR_POPUP_MESSAGES);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    editorPopupMesgsEnabled = "TRUE".equalsIgnoreCase(tmp);

    tmp = appCntxt.getUserProperty(PROPS_EDITOR_POPUP_MESSAGE_DURATION);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    editorPopupMessageDuration = Integer.parseInt(tmp);

    tmp = appCntxt.getUserProperty(PROPS_PROPERTIES_TO_MASK_LIST);
    if (tmp != null) {
      tmp = tmp.trim();

      maskPropNamesArr = tmp.split(",");

      maskPropNamesSet = new HashSet<String>();

      for (String pn : maskPropNamesArr) {
        pn = pn.trim();
        maskPropNamesSet.add(pn);
      }
    }
    else {
      maskPropNamesArr = null;
      maskPropNamesSet = null;
    }

    tmp = appCntxt.getUserProperty(PROPS_FORCE_DISABLE_AUTO_SEND_INIT_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    forceDisableAutoSendInitPrompt = "TRUE".equalsIgnoreCase(tmp);

    tmp = appCntxt.getUserProperty(PROPS_CODE_SNIPPET_START_MARKER);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    codeSnippetStartMarker = tmp;

    tmp = appCntxt.getUserProperty(PROPS_CODE_SNIPPET_END_MARKER);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    codeSnippetEndMarker = tmp;

    //Ask For Prompts---------------------------------------------------->

    tmp = appCntxt.getUserProperty(PROPS_ASK_COMMENT_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCommentsPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_COMMENT_CLASS_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCommentsClassPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_COMMENT_METHOD_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCommentsMethodPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_CODE_REVIEW_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCodeReviewPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_CODE_REVIEW_CLASS_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCodeReviewClassPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_CODE_REVIEW_METHOD_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCodeReviewMethodPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_CODE_GEN_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCodeGenPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_CODE_GEN_CLASS_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCodeGenClassPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_CODE_GEN_METHOD_PROMPT);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askCodeGenMethodPrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_OPTIMIZE_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askOptimizePrompt = tmp;

    tmp = appCntxt.getUserProperty(PROPS_ASK_OPTIMIZE_CLASS_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askOptimizeClassPrompt = tmp;

    ;
    tmp = appCntxt.getUserProperty(PROPS_ASK_OPTIMIZE_METHOD_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    askOptimizeMethodPrompt = tmp;

    //In Line Chat Prefixes---------------------------------------------->

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CHAT_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineChatPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_COMMENT_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCommentPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_COMMENT_CLASS_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCommentClassPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_COMMENT_METHOD_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCommentMethodPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CODE_GEN_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCodeGenPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CODE_GEN_CLASS_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCodeGenClassPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CODE_GEN_METHOD_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCodeGenMethodPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_OPTIMIZE_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineOptimizePromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_OPTIMIZE_CLASS_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineOptimizeClassPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_OPTIMIZE_METHOD_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineOptimizeMethodPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CODE_REVIEW_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCodeReviewPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CODE_REVIEW_CLASS_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCodeReviewClassPromptPrefix = tmp;

    tmp = appCntxt.getUserProperty(PROPS_INLINE_CODE_REVIEW_METHOD_PROMPT_PREFIX);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    inlineCodeReviewMethodPromptPrefix = tmp;

    //Add Property Reads Above This Line-------------------->
    tmp = null;
  }

  private ProggieBuddieUi createUi(Composite parent) {
    ProggieBuddieUi ui;
    ProggieBuddieUiBuilder pbUiBuilder;

    logger.debug("Buidling Plugin UI...");

    pbUiBuilder = new ProggieBuddieUiBuilder();

    ui = pbUiBuilder.buildPluginUi(parent, plugin, this);

    return ui;
  }

  public void setFocus() {
    if (container != null) {
      container.setFocus();
    }
  }

  protected void handleSendMessageUiAction(OpenAiValueObjectType oaVoType) {
    String inputText;
    ChatKey cKey;

    synchronized (pbLock) {
      try {
        logger.debug("Send Message Event Fired.");

        while (uiSendingMessage) {
          pbLock.wait(1000);
        }

        uiSendingMessage = true;

        //chatKey = chatGptSession.getChatKeyByName(currentSelectedChatContextName);

        /*if (chatKey == null) {
          throw new ProggieBuddieException("Could Not Find Chat Key for: " + currentSelectedChatNContextName);
        }*/

        cKey = getCurrentChatKey();

        inputText = getChatInputText();

        if (inputText != null && !inputText.isBlank()) {
          inputText = inputText.trim();

          setChatInputText("");

          if (inputText.length() > 0) {
            handleSendMessage(inputText, cKey, oaVoType);
          }
        } //End inputText Check
        else {
          //If Empty String just check if Initial Prompt
          //was sent and if not, send.
          //ensureEngineeredPromptWasSent(cKey);
        }
      } //End try block
      catch (Exception e) {
        //e.printStackTrace();
        logger.throwing(e);
      } //End catch block
      finally {
        if (uiSendingMessage) {
          uiSendingMessage = false;
          pbLock.notifyAll();
        }
      }
    } //End sync block
  }

  protected void handleSendMessage(String mesg, ChatKey cKey, OpenAiValueObjectType oaVoType) throws ChatAiException, ProggieBuddieException {
    try {
      disableChatControls();

      logger.debug("Using Chat Context: " + cKey);

      _handleSendMessage(mesg, cKey, oaVoType);
    } //End try block
    finally {
      enableChatControls();
    }
  }

  protected void _handleSendMessage(String mesg, ChatKey cKey, OpenAiValueObjectType oaVoType) {
    String inputText;
    ChatTexts chTxts;
    Thread t;
    int cnt;

    synchronized (pbLock) {
      try {
        while (sendingMessage) {
          logger.debug("_handleSendMessage Invoked. Previous call to _handleSendMessage still processing. Waiting...");
          pbLock.wait(1000);
        }

        sendingMessage = true;
        pbLock.notifyAll();

        logger.debug("Handing Send Message Request - Chat Key: " + cKey + "; Mesg: " + mesg);

        if (cKey == null) {
          throw new ProggieBuddieException("Chat Key Cannot be NULL!");
        }

        inputText = mesg;
        if (inputText != null) {
          inputText = inputText.trim();

          if (inputText.length() > 0) {
            //Add Mesg to Chat Display
            chTxts = updateSpecificChatTextBuffer("Me: " + inputText + "\n\n", cKey);
            updateToChatDisplays(chTxts);

            //Disable Controls
            //disableChatControls();

            //Send Message to Chat
            //asyncSendMessage(inputText, chatKey);
            chatKeySendStack.push(cKey);

            chatSession.addNewRequestToDialog(inputText, cKey, oaVoType);

            t = new Thread(asyncMessageSenderRunnable);
            t.start();

            while (!asyncSendingMessageStarted) {
              logger.debug("Waiting for Async Sender Thread to Start");
              pbLock.wait(1000);
            }

            logger.debug("Async Sender Thread Running!");

            cnt = 0;

            while (sendingMessage) {
              logger.debug("Waiting for Async Sender Thread to Finish (" + cnt + ")");
              cnt++;
              pbLock.wait(1000);
            }

            logger.debug("Async Sender Thread Finished!");
          } //End empty input text check
        } //End null input text check
      } //End try block
      catch (Exception e) {
        e.printStackTrace();
        logger.throwing(e);
      } //End catch block
      finally {
        if (sendingMessage) {
          sendingMessage = false;
          pbLock.notifyAll();
        }

        //Re-Enable Controls
        //enableChatControls();
      }
    } //End sync block
  }

  private void sendDialogToAi(ChatKey cKey) throws ProggieBuddieException, ChatAiException {
    OpenAiChatRequest oaReq;
    OpenAiChatResponse oaRes;

    synchronized (pbLock) {
      logger.debug("Sending Chat Dialog to AI - ChatKey: " + cKey);

      if (cKey == null) {
        throw new ProggieBuddieException("Chat Key is NULL!");
      }

      oaReq = chatSession.getCompleteDialogAsRequest(cKey, true);

      oaRes = chatSrv.send(oaReq, cKey);

      chatSession.addResponseToDialog(oaRes, cKey);

      updateMessageTokenCounts(cKey, oaReq, oaRes);

      chatKeyReceivedStack.push(cKey);

      logger.debug("Finished Sending Dialog to AI, Processing Response Postponed to Free Up Thread...");
    } //End Synchronized Block
  }

  private void updateMessageTokenCounts(ChatKey cKey, OpenAiChatRequest oaReq, OpenAiChatResponse oaRes) {
    int promptTokens, completionTokens, remoteTotalTokens, localTotalTokens;
    OpenAiChatSuccessResponse oaiChatSuccessRes;
    OpenAiUsage oaiUsage;

    synchronized (pbLock) {
      if (oaRes instanceof OpenAiChatSuccessResponse) {
        logger.debug("Updating Token Count of Request and Response Messages for chatKey: " + cKey);

        oaiChatSuccessRes = (OpenAiChatSuccessResponse) oaRes;
        oaiUsage = oaiChatSuccessRes.getUsage();

        promptTokens = updateRequestMessageTokenCount(cKey, oaReq, oaiUsage);
        completionTokens = updateResponseMessageTokenCount(cKey, oaiChatSuccessRes);

        remoteTotalTokens = oaiUsage.getTotalTokens();
        localTotalTokens = promptTokens + completionTokens;

        logger.debug("Token Count Usage Report for ChatKey: " + cKey + " ; PromptTokens = " + promptTokens + ", CompletionTokens = " + completionTokens + ", LocalTotalTokens = " + localTotalTokens
            + ", RemoteTotalTokens = " + remoteTotalTokens + " ; Local-Remote-Equal? " + (localTotalTokens == remoteTotalTokens ? "YES" : "NO"));
      }
    }
  }

  private int updateRequestMessageTokenCount(ChatKey cKey, OpenAiChatRequest oaReq, OpenAiUsage oaiUsage) {
    List<OpenAiMessage> mesgLst;
    OpenAiMessage mesg;
    int tokenCnt, prevTokenCnt, mesgTokenCnt, promptTokens;

    synchronized (pbLock) {
      logger.debug("Updating Token Count of Request Messages: " + oaReq + " ; chatKey: " + cKey);

      tokenCnt = oaiUsage.getPromptTokens();

      mesgLst = oaReq.getMessages();

      prevTokenCnt = 0;
      promptTokens = 0;

      for (int i = 0; i < mesgLst.size(); i++) {
        mesg = mesgLst.get(i);

        mesgTokenCnt = mesg.getMesgTokenCnt();
        prevTokenCnt += mesgTokenCnt;

        if (mesgTokenCnt <= 0) {
          mesgTokenCnt = Math.abs(tokenCnt - prevTokenCnt);

          mesg.setMesgTokenCnt(mesgTokenCnt);

          promptTokens += mesgTokenCnt;

          prevTokenCnt += mesgTokenCnt;
        }
        else {
          promptTokens += mesgTokenCnt;
        }
      }

      logger.debug("Total Token Count of Request Messages for chatKey: " + cKey + " ; Prompt Tokens: " + promptTokens);

      return promptTokens;
    }
  }

  private int updateResponseMessageTokenCount(ChatKey cKey, OpenAiChatSuccessResponse oaRes) {
    OpenAiMessage mesg;
    List<OpenAiChoice> oaiChoices;
    OpenAiChoice oaiChoice;
    OpenAiUsage oaiUsage;
    int tokenCnt, prevTokenCnt, mesgTokenCnt, completionTokens;

    synchronized (pbLock) {
      logger.debug("Updating Token Count of Response Messages: " + oaRes + " ; chatKey: " + cKey);

      oaiUsage = oaRes.getUsage();
      oaiChoices = oaRes.getChoices();

      tokenCnt = oaiUsage.getCompletionTokens();

      prevTokenCnt = 0;
      completionTokens = 0;

      for (int i = 0; i < oaiChoices.size(); i++) {
        oaiChoice = oaiChoices.get(i);
        mesg = oaiChoice.getMessage();

        mesgTokenCnt = mesg.getMesgTokenCnt();
        prevTokenCnt += mesgTokenCnt;

        if (mesgTokenCnt <= 0) {
          mesgTokenCnt = Math.abs(tokenCnt - prevTokenCnt);

          mesg.setMesgTokenCnt(mesgTokenCnt);

          completionTokens += mesgTokenCnt;

          prevTokenCnt += mesgTokenCnt;
        }
        else {
          completionTokens += mesgTokenCnt;
        }
      }

      logger.debug("Total Token Count of Response Messages for chatKey: " + cKey + " ; Completion Tokens: " + completionTokens);

      return completionTokens;
    }
  }

  public void processLastResponse(boolean appendToChatDisplay) {
    synchronized (pbLock) {
      try {
        ChatKey cKey;

        //cKey = getCurrentChatKey();
        cKey = chatKeyReceivedStack.pop();

        processLatestResponses(cKey, appendToChatDisplay);

        if (chatSession.isStatelessChat(cKey)) {
          logger.debug("Chat Context: " + cKey.getChatName() + " is Stateless. Clearing Ring Buffer after Processing was completed.");

          chatSession.clearRingBuffer(cKey);
        }

        cKey = null;
      } //End try block
      catch (Exception e) {
        e.printStackTrace();
        logger.throwing(e);
      }
    }
  }

  private void processLatestResponses(ChatKey cKey, boolean appendToChatDisplay) throws ChatAiException, BadLocationException, IlardiSysEclipseException, IOException {
    List<OpenAiChatResponse> oaResLst;
    OpenAiChatResponse oaRes;
    int sz;

    synchronized (pbLock) {
      oaResLst = chatSession.getUnprocessedResponses(cKey);

      if (oaResLst != null) {
        sz = oaResLst.size();

        for (int i = 0; i < sz; i++) {
          logger.debug("Processing Latest AI Responses for Chat Key: " + cKey + "; Unprocessed Response: " + (i + 1) + " of " + sz);

          oaRes = oaResLst.get(i);

          processResponse(oaRes, cKey, appendToChatDisplay);
        }

        oaResLst.clear();
        oaResLst = null;
      }
    }
  }

  protected void processResponse(OpenAiChatResponse oaRes, ChatKey cKey, boolean appendToChatDisplay) throws ChatAiException, BadLocationException, IlardiSysEclipseException, IOException {
    OpenAiChatSuccessResponse oaSuccessRes;
    OpenAiChatErrorResponse oaErrorRes;
    String replyTxt;
    OpenAiValueObjectType resType;
    ChatTexts chTxts;

    synchronized (pbLock) {
      if (cKey == null) {
        throw new ProggieBuddieException("Chat Key is NULL!");
      }

      //oaRes = chatSession.getLastResponse(cKey);

      if (oaRes == null) {
        throw new ProggieBuddieException("Last Response was NULL for Chat Key = " + cKey);
      }

      if (!oaRes.isResponseProcessed()) {
        logger.debug("Processing Latest Response from AI");

        if (oaRes instanceof OpenAiChatSuccessResponse) {
          logger.debug("AI Return a Successful Response to our Chat Request");

          oaSuccessRes = (OpenAiChatSuccessResponse) oaRes;

          replyTxt = oaSuccessRes.getAllReplyText();

          chTxts = updateSpecificChatTextBuffer("ChatGPT: " + replyTxt + "\n\n", cKey);
          updateToChatDisplays(chTxts);

          resType = oaSuccessRes.getOaVoType();

          //Special Processing for certain types
          //Otherwise just do the default for all
          switch (resType) {
            case BOT_ENGINEERED_PROMPT_SUCCESS:
              performExtendedUiResponseProcessing(replyTxt);
              break;
            case BOT_BASIC_CHAT_SUCCESS:
              performExtendedUiResponseProcessing(replyTxt);
              break;
            case BOT_INLINE_GENERAL_CHAT_SUCCESS:
              deleteSelectedLines();
              performExtendedUiResponseProcessing(replyTxt);
              break;
            case BOT_SUCCESS_COMMENTS:
              deleteSelectedLines();
              insertCodeInPlace(replyTxt);
              break;
            case BOT_SUCCESS_COMMENTS_CLASS:
              replaceAllCode(replyTxt);
              break;
            case BOT_SUCCESS_COMMENTS_METHOD:
              replaceRewrittenMethod(replyTxt);
              break;
            case BOT_SUCCESS_CODE_GEN:
              deleteSelectedLines();
              insertCodeInPlace(replyTxt);
              break;
            case BOT_SUCCESS_CODE_GEN_CLASS:
              replaceAllCode(replyTxt);
              break;
            case BOT_SUCCESS_CODE_GEN_METHOD:
              deleteSelectedLines();
              replaceRewrittenMethod(replyTxt);
              break;
            case BOT_SUCCESS_CODE_REVIEW:
              deleteSelectedLines();
              displayCodeReview(replyTxt);
              break;
            case BOT_SUCCESS_CODE_REVIEW_CLASS:
              displayCodeReview(replyTxt);
              break;
            case BOT_SUCCESS_CODE_REVIEW_METHOD:
              displayCodeReview(replyTxt);
              break;
            case BOT_SUCCESS_OPTIMIZE:
              deleteSelectedLines();
              insertCodeInPlace(replyTxt);
              break;
            case BOT_SUCCESS_OPTIMIZE_CLASS:
              replaceAllCode(replyTxt);
              break;
            case BOT_SUCCESS_OPTIMIZE_METHOD:
              deleteSelectedLines();
              replaceRewrittenMethod(replyTxt);
              break;
            default:
              performExtendedUiResponseProcessing(replyTxt);
          }
        }
        else if (oaRes instanceof OpenAiChatErrorResponse) {
          logger.debug("AI Return an Error Response to our Chat Request");

          oaErrorRes = (OpenAiChatErrorResponse) oaRes;

          replyTxt = oaErrorRes.getErrorText();

          chTxts = updateSpecificChatTextBuffer("ChatGPT-Error: " + replyTxt + "\n\n", cKey);
          updateToChatDisplays(chTxts);
        }
        else {
          throw new ProggieBuddieException("Unsupported Response Type: " + oaRes);
        }

        oaRes.setResponseProcessed(true); //So we don't process twice

        logger.debug("Finished Processing AI Response");
      }
      else {
        logger.debug("Skipping Processing Latest Response from AI, Already Processed for Chat Key = " + cKey);
      }
    } //End Synchronized Block
  }

  /**
   * @param replyTxt
   * @throws ProggieBuddieException
   */
  protected void performExtendedUiResponseProcessing(String replyTxt) throws ProggieBuddieException {
    if (textToSpeechEnabled) {
      speakReply(replyTxt);
    }

    if (editorPopupMesgsEnabled) {
      showEditorChatGptReplyPopupMessage(replyTxt);
    }
  }

  private void deleteSelectedLines() {
    // TODO Auto-generated method stub

  }

  private void showEditorChatGptReplyPopupMessage(String replyTxt) {
    NotificationWidget note;
    Point loc;
    //Control eclipseEditor;

    //eclipseEditor = eUtils.getCurrentTextEditorSwtWidget();
    //loc = eUtils.getEclipseTextWidgetCaretLocation(eclipseEditor);
    loc = eUtils.getEclipseActiveTextEditorCaretLocation();

    if (loc == null) {
      loc = eUtils.getCenterOfScreen();
    }

    note = new NotificationWidget();
    note.showNotification(loc, replyTxt, editorPopupMessageDuration);
  }

  private void speakReply(String replyTxt) throws ProggieBuddieException {
    FreeTtsAdapter tts;

    tts = FreeTtsAdapter.getInstance();

    tts.speak(replyTxt);
  }

  private Runnable asyncMessageSenderRunnable = new Runnable() {
    @Override
    public void run() {
      synchronized (pbLock) {
        logger.debug("Async Message Sender Runnable Starting...");
        asyncSendingMessageStarted = true;
        pbLock.notifyAll();
      } //End sync block

      try {
        Thread.sleep(50);
      }
      catch (Exception e) {
        e.printStackTrace();
        logger.throwing(e);
      }

      logger.debug("Async Message Sender Runnable Started!");

      synchronized (pbLock) {
        try {
          logger.debug("Async Message Sender Runnable Preparing to Send Message to AI...");

          //ChatKey cKey = getCurrentChatKey();
          ChatKey cKey = chatKeySendStack.pop();

          sendDialogToAi(cKey);

          logger.debug("Async Message Sender Runnable Message Successfully Sent to AP!");

          updateChatRingBufferStats();
        } //End try block
        catch (Exception e) {
          e.printStackTrace();
          logger.throwing(e);
        } //End catch block
        finally {
          sendingMessage = false;
          asyncSendingMessageStarted = false;
          pbLock.notifyAll();
        } //End finally block
      } //End sync block
    } //End run method
  };

  private Runnable consoleDisplayRunnable = new Runnable() {
    @Override
    public void run() {
      LogEventQueue leQueue;
      String logMesgTxt;

      leQueue = LogEventQueue.getInstance();

      while (appCntxt.isAppRunning()) {
        try {
          logMesgTxt = leQueue.dequeueLogMessage();

          if (logMesgTxt != null) {
            logMesgTxt = logMesgTxt.trim();
            appendToConsoleDisplay(logMesgTxt + "\n\n");
          } //End logMesgTxt Null Check
        } //End try block
        catch (Exception e) {
          e.printStackTrace();
        } //End catch block
      } //End while loop
    } //End run method
  };

  private Runnable aiPeerProgrammerRunnable = new Runnable() {
    @Override
    public void run() {
      while (appCntxt.isAppRunning()) {
        try {
          //TODO Do Asynchronous Processing
          Thread.sleep(5000);
        } //End try block
        catch (Exception e) {
          e.printStackTrace();
        } //End catch block
      } //End while loop
    } //End run method
  };

  protected void appendToConsoleDisplay(String txt) {
    Display.getDefault().asyncExec(() -> {
      synchronized (consoleDisplayLock) {
        if (!pbUi.consoleDisplay.isDisposed()) {
          pbUi.consoleDisplay.append(txt);
        }
      }
    });
  }

  private void updateToChatDisplays(ChatTexts chTxts) {
    Display.getDefault().asyncExec(() -> {
      synchronized (chatDisplayLock) {
        String txt;
        ChatKey chTxtKey, curKey;

        curKey = getCurrentChatKey();
        chTxtKey = chTxts.getChatKey();

        if (!pbUi.chatDisplay.isDisposed()) {
          txt = chTxts.getChatContextTxt();

          if (chTxts.isChatContextChanged()) {
            //Chat Context Changed, replace all text
            if (chTxtKey.isVisible()) {
              if (txt != null && !txt.isBlank()) {
                pbUi.chatDisplay.setText(txt);
              }
              else {
                pbUi.chatDisplay.setText(""); //Clear
              }
            }
            else {
              logger.debug("Invisible Chat Context, Skipping Updating Chat Display: " + chTxtKey);
            }
          }
          else {
            //Chat Context Not Changed
            if (txt != null && !txt.isBlank()) {
              pbUi.chatDisplay.append(txt);
            }
            else {
              pbUi.chatDisplay.setText(""); //Clear
            }
          }
        } //End check of isDisposed

        if (!pbUi.historyGlobalChatDisplay.isDisposed()) {
          txt = chTxts.getGlobalChatTxt();

          if (txt != null && !txt.isBlank()) {
            pbUi.historyGlobalChatDisplay.append(txt);
          }

          if (!chTxtKey.isVisible()) {
            //Quick Switch between contexts back and forth
            txt = getChatContextChangedText(curKey, chTxtKey);
            pbUi.historyGlobalChatDisplay.append(txt);
          }
        } //End check of isDisposed

        chTxtKey = null;
        txt = null;
      } //End sync block
    });
  }

  private ChatTexts updateSpecificChatTextBuffer(String txt, ChatKey specificKey) {
    ChatTexts chTxts;
    StringBuilder sb;
    String tmp;
    ChatKey curKey;

    synchronized (pbLock) {
      chTxts = new ChatTexts();
      chTxts.setChatKey(specificKey);

      if (isCurrentKeyEqual(specificKey)) {
        //Chat Context Not Changed
        chTxts.setChatContextChanged(false);

        //First add to global chat text buffer
        if (txt != null) {
          globalChatText.append(txt);
          chTxts.setGlobalChatTxt(txt);
        }

        //----------------------------------------------------->

        //Second add to cKey Mapped text buffer
        sb = chatsTextMap.get(specificKey);

        if (sb == null) {
          sb = new StringBuilder();
          chatsTextMap.put(specificKey, sb);
        }

        if (txt != null) {
          sb.append(txt);
          chTxts.setChatContextTxt(txt);
        }

        sb = null;
      } //End if block
      else {
        //Chat Context Changed
        chTxts.setChatContextChanged(true);

        //First add to global chat text buffer
        sb = new StringBuilder();

        curKey = getCurrentChatKey();
        tmp = getChatContextChangedText(specificKey, curKey);

        sb.append(tmp);
        sb.append("\n");

        if (txt != null && !txt.isBlank()) {
          sb.append(txt);
        }

        tmp = sb.toString();

        globalChatText.append(tmp);
        chTxts.setGlobalChatTxt(tmp);

        tmp = null;
        sb = null;
        curKey = null;

        //----------------------------------------------------->

        //Second add to cKey Mapped text buffer
        sb = chatsTextMap.get(specificKey);

        if (sb == null) {
          sb = new StringBuilder();
          chatsTextMap.put(specificKey, sb);
        }

        if (txt != null) {
          sb.append(txt);
        }

        tmp = sb.toString(); //Return entire Text Buffer
        chTxts.setChatContextTxt(tmp);

        sb = null;
        tmp = null;
      } //End else block

      //Store curChatKey as Previous
      //prevChatDisplayKey = curChatKey;

      return chTxts;
    } //End sync block
  }

  private boolean isCurrentKeyEqual(ChatKey specificKey) {
    boolean same;
    ChatKey curKey;

    curKey = getCurrentChatKey();

    if (curKey != null && specificKey != null) {
      same = _currentSelectedChatContextChatKey.equals(specificKey);
    }
    else {
      same = false;
    }

    curKey = null;

    return same;
  }

  protected boolean isCurrentKeyEqualedToPreviousKey() {
    boolean same;
    ChatKey curKey, prevKey;

    curKey = getCurrentChatKey();
    prevKey = getPreviousChatKey();

    if (curKey != null && prevKey != null) {
      same = curKey.equals(prevKey);
    }
    else {
      same = false;
    }

    curKey = null;
    prevKey = null;

    return same;
  }

  private String getChatContextChangedText(ChatKey curKey, ChatKey prevKey) {
    StringBuilder sb = new StringBuilder();
    String tmp;

    sb.append("\n\n");
    sb.append("------------------------------------------------------");
    sb.append("\n");

    sb.append("Chat Context Changed at: ");
    sb.append(StringUtils.GetTimeStamp());
    sb.append("\n");

    sb.append("Previous Context: ");
    sb.append(prevKey);
    sb.append("\n");

    sb.append("Current Context: ");
    sb.append(curKey);
    sb.append("\n");

    sb.append("------------------------------------------------------");
    sb.append("\n\n");

    tmp = sb.toString();
    sb = null;

    return tmp;
  }

  public void clearChatTextBuffer(ChatKey cKey) {
    synchronized (pbLock) {
      logger.debug("Clearing Chat Text Buffer for ChatKey: " + cKey);

      if (chatsTextMap.containsKey(cKey)) {
        chatsTextMap.remove(cKey);
      }
    } //End sync block
  }

  public void clearGlobalChatTextBuffer() {
    synchronized (pbLock) {
      globalChatText = new StringBuilder();
    } //End sync block
  }

  protected SelectionListener tabSelectionListener = new SelectionListener() {
    @Override
    public void widgetSelected(SelectionEvent e) {
      CTabItem selectedTab = pbUi.tabFolder.getSelection();

      if (selectedTab != null) {
        if (selectedTab.equals(pbUi.propertiesTab)) {
          Display.getDefault().asyncExec(() -> {
            resizePropertiesTableSize();
            resizePropertiesTableColumns();
          });
        }
        else if (selectedTab.equals(pbUi.chatTab)) {
          Display.getDefault().asyncExec(() -> {
            pbUi.chatDisplay.setSelection(pbUi.chatDisplay.getText().length());
          });
        }
        else if (selectedTab.equals(pbUi.consoleTab)) {
          Display.getDefault().asyncExec(() -> {
            pbUi.consoleDisplay.setSelection(pbUi.consoleDisplay.getText().length());
          });
        }
      }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {}
  };

  protected void disableChatControls() {
    Display.getDefault().asyncExec(() -> {
      pbUi.chatInput.setEnabled(false);
      pbUi.chatSendButton.setEnabled(false);
      pbUi.chatContextCombo.setEnabled(false);
      pbUi.chatRingBufferClearButton.setEnabled(false);
    });
  }

  protected void enableChatControls() {
    Display.getDefault().asyncExec(() -> {
      pbUi.chatInput.setEnabled(true);
      pbUi.chatSendButton.setEnabled(true);
      pbUi.chatContextCombo.setEnabled(true);
      pbUi.chatRingBufferClearButton.setEnabled(true);
    });
  }

  protected void setChatInputFocus() {
    pbUi.chatInput.setFocus();
  }

  protected void setChatInputText(String txt) {
    Display.getDefault().asyncExec(() -> {
      pbUi.chatInput.setText(txt);
    });
  }

  protected String getChatInputText() {
    return pbUi.chatInput.getText();
  }

  protected void resizePropertiesTableColumns() {
    int tableWidth = pbUi.propertiesTable.getBounds().width;
    int columnWidth = (tableWidth / 2);

    pbUi.propNameCol.setWidth(columnWidth);
    pbUi.propValueCol.setWidth(columnWidth);
  }

  protected void resizePropertiesTableSize() {
    //int propMainCompositeWidth = pbUi.propertiesComposite.getBounds().width;
    int propMainCompositeWidth = pbUi.propertiesTablePanel.getBounds().width;
    int tableItemWidth = propMainCompositeWidth - 10;

    //int propMainCompositeHeight = pbUi.propertiesComposite.getBounds().height;
    int propMainCompositeHeight = pbUi.propertiesTablePanel.getBounds().height;

    if (propMainCompositeWidth <= 0) {
      tableItemWidth = DEFAULT_PROP_TABLE_WIDTH;
    }

    int tableItemHeight = propMainCompositeHeight - 10;

    if (propMainCompositeHeight <= 0) {
      tableItemWidth = DEFAULT_PROP_TABLE_HEIGHT;
    }

    pbUi.propertiesTable.setSize(tableItemWidth, tableItemHeight);
  }

  protected void loadPropertiesTab(SelectionEvent se) {
    logger.debug("Load Properties Called for Properties Tab");

    Display.getDefault().syncExec(() -> {
      try {
        logger.debug("Reloading Properties");
        appCntxt.loadUserProperties();

        populatePropertiesTable();

        resizePropertiesTableSize();
        resizePropertiesTableColumns();

        //pbUi.propNameCol.pack();
        //pbUi.propValueCol.pack();
      }
      catch (IlardiSystemsException e) {
        logger.throwing(e);
      }
    });
  }

  private void populatePropertiesTable() throws IlardiSystemsException {
    Iterator<String> iter;
    TableItem row;
    String propName, propValue;
    List<String> propNames;

    logger.debug("Populating Properties Table");

    if (pbUi.propertiesTable.getItemCount() > 0) {
      pbUi.propertiesTable.removeAll();
    }

    propNames = appCntxt.getUserPropertyNames();
    iter = propNames.iterator();

    while (iter.hasNext()) {
      propName = iter.next();

      if (propName != null) {
        propName = propName.trim();
      }

      if (!maskPropNamesSet.contains(propName)) {
        propValue = appCntxt.getUserProperty(propName);

        if (propValue != null) {
          propValue = propValue.trim();
        }
      }
      else {
        propValue = DEFAULT_PROPERTY_VALUE_MASK_STRING;
      }

      row = new TableItem(pbUi.propertiesTable, SWT.NONE);

      row.setText(0, propName);
      row.setText(1, propValue);

      row = null;
    }
  }

  protected void saveProperties(SelectionEvent se) throws ProggieBuddieException {
    TableItem row;
    int tiCnt;
    String pn, pv;
    Properties origMaskedProps;

    try {
      logger.debug("Save Properties Called for Properties Tab");

      origMaskedProps = new Properties();

      for (String n : maskPropNamesSet) {
        if (maskPropNamesSet.contains(n)) {
          n = n.trim();
          pv = appCntxt.getUserProperty(n);
          pn = pv.trim();

          origMaskedProps.setProperty(n, pv);
        }
      }

      appCntxt.clearUserProperty();

      tiCnt = pbUi.propertiesTable.getItemCount();

      pn = null;
      pv = null;

      for (int i = 0; i < tiCnt; i++) {
        row = pbUi.propertiesTable.getItem(i);

        pn = row.getText(0);

        if (pn != null) {
          pn = pn.trim();

          if (!maskPropNamesSet.contains(pn)) {
            pv = row.getText(1);
          }
          else {
            pv = origMaskedProps.getProperty(pn);
          }
        }
        else {
          pv = null;
        }

        if (pn != null && pv != null) {
          pn = pn.trim();
          pv = pv.trim();

          appCntxt.setUserProperty(pn, pv);
        }

        pn = null;
        pv = null;

        appCntxt.saveUserProperties(null);
      } //End try block
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An Error occurred while attempting to Save Properties. System Message: " + e.getMessage(), e);
    }
  }

  protected void addNewProperty(SelectionEvent se) {
    // TODO Auto-generated method stub
    logger.debug("Add New Property Called for Properties Tab");

  }

  protected void removeSelectedProperty(SelectionEvent se) {
    // TODO Auto-generated method stub
    logger.debug("Remove Selected Property Called for Properties Tab");

  }

  protected void editSelectedProperty(SelectionEvent se) {
    // TODO Auto-generated method stub
    logger.debug("Edit Selected Property Called for Properties Tab");

  }

  public void handleEclipseHotKeyInlineChat() throws ProggieBuddieException {
    String inputTxt;
    ChatKey cKey;

    try {
      logger.debug("Handle Eclipse Hot Key Inline Chat Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(true);

      logger.debug("Inline Chat Text: " + inputTxt);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        //cKey = getCurrentChatKey();
        cKey = determineChatKey(inputTxt);

        if (inputTxt.startsWith(inlineChatPromptPrefix)) {
          //Send Inline General Chat
          handleEclipseSendInlineGeneralChat(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCommentPromptPrefix)) {
          //Ask to Comment
          handleEclipseAskComment(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCommentClassPromptPrefix)) {
          //Ask to Comment on Class
          handleEclipseAskCommentClass(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCommentMethodPromptPrefix)) {
          //Ask to Comment on Method
          handleEclipseAskCommentMethod(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCodeGenPromptPrefix)) {
          //Ask for Code Gen
          handleEclipseAskCodeGen(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCodeGenClassPromptPrefix)) {
          //Ask to Code Gen Class
          handleEclipseAskCodeGenClass(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCodeGenMethodPromptPrefix)) {
          //Ask to Code Gen Method
          handleEclipseAskCodeGenMethod(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineOptimizePromptPrefix)) {
          //Ask for Optimize
          handleEclipseAskOptimize(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineOptimizeClassPromptPrefix)) {
          //Ask to Optimize Class
          handleEclipseAskOptimizeClass(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineOptimizeMethodPromptPrefix)) {
          //Ask to Optimize Method
          handleEclipseAskOptimizeMethod(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCodeReviewPromptPrefix)) {
          //Ask for Code Review
          handleEclipseAskCodeReview(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCodeReviewClassPromptPrefix)) {
          //Ask to Code Review Class
          handleEclipseAskCodeReviewClass(inputTxt, cKey);
        }
        else if (inputTxt.startsWith(inlineCodeReviewMethodPromptPrefix)) {
          //Ask to Code Review Method
          handleEclipseAskCodeReviewMethod(inputTxt, cKey);
        }
        else {
          logger.debug("Unsupported Prompt Prefix or Prompt Prefix Not Found - inputTxt: " + inputTxt + "; ChatKey: " + cKey);
        }
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Hot Key Inline Chat. System Message: " + e.getMessage(), e);
    }
  }

  private ChatKey determineChatKey(String inputTxt) throws ProggieBuddieException {
    ChatKey cKey;

    try {
      logger.debug("Attempting to determine Chat Key from Input Text: " + inputTxt);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        if (inputTxt.startsWith(inlineChatPromptPrefix)) {
          //Send Inline General Chat
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCommentPromptPrefix)) {
          //Ask to Comment
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCommentClassPromptPrefix)) {
          //Ask to Comment on Class
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCommentMethodPromptPrefix)) {
          //Ask to Comment on Method
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCodeGenPromptPrefix)) {
          //Ask for Code Gen
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCodeGenClassPromptPrefix)) {
          //Ask to Code Gen Class
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCodeGenMethodPromptPrefix)) {
          //Ask to Code Gen Method
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineOptimizePromptPrefix)) {
          //Ask for Optimize
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineOptimizeClassPromptPrefix)) {
          //Ask to Optimize Class
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineOptimizeMethodPromptPrefix)) {
          //Ask to Optimize Method
          cKey = getCurrentChatKey();
        }
        else if (inputTxt.startsWith(inlineCodeReviewPromptPrefix)) {
          //Ask for Code Review
          cKey = getDynamicContextChatKey();
        }
        else if (inputTxt.startsWith(inlineCodeReviewClassPromptPrefix)) {
          //Ask to Code Review Class
          cKey = getDynamicContextChatKey();
        }
        else if (inputTxt.startsWith(inlineCodeReviewMethodPromptPrefix)) {
          //Ask to Code Review Method
          cKey = getDynamicContextChatKey();
        }
        else {
          //Default to Main Chat
          cKey = getMainContextChatKey();
        }
      } //End null inputTxt check
      else {
        //Default to Main Chat
        cKey = getMainContextChatKey();
      }

      return cKey;
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Determine Chat Key from Input Text: " + inputTxt + " ; System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemSendInlineGeneralChat() throws ProggieBuddieException {
    String inputTxt;
    ChatKey cKey;

    try {
      logger.debug("Handle Eclipse Send Inline General Chat Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseSendInlineGeneralChat(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Send Inline General Chat Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskComment() throws ProggieBuddieException {
    String inputTxt;
    ChatKey cKey;

    try {
      logger.debug("Handle Eclipse Menu Item Ask Comment Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();
      }

      cKey = getCurrentChatKey();

      handleEclipseAskComment(inputTxt, cKey);
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Menu Item Ask Comment. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCommentClass() throws ProggieBuddieException {
    String inputTxt;
    ChatKey cKey;

    try {
      logger.debug("Handle Eclipse Menu Item Ask Comment Class Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();
      }

      cKey = getCurrentChatKey();

      handleEclipseAskCommentClass(inputTxt, cKey);
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Menu Item Ask Comment Class. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCommentMethod() throws ProggieBuddieException {
    String inputTxt;
    ChatKey cKey;

    try {
      logger.debug("Handle Eclipse Menu Item Ask Comment Method Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();
      }

      cKey = getCurrentChatKey();

      handleEclipseAskCommentMethod(inputTxt, cKey);
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Menu Item Ask Comment Method. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCodeGen() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Code Gen Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseAskCodeGen(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Code Gen Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCodeGenClass() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Code Gen Class Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseAskCodeGenClass(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Code Gen Class Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCodeGenMethod() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Code Gen Method Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseAskCodeGenMethod(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Code Gen Method Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCodeReview() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Code Review Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getDynamicContextChatKey();

        handleEclipseAskCodeReview(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Code Review Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCodeReviewClass() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Code Review Class Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getDynamicContextChatKey();

        handleEclipseAskCodeReviewClass(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Code Review Class Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskCodeReviewMethod() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Code Review Method Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getDynamicContextChatKey();

        handleEclipseAskCodeReviewMethod(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Code Review Method Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskOptimize() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Optimize Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseAskOptimize(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Optimize Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskOptimizeClass() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Optimize Class Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseAskOptimizeClass(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Optimize Class Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  public void handleEclipseMenuItemAskOptimizeMethod() throws ProggieBuddieException {
    ChatKey cKey;
    String inputTxt;

    try {
      logger.debug("Handle Eclipse Ask Optimize Method Menu Item Called");

      //inputTxt = eUtils.getCurrentTextEditorTextLine(true);
      inputTxt = eUtils.getCurrentSelectedText(false);

      if (inputTxt != null) {
        inputTxt = inputTxt.trim();

        cKey = getCurrentChatKey();

        handleEclipseAskOptimizeMethod(inputTxt, cKey);
      } //End null inputTxt check
    } //End try block
    catch (Exception e) {
      throw new ProggieBuddieException("An error occurred while attempting to Handle Eclipse Ask Optimize Method Menu Item. System Message: " + e.getMessage(), e);
    }
  }

  private void handleEclipseSendInlineGeneralChat(String inputTxt, ChatKey cKey) throws ProggieBuddieException, ChatAiException {
    //General Inline Chat
    logger.debug("Send Inline General Chat - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    if (inputTxt.startsWith(inlineChatPromptPrefix)) {
      //Remove inlineChatPrompt from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineChatPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (inputTxt.length() > 0) {
      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_GENERAL_CHAT);
    }
  }

  private void handleEclipseAskComment(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;

    logger.debug("Send Ask Comment - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    if (inputTxt.startsWith(inlineCommentPromptPrefix)) {
      //Remove inlineCommentPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCommentPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    sb = new StringBuilder();

    if (inputTxt != null && !inputTxt.isBlank()) {
      sb.append(askCommentsPrompt);

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(inputTxt);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_COMMENTS);
    }
  }

  private void handleEclipseAskCommentClass(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curCode;

    logger.debug("Send Ask Comment on Class - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curCode = eUtils.getCurrentJavaClassSourceAsText();

    if (inputTxt.startsWith(inlineCommentClassPromptPrefix)) {
      //Remove inlineCommentClassPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCommentClassPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curCode != null && !curCode.isBlank()) {
      sb = new StringBuilder();

      sb.append(askCommentsClassPrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curCode);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_COMMENTS_CLASS);
    }
  }

  private void handleEclipseAskCommentMethod(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curCode;

    logger.debug("Send Ask Comment on Method - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curCode = eUtils.getCurrentJavaMethodSourceAsText();

    if (inputTxt.startsWith(inlineCommentMethodPromptPrefix)) {
      //Remove inlineCommentMethodPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCommentMethodPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curCode != null && !curCode.isBlank()) {
      sb = new StringBuilder();

      sb.append(askCommentsMethodPrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curCode);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_COMMENTS_METHOD);
    }
  }

  private void handleEclipseAskCodeGen(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    //String curSrc;

    logger.debug("Send Ask Code Gen Request - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    //curSrc = eUtils.getCurrentSelectedSourceAsText();

    if (inputTxt.startsWith(inlineCodeGenPromptPrefix)) {
      //Remove inlineCodeGenPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCodeGenPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    sb = new StringBuilder();

    sb.append(askCodeGenPrompt);
    sb.append("\n");

    if (inputTxt != null && !inputTxt.isBlank()) {
      sb.append(inputTxt);
      sb.append("\n");
    }

    sb.append(codeSnippetStartMarker);
    sb.append("\n");

    /*if (curSrc != null && !curSrc.isBlank()) {
      sb.append(curSrc);
      sb.append("\n");
    }*/

    sb.append(codeSnippetEndMarker);
    sb.append("\n");

    inputTxt = sb.toString();
    sb = null;

    handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_CODE_GEN);
  }

  private void handleEclipseAskCodeGenClass(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curSrc;

    logger.debug("Send Ask Code Gen Class Request - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curSrc = eUtils.getCurrentJavaClassSourceAsText();

    if (inputTxt.startsWith(inlineCodeGenClassPromptPrefix)) {
      //Remove inlineCodeGenClassPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCodeGenClassPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    sb = new StringBuilder();

    sb.append(askCodeGenClassPrompt);
    sb.append("\n");

    if (inputTxt != null && !inputTxt.isBlank()) {
      sb.append(inputTxt);
      sb.append("\n");
    }

    sb.append(codeSnippetStartMarker);
    sb.append("\n");

    if (curSrc != null && !curSrc.isBlank()) {
      sb.append(curSrc);
      sb.append("\n");
    }

    sb.append(codeSnippetEndMarker);
    sb.append("\n");

    inputTxt = sb.toString();
    sb = null;

    handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_CODE_GEN_CLASS);
  }

  private void handleEclipseAskCodeGenMethod(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curSrc;

    logger.debug("Send Ask Code Gen Method Request - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curSrc = eUtils.getCurrentJavaMethodSourceAsText();

    if (inputTxt.startsWith(inlineCodeGenMethodPromptPrefix)) {
      //Remove inlineCodeGenMethodPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCodeGenMethodPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    sb = new StringBuilder();

    sb.append(askCodeGenMethodPrompt);
    sb.append("\n");

    if (inputTxt != null && !inputTxt.isBlank()) {
      sb.append(inputTxt);
      sb.append("\n");
    }

    sb.append(codeSnippetStartMarker);
    sb.append("\n");

    if (curSrc != null && !curSrc.isBlank()) {
      sb.append(curSrc);
      sb.append("\n");
    }

    sb.append(codeSnippetEndMarker);
    sb.append("\n");

    inputTxt = sb.toString();
    sb = null;

    handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_CODE_GEN_METHOD);
  }

  private void handleEclipseAskOptimize(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curSrc;

    logger.debug("Send Ask Optimize Selected Code Request - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curSrc = eUtils.getCurrentSelectedText(false);

    if (inputTxt.startsWith(inlineOptimizePromptPrefix)) {
      //Remove inlineOptimizePromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineOptimizePromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curSrc != null && !curSrc.isBlank()) {
      sb = new StringBuilder();

      sb.append(askOptimizePrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curSrc);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_OPTIMIZE);
    }
  }

  private void handleEclipseAskOptimizeClass(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curSrc;

    logger.debug("Send Ask Optimize Class Code Request - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curSrc = eUtils.getCurrentJavaClassSourceAsText();

    if (inputTxt.startsWith(inlineOptimizeClassPromptPrefix)) {
      //Remove inlineOptimizeClassPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineOptimizeClassPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curSrc != null && !curSrc.isBlank()) {
      sb = new StringBuilder();

      sb.append(askOptimizeClassPrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curSrc);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_OPTIMIZE_CLASS);
    }
  }

  private void handleEclipseAskOptimizeMethod(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curSrc;

    logger.debug("Send Ask Optimize Method Code Request - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curSrc = eUtils.getCurrentJavaMethodSourceAsText();

    if (inputTxt.startsWith(inlineOptimizeMethodPromptPrefix)) {
      //Remove inlineOptimizeMethodPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineOptimizeMethodPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curSrc != null && !curSrc.isBlank()) {
      sb = new StringBuilder();

      sb.append(askOptimizeMethodPrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curSrc);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_OPTIMIZE_METHOD);
    }
  }

  private void handleEclipseAskCodeReview(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;

    logger.debug("Send Ask Code Review - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    if (inputTxt.startsWith(inlineCodeReviewPromptPrefix)) {
      //Remove inlineCodeReviewPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCodeReviewPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    sb = new StringBuilder();

    if (inputTxt != null && !inputTxt.isBlank()) {
      sb.append(askCodeReviewPrompt);

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(inputTxt);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_CODE_REVIEW);
    }
  }

  private void handleEclipseAskCodeReviewClass(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curCode;

    logger.debug("Send Ask Code Review on Class - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curCode = eUtils.getCurrentJavaClassSourceAsText();

    if (inputTxt.startsWith(inlineCodeReviewClassPromptPrefix)) {
      //Remove inlineCodeReviewClassPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCodeReviewClassPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curCode != null && !curCode.isBlank()) {
      sb = new StringBuilder();

      sb.append(askCodeReviewClassPrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curCode);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_CODE_REVIEW_CLASS);
    }
  }

  private void handleEclipseAskCodeReviewMethod(String inputTxt, ChatKey cKey) throws BadLocationException, ProggieBuddieException, ChatAiException {
    StringBuilder sb;
    String curCode;

    logger.debug("Send Ask Code Review on Method - inputTxt: " + inputTxt + "; ChatKey: " + cKey);

    curCode = eUtils.getCurrentJavaMethodSourceAsText();

    if (inputTxt.startsWith(inlineCodeReviewMethodPromptPrefix)) {
      //Remove inlineCodeReviewMethodPromptPrefix from line if exists
      inputTxt = inputTxt.trim();
      inputTxt = inputTxt.substring(inlineCodeReviewMethodPromptPrefix.length());
      inputTxt = inputTxt.trim(); //Second trim if there were spaces after the prompt
    }

    if (curCode != null && !curCode.isBlank()) {
      sb = new StringBuilder();

      sb.append(askCodeReviewMethodPrompt);
      sb.append("\n");

      if (inputTxt != null && !inputTxt.isBlank()) {
        sb.append(inputTxt);
        sb.append("\n");
      }

      sb.append(codeSnippetStartMarker);
      sb.append("\n");

      sb.append(curCode);
      sb.append("\n");

      sb.append(codeSnippetEndMarker);
      sb.append("\n");

      inputTxt = sb.toString();
      sb = null;

      handleSendMessage(inputTxt, cKey, OpenAiValueObjectType.INLINE_ASK_CODE_REVIEW_METHOD);
    }
  }

  protected void handleChatComboSelectionUiAction(int selectedContextIndex, String selectedContextName) {
    ChatKey cKey;

    logger.debug("handleChatComboSelectionUiAction - selectedContextIndex = " + selectedContextIndex + ", selectedContextName = " + selectedContextName);

    cKey = changeCurrentSelectedChatContextChatKeyTo(selectedContextName);

    logger.debug("handleChatComboSelectionUiAction - Current Chat Key Now: " + cKey);

    //Auto Send if Required
    autoSendInitialEngineeredPrompt(cKey);

    //Update Ring Buffer Stats
    updateChatRingBufferStats();

    cKey = null;
  }

  private ChatKey changeCurrentSelectedChatContextChatKeyTo(String chatKeyName) {
    ChatKey cKey;
    ChatTexts chTxts;

    cKey = chatSession.getChatKeyByName(chatKeyName);

    chTxts = updateSpecificChatTextBuffer(null, cKey);
    updateToChatDisplays(chTxts);

    cKey = changeCurrentSelectedChatContextChatKeyTo(cKey);

    chTxts = null;

    return cKey;
  }

  protected void updateChatContextToUiCombo() {
    ArrayList<ChatKey> sortedChatKeyList;

    sortedChatKeyList = chatSession.getSortedChatKeyListCopy();

    updateChatContextToUiCombo(sortedChatKeyList);

    sortedChatKeyList = null;
  }

  protected void updateChatContextToUiCombo(ArrayList<ChatKey> sortedChatKeyList) {
    Display.getDefault().syncExec(() -> {
      ChatKey cKey;
      String cName;

      if (!pbUi.chatDisplay.isDisposed()) {
        pbUi.chatContextCombo.removeAll();

        if (sortedChatKeyList != null) {
          for (int i = 0; i < sortedChatKeyList.size(); i++) {
            cKey = sortedChatKeyList.get(i);

            if (cKey.isVisible()) {
              logger.debug("Adding Chat Context to Chat Context ComboBox: " + cKey);

              cName = cKey.getChatName();

              pbUi.chatContextCombo.add(cName);
            }
            else {
              logger.debug("Skipping Invisible Chat Context: " + cKey);
            }

            cName = null;
            cKey = null;
          } //End for loop through Sorted Chat Key List
        } //End null check

        pbUi.chatContextCombo.select(0);
      } //End isDisposed Check
    });
  }

  protected void addChatContext(String entityName) {
    if (!pbUi.chatDisplay.isDisposed()) {
      logger.debug("Adding Chat Context for EntityName: " + entityName);

      chatSession.createChatRingBuffer(entityName);

      updateChatContextToUiCombo();
    }
  }

  protected void removeChatContext(String chatKeyName) {
    if (!pbUi.chatDisplay.isDisposed()) {
      logger.debug("Removing Chat Context: " + chatKeyName);

      ChatKey cKey = chatSession.findActiveChatKeyByMatchingExpression(chatKeyName);

      //Do we really remove the entire chat context? - Yes as of 2023-10-09
      chatSession.removeChatRingBuffer(cKey);

      //Remove Chat Text Buffer
      chatsTextMap.remove(cKey);

      updateChatContextToUiCombo();
    }
  }

  protected ChatKey getCurrentChatKey() {
    if (_currentSelectedChatContextChatKey == null) {
      ChatKey cKey;

      //Default to Main
      cKey = getMainContextChatKey();

      _currentSelectedChatContextChatKey = cKey;

      cKey = null;
    }

    return _currentSelectedChatContextChatKey;
  }

  protected ChatKey getDynamicContextChatKey() {
    if (_dynamicContextChatKey == null) {
      ArrayList<ChatKey> ckLst;
      ChatKey cKey;

      //Default to first Dynamic Context
      ckLst = chatSession.getChatKeysBySystemTag(SystemChatTag.SYS_DYNAMIC);
      cKey = ckLst.get(0); //For Sys Main Dynamic Should Only Be One Element!

      _dynamicContextChatKey = cKey;

      ckLst = null;
      cKey = null;
    }

    return _dynamicContextChatKey;
  }

  protected ChatKey getMainContextChatKey() {
    if (_mainContextChatKey == null) {
      ArrayList<ChatKey> ckLst;
      ChatKey cKey;

      //Default to first Main Context
      ckLst = chatSession.getChatKeysBySystemTag(SystemChatTag.SYS_MAIN);
      cKey = ckLst.get(0); //For Sys Main Should Only Be One Element!

      _mainContextChatKey = cKey;

      ckLst = null;
      cKey = null;
    }

    return _mainContextChatKey;
  }

  private void replaceRewrittenMethod(String replyTxt) throws BadLocationException {
    replyTxt = removeCodeSnippetMarkers(replyTxt);
    eUtils.replaceCurrentJavaMethod(replyTxt);
  }

  private String removeCodeSnippetMarkers(String replyTxt) {
    String cleanedCode;
    int tokenLen, tokenPos;

    cleanedCode = replyTxt.trim();

    //Remove Start Marker if Present and anything before
    if (cleanedCode.indexOf(codeSnippetStartMarker) >= 0) {
      tokenLen = codeSnippetStartMarker.length();
      tokenPos = cleanedCode.indexOf(codeSnippetStartMarker);
      tokenPos += tokenLen;
      cleanedCode = cleanedCode.substring(tokenPos);
    }

    //Remove End Marker if Present and anything after
    if (cleanedCode.lastIndexOf(codeSnippetEndMarker) >= 0) {
      tokenLen = codeSnippetEndMarker.length();
      tokenPos = cleanedCode.lastIndexOf(codeSnippetEndMarker);
      cleanedCode = cleanedCode.substring(0, tokenPos);
    }

    return cleanedCode;
  }

  private void insertCodeInPlace(String replyTxt) throws BadLocationException {
    replyTxt = removeCodeSnippetMarkers(replyTxt);
    eUtils.insertCodeInPlace(replyTxt);
  }

  private void replaceAllCode(String replyTxt) throws BadLocationException {
    replyTxt = removeCodeSnippetMarkers(replyTxt);
    eUtils.replaceCurrentJavaClass(replyTxt);
  }

  private void displayCodeReview(String replyTxt) throws IOException, IlardiSysEclipseException {
    File tmpFile = SystemUtils.createTempFile("pb-code-review-", ".txt", replyTxt);
    eUtils.openFileWithDefaultApplication(tmpFile);
  }

  protected void handleClearRingBufferUiAction() {
    ChatKey cKey;
    ChatTexts chTxts;

    cKey = getCurrentChatKey();

    logger.debug("Clear Ring Buffer UI Action Called for ChatKey: " + cKey);

    chatSession.clearRingBuffer(cKey);

    clearChatTextBuffer(cKey);

    chTxts = updateSpecificChatTextBuffer(null, cKey);
    updateToChatDisplays(chTxts);

    SystemUtils.SleepTight(25); //UI Issue Quick Fix

    updateChatRingBufferStats();

    SystemUtils.SleepTight(25); //UI Issue Quick Fix

    autoSendInitialEngineeredPrompt(cKey);

    SystemUtils.SleepTight(25); //UI Issue Quick Fix

    chTxts = null;
    cKey = null;
  }

  protected void updateChatRingBufferStats() {
    ChatKey cKey;
    String statsTxt, tmp;
    StringBuilder sb;
    int tokenCnt, maxTokenCount;
    double bufUsage;

    cKey = getCurrentChatKey();

    tokenCnt = chatSession.countCurrentTokens(cKey);
    maxTokenCount = chatSession.getMaxDialogTokens();

    bufUsage = ((double) tokenCnt) / ((double) maxTokenCount);
    bufUsage = bufUsage * 100.0d;

    sb = new StringBuilder();

    sb.append(UI_CHAT_RING_BUFFER_LABEL_PREFIX);
    sb.append(" - ");

    sb.append("Tokens: ");
    sb.append(tokenCnt);
    sb.append(" / ");
    sb.append(maxTokenCount);

    sb.append(", Buffer Usage: ");
    tmp = StringUtils.FormatDouble(bufUsage, 1);
    sb.append(tmp);
    sb.append("%");

    statsTxt = sb.toString();

    logger.debug("Updated Ring Buffer Stats for Chat: " + cKey + " ; " + statsTxt);

    SystemUtils.SleepTight(25); //UI Issue Quick Fix

    updateChatRingBufferStatsLabel(cKey, statsTxt);

    SystemUtils.SleepTight(25); //UI Issue Quick Fix

    sb = null;
    tmp = null;
    cKey = null;
    statsTxt = null;
  }

  protected void updateChatRingBufferStatsLabel(ChatKey cKey, String statsTxt) {
    Display.getDefault().asyncExec(() -> {
      if (!pbUi.chatDisplay.isDisposed()) {
        pbUi.chatRingBufferLabel.setText(statsTxt);
      } //End isDisposed Check
    });
  }

}
