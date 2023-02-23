import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlParser {
    public void parse(String filepath) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(filepath);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("city");

        for(int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);

            if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element currentElement = (Element) currentNode;

                currentElement.getElementsByTagName("name").item(0).getTextContent();
                Integer.parseInt(currentElement.getElementsByTagName("year").item(0).getTextContent());
                currentElement.getElementsByTagName("month").item(0).getTextContent();
            }

            System.out.println("---------------");
        }
    }
}
