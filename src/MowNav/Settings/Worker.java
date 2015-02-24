/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MowNav.Settings;


import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author Victor
 */
public class Worker {

    /**
     * Блок инициализации статических членов класса Создаем единственный
     * экземпляр данного объекта в статической переменной SINGLETON
     */
    private HashMap fHashMap;
    private static Worker SINGLETON;

    static {
        SINGLETON = new Worker();
    }

    /**
     * Конструктор класа
     */
    private Worker() {
        fHashMap = new HashMap();
    }

    // Извлечение объекта из коллекции
    public static Object get(String key) {
        return SINGLETON.fHashMap.get(key);
    }

    // Извлечение объекта из коллекции
    // при отсутствии данных возвращается значение по умолчанию
    public static Object get(String key, Object deflt) {
        Object obj = SINGLETON.fHashMap.get(key);
        if (obj == null) {
            return deflt;
        } else {
            return obj;
        }
    }

    // Для упрощения извлечения данных типа int
    public static int getInt(String key, int deflt) {
        Object obj = SINGLETON.fHashMap.get(key);
        if (obj == null) {
            return deflt;
        } else {
            return new Integer((String) obj).intValue();
        }
    }

    // Добавление объекта в коллекцию
    // Делаем проверку нулевых значений т.к. HashMap() допускает их
    public static void put(String key, Object data) {
        //prevent null values. Hasmap allow them
        if (data == null) {
            throw new IllegalArgumentException();
        } else {
            SINGLETON.fHashMap.put(key, data);
        }
    }

    // Очистка, вызывается только, когда будет повторно
    // использоваться HashMap с приблизительно тем-же количеством 
    // записей во избежании безполезного изменения размера HashMap. 
    public static void clear() {
        SINGLETON.fHashMap.clear();
    }

    // Сохраняем коллекцию в xml файл.
    // Данная структура позволяет хранить не только ключ/значение,
    // но и другие типы объектов. Класс сериализации DOM дерева 
    // позаимствован Brett McLaughlin “Java& XML, 2nd Edition”
    public static boolean save(File file) throws Exception {
        // Создаем новое DOM дерево
        DOMImplementation domImpl = new DOMImplementationImpl();
        Document doc = domImpl.createDocument(null, "app-settings", null);
        Element root = doc.getDocumentElement();
        Element propertiesElement = doc.createElement("properties");
        root.appendChild(propertiesElement);
        Set set = SINGLETON.fHashMap.keySet();
        if (set != null) {
            for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                String key = iterator.next().toString();
                Element propertyElement = doc.createElement("property");
                propertyElement.setAttribute("key", key);
                Text nameText = doc.createTextNode(get(key).toString());
                propertyElement.appendChild((Node) nameText);
                propertiesElement.appendChild(propertyElement);
            }
        }
        
        // Сериализируем DOM дерево в файл
        DOMSerializer serializer = new DOMSerializer();
        serializer.serialize(doc, file);
        return true;
    }

    // Чтение данных в коллекцию обратной операцией 
    public static boolean load(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        if (doc == null) {
            throw new NullPointerException();
        }
        NodeList propertiesNL = doc.getDocumentElement().getChildNodes();
        if (propertiesNL != null) {
            for (int i = 0; (i < propertiesNL.getLength()); i++) {
                if (propertiesNL.item(i).getNodeName().equals("properties")) {
                    NodeList propertyList = propertiesNL.item(i).getChildNodes();
                    for (int j = 0; j < propertyList.getLength(); j++) {
                        NamedNodeMap attributes = propertyList.item(j).getAttributes();
                        if (attributes != null) {
                            Node n = attributes.getNamedItem("key");
                            NodeList childs = propertyList.item(j).getChildNodes();
                            if (childs != null) {
                                for (int k = 0; k < childs.getLength(); k++) {
                                    if (childs.item(k).getNodeType() == Node.TEXT_NODE) {
                                        put(n.getNodeValue(), childs.item(k).getNodeValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}

/* Пример использования:
 *
 * File file = new File(propDir, "settings.xml");
 * try {
 *   AppSettings.clear();
 *   AppSettings.load(file);
 *   String lnfName = UIManager.getLookAndFeel().getClass().getName();
 *   if (AppSettings.get(LF_KEY, lnfName) != lnfName) {
 *     UIManager.setLookAndFeel(
 *     (String) AppSettings.get(LF_KEY, lnfName));
 *     SwingUtilities.updateComponentTreeUI(MainFrame.this);
 *   }
 * 
 *   this.setSize(new Dimension(
 *   AppSettings.getInt(WIDTH_KEY, getWidth()),
 *   AppSettings.getInt(HEIGHT_KEY, getHeight())
 *   ));
 * } catch (Exception e) {
 *   e.printStackTrace();
 * }
 *
 */
