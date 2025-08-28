package com.radja.util;

import com.radja.main.Main;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.LoggingFacade;
import java.awt.Desktop;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.DefaultCaret;
import net.miginfocom.swing.MigLayout;

public class About extends JPanel{

    private JLabel imageLogo;
    
    public About() {
        init();
        addThemeChangeListener();
    }

    private void updateLogo() {
        if (FlatLaf.isLafDark()) {
            imageLogo.setIcon(new FlatSVGIcon("com/absensi/icon/logo_qrcode_white.svg", 100, 100));
        } else {
            imageLogo.setIcon(new FlatSVGIcon("com/absensi/icon/logo_qrcode.svg", 100, 100));
        }

        imageLogo.revalidate();
        imageLogo.repaint();
    }

     private void addThemeChangeListener() {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    updateLogo(); 
                }
            }
        });
    }   
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 5 30 5 30,width 400", "[fill,330::]", ""));

        imageLogo = new JLabel();
        updateLogo();
        
        JTextPane title = createText("Attendance Management System");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");

        JTextPane description = createText("");
        description.setContentType("text/html");
        description.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    showUrl(e.getURL());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        add(imageLogo);
        add(title);
        add(description);
        add(createSystemInformation());
        add(createLibraryInformation());
    }

    private JTextPane createText(String text) {
        JTextPane textPane = new JTextPane();
        textPane.setBorder(BorderFactory.createEmptyBorder());
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
            }
        });
        return textPane;
    }

    

    private String getSystemInformationText() {
        String text = "<b>App Version: </b>%s<br/>" +
                "<b>Java: </b>%s<br/>" +
                "<b>MySQL: </b>%s<br/>" +
                "<b>System: </b>%s<br/>"; 

        return text;
    }

     private String getLibraryInformationText() {
        String text = "<div style=\"padding-left: 10px;\">" +
                    "  <b>• MySQL Connector</b><br>\n" +
                    "  <b>• JBcrypt</b><br>\n" +
                    "  <b>• JasperReport</b><br>\n" +
                    "  <b>• JFreeChart</b><br>\n" +
                    "  <b>• DatePicker</b><br>\n" +
                    "  <b>• QR Code Scanner</b><br>\n" +
                    "  <b>• Webcam Capture</b><br>\n" +
                    "  <b>• FlatLaf</b><br>\n" +
                    "  <b>• MigLayout</b><br> </div>";
        return text;
    }
    
    private JComponent createSystemInformation() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("System Information"));
        JTextPane textPane = createText("");
        textPane.setContentType("text/html");
        String version = Main.APP_VERSION;
        String java = System.getProperty("java.vendor") + " - v" + System.getProperty("java.version");
        String system = System.getProperty("os.name") + " " + System.getProperty("os.arch") + " - v" + System.getProperty("os.version");
        String mysqlVersion = getMySQLVersion();
        
        String text = String.format(getSystemInformationText(),
                version,
                java,
                mysqlVersion,
                system);
        textPane.setText(text);
        panel.add(textPane);
        return panel;
    }
    
    private String getMySQLVersion() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myabsensidb", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                return rs.getString(1); // Mengambil versi MySQL
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Not Connected";
    }
    
    private JComponent createLibraryInformation() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Libraries Used"));

        JTextPane textPane = createText("");
        textPane.setContentType("text/html");
        String text = getLibraryInformationText();
        textPane.setText(text);

        textPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                try {
                    showUrl(e.getURL());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panel.add(textPane);
        return panel;
    }


    private void showUrl(URL url) throws URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(url.toURI());
                } catch (IOException | URISyntaxException e) {
                    LoggingFacade.INSTANCE.logSevere("Error browse url", e);
                }
            }
        }
    }
}
