/**
 * Created Jul 27, 2023
 */
package com.ilardi.systems.eclipse;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.ilardi.systems.util.IntWrapper;

/**
 * @author robert.ilardi
 *
 */

public class EclipseUtils {

  private static final Logger logger = LogManager.getLogger(EclipseUtils.class);

  private static EclipseUtils instance = null;

  private IWorkbench workbench;
  private IWorkbenchWindow workbenchWindow;
  private IWorkbenchPage workbenchPage;
  
  private Display workbenchDisplay;

  private EclipseUtils() {
    obtainEclipseWorkspace();
  }

  public static synchronized EclipseUtils getInstance() {
    if (instance == null) {
      instance = new EclipseUtils();
    }

    return instance;
  }

  private synchronized void obtainEclipseWorkspace() {
    logger.debug("Eclipse Utils is Obtaining Base Eclise Workspace Objects");

    if (workbench == null) {
      workbench = PlatformUI.getWorkbench();
    }
    
    if (workbench != null) {
      workbenchDisplay = workbench.getDisplay();
    }
    else {
      workbenchDisplay = Display.getDefault();
    }

    if (workbenchWindow == null) {
      workbenchWindow = workbench.getActiveWorkbenchWindow();
    }

    if (workbenchPage == null) {
      workbenchPage = workbenchWindow.getActivePage();
    }

    if (workbenchPage == null) {
      IWorkbenchPage[] pages = workbenchWindow.getPages();

      if (pages != null && pages.length > 0) {
        workbenchPage = pages[0];
      }
    }
  }

  public IWorkbench getWorkbench() {
    return workbench;
  }

  public IWorkbenchWindow getWorkbenchWindow() {
    return workbenchWindow;
  }

  public IWorkbenchPage getWorkbenchPage() {
    return workbenchPage;
  }

  public ITextEditor getCurrentTextEditor() {
    IEditorPart editor;
    ITextEditor textEditor = null;

    logger.debug("Eclipse Utils is attempting to get Reference to Current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;
      }
    }

    return textEditor;
  }

  public Control getCurrentTextEditorSwtWidget() {
    IEditorPart editor;
    ITextEditor textEditor;
    Control widget = null;

    logger.debug("Eclipse Utils is attempting to get Reference to Current Text Editor SWT Widget");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;
        widget = textEditor.getAdapter(Control.class);
      }
    }

    return widget;
  }

  public Point getEclipseWidgetLocation(Control widget) {
    final Point tedPnt = new Point(0, 0);

    if (widget != null) {
      workbenchDisplay.syncExec(() -> {
        int totalX = 0, totalY = 0;
        Composite c;
        Point p;

        p = widget.getLocation();
        totalX += p.x;
        totalY += p.y;

        c = widget.getParent();

        while (c != null) {
          p = c.getLocation();

          if (p != null) {
            if (p.x > 0) {
              totalX += p.x;
            }

            if (p.y > 0) {
              totalY += p.y;
            }
          }

          c = c.getParent();
        }

        //logger.debug("Eclipse Widget: " + widget + " - Location: " + totalX + ", " + totalY);

        tedPnt.x = totalX;
        tedPnt.y = totalY;

        logger.debug("Eclipse Widget: " + widget + " - Location: " + tedPnt);
      });
    }
    else {
      logger.debug("Eclipse Widget is NULL - Location: " + tedPnt);
    }

    return tedPnt;
  }

  public Point getEclipseTextWidgetCaretLocation(Control widget) {
    final Point tedPnt = new Point(0, 0);

    if (widget != null) {
      workbenchDisplay.syncExec(() -> {
        int totalX = 0, totalY = 0;
        Composite c;
        Caret crt;
        StyledText st;
        Point p;

        //Get Total X, Y by traversing ALL Widgets until Root/NULL
        p = widget.getLocation();
        totalX += p.x;
        totalY += p.y;

        c = widget.getParent();

        while (c != null) {
          p = c.getLocation();

          if (p != null) {
            if (p.x > 0) {
              totalX += p.x;
            }

            if (p.y > 0) {
              totalY += p.y;
            }
          }

          c = c.getParent();
        }

        logger.debug("Eclipse Text Widget: " + widget + " - Location: " + totalX + ", " + totalY);

        //Add the position of the Caret within the Text Editor
        st = (StyledText) widget;
        crt = st.getCaret();

        if (crt != null) {
          p = crt.getLocation();

          if (p != null) {
            if (p.x > 0) {
              totalX += p.x;
            }

            if (p.y > 0) {
              totalY += p.y;
            }
          }
        }

        tedPnt.x = totalX;
        tedPnt.y = totalY;

        logger.debug("Eclipse Text Widget: " + widget + " - Caret Location: " + tedPnt);
      });
    }
    else {
      logger.debug("Eclipse Widget is NULL - Location: " + tedPnt);
    }

    return tedPnt;
  }

  public Point getEclipseActiveTextEditorCaretLocation() {
    Point p = null;
    Control c = getCurrentTextEditorSwtWidget();

    if (c != null) {
      p = getEclipseTextWidgetCaretLocation(c);
    }

    return p;
  }

  public Point getCenterOfScreen() {
    final Point center = new Point(0, 0);

    workbenchDisplay.syncExec(() -> {
      Rectangle rect = workbenchDisplay.getBounds();

      int x = rect.width / 2;
      int y = rect.height / 2;

      center.x = x;
      center.y = y;
    });

    return center;
  }

  private String getCurrentTextLine(boolean deleteLineAfterRead) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    int lineNum, lineOffset, lineLen;
    IRegion lineRegion;
    String line = null;

    logger.debug("Eclipse Utils is attempting to get the current line of text from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          lineNum = textSelection.getStartLine();

          lineRegion = document.getLineInformation(lineNum);

          lineOffset = lineRegion.getOffset();

          lineLen = lineRegion.getLength();

          logger.debug("Getting Text At (lineNum: " + lineNum + ", lineOffset: " + lineOffset + ", lineLen: " + lineLen + ")");

          line = document.get(lineOffset, lineLen);

          if (deleteLineAfterRead) {
            document.replace(lineOffset, lineLen, "");
          }

          logger.debug("Current Line Text: " + line);
        }
      }
    }

    return line;
  }

  public String getTextBlockFromCurrentLine(int numOfLines, boolean deleteLinesAfterRead) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion, endLineRegion;
    int startLineNum, blockStartOffset, blockEndOffset, endLineNum;
    String textBlock = null;

    logger.debug("Eclipse Utils is attempting to get the request number of lines " + numOfLines + " of text from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();

          startLineRegion = document.getLineInformation(startLineNum);

          endLineNum = Math.min(document.getNumberOfLines() - 1, startLineNum + numOfLines - 1);

          endLineRegion = document.getLineInformation(endLineNum);

          blockStartOffset = startLineRegion.getOffset();
          blockEndOffset = endLineRegion.getOffset() + endLineRegion.getLength();

          logger.debug("Getting Text At (lineNum: " + startLineNum + ", blockStartOffset: " + blockStartOffset + ", blockEndOffset: " + blockEndOffset + ")");

          textBlock = document.get(blockStartOffset, blockEndOffset - blockStartOffset);

          if (deleteLinesAfterRead) {
            document.replace(blockStartOffset, blockEndOffset - blockStartOffset, "");
          }
        }
      }
    }

    return textBlock;
  }

  public String getCurrentJavaClassSourceAsText() {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    String textBlock = null;

    logger.debug("Eclipse Utils is attempting to get the entire text from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        textBlock = document.get();
      }
    }

    return textBlock;
  }

  public String getCurrentJavaMethodSourceAsText() throws BadLocationException {
    String srcCd = null;
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion;
    int startLineNum, blockStartOffset, methodStartOffset, methodLen;
    ASTParser parser;
    CompilationUnit cu;
    IntWrapper methodStartOffsetObj, methodLenObj;

    logger.debug("Eclipse Utils is attempting to get the current Java Method Source from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();

          startLineRegion = document.getLineInformation(startLineNum);

          blockStartOffset = startLineRegion.getOffset();

          parser = ASTParser.newParser(AST.getJLSLatest());
          parser.setSource(document.get().toCharArray());
          parser.setResolveBindings(true);

          cu = (CompilationUnit) parser.createAST(null);

          methodStartOffsetObj = new IntWrapper();
          methodStartOffsetObj.setNum(-1);

          methodLenObj = new IntWrapper();
          methodLenObj.setNum(-1);

          cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration node) {
              int nodeStart = node.getStartPosition();
              int nodeLen = node.getLength();
              int nodeEnd = nodeStart + nodeLen;

              if (blockStartOffset >= nodeStart && blockStartOffset <= nodeEnd) {
                logger.debug("Caret is within method: " + node.getName().getIdentifier() + " at: (nodeStart = " + nodeStart + ", nodeLen = " + nodeLen + ", nodeEnd = " + nodeEnd + ")");

                methodStartOffsetObj.setNum(nodeStart);
                methodLenObj.setNum(nodeLen);

                return false;
              }
              else {
                return super.visit(node);
              }
            }
          });

          methodLen = methodLenObj.getNum();
          methodStartOffset = methodStartOffsetObj.getNum();

          if (methodLen > 0 && methodStartOffset >= 0) {
            srcCd = document.get(methodStartOffset, methodLen);
          }
          else {
            throw new BadLocationException("AST Visitor Returned Invalid Values: " + methodStartOffset + ", " + methodLen);
          }
        }
      }
    }

    return srcCd;
  }

  public void replaceCurrentJavaMethod(String newSrcCd) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion;
    int startLineNum, blockStartOffset, methodStartOffset, methodLen;
    ASTParser parser;
    CompilationUnit cu;
    IntWrapper methodStartOffsetObj, methodLenObj;

    logger.debug("Eclipse Utils is attempting to replace the current Java Method Source in the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();

          startLineRegion = document.getLineInformation(startLineNum);

          blockStartOffset = startLineRegion.getOffset();

          parser = ASTParser.newParser(AST.getJLSLatest());
          parser.setSource(document.get().toCharArray());
          parser.setResolveBindings(true);

          cu = (CompilationUnit) parser.createAST(null);

          methodStartOffsetObj = new IntWrapper();
          methodStartOffsetObj.setNum(-1);

          methodLenObj = new IntWrapper();
          methodLenObj.setNum(-1);

          cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration node) {
              int nodeStart = node.getStartPosition();
              int nodeLen = node.getLength();
              int nodeEnd = nodeStart + nodeLen;

              if (blockStartOffset >= nodeStart && blockStartOffset <= nodeEnd) {
                logger.debug("Caret is within method: " + node.getName().getIdentifier() + " at: (nodeStart = " + nodeStart + ", nodeLen = " + nodeLen + ", nodeEnd = " + nodeEnd + ")");

                methodStartOffsetObj.setNum(nodeStart);
                methodLenObj.setNum(nodeLen);

                return false;
              }
              else {
                return super.visit(node);
              }
            }
          });

          methodLen = methodLenObj.getNum();
          methodStartOffset = methodStartOffsetObj.getNum();

          if (methodLen > 0 && methodStartOffset >= 0) {
            document.replace(methodStartOffset, methodLen, newSrcCd);
          }
          else {
            throw new BadLocationException("AST Visitor Returned Invalid Values: " + methodStartOffset + ", " + methodLen);
          }
        }
      }
    }
  }

  public void insertCodeInPlace(String newSrcCd) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion;
    int startLineNum, startOffset;

    logger.debug("Eclipse Utils is attempting to Insert New Source Code in place in the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();

          startLineRegion = document.getLineInformation(startLineNum);

          startOffset = startLineRegion.getOffset();

          document.replace(startOffset, 0, newSrcCd);
        }
      }
    }
  }

  public String getCurrentSelectedText(boolean deleteLinesAfterRead) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion, endLineRegion;
    int startLineNum, blockStartOffset, blockEndOffset, endLineNum;
    String textBlock = null;

    logger.debug("Eclipse Utils is attempting to get the request selected text block from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();
          endLineNum = textSelection.getEndLine();

          if (startLineNum == endLineNum) {
            textBlock = getCurrentTextLine(deleteLinesAfterRead);
          }
          else {
            startLineRegion = document.getLineInformation(startLineNum);

            endLineRegion = document.getLineInformation(endLineNum);

            blockStartOffset = startLineRegion.getOffset();
            blockEndOffset = endLineRegion.getOffset() + endLineRegion.getLength();

            logger.debug("Getting Text At (startLineNum: " + startLineNum + ", blockStartOffset: " + blockStartOffset + ", endLineNum: " + endLineNum + ", blockEndOffset: " + blockEndOffset + ")");

            textBlock = document.get(blockStartOffset, blockEndOffset - blockStartOffset);

            if (deleteLinesAfterRead) {
              document.replace(blockStartOffset, blockEndOffset - blockStartOffset, "");
            }
          }
        }
      }
    }

    return textBlock;
  }

  public void replaceCurrentJavaClass(String newSrcCd) {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;

    logger.debug("Eclipse Utils is attempting to replace the entire text from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        document.set(newSrcCd);
      }
    }
  }

  public void deleteCurrentSelectedText() throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion, endLineRegion;
    int startLineNum, blockStartOffset, blockEndOffset, endLineNum;

    logger.debug("Eclipse Utils is attempting to delete the request selected text block from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();
          endLineNum = textSelection.getEndLine();

          if (startLineNum == endLineNum) {
            deleteCurrentTextLine();
          }
          else {
            startLineRegion = document.getLineInformation(startLineNum);

            endLineRegion = document.getLineInformation(endLineNum);

            blockStartOffset = startLineRegion.getOffset();
            blockEndOffset = endLineRegion.getOffset() + endLineRegion.getLength();

            logger.debug("Deleting Text At (startLineNum: " + startLineNum + ", blockStartOffset: " + blockStartOffset + ", endLineNum: " + endLineNum + ", blockEndOffset: " + blockEndOffset + ")");

            document.replace(blockStartOffset, blockEndOffset - blockStartOffset, "");
          }
        }
      }
    }
  }

  public void deleteCurrentTextLine() throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    int lineNum, lineOffset, lineLen;
    IRegion lineRegion;

    logger.debug("Eclipse Utils is attempting to delete the current line of text from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          lineNum = textSelection.getStartLine();

          lineRegion = document.getLineInformation(lineNum);

          lineOffset = lineRegion.getOffset();

          lineLen = lineRegion.getLength();

          logger.debug("Deleting Text At (lineNum: " + lineNum + ", lineOffset: " + lineOffset + ", lineLen: " + lineLen + ")");

          document.replace(lineOffset, lineLen, "");
        }
      }
    }
  }

  public void replaceSelectedBlock(String newTxt) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    IRegion startLineRegion, endLineRegion;
    int startLineNum, blockStartOffset, blockEndOffset, endLineNum;

    logger.debug("Eclipse Utils is attempting to replace the request selected text block from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();
          endLineNum = textSelection.getEndLine();

          if (startLineNum == endLineNum) {
            replaceCurrentLine(newTxt);
          }
          else {
            startLineRegion = document.getLineInformation(startLineNum);

            endLineRegion = document.getLineInformation(endLineNum);

            blockStartOffset = startLineRegion.getOffset();
            blockEndOffset = endLineRegion.getOffset() + endLineRegion.getLength();

            logger.debug("Replacing Text At (startLineNum: " + startLineNum + ", blockStartOffset: " + blockStartOffset + ", endLineNum: " + endLineNum + ", blockEndOffset: " + blockEndOffset
                + ") with New Text: " + newTxt);

            document.replace(blockStartOffset, blockEndOffset - blockStartOffset, newTxt);
          }
        }
      }
    }
  }

  private void replaceCurrentLine(String newTxt) throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    IDocumentProvider documentProvider;
    IDocument document;
    ISelection selection;
    ITextSelection textSelection;
    int lineNum, lineOffset, lineLen;
    IRegion lineRegion;

    logger.debug("Eclipse Utils is attempting to replace the current line of text from the current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        documentProvider = textEditor.getDocumentProvider();
        document = documentProvider.getDocument(textEditor.getEditorInput());

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          lineNum = textSelection.getStartLine();

          lineRegion = document.getLineInformation(lineNum);

          lineOffset = lineRegion.getOffset();

          lineLen = lineRegion.getLength();

          logger.debug("Replacing Text At (lineNum: " + lineNum + ", lineOffset: " + lineOffset + ", lineLen: " + lineLen + ") with New Text: " + newTxt);

          document.replace(lineOffset, lineLen, "");
        }
      }
    }
  }

  public boolean isMultiLinesSelected() throws BadLocationException {
    IEditorPart editor;
    ITextEditor textEditor;
    ISelection selection;
    ITextSelection textSelection;
    int startLineNum, endLineNum;
    boolean multiLinesSelected = false;

    logger.debug("Eclipse Utils is attempting to detect if more than one line is selected in current Text Editor");

    editor = workbenchPage.getActiveEditor();

    if (editor != null) {
      logger.debug("Editor(" + editor.getTitle() + ") is Active");

      if (editor instanceof ITextEditor) {
        logger.debug("Editor is Text Editor(" + editor.getTitle() + ")");
        textEditor = (ITextEditor) editor;

        selection = textEditor.getSelectionProvider().getSelection();

        if (selection instanceof ITextSelection) {
          textSelection = (ITextSelection) selection;

          startLineNum = textSelection.getStartLine();
          endLineNum = textSelection.getEndLine();

          multiLinesSelected = (startLineNum != endLineNum);
        }
      }
    }

    return multiLinesSelected;
  }

  public void openFileWithDefaultApplication(String filePath) throws IlardiSysEclipseException {
    String ext;
    int lastDot;

    lastDot = filePath.lastIndexOf('.');
    ext = filePath.substring(lastDot);

    Program program = Program.findProgram(ext);

    if (program != null) {
      program.execute(filePath);
    }
    else {
      throw new IlardiSysEclipseException("Opening files is not supported on this platform");
    }
  }

  public void openFileWithDefaultApplication(File tmpFile) throws IlardiSysEclipseException {
    openFileWithDefaultApplication(tmpFile.getPath());
  }

}
