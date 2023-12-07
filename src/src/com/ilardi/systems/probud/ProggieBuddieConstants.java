/**
 * Created Jul 6, 2023
 */
package com.ilardi.systems.probud;

/**
 * @author robert.ilardi
 *
 */

public final class ProggieBuddieConstants {

  public static final String PLUGIN_TITLE = "Proggie Buddie";

  public static final String PLUGIN_DESCRIPTION = "Ilardi Systems' Proggie Buddie is a ChatGPT LLM AI-based Chat Programming Assistant Eclipse IDE Plugin using the Pair Programming Paradigm.";

  public static final String PLUGIN_SHORT_DESCRIPTION = "ChatGPT Pair Programmer - Proggie Buddie";

  public static final String PLUGIN_ICON_PATH = "/rl16x16.jpg";

  public static final String DEFAULT_USER_PROPS_FILENAME = "proggie-buddie.properties";

  public static final String SYS_PROP_DEFAULT_USER_PROPS_FILEPATH = "com.ilardi.systems.probud.UserPropsPath";

  public static final int DEFAULT_PROP_TABLE_WIDTH = 150;
  public static final int DEFAULT_PROP_TABLE_HEIGHT = 200;

  public static final String DEFAULT_PROPERTY_VALUE_MASK_STRING = "**********";

  public static final String PROPS_PROGRAMMER_NAME = "com.ilardi.systems.probud.ProgrammerName";
  public static final String PROPS_PROGRAMMER_DESCRIPTION = "com.ilardi.systems.probud.ProgrammerDescription";

  public static final String PROPS_BOT_NAME = "com.ilardi.systems.probud.BotName";
  public static final String PROPS_BOT_CHARACTER_DESCRIPTION = "com.ilardi.systems.probud.BotCharacterDescription";

  public static final String PROPS_PROGRAMMING_LANGUAGE = "com.ilardi.systems.probud.ProgrammingLanguage";
  public static final String PROPS_PROGRAMMING_TECHNIQUE = "com.ilardi.systems.probud.ProgrammingTechnique";

  public static final String PROPS_PROJECT_TITLE = "com.ilardi.systems.probud.ProjectTitle";
  public static final String PROPS_PROJECT_DESCRIPTION = "com.ilardi.systems.probud.ProjectDescription";
  public static final String PROPS_PROJECT_REQUIREMENTS = "com.ilardi.systems.probud.ProjectRequirements";

  public static final String PROPS_MAX_DIALOG_MESSAGE_TOKENS = "com.ilardi.systems.probud.MaxMessageTokens";
  public static final String PROPS_MAX_REQUEST_HISTORY_OBJECTS = "com.ilardi.systems.probud.MaxRequestHistoryObjects";
  public static final String PROPS_MAX_RESPONSE_HISTORY_OBJECTS = "com.ilardi.systems.probud.MaxResponseHistoryObjects";
  public static final String PROPS_TOKEN_SAFETY_MARGIN_PERCENTAGE = "com.ilardi.systems.probud.TokenSafetyMarginPercentage";

  public static final String PROPS_OPENAI_API_URL = "com.ilardi.systems.probud.OpenAiApiUrl";
  public static final String PROPS_OPENAI_API_KEY = "com.ilardi.systems.probud.OpenAiApiKey";
  public static final String PROPS_OPENAI_API_CHATGPT_MODEL = "com.ilardi.systems.probud.OpenAiApiChatGptModel";
  public static final String PROPS_OPENAI_API_CHATGPT_ROLE = "com.ilardi.systems.probud.OpenAiApiChatGptRole";

  public static final String APP_CNTXT_KEY_CHAT_SESSION = "com.ilardi.systems.probud.ChatSession";

  public static final String PROPS_PROPERTIES_TO_MASK_LIST = "com.ilardi.systems.probud.PropertiesToMaskList";

  public static final String PROPS_CODE_SNIPPET_START_MARKER = "com.ilardi.systems.probud.CodeSnippetStartMarker";
  public static final String PROPS_CODE_SNIPPET_END_MARKER = "com.ilardi.systems.probud.CodeSnippetEndMarker";

  public static final String PROPS_INLINE_CHAT_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineChatPromptPrefix";

  public static final String PROPS_INLINE_COMMENT_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCommentPromptPrefix";
  public static final String PROPS_INLINE_COMMENT_CLASS_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCommentClassPromptPrefix";
  public static final String PROPS_INLINE_COMMENT_METHOD_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCommentMethodPromptPrefix";

  public static final String PROPS_INLINE_CODE_REVIEW_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCodeReviewPromptPrefix";
  public static final String PROPS_INLINE_CODE_REVIEW_CLASS_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCodeReviewClassPromptPrefix";
  public static final String PROPS_INLINE_CODE_REVIEW_METHOD_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCodeReviewMethodPromptPrefix";

  public static final String PROPS_INLINE_CODE_GEN_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCodeGenPromptPrefix";
  public static final String PROPS_INLINE_CODE_GEN_CLASS_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCodeGenClassPromptPrefix";
  public static final String PROPS_INLINE_CODE_GEN_METHOD_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineCodeGenMethodPromptPrefix";

  public static final String PROPS_INLINE_OPTIMIZE_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineOptimizePromptPrefix";
  public static final String PROPS_INLINE_OPTIMIZE_CLASS_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineOptimizeClassPromptPrefix";
  public static final String PROPS_INLINE_OPTIMIZE_METHOD_PROMPT_PREFIX = "com.ilardi.systems.probud.InlineOptimizeMethodPromptPrefix";

  public static final String PROPS_ASK_COMMENT_PROMPT = "com.ilardi.systems.probud.AskCommentPrompt";
  public static final String PROPS_ASK_COMMENT_CLASS_PROMPT = "com.ilardi.systems.probud.AskCommentClassPrompt";
  public static final String PROPS_ASK_COMMENT_METHOD_PROMPT = "com.ilardi.systems.probud.AskCommmentMethodPrompt";

  public static final String PROPS_ASK_CODE_REVIEW_PROMPT = "com.ilardi.systems.probud.AskCodeReviewPrompt";
  public static final String PROPS_ASK_CODE_REVIEW_CLASS_PROMPT = "com.ilardi.systems.probud.AskCodeReviewClassPrompt";
  public static final String PROPS_ASK_CODE_REVIEW_METHOD_PROMPT = "com.ilardi.systems.probud.AskCodeReviewMethodPrompt";

  public static final String PROPS_ASK_CODE_GEN_PROMPT = "com.ilardi.systems.probud.AskCodeGenPrompt";
  public static final String PROPS_ASK_CODE_GEN_CLASS_PROMPT = "com.ilardi.systems.probud.AskCodeGenClassPrompt";
  public static final String PROPS_ASK_CODE_GEN_METHOD_PROMPT = "com.ilardi.systems.probud.AskCodeGenMethodPrompt";

  public static final String PROPS_ASK_OPTIMIZE_PROMPT_PREFIX = "com.ilardi.systems.probud.AskOptimizePromptPrefix";
  public static final String PROPS_ASK_OPTIMIZE_CLASS_PROMPT_PREFIX = "com.ilardi.systems.probud.AskOptimizeClassPromptPrefix";
  public static final String PROPS_ASK_OPTIMIZE_METHOD_PROMPT_PREFIX = "com.ilardi.systems.probud.AskOptimizeMethodPromptPrefix";

  public static final String PB_ACTION_CODE_PARAM_NAME = "ProggieBuddie.ActionCode";

  public static final String PB_ACTION_CODE_PARAM_VALUE_HOTKEY = "HotKey";
  public static final String PB_ACTION_CODE_PARAM_VALUE_GENERAL_CHAT = "SendGeneralChat";

  public static final String PB_ACTION_CODE_PARAM_VALUE_COMMENT = "AskComment";
  public static final String PB_ACTION_CODE_PARAM_VALUE_COMMENT_CLASS = "AskCommentClass";
  public static final String PB_ACTION_CODE_PARAM_VALUE_COMMENT_METHOD = "AskCommentMethod";

  public static final String PB_ACTION_CODE_PARAM_VALUE_CODE_GEN = "AskCodeGen";
  public static final String PB_ACTION_CODE_PARAM_VALUE_CODE_GEN_CLASS = "AskCodeGenClass";
  public static final String PB_ACTION_CODE_PARAM_VALUE_CODE_GEN_METHOD = "AskCodeGenMethod";

  public static final String PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW = "AskCodeReview";
  public static final String PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW_CLASS = "AskCodeReviewClass";
  public static final String PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW_METHOD = "AskCodeReviewMethod";

  public static final String PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE = "AskOptimize";
  public static final String PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE_CLASS = "AskOptimizeClass";
  public static final String PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE_METHOD = "AskOptimizeMethod";

  public static final long DEFAULT_MESSAGE_PRIORITY = 0L;

  public static final String UI_CHAT_RING_BUFFER_LABEL_PREFIX = "Ring Buffer Stats";

  public static final String PROPS_ENABLE_TEXT_TO_SPEECH = "com.ilardi.systems.probud.EnableTextToSpeech";
  public static final String PROPS_ENABLE_EDITOR_POPUP_MESSAGES = "com.ilardi.systems.probud.EnableEditorPopupMessages";
  public static final String PROPS_EDITOR_POPUP_MESSAGE_DURATION = "com.ilardi.systems.probud.EditorPopupMessageDuration";
  public static final String PROPS_FORCE_DISABLE_AUTO_SEND_INIT_PROMPT = "com.ilardi.systems.probud.ForceDisableAutoSendInitialPrompt";

  public static final String PROPS_SUFFIX_CHAT_KEY_NAME = "KeyName";
  public static final String PROPS_SUFFIX_CHAT_TOPIC = "Topic";
  public static final String PROPS_SUFFIX_CHAT_DESCRIPTION = "Description";
  public static final String PROPS_SUFFIX_INIT_MESG = "InitialMesg";
  public static final String PROPS_SUFFIX_AUTO_SEND_INIT_PROMPT = "AutoSendInitialPrompt";
  public static final String PROPS_SUFFIX_INCLUDE_PROJECT_ENGINEERED_PROMPT = "IncludeProjectEngineeredPrompt";
  public static final String PROPS_SUFFIX_ENABLE_INIT_PROMPT_EXTENDED_UI_PROCESSING = "EnableInitialPromptExtendedUiProcessing";
  public static final String PROPS_SUFFIX_ENABLE_EXTENDED_UI_PROCESSING = "EnableExtendedUiProcessing";
  public static final String PROPS_SUFFIX_CONTEXT_MODE = "ContextMode";
  public static final String PROPS_SUFFIX_GROUP_TYPE = "GroupType";
  public static final String PROPS_SUFFIX_SYSTEM_CHAT_TAG = "SystemChatTag";
  public static final String PROPS_SUFFIX_PRECONFIGURED_CHAT = "PreconfiguredChat";
  public static final String PROPS_SUFFIX_ORDER_INDEX = "OrderIndex";
  public static final String PROPS_SUFFIX_DISPLAY_NAME = "DisplayName";
  public static final String PROPS_SUFFIX_TOOLTIP = "ToolTip";
  public static final String PROPS_SUFFIX_VISIBLE = "Visible";
  public static final String PROPS_SUFFIX_ENTITY_MATCHING_EXPRESSION = "EntityMatchingExpression";

  public static final String PROPS_PREFIX_CHATS_CONFIGS = "com.ilardi.systems.probud.ChatsConfigs.";

  public static final String PROPS_PREFIX_RUNTIME_ADDITION_DEFAULT_CONFIGS = "com.ilardi.systems.probud.DefaultConfig.RuntimeAddition.";

  public static final String PROPS_VALUE_VARIABLE_TOKEN_FILENAME = "{$FILENAME}";
  public static final String PROPS_VALUE_VARIABLE_TOKEN_NULL = "{$NULL}";
  public static final String PROPS_VALUE_VARIABLE_TOKEN_MATCH_KEY_NAME = "{$MATCH-KEY-NAME}";
  public static final String PROPS_VALUE_VARIABLE_TOKEN_EXACT_MATCH = "{$EXACT-MATCH}";

  public static final String PROPS_CHATS_CONFIGS_PROP_SUFFIX_LIST = "com.ilardi.systems.probud.ChatsConfigsPropertySuffixList";

}
