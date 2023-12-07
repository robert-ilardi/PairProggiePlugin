/**
 * Created Jul 2, 2023
 */
package com.ilardi.systems.probud;

import static com.ilardi.systems.probud.ProggieBuddieConstants.UI_CHAT_RING_BUFFER_LABEL_PREFIX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.ilardi.systems.openai.OpenAiValueObjectType;

/**
 * @author robert.ilardi
 *
 */

public class ProggieBuddieUiBuilder {

  private static final Logger logger = LogManager.getLogger(ProggieBuddieUiBuilder.class);

  public ProggieBuddieUi buildPluginUi(Composite parent, ProggieBuddiePlugin plugin, ProggieBuddieUiCore uiCore) {
    ProggieBuddieUi pbUi;

    logger.debug("Buidling Plugin UI...");

    //pbUi = new ProggieBuddieUi(parent, plugin, uiCore);
    pbUi = new ProggieBuddieUi(parent);

    buildPluginUi(pbUi, uiCore);

    return pbUi;
  }

  private void buildPluginUi(ProggieBuddieUi pbUi, ProggieBuddieUiCore uiCore) {
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;

    pbUi.parent.setLayout(gridLayout);
    pbUi.parent.setLayoutData(gridData);

    createTabs(pbUi, uiCore);
  }

  private void createTabs(ProggieBuddieUi pbUi, ProggieBuddieUiCore uiCore) {
    logger.debug("Creating Tabs Controls");

    //Create Tab Folder Control
    pbUi.tabFolder = new CTabFolder(pbUi.parent, SWT.BORDER);

    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;

    pbUi.tabFolder.setLayout(gridLayout);
    pbUi.tabFolder.setLayoutData(gridData);

    //Chat Tab
    createChatTab(pbUi, uiCore);

    //Console Tab
    createConsoleTab(pbUi);

    //Properties Tab
    createPropertiesTab(pbUi, uiCore);

    // History Tab
    createHistoryTab(pbUi);

    addTabListener(pbUi, uiCore);

    pbUi.tabFolder.setSelection(0); // Set the initial selected tab
  }

  private void addTabListener(ProggieBuddieUi pbUi, ProggieBuddieUiCore uiCore) {
    pbUi.tabFolder.addSelectionListener(uiCore.tabSelectionListener);
  }

  private void createChatTab(ProggieBuddieUi pbUi, ProggieBuddieUiCore uiCore) {
    logger.debug("Creating Chat Tab");

    pbUi.chatTab = new CTabItem(pbUi.tabFolder, SWT.NONE);
    pbUi.chatTab.setText("Chat");
    pbUi.chatComposite = new Composite(pbUi.tabFolder, SWT.NONE);
    pbUi.chatTab.setControl(pbUi.chatComposite);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    GridData gridData = new GridData(GridData.FILL_BOTH);

    pbUi.chatComposite.setLayout(layout);
    pbUi.chatComposite.setLayoutData(gridData);

    //---------------------------------------------------------->

    //Chat Context Controls------------------------------------>

    logger.debug("Adding Chat Context Controls");

    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.horizontalSpan = 2;

    pbUi.chatContextControlPanel = new Composite(pbUi.chatComposite, SWT.NONE);

    layout = new GridLayout();
    layout.numColumns = 2;

    pbUi.chatContextControlPanel.setLayout(layout);
    pbUi.chatContextControlPanel.setLayoutData(gridData);

    pbUi.chatContextLabel = new Label(pbUi.chatContextControlPanel, SWT.NONE);
    pbUi.chatContextLabel.setText("Chat Context:");

    gridData = new GridData(GridData.FILL_HORIZONTAL);

    pbUi.chatContextCombo = new Combo(pbUi.chatContextControlPanel, SWT.READ_ONLY);
    pbUi.chatContextCombo.setLayoutData(gridData);

    pbUi.chatContextCombo.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        int selectedConextIndex = pbUi.chatContextCombo.getSelectionIndex();

        String selectedContextName = pbUi.chatContextCombo.getItem(selectedConextIndex);

        uiCore.handleChatComboSelectionUiAction(selectedConextIndex, selectedContextName);
      }
    });

    //---------------------------------------------------------->

    //Chat Ring Buffer Controls--------------------------------->

    logger.debug("Adding Chat Ring Buffer Controls");

    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.horizontalSpan = 2;

    pbUi.chatRingBufferPanel = new Composite(pbUi.chatComposite, SWT.NONE);

    layout = new GridLayout();
    layout.numColumns = 2;

    pbUi.chatRingBufferPanel.setLayout(layout);
    pbUi.chatRingBufferPanel.setLayoutData(gridData);

    gridData = new GridData(GridData.FILL_HORIZONTAL);

    pbUi.chatRingBufferLabel = new Label(pbUi.chatRingBufferPanel, SWT.NONE);
    pbUi.chatRingBufferLabel.setLayoutData(gridData);
    pbUi.chatRingBufferLabel.setText(UI_CHAT_RING_BUFFER_LABEL_PREFIX);

    pbUi.chatRingBufferClearButton = new Button(pbUi.chatRingBufferPanel, SWT.PUSH);
    pbUi.chatRingBufferClearButton.setText("Clear");

    pbUi.chatRingBufferClearButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        uiCore.handleClearRingBufferUiAction();
      }
    });

    //---------------------------------------------------------->

    //Chat Main Display----------------------------------------->

    logger.debug("Adding Main Chat Display");

    layout = new GridLayout();
    layout.numColumns = 1;

    gridData = new GridData(GridData.FILL_BOTH);

    pbUi.chatDisplayPanel = new Composite(pbUi.chatComposite, SWT.NONE);

    pbUi.chatDisplayPanel.setLayout(layout);
    pbUi.chatDisplayPanel.setLayoutData(gridData);

    Color bg = new Color(0, 0, 0);
    Color fg = new Color(0, 192, 0);

    pbUi.chatDisplay = new Text(pbUi.chatDisplayPanel, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);

    gridData = new GridData(GridData.FILL_BOTH);

    pbUi.chatDisplay.setLayoutData(gridData);

    pbUi.chatDisplay.setEditable(false); // prevent editing of chat display

    //---------------------------------------------------------->

    //Chat Input Controls--------------------------------------->

    pbUi.chatDisplay.setBackground(bg);
    pbUi.chatDisplay.setForeground(fg);

    logger.debug("Adding Chat Input");

    gridData = new GridData(GridData.FILL_HORIZONTAL);

    pbUi.chatInputControlPanel = new Composite(pbUi.chatComposite, SWT.NONE);

    layout = new GridLayout();
    layout.numColumns = 2;

    pbUi.chatInputControlPanel.setLayout(layout);
    pbUi.chatInputControlPanel.setLayoutData(gridData);

    bg = new Color(0, 0, 0);
    fg = new Color(0, 192, 0);

    gridData = new GridData(GridData.FILL_HORIZONTAL);

    pbUi.chatInput = new Text(pbUi.chatInputControlPanel, SWT.BORDER);

    pbUi.chatInput.setLayoutData(gridData);

    pbUi.chatInput.setBackground(bg);
    pbUi.chatInput.setForeground(fg);

    pbUi.chatInput.setFocus();

    pbUi.chatInput.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
          uiCore.handleSendMessageUiAction(OpenAiValueObjectType.BASIC_CHAT);
          uiCore.processLastResponse(true);
        }
      }
    });

    logger.debug("Adding Chat Send Button");

    pbUi.chatSendButton = new Button(pbUi.chatInputControlPanel, SWT.PUSH);
    pbUi.chatSendButton.setText("Send");

    pbUi.chatSendButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        uiCore.handleSendMessageUiAction(OpenAiValueObjectType.BASIC_CHAT);
        uiCore.processLastResponse(true);
      }
    });

    //---------------------------------------------------------->
  }

  private void createConsoleTab(ProggieBuddieUi pbUi) {
    logger.debug("Creating Console Tab");

    pbUi.consoleTab = new CTabItem(pbUi.tabFolder, SWT.NONE);
    pbUi.consoleTab.setText("Console");

    pbUi.consoleComposite = new Composite(pbUi.tabFolder, SWT.NONE);
    pbUi.consoleTab.setControl(pbUi.consoleComposite);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;

    pbUi.consoleComposite.setLayout(layout);

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;

    Color bg, fg;

    logger.debug("Adding Console Display");

    bg = new Color(0, 0, 0);
    fg = new Color(0, 192, 0);

    pbUi.consoleDisplay = new Text(pbUi.consoleComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);

    pbUi.consoleDisplay.setLayoutData(gridData);

    pbUi.consoleDisplay.setEditable(false);

    pbUi.consoleDisplay.setBackground(bg);
    pbUi.consoleDisplay.setForeground(fg);
  }

  private void createPropertiesTab(ProggieBuddieUi pbUi, ProggieBuddieUiCore uiCore) {
    logger.debug("Creating Properties Tab");

    pbUi.propertiesTab = new CTabItem(pbUi.tabFolder, SWT.NONE);
    pbUi.propertiesTab.setText("Properties");
    pbUi.propertiesComposite = new Composite(pbUi.tabFolder, SWT.NONE);
    pbUi.propertiesTab.setControl(pbUi.propertiesComposite);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;

    pbUi.propertiesComposite.setLayout(layout);

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 2;

    pbUi.propertiesComposite.setLayoutData(gridData);

    logger.debug("Adding Properties Table Panel");

    pbUi.propertiesTablePanel = new Composite(pbUi.propertiesComposite, SWT.NONE);

    layout = new GridLayout();
    layout.numColumns = 1;

    gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;

    pbUi.propertiesTablePanel.setLayout(layout);
    pbUi.propertiesTablePanel.setLayoutData(gridData);

    gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    //GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);

    logger.debug("Adding Properties Table");

    //Create Table
    pbUi.propertiesTable = new Table(pbUi.propertiesTablePanel, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
    pbUi.propertiesTable.setLayoutData(gridData);
    pbUi.propertiesTable.setHeaderVisible(true);
    pbUi.propertiesTable.setLinesVisible(true);
    pbUi.propertiesTable.setVisible(true);

    pbUi.propNameCol = new TableColumn(pbUi.propertiesTable, SWT.NONE);
    pbUi.propNameCol.setText("Property Name");

    pbUi.propValueCol = new TableColumn(pbUi.propertiesTable, SWT.NONE);
    pbUi.propValueCol.setText("Property Value");

    //uiCore.populatePropertiesTable();

    //uiCore.resizePropertiesTableSize();

    pbUi.propNameCol.pack();
    pbUi.propValueCol.pack();

    logger.debug("Adding Properties Buttons Panel");

    //Add Buttons    
    pbUi.propertiesButtonPanel = new Composite(pbUi.propertiesComposite, SWT.NONE);

    layout = new GridLayout();
    layout.numColumns = 5;

    pbUi.propertiesButtonPanel.setLayout(layout);

    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.horizontalSpan = 1;

    logger.debug("Adding Properties Load Button");

    pbUi.loadPropsButton = new Button(pbUi.propertiesButtonPanel, SWT.PUSH);
    pbUi.loadPropsButton.setText("Load");

    pbUi.loadPropsButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        uiCore.loadPropertiesTab(e);
      }
    });

    logger.debug("Adding Properties Save Button");

    pbUi.savePropsButton = new Button(pbUi.propertiesButtonPanel, SWT.PUSH);
    pbUi.savePropsButton.setText("Save");

    pbUi.savePropsButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        try {
          uiCore.saveProperties(e);
        }
        catch (Exception ex) {
          logger.throwing(ex);
        }
      }
    });

    logger.debug("Adding Properties Add Button");

    pbUi.addPropsButton = new Button(pbUi.propertiesButtonPanel, SWT.PUSH);
    pbUi.addPropsButton.setText("Add");

    pbUi.addPropsButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        uiCore.addNewProperty(e);
      }
    });

    logger.debug("Adding Properties Remove Button");

    pbUi.removePropsButton = new Button(pbUi.propertiesButtonPanel, SWT.PUSH);
    pbUi.removePropsButton.setText("Remove");

    pbUi.removePropsButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        uiCore.removeSelectedProperty(e);
      }
    });

    logger.debug("Adding Properties Edit Button");

    pbUi.editPropsButton = new Button(pbUi.propertiesButtonPanel, SWT.PUSH);
    pbUi.editPropsButton.setText("Edit");

    pbUi.editPropsButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        uiCore.editSelectedProperty(e);
      }
    });
  }

  private void createHistoryTab(ProggieBuddieUi pbUi) {
    logger.debug("Creating History Tab");

    //Create History Main Tab
    pbUi.historyTab = new CTabItem(pbUi.tabFolder, SWT.NONE);
    pbUi.historyTab.setText("History");

    pbUi.historyComposite = new Composite(pbUi.tabFolder, SWT.NONE);
    pbUi.historyTab.setControl(pbUi.historyComposite);

    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;

    pbUi.historyComposite.setLayout(gridLayout);
    pbUi.historyComposite.setLayoutData(gridData);

    createHistoryTabSubTabFolder(pbUi);

    createHistoryTabGlobalChatSubTag(pbUi);

    createHistoryTabReqResSubTag(pbUi);
  }

  private void createHistoryTabSubTabFolder(ProggieBuddieUi pbUi) {
    logger.debug("Creating History Tab - Sub Tab Folder");

    //Create History Sub Tab Folder Control
    pbUi.historySubTabFolder = new CTabFolder(pbUi.historyComposite, SWT.BORDER);

    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;

    pbUi.historySubTabFolder.setLayout(gridLayout);
    pbUi.historySubTabFolder.setLayoutData(gridData);
  }

  private void createHistoryTabGlobalChatSubTag(ProggieBuddieUi pbUi) {
    logger.debug("Creating History Tab - Global Chat Sub Tab");

    pbUi.historyGlobalChatTab = new CTabItem(pbUi.historySubTabFolder, SWT.NONE);
    pbUi.historyGlobalChatTab.setText("Global Chat");

    pbUi.historyGlobalChatComposite = new Composite(pbUi.historySubTabFolder, SWT.NONE);
    pbUi.historyGlobalChatTab.setControl(pbUi.historyGlobalChatComposite);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;

    pbUi.historyGlobalChatComposite.setLayout(layout);

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;

    Color bg, fg;

    logger.debug("Adding Global Chat Display");

    bg = new Color(0, 0, 0);
    fg = new Color(0, 192, 0);

    pbUi.historyGlobalChatDisplay = new Text(pbUi.historyGlobalChatComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);

    pbUi.historyGlobalChatDisplay.setLayoutData(gridData);

    pbUi.historyGlobalChatDisplay.setEditable(false);

    pbUi.historyGlobalChatDisplay.setBackground(bg);
    pbUi.historyGlobalChatDisplay.setForeground(fg);
  }

  private void createHistoryTabReqResSubTag(ProggieBuddieUi pbUi) {
    logger.debug("Creating History Tab - Request/Response Tab");

    pbUi.historyReqResTab = new CTabItem(pbUi.historySubTabFolder, SWT.NONE);
    pbUi.historyReqResTab.setText("API Call Explorer");

    pbUi.historyReqResComposite = new Composite(pbUi.historySubTabFolder, SWT.NONE);
    pbUi.historyReqResTab.setControl(pbUi.historyReqResComposite);

    GridLayout layout = new GridLayout();
    layout.numColumns = 1;

    pbUi.historyReqResComposite.setLayout(layout);

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.horizontalSpan = 1;
  }

}
