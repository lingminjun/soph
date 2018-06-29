package ssn.lmj.user.service.impl;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by syevia on 2015/8/1.
 */
public class XMLUtil {

    /**
     * 将xml序列号为map
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Map<String, String> doXMLParse(String strxml) {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        byte[] bytes = null;
        try {
            bytes = strxml.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (bytes == null) {
            return m;
        }
        InputStream in = new ByteArrayInputStream(bytes);
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(in);
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return m;
        }
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XMLUtil.getChildrenText(children);
            }

            m.put(k, v);
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return m;
    }

    /**
     * 获取xml的children text
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    /**
     * 获取xmlstr的某个节点的值
     * @param xmlStr
     * @param nodeName
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static String getNodeValue(String xmlStr, String nodeName) {
        DocumentBuilder dob = null;
        try {
            dob = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (dob == null) {
            return "";
        }
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        org.w3c.dom.Document doc = null;
        try {
            doc = dob.parse(in);
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
        org.w3c.dom.Element root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        String result = "";
        if (root != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeName().equals(nodeName)) {
                    result = node.getTextContent();
                    break;
                }
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
