package at.theOnlyHorst.tetrij.engine;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    private ResourceManager(){
    }

    private static Map<String,String> shaders;
    //TODO implement Textures List



    public static void initResManager() throws ParserConfigurationException, IOException, SAXException {
        shaders = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(ResourceManager.class.getClassLoader().getResourceAsStream("resources.xml"));

        Element root = doc.getDocumentElement();

        Node shadersN = root.getElementsByTagName("Shaders").item(0);

        readResourceTypeDOM(shaders,shadersN);


    }

    private static void readResourceTypeDOM(Map<String,String> resultMap,Node parent)
    {
        if (parent.getNodeType() == Node.ELEMENT_NODE) {
            Element parentE = (Element) parent;
            String parentPath = parentE.getAttribute("path");
            NodeList parentChildList = parentE.getElementsByTagName("Resource");
            for(int i =0;i<parentChildList.getLength();i++)
            {
                Node current = parentChildList.item(i);
                if(current.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element currentE = (Element) current;
                    String name = currentE.getAttribute("name");
                    String pathE = currentE.getAttribute("path");

                    resultMap.put(name,readResFileString(parentPath+pathE));
                }
            }

        }
    }


    private static String readResFileString(String path)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(ResourceManager.class.getClassLoader().getResourceAsStream(path)));
        String line;
        String combined = "";
        try {
            line = br.readLine();
            while (line != null)
            {
                combined += line + "\n";
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return combined;
    }


    public static String getShader(String name)
    {
        return shaders.get(name);
    }


}
