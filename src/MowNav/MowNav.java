/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MowNav;

import java.io.File;
import javax.swing.UIManager;

import MowNav.Settings.Worker;

/**
 *
 * @author mowgli
 */
public class MowNav {

    // Директория хранения файла настроек
    private final static String propDir = System.getProperty("user.home") + "/.appropdemo";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Загружаем данные из файла настроек
        try {
            loadSettings();
            saveSettings();
        } catch (Exception e) {
        }
    }

    private static void saveSettings() {

        try {
            File file = new File(propDir, "settings.xml");
            try {
                Worker.save(file);
            } catch (Exception e) {
            }
        } finally {
            // statusBar.setText("...");
        }
    }

    private static void loadSettings() {
        try {
            File file = new File(propDir, "settings.xml");
            try {
                Worker.clear();
                Worker.load(file);
            } catch (Exception e) {
            }
        } finally {
            // statusBar.setText("...");
        }
    }

}
