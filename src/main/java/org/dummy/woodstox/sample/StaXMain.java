/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dummy.woodstox.sample;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author davik3000
 */
public class StaXMain {

    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    private static void testEventReaderWriter(String xmlInputPath,
            String xmlMatchingNamespaceUri,
            String xmlMatchingLocalPart,
            String xmlOutputPath) {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        XMLOutputFactory xof = XMLOutputFactory.newInstance();

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(xmlInputPath);
            os = new FileOutputStream(xmlOutputPath);

            XMLEventReader xer = xif.createXMLEventReader(is);
            XMLEventWriter xew = xof.createXMLEventWriter(os);

            // use if you need new XMLEvent objects to inject
            XMLEventFactory xef = XMLEventFactory.newInstance();

            if ((xmlMatchingNamespaceUri != null) && (xmlMatchingLocalPart != null)) {
                while (xer.hasNext()) {
                    XMLEvent event = (XMLEvent) xer.next();
                    if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                        StartElement se = event.asStartElement();
                        QName qName = se.getName();
                        NamespaceContext nc = se.getNamespaceContext();
                        if (qName.getNamespaceURI().equals(xmlMatchingNamespaceUri)
                                && qName.getLocalPart().equals(xmlMatchingLocalPart)) {
                            String defaultNsUri = nc.getNamespaceURI(XMLConstants.DEFAULT_NS_PREFIX);
                            String nsPrefix = nc.getPrefix(defaultNsUri);
                            LOG.debug("Detected start element that match input parameters");
                            LOG.debug("Namespace URI: {}", defaultNsUri);
                            LOG.debug("Namespace prefix: {}", nsPrefix);
                        }
                    }
                    xew.add(event);
                }
            } else {
                // invalid parameters, copy everything from the reader to the writer
                xew.add(xer);
            }
            xew.flush();
            xew.close();
        } catch (FileNotFoundException ex) {
            LOG.error(ex.getMessage());
        } catch (XMLStreamException ex) {
            LOG.error(ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex) {
                LOG.error(ex.getMessage());
            }
        }
    }

//    private static void testEventReaderTransformer(String xmlInputPath, String xmlOutputPath) throws FileNotFoundException, TransformerConfigurationException, XMLStreamException, TransformerException {
//        InputStream inputStream = new FileInputStream(xmlInputPath);
//        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//
//        XMLInputFactory factory = XMLInputFactory.newInstance();
//        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
//
//        TransformerFactory tf = TransformerFactory.newInstance();
//        //tf.setFeature(, true);
//        Transformer t = tf.newTransformer();
//
//        XMLStreamReader streamReader = factory.createXMLStreamReader(in);
//
//        while (streamReader.hasNext()) {
//            streamReader.next();
//
//            if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT
//                    && "nextTag".equals(streamReader.getLocalName())) {
//
//                StringWriter writer = new StringWriter();
//                t.transform(new StAXSource(streamReader), new StreamResult(writer));
//                String output = writer.toString();
//                System.out.println(output);
//
//            }
//
//        }
//    }
    public static void main(String[] args) throws FileNotFoundException, TransformerConfigurationException, XMLStreamException, TransformerException, IOException {
        LOG.debug("Starting application");

        if (args.length > 0) {
            String filePath = args[0];
//            OutputStream os = new FileOutputStream("output.xml");
//            os.write(65);
//            os.flush();
//            os.close();

            testEventReaderWriter(filePath, "http://dummy.org/2.0", "tag2", filePath + ".output.xml");

        }

        LOG.debug("Ending application");
    }
}
