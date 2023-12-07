/**
 * Created Jul 5, 2023
 */
package com.ilardi.systems.probud;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.ilardi.systems.eclipse.EclipseUtils;
import com.ilardi.systems.eclipse.IlardiSysEclipseException;

/**
 * @author robert.ilardi
 *
 */

public class CodeObserver {

  private static final Logger logger = LogManager.getLogger(CodeObserver.class);

  /*
  private IEditorPart activeEditor;
  private ITextEditor textEditor;
  private IDocumentProvider docProvider;
  private IDocument document;*/

  private HashSet<IWorkbenchPart> txtEditorWatchLst;

  private static CodeObserver instance = null;

  private EclipseUtils eclipseUtils;

  private ProggieBuddieUiCore uiCore;

  private CodeObserver() {
    txtEditorWatchLst = new HashSet<IWorkbenchPart>();

    eclipseUtils = EclipseUtils.getInstance();

    uiCore = ProggieBuddieUiCore.getInstance();
  }

  public synchronized static CodeObserver getInstance() {
    if (instance == null) {
      instance = new CodeObserver();
    }

    return instance;
  }

  private synchronized void addWorkspaceBaseListeners() throws IlardiSysEclipseException {
    logger.debug("Code Observer is attempting to add Eclipse Workspace Base Event Listeners");

    IWorkbenchWindow workbenchWindow = eclipseUtils.getWorkbenchWindow();

    ISelectionService wbWinSelectionService = workbenchWindow.getSelectionService();
    wbWinSelectionService.addSelectionListener(wbWinSelectionListener);

    IWorkbenchPage workbenchPage = eclipseUtils.getWorkbenchPage();

    workbenchPage.addPartListener(workbenchPartListener);

    addPreviouslyOpenedTextEditorListeners();

    /*workbenchPage = workbenchWindow.getActivePage();
    activeEditor = workbenchPage.getActiveEditor();
    
    if (activeEditor instanceof ITextEditor) {
      textEditor = (ITextEditor) activeEditor;
    }
    else {
      throw new IlardiSysEclipseException("Active Editor is NOT a Text Editor. Cannot Initialize Code Observer.");
    }
    
    textEditor.addPropertyListener(txtEditorListener);
    
    docProvider = textEditor.getDocumentProvider();
    document = docProvider.getDocument(textEditor.getEditorInput());*/
  }

  private synchronized void addPreviouslyOpenedTextEditorListeners() {
    IEditorReference[] edRefs;
    IEditorReference edRef;
    IEditorPart editor;
    ITextEditor textEditor;
    String filename;

    logger.debug("Code Observer is attempting to add Text Editor Listeners to Previously Opened Editors");

    IWorkbenchPage workbenchPage = eclipseUtils.getWorkbenchPage();

    edRefs = workbenchPage.getEditorReferences();

    for (int i = 0; i < edRefs.length; i++) {
      edRef = edRefs[i];

      editor = edRef.getEditor(false);

      if (editor instanceof ITextEditor) {
        textEditor = (ITextEditor) editor;

        filename = editor.getTitle();

        logger.debug("Workbench Part is a Text Editor(" + filename + "), adding listener...");

        uiCore.addChatContext(filename);

        textEditor.addPropertyListener(txtEditorListener);

        txtEditorWatchLst.add(textEditor);
      }
    }
  }

  private ISelectionListener wbWinSelectionListener = new ISelectionListener() {
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
      String filename;
      //logger.debug("Workbench Window Selection Listener Fired! Part: " + part + "; Selection: " + selection);
      logger.debug("Workbench Window Selection Listener Fired! Part: " + part);

      if (part instanceof ITextEditor) {
        if (!txtEditorWatchLst.contains(part)) {
          filename = part.getTitle();

          logger.debug("Workbench Part is a Text Editor(" + filename + ") adding listener...");

          uiCore.addChatContext(filename);

          ITextEditor textEditor = (ITextEditor) part;
          textEditor.addPropertyListener(txtEditorListener);

          txtEditorWatchLst.add(textEditor);
        }
      }
    }
  };

  private IPartListener2 workbenchPartListener = new IPartListener2() {
    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
      logger.debug("Workbench Part Listener Event = OPENED Fired! Part: " + partRef);
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
      String filename;

      logger.debug("Workbench Part Listener Event = CLOSED Fired! Part: " + partRef);

      IWorkbenchPart part = partRef.getPart(false);

      if (part instanceof ITextEditor) {
        filename = part.getTitle();
        logger.debug("Workbench Part is a Text Editor(" + filename + ")");

        if (txtEditorWatchLst.contains(part)) {
          logger.debug("Text Editor(" + filename + ") was being watched, removing listener...");

          uiCore.removeChatContext(filename);

          part.removePropertyListener(txtEditorListener);
          txtEditorWatchLst.remove(part);
        }
      }
    }
  };

  private IPropertyListener txtEditorListener = new IPropertyListener() {
    @Override
    public void propertyChanged(Object source, int propId) {
      ITextEditor textEditor = (ITextEditor) source;
      IDocumentProvider docProvider = textEditor.getDocumentProvider();
      IDocument document = docProvider.getDocument(textEditor.getEditorInput());

      logger.debug("TextEditorListener Fired! PropId = " + propId + "; Source: " + source);

      String txt = document.get();
      logger.debug(txt);
    }
  };

  public void startObservation() {
    Display.getDefault().asyncExec(() -> {
      try {
        logger.debug("Starting Code Observer");
        addWorkspaceBaseListeners();
      }
      catch (IlardiSysEclipseException e) {
        e.printStackTrace();
      }
    });
  }

  public void stopObservation() {
    Display.getDefault().asyncExec(() -> {
      try {
        logger.debug("Stopping Code Observer");
        removeWorkspaceListeners();
      }
      catch (IlardiSysEclipseException e) {
        e.printStackTrace();
      }
    });
  }

  private synchronized void removeWorkspaceListeners() throws IlardiSysEclipseException {
    ISelectionService wbWinSelectionService;

    logger.debug("Removing Eclipse Workspace Listeners...");

    removeAllTextEditorListeners();

    IWorkbenchPage workbenchPage = eclipseUtils.getWorkbenchPage();

    if (workbenchPage != null) {
      workbenchPage.removePartListener(workbenchPartListener);
      workbenchPage = null;
    }

    IWorkbenchWindow workbenchWindow = eclipseUtils.getWorkbenchWindow();

    if (workbenchWindow != null) {
      wbWinSelectionService = workbenchWindow.getSelectionService();
      wbWinSelectionService.removeSelectionListener(wbWinSelectionListener);
      workbenchWindow = null;
    }
  }

  private synchronized void removeAllTextEditorListeners() {
    Iterator<IWorkbenchPart> iter;
    IWorkbenchPart part;

    logger.debug("Removing ALL Text Editor Listeners...");

    iter = txtEditorWatchLst.iterator();

    while (iter.hasNext()) {
      part = iter.next();
      part.removePropertyListener(txtEditorListener);
      iter.remove();
    }
  }

  public String[] getOpenFileList() throws IlardiSysEclipseException {
    String[] fList = null;

    try {
      //TODO
      return fList;
    } //End try block
    catch (Exception e) {
      throw new IlardiSysEclipseException("An error occurred while attempting to get Opened File List. System Message: " + e.getMessage(), e);
    }
  }

}
