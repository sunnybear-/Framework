package com.sunnybear.framework.tools.log;

import android.util.Log;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Xml日志
 * Created by chenkai.gu on 2017/4/12.
 */
class XmlLogger {

    public static void printXml(String tag, String xml, String headString) {
        if (xml != null) {
            xml = formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + Logger.NULL_TIPS;
        }
        LoggerUtil.printLine(tag, true);
        String[] lines = xml.split(Logger.LINE_SEPARATOR);
        for (String line : lines) {
            if (!LoggerUtil.isEmpty(line))
                Log.d(tag, "║ " + line);
        }
        LoggerUtil.printLine(tag, false);
    }

    private static String formatXML(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
            return inputXML;
        }
    }
}
