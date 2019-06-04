package at.theOnlyHorst.tetrij.engine;

import at.theOnlyHorst.tetrij.renderer.Texture;
import at.theOnlyHorst.tetrij.util.DDSParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    private static Map<String, Texture> textures;
    //TODO implement Textures List



    public static void initResManager() throws ParserConfigurationException, IOException, SAXException {
        shaders = new HashMap<>();
        textures = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(ResourceManager.class.getClassLoader().getResourceAsStream("resources.xml"));

        Element root = doc.getDocumentElement();

        Node shadersN = root.getElementsByTagName("Shaders").item(0);

        Map <String,String> pathMapSh = readResourceTypeDOM(shadersN);
        pathMapSh.forEach((name,path) -> shaders.put(name,readResFileString(path)));

        Node texturesN = root.getElementsByTagName("Textures").item(0);

        Map<String,String> pathMapTx = readResourceTypeDOM(texturesN);

        pathMapTx.forEach((name,path)-> textures.put(name,readTextureFile(path)));



    }

    //returns a Map with name and path
    private static Map<String,String> readResourceTypeDOM(Node parent)
    {
        Map<String,String> resultMap = new HashMap<>();
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

                    resultMap.put(name,parentPath+pathE);
                }
            }

        }
        return resultMap;
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

    private static Texture readTextureFile(String path)
    {
        return DDSParser.readDDS(path);
    }




    public static String getShader(String name)
    {
        return shaders.get(name);
    }

    public static Texture getTexture(String name) {
        System.out.println(name);
        return textures.get(name);
    }


}
