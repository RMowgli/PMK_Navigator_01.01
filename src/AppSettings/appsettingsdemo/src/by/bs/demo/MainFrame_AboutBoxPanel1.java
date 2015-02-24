package by.bs.demo;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Dimension;

public class MainFrame_AboutBoxPanel1 extends JPanel {

    public MainFrame_AboutBoxPanel1() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void jbInit() throws Exception {
        this.setLayout(layoutMain);
        this.setBorder(border);
        this.setSize(new Dimension(292, 212));
        jLabel1.setText("Application properties demo");
        this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }


    private Border border = BorderFactory.createEtchedBorder();
    private GridBagLayout layoutMain = new GridBagLayout();
    private JLabel jLabel1 = new JLabel();

}
