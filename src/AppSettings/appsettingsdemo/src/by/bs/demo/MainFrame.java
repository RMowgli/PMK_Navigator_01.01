package AppSettings.appsettingsdemo.src.by.bs.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
/**
 *@author     Siarhej Berdachuk
 *@created    Jan 2004.
 *@version    1.0
 */
public final class MainFrame extends JFrame {

  public MainFrame() {
    try {
      jbInit();
      loadSettings();
      updateState();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  private void jbInit() throws Exception {
    this.setJMenuBar(menuBar);
    this.getContentPane().setLayout(layoutMain);

    this.setSize(new Dimension(390, 221));
    this.addComponentListener(new MainFrame_this_componentAdapter(this));
    panelCenter.setLayout(flowLayout1);
    panelCenter.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    windowsButton.setActionCommand(windowsClassName);
    motifButton.setActionCommand(motifClassName);
    metalButton.setActionCommand(metalClassName);
    // Group the radio buttons.
    group.add(metalButton);
    group.add(motifButton);
    group.add(windowsButton);
    // Register a listener for the radio buttons.
    RadioListener radioListener = new RadioListener();
    metalButton.addActionListener(radioListener);
    motifButton.addActionListener(radioListener);
    windowsButton.addActionListener(radioListener);

    mmFile.setText("File");
    mmiExit.setText("Exit");
    mmiExit.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          fileExit_ActionPerformed(ae);
        }
      });
    mmHelp.setText("Help");
    mmiHelp.setText("About");
    mmiHelp.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          helpAbout_ActionPerformed(ae);
        }
      });
    statusBar.setText("...");
    btnLoadSettings.setIcon(imgOpen);

    btnSaveSettings.setIcon(imgSave);

    mmFile.add(mmiLoadSettings);
    mmFile.add(mmiSaveSettings);
    mmFile.add(mmiExit);

    menuBar.add(mmFile);
    mmHelp.add(mmiHelp);
    menuBar.add(mmHelp);
    toolBar.add(btnLoadSettings);
    toolBar.add(btnSaveSettings);
    this.getContentPane().add(toolBar, BorderLayout.NORTH);
    panelCenter.add(windowsButton, null);
    panelCenter.add(motifButton, null);
    panelCenter.add(metalButton, null);
    this.getContentPane().add(panelCenter, BorderLayout.CENTER);
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);

  }


  private static void fileExit_ActionPerformed(ActionEvent e) {
    System.exit(0);
  }


  void helpAbout_ActionPerformed(ActionEvent e) {
    // JOptionPane.showMessageDialog(this, new MainFrame_AboutBoxPanel1(), "About", JOptionPane.PLAIN_MESSAGE);
  }


  public void updateState() {
    String lnfName = UIManager.getLookAndFeel().getClass().getName();
    if (lnfName.indexOf(metalStr) >= 0) {
      metalButton.setSelected(true);
    } else if (lnfName.indexOf(windowsStr) >= 0) {
      windowsButton.setSelected(true);
    } else if (lnfName.indexOf(motifStr) >= 0) {
      motifButton.setSelected(true);
    } else {
      System.err.println("Unknown L&F: " + lnfName);
    }
    if (AppSettings.get(LF_KEY) != lnfName) {
      AppSettings.put(LF_KEY, lnfName);
    }

    String w = new Integer(getWidth()).toString();
    if (AppSettings.get(WIDTH_KEY) != w) {
      AppSettings.put(WIDTH_KEY, w);
    }

    String h = new Integer(getHeight()).toString();
    if (AppSettings.get(HEIGHT_KEY) != h) {
      AppSettings.put(HEIGHT_KEY, h);
    }
  }


  private void saveSettings() {
    //String propDir = System.getProperty("user.home") + "/.appropdemo";
    try {
      statusBar.setText("saving ...");
      String propDir = "./";
      File file = new File(propDir, "settings.xml");
      try {
        AppSettings.save(file);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } finally {
      statusBar.setText("...");
    }
  }


  private void loadSettings() {
    try {
      statusBar.setText("loading...");
      //String propDir = System.getProperty("user.home") + "/.appropdemo";

      File file = new File(propDir, "settings.xml");
      try {
        AppSettings.clear();
        AppSettings.load(file);
        String lnfName = UIManager.getLookAndFeel().getClass().getName();
        if (AppSettings.get(LF_KEY, lnfName) != lnfName) {
          UIManager.setLookAndFeel(
              (String) AppSettings.get(LF_KEY, lnfName));
          SwingUtilities.updateComponentTreeUI(MainFrame.this);
        }

        this.setSize(new Dimension(
            AppSettings.getInt(WIDTH_KEY, getWidth()),
            AppSettings.getInt(HEIGHT_KEY, getHeight())
            ));
      } catch (Exception e) {
        e.printStackTrace();
      }
    } finally {
      statusBar.setText("...");
    }
  }


  void this_componentResized(ComponentEvent e) {
    updateState();
  }


  final class RadioListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String lnfName = e.getActionCommand();

      try {
        UIManager.setLookAndFeel(lnfName);
        SwingUtilities.updateComponentTreeUI(MainFrame.this);
        updateState();
      } catch (Exception exc) {
        JRadioButton button = (JRadioButton) e.getSource();
        button.setEnabled(false);
        updateState();
        System.err.println("Could not load LookAndFeel: " + lnfName);
      }

    }
  }


  private ImageIcon imgSave = new ImageIcon(MainFrame.class.getResource("savefile.gif"));
  private ImageIcon imgOpen = new ImageIcon(MainFrame.class.getResource("openfile.gif"));

  private Action actSaveSettings =
    new AbstractAction("Save settings", imgSave) {
      public void actionPerformed(ActionEvent e) {
        saveSettings();
      }
    };

  private Action actLoadSettings =
    new AbstractAction("Load settings", imgOpen) {
      public void actionPerformed(ActionEvent e) {
        loadSettings();
      }
    };

  private JButton btnSaveSettings = new JButton(actSaveSettings);
  private JButton btnLoadSettings = new JButton(actLoadSettings);
  private JToolBar toolBar = new JToolBar();
  private JLabel statusBar = new JLabel();
  private JMenuItem mmiHelp = new JMenuItem();
  private JMenu mmHelp = new JMenu();
  private JMenuItem mmiExit = new JMenuItem();
  private JMenuItem mmiLoadSettings = new JMenuItem(actLoadSettings);
  private JMenuItem mmiSaveSettings = new JMenuItem(actSaveSettings);
  private JMenu mmFile = new JMenu();
  private JMenuBar menuBar = new JMenuBar();
  private JPanel panelCenter = new JPanel();
  private BorderLayout layoutMain = new BorderLayout();
  private FlowLayout flowLayout1 = new FlowLayout();
  private final static String metalStr = "Metal";
  private final static String metalClassName = "javax.swing.plaf.metal.MetalLookAndFeel";

  private final static String motifStr = "Motif";
  private final static String motifClassName =
      "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

  private final static String windowsStr = "Windows";
  private final static String windowsClassName =
      "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

  ButtonGroup group = new ButtonGroup();
  JRadioButton windowsButton = new JRadioButton(windowsStr);
  JRadioButton motifButton = new JRadioButton(motifStr);
  JRadioButton metalButton = new JRadioButton(metalStr);
  private final static String LF_KEY = "LookAndFeel";
  private final static String WIDTH_KEY = "MainFrame.width";
  private final static String HEIGHT_KEY = "MainFrame.height";
  private final static String propDir = "./";
}

class MainFrame_this_componentAdapter extends java.awt.event.ComponentAdapter {


  MainFrame_this_componentAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }


  public void componentResized(ComponentEvent e) {
    adaptee.this_componentResized(e);
  }


  MainFrame adaptee;
}
