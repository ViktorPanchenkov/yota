package com.chi.testtask.yota.helper;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class XmlParser {

    private final Document document;

    /**
     * Constructor initialize parser from String
     *
     * @param message xml string representation
     * @throws IOException SAXException, ParserConfigurationException
     */
    public XmlParser(@NotNull final String message) throws ParserConfigurationException, IOException, SAXException {
        if (isEmpty(message)) {
            throw new IllegalArgumentException("Unable to initialise org.w3c.dom.Document: Input is empty");
        }
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        this.document = documentBuilder.parse(new InputSource(new StringReader(message)));
        this.document.getDocumentElement().normalize();
    }

    /**
     * Returns first found Xml node with tagname specified
     *
     * @param tagName node name to be parsed
     * @return Map of child nodes or throws exception if node not found
     */
    public List<Map<Object, Object>> getXmlNode(final String tagName) {
        return getXmlNodeByIndex(tagName, 0);
    }

    /**
     * Returns founded Xml node with tagname specified by the index
     *
     * @param tagName node name to be parsed
     * @param index   index of the node in the document
     * @return Map of child nodes or throws exception if node not found or no node with index specified
     */
    public List<Map<Object, Object>> getXmlNodeByIndex(final String tagName, int index) {

        NodeList nodeList = document.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            throw new IllegalArgumentException(String.format("No node with tagname %s in this XML document", tagName));
        }
        try {
            Node node = nodeList.item(index);
            final List<Map<Object, Object>> map = new ArrayList<>();
            fillMap(map, node);

            return map;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(String.format("Node %s is presented in the document, but not with index %s", tagName, index));
        }
    }

    /**
     * Returns list of the tags values for tagname specified
     *
     * @param tagName node name to be parsed
     * @return first detected string node value from DOM
     */
    public List<String> getValueListByTagName(final String tagName) {
        NodeList nodeList = document.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) {
            throw new IllegalArgumentException(String.format("No node with tagname %s in this XML document", tagName));
        }
        List<String> result = new ArrayList<>();
        IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item)
                .forEach(item -> result.add(item.getTextContent()));
        return result;
    }


    private void fillMap(final List<Map<Object, Object>> nodeMap, final Node node) {

        if (!(node.getTextContent().trim().equals("") && node.getNodeName().trim().equals("#text")) && !node.getNodeName().trim().equals("#comment")) {
            Map<Object,Object> keyValue = new HashMap<>();

            if (node.getChildNodes().getLength() == 1 && node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                keyValue.put(node.getNodeName(), node.getFirstChild().getTextContent());
                nodeMap.add(keyValue);
            } else if (node.getChildNodes().getLength() == 0) {
                keyValue.put(node.getNodeName(), null);
                nodeMap.add(keyValue);
            } else {
                List<Map<Object, Object>> childNodesMap = new ArrayList<>();
                IntStream.range(0, node.getChildNodes().getLength()).mapToObj(node.getChildNodes()::item)
                        .forEach(childNode -> {fillMap(childNodesMap, childNode);
                        });
                keyValue.put(node.getNodeName(), childNodesMap);
                nodeMap.add(keyValue);
            }
        }
    }

}
