/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer;

import java.awt.event.ActionEvent;
import java.io.StringReader;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.event.DocumentListener;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.util.GeneralUtil;

/**
 *
 * @author sikkenj
 */
public class FlyingSaucerPanel extends javax.swing.JPanel
{

   private static final Logger logger = Logger.getLogger(FlyingSaucerPanel.class);

   /** Creates new form SwingBrowserPanel */
   public FlyingSaucerPanel()
   {
      initComponents();
      setHomeUrl("http://www.scy-lab.eu/xhtml/borders.xhtml");
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   private void initComponents()
   {

      homeButton = new javax.swing.JButton();
      urlField = new javax.swing.JTextField();
      loadButton = new javax.swing.JButton();
      browserScrollPane = new FSScrollPane();
      System.getProperties().setProperty("xr.use.listeners", "false");
      browser = new MyXhtmlPanel(new BrowserAgentCallback(this),true);
      System.getProperties().setProperty("xr.use.listeners", "true");
      browser.addDocumentListener(new DocumentListener()
      {

         @Override
         public void documentStarted()
         {
//            System.out.println("Start loading document: " + browser.getURL());
         }

         @Override
         public void documentLoaded()
         {
//            System.out.println("Loaded document: \n- url:" + browser.getURL() + "\n- title:" + browser.getDocumentTitle());
            urlField.setText(browser.getURL().toString());
         }

         @Override
         public void onLayoutException(Throwable e)
         {
//            System.err.println("Exception during layout in: \n- url:" + browser.getURL() + "\n- exception:" + e.getMessage());
            e.printStackTrace(System.err);
         }

         @Override
         public void onRenderException(Throwable e)
         {
//            System.err.println("Exception during rendering in: \n- url:" + browser.getURL() + "\n- exception:" + e.getMessage());
            e.printStackTrace(System.err);
         }
      });

      homeButton.setText("Home");
      homeButton.setEnabled(false);
      homeButton.setToolTipText("Press control to save url");
      homeButton.addActionListener(new java.awt.event.ActionListener()
      {

         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            homeButtonActionPerformed(evt);
         }
      });

      urlField.setText("");
      urlField.addKeyListener(new java.awt.event.KeyAdapter()
      {

         public void keyTyped(java.awt.event.KeyEvent evt)
         {
            urlFieldKeyTyped(evt);
         }
      });

      loadButton.setText("Load");
      loadButton.addActionListener(new java.awt.event.ActionListener()
      {

         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            loadButtonActionPerformed(evt);
         }
      });

      browserScrollPane.setViewportView(browser);

      org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(homeButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(urlField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(loadButton)).add(browserScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
      layout.setVerticalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(homeButton).add(urlField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(loadButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(browserScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)));
   }// </editor-fold>

   private void homeButtonActionPerformed(java.awt.event.ActionEvent evt)
   {
      if ((evt.getModifiers() & ActionEvent.CTRL_MASK) != 0)
      {
         if (isPageLoaded)
         {
            saveUrlAsHome(browser.getURL());
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
//       System.out.println(evt.getKeyCode() + ", " + evt.getKeyText(evt.getKeyCode()) + ", " + (int)evt.getKeyChar());
      if ((int) evt.getKeyChar() == 10)
      {
         loadUrl(urlField.getText());
      }
   }
   // Variables declaration - do not modify
   private XHTMLPanel browser;
   private FSScrollPane browserScrollPane;
   private javax.swing.JButton homeButton;
   private javax.swing.JButton loadButton;
   private javax.swing.JTextField urlField;
   // End of variables declaration
   private String homeUrl = null;
   private boolean isPageLoaded = false;

   public void setHomeUrl(String homeUrl)
   {
      this.homeUrl = homeUrl;
      boolean urlIsNotEmpty = homeUrl != null && homeUrl.length() > 0;
      homeButton.setEnabled(urlIsNotEmpty);
      if (urlIsNotEmpty)
      {
         loadUrl(homeUrl);
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

   public void loadUrl(String url)
   {
      try
      {
         browser.setDocument(url);
         isPageLoaded = true;
      }
      catch (Exception e)
      {
         System.out.println("An exception occured while loading '" + url + "', " + e.getMessage());
         handlePageLoadFailed(url, e);
//         showErrorMessage(e.getMessage());
      }
   }

   private void XXshowErrorMessage(String errorMessage)
   {
//      browser.setDocumentFromString(getXhtmlErrorText(browser.getURL(), errorMessage),browser.getURL().toString(), null);
      String dialoTitle = "An error occured";
      String dialogContent = "An error occured.\nURL: " + browser.getURL().toString() + "\nError: " + errorMessage;
      JOptionPane.showMessageDialog(browser, dialogContent, dialoTitle, JOptionPane.ERROR_MESSAGE);
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
      logger.info("show error for url: " + url_text,ex);
      isPageLoaded = false;
      final XMLResource xr;
      final String rootCause = getRootCause(ex);
      final String msg = GeneralUtil.escapeHTML(addLineBreaks(rootCause, 80));
      String notFound =
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<!DOCTYPE html PUBLIC \" -//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
         "<head><title>Document can't be loaded</title></head>\n" +
         "<body>\n" +
         "<h1>Document can't be loaded</h1>\n" +
         "<p>Could not load the page at </p>\n" +
         "<pre>" + GeneralUtil.escapeHTML(url_text) + "</pre>\n" +
         "<p>The page failed to load; the error was </p>\n" +
         "<pre>" + msg + "</pre>\n" +
         "</body>\n" +
         "</html>";
      logger.debug("error message xhtml:\n" + notFound);
      xr = XMLResource.load(new StringReader(notFound));
      SwingUtilities.invokeLater(new Runnable()
      {

         public void run()
         {
            browser.setDocument(xr.getDocument(), null);
         }
      });
   }

   private String getRootCause(Exception ex)
   {
      Throwable cause = ex;
      while (cause != null)
      {
         cause = cause.getCause();
      }

      return cause == null ? ex.getMessage() : cause.getMessage();
   }

   private String addLineBreaks(String _text, int maxLineLength)
   {
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
            System.out.println("Breaking: " + broken.toString());
            needBreak = false;
            broken.append('\n');
         }
         else
         {
            broken.append(c);
         }
      }
      System.out.println("Broken! " + broken.toString());
      return broken.toString();
   }
}
