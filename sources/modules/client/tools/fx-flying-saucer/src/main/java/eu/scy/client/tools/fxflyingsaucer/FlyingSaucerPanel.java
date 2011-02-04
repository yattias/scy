/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xhtmlrenderer.event.DocumentListener;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.util.GeneralUtil;

/**
 *
 * @author sikkenj
 */
public class FlyingSaucerPanel extends javax.swing.JPanel
{

   private static final Logger logger = Logger.getLogger(FlyingSaucerPanel.class);

   /** Creates new form SwingBrowserPanel */
   public FlyingSaucerPanel(boolean showNavigationButtons)
   {
      initComponents(showNavigationButtons);
      //setHomeUrl("http://www.scy-lab.eu/xhtml/borders.xhtml");
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   private void initComponents(boolean showNavigationButtons)
   {
      previousButton = new javax.swing.JButton();
      nextButton = new javax.swing.JButton();
      homeButton = new javax.swing.JButton();
      urlField = new javax.swing.JTextField();
      loadButton = new javax.swing.JButton();
//      browserScrollPane = new FSScrollPane();
      browserScrollPane = new JScrollPane();
      System.getProperties().setProperty("xr.use.listeners", "false");
      uriManager = new UriManager(this);
      browser = new MyXhtmlPanel(uriManager, true);
      System.getProperties().setProperty("xr.use.listeners", "true");
      browser.addDocumentListener(new DocumentListener()
      {

         long startNanos = -1;

         @Override
         public void documentStarted()
         {
//            // System.out.println("Start loading document: " + browser.getURL());
            startNanos = System.nanoTime();
         }

         @Override
         public void documentLoaded()
         {
            if (startNanos > 0)
            {
               long usedNanos = System.nanoTime() - startNanos;
               long usedMillis = usedNanos / 1000000;
               logger.info("Loaded page in " + usedMillis + " msec, url: " + browser.getURL());
               startNanos = -1;
            }
//            // System.out.println("Loaded document: \n- url:" + browser.getURL() + "\n- title:" + browser.getDocumentTitle());
            if (urlFieldIsTitle)
            {
               urlField.setText(browser.getDocumentTitle());
            }
            else
            {
               urlField.setText(browser.getURL().toString());
            }
         }

         @Override
         public void onLayoutException(Throwable e)
         {
//            System.err.println("Exception during layout in: \n- url:" + browser.getURL() + "\n- exception:" + e.getMessage());
            logger.debug("onLayoutException during " + browser.getURL(), e);
//            e.printStackTrace(System.err);
         }

         @Override
         public void onRenderException(Throwable e)
         {
//            System.err.println("Exception during rendering in: \n- url:" + browser.getURL() + "\n- exception:" + e.getMessage());
            logger.debug("onRenderException during " + browser.getURL(), e);
//            e.printStackTrace(System.err);
         }
      });

      //previousButton.setText("<-");
      previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/leftArrow.png"))); // NOI18N
      previousButton.setEnabled(false);
      previousButton.addActionListener(new java.awt.event.ActionListener()
      {

         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            previousButtonActionPerformed(evt);
         }
      });

      //nextButton.setText("->");
      nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/rightArrow.png"))); // NOI18N
      nextButton.setEnabled(false);
      nextButton.addActionListener(new java.awt.event.ActionListener()
      {

         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            nextButtonActionPerformed(evt);
         }
      });

      //homeButton.setText("H");
      homeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/home.png"))); // NOI18N
      homeButton.setEnabled(false);
      if (homeUrl == null)
      {
         homeButton.setToolTipText("Save as ELO");
      }
      homeButton.addActionListener(new java.awt.event.ActionListener()
      {

         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            homeButtonActionPerformed(evt);
         }
      });

      urlField.setText("");
      urlField.addKeyListener(new java.awt.event.KeyAdapter()
      {

         @Override
         public void keyTyped(java.awt.event.KeyEvent evt)
         {
            urlFieldKeyTyped(evt);
         }
      });

      loadButton.setText("Load");
      loadButton.addActionListener(new java.awt.event.ActionListener()
      {

         @Override
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            loadButtonActionPerformed(evt);
         }
      });

      browserScrollPane.setViewportView(browser);

      if (showNavigationButtons)
      {
         org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
         this.setLayout(layout);
         layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(previousButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(nextButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(homeButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(urlField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)).add(browserScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
         layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(previousButton).add(nextButton).add(homeButton).add(urlField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(browserScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)));
      }
      else
      {
         this.setLayout(new BorderLayout());
         this.add(browserScrollPane, BorderLayout.CENTER);
      }
   }// </editor-fold>

   private void previousButtonActionPerformed(java.awt.event.ActionEvent evt)
   {
      loadUrl(uriManager.getPrevious());
   }

   private void nextButtonActionPerformed(java.awt.event.ActionEvent evt)
   {
      loadUrl(uriManager.getNext());
   }

   private void homeButtonActionPerformed(java.awt.event.ActionEvent evt)
   {
      if (homeUrl == null)
      {
         if (isPageLoaded)
         {
            try
            {
               saveUrlAsHome(new URL(enteredUrlString));
            }
            catch (MalformedURLException ex)
            {
               logger.warn("title field (" + urlField.getText() + ") does not contain a valid url, " + ex.getMessage());
            }
         }
         else
         {
            logger.info("can't save url, because it is not loaded");
         }
      }
      else
      {
         loadUrl(homeUrl);
      }
   }

   private void loadButtonActionPerformed(java.awt.event.ActionEvent evt)
   {
      loadUrl(urlField.getText());
   }

   private void urlFieldKeyTyped(java.awt.event.KeyEvent evt)
   {
//       // System.out.println(evt.getKeyCode() + ", " + evt.getKeyText(evt.getKeyCode()) + ", " + (int)evt.getKeyChar());
      if (((int) evt.getKeyChar() == 10) && !urlFieldIsTitle)
      {
         enteredUrlString = urlField.getText();
         loadUrl(urlField.getText());
      }
   }
   // Variables declaration - do not modify
   private UriManager uriManager;
   private MyXhtmlPanel browser;
   private JScrollPane browserScrollPane;
   private javax.swing.JButton previousButton;
   private javax.swing.JButton nextButton;
   private javax.swing.JButton homeButton;
   private javax.swing.JButton loadButton;
   private javax.swing.JTextField urlField;
   // End of variables declaration
   private final ResourceBundleWrapper resourceBundleWrapper = new ResourceBundleWrapper(this);
   private String homeUrl = null;
   private boolean isPageLoaded = false;
   private boolean urlFieldIsTitle = false;
   private boolean urlFieldIsTitleBeforeLoading = false;
   private boolean errorInPage = false;
   private String enteredUrlString = null;

   public void setHomeUrl(String homeUrl)
   {
      this.homeUrl = homeUrl;
      boolean urlIsNotEmpty = homeUrl != null && homeUrl.length() > 0;
      homeButton.setEnabled(urlIsNotEmpty);
      if (urlIsNotEmpty)
      {
         loadUrl(homeUrl);
         homeButton.setToolTipText("");
      }
   }

   protected void saveUrlAsHome(URL url)
   {
      String newHomeUrl = null;
      if (url != null)
      {
         newHomeUrl = url.toString();
      }
      setHomeUrl(newHomeUrl);
   }

   public void loadUrl(final String url)
   {
      urlFieldIsTitleBeforeLoading = urlFieldIsTitle;
      errorInPage = false;
      try
      {
         urlFieldIsTitle = true;
         // loading the document in the background causes some time a nullpointer exception
         // it seems to happen only for certain documents, one such document is:
         // http://www.scy-lab.eu/content/en/mission1/LAS_Conceptualization_design/Assignments/A_First_ideas.html
         // the exception occurs at org.xhtmlrenderer.context.StyleReference:199
         // for some reason org.xhtmlrenderer.css.newmatch.Matcher _matcher is null
         //
//         Thread t = new Thread() {
//
//                @Override
//                public void run() {
//                    browser.setDocument(url);
//                }
//
//         };
//         t.start();
         browser.setDocument(url);
         if (!errorInPage)
         {
            isPageLoaded = true;
            urlField.setEditable(false);
            homeButton.setEnabled(true);
         }

      }
      catch (Exception e)
      {
         // the error should allready be handled
         logger.info("An exception occured while loading '" + url + "', " + e.getMessage(), e);
         //handlePageLoadFailed(url, e);
         urlFieldIsTitle = urlFieldIsTitleBeforeLoading;
      }
   }

   public void updatePreviousNextButtonState()
   {
      if (urlFieldIsTitle)
      {
         homeButton.setEnabled(uriManager.hasPrevious() || uriManager.hasNext());
      }
      previousButton.setEnabled(uriManager.hasPrevious());
      nextButton.setEnabled(uriManager.hasNext());
   }

   public void clearHistory()
   {
      uriManager.clear();
   }

   public void setShowMoreInfo(ShowMoreInfo showMoreInfo)
   {
      browser.setShowMoreInfo(showMoreInfo);
   }

   public void setEloUri(URI eloUri)
   {
      browser.setEloUri(eloUri);
   }

   private String getXhtmlErrorText(URL url, String errorMessage)
   {
      StringBuilder builder = new StringBuilder();
      builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      builder.append("<!DOCTYPE html PUBLIC");
      builder.append("  \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
      builder.append("  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
      builder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
      builder.append(" <head>");
      builder.append("  <title>An error occured</title>");
      builder.append(" </head>");
      builder.append(" <body>");
      builder.append("  <p>url:");
      builder.append(url.toString());
      builder.append("  </p>");
      builder.append("  <p>error::");
      builder.append(errorMessage);
      builder.append("  </p>");
      builder.append(" </body>");
      builder.append("</html>");
      return builder.toString();
   }

   public void handlePageLoadFailed(String url_text, Exception ex)
   {
      //logger.info("show error for url: " + url_text, ex);
      logger.info("show error for url: " + url_text + " : " + ex.getMessage());
      urlFieldIsTitle = urlFieldIsTitleBeforeLoading;
      errorInPage = true;
      isPageLoaded = false;
      final XMLResource xr;
      final String rootCause = getRootCause(ex);
      final String msg = GeneralUtil.escapeHTML(addLineBreaks(rootCause, 80));
      final String triedUri = url_text;
      final String httpResult = getHttpResult(url_text);
      String httpResultError = "";
      if (httpResult != null)
      {
         httpResultError = "<p>" + resourceBundleWrapper.getString("loadFailed.httpResultCode") + "</p>\n"
            + "<pre>" + httpResult + "</pre>\n";
      }
      String notFound =
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
         + "<!DOCTYPE html PUBLIC \" -//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
         + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
         + "<head><title>"
         + resourceBundleWrapper.getString("loadFailed.htmlTitle")
         + "</title></head>\n"
         + "<body>\n"
         + "<h1>"
         + resourceBundleWrapper.getString("loadFailed.contentTitle")
         + "</h1>\n"
         + "<p>"
         + resourceBundleWrapper.getString("loadFailed.couldNotLoadPageAt")
         + "</p>\n"
         + "<pre>" + GeneralUtil.escapeHTML(url_text) + "</pre>\n"
         + "<p>"
         + resourceBundleWrapper.getString("loadFailed.errorMessage")
         + "</p>\n"
         + "<pre>" + msg + "</pre>\n"
         + httpResultError
         + "</body>\n"
         + "</html>";
      logger.debug("error message xhtml:\n" + notFound);
      xr = XMLResource.load(new StringReader(notFound));
      Document doc = xr.getDocument();
      SwingUtilities.invokeLater(new Runnable()
      {

         @Override
         public void run()
         {
            browser.setDocument(xr.getDocument(), triedUri);
         }
      });
   }

   private String getRootCause(Exception ex)
   {
      String lastNotNullExceptionMessage = ex.getMessage();
      Throwable lastCause = ex;
      Throwable cause = ex;
      while (cause != null)
      {
         lastCause = cause;
         if (!isEmpty(cause.getMessage()))
         {
            lastNotNullExceptionMessage = cause.getMessage();
         }
         cause = cause.getCause();
      }
//      String exceptionMessage = cause == null ? ex.getMessage() : cause.getMessage();
//      if (exceptionMessage != null)
//      {
//         return exceptionMessage;
//      }
      if (isEmpty(lastNotNullExceptionMessage))
      {
         return lastCause.getClass().getName();
      }
//      if (lastNotNullExceptionMessage.equals("Stream closed"))
//      {
//         lastNotNullExceptionMessage += " (file does not exists?)";
//      }
      return lastNotNullExceptionMessage;
   }

   private String getHttpResult(String urlText)
   {
      String httpResult = null;
      try
      {
         URL url = new URL(urlText);
         URLConnection urlLConnection = url.openConnection();
         urlLConnection.connect();
         httpResult = urlLConnection.getHeaderField(null);
         Map<String, List<String>> headerFields = urlLConnection.getHeaderFields();
         for (Map.Entry<String, List<String>> entry : headerFields.entrySet())
         {
            // System.out.println(entry.getKey() + " -> " + entry.getValue());
         }
      }
      catch (IOException ex1)
      {
         logger.debug("failed to get http result code, " + ex1.getMessage());
      }
      return httpResult;
   }

   private boolean isEmpty(String string)
   {
      return string == null || string.length() == 0;
   }

   private String addLineBreaks(String _text, int maxLineLength)
   {
      if (_text == null)
      {
         return "";
      }
      StringBuffer broken = new StringBuffer(_text.length() + 10);
      boolean needBreak = false;
      for (int i = 0; i < _text.length(); i++)
      {
         if (i > 0 && i % maxLineLength == 0)
         {
            needBreak = true;
         }

         final char c = _text.charAt(i);
         if (needBreak && Character.isWhitespace(c))
         {
            // System.out.println("Breaking: " + broken.toString());
            needBreak = false;
            broken.append('\n');
         }
         else
         {
            broken.append(c);
         }
      }
//      // System.out.println("Broken! " + broken.toString());
      return broken.toString();
   }

   @Override
   public Dimension getPreferredSize()
   {
      Dimension preferedSize = super.getPreferredSize();
      preferedSize.height = Math.max(preferedSize.height, 100);
      return preferedSize;
   }
}
