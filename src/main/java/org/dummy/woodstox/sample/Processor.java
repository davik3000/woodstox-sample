/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dummy.woodstox.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;

/**
 *
 * @author artasda
 */
public class Processor {

    private static final Logger LOG = LogManager.getLogger();

    public Processor() {
    }

    public void process() {
    }

    public void testEventReaderWriter(String xmlInputPath,
            String xmlMatchingNamespaceUri,
            String xmlMatchingLocalPart,
            String xmlOutputPath) {
        XMLInputFactory xif = XMLInputFactory2.newInstance();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        XMLOutputFactory xof = XMLOutputFactory2.newInstance();

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
}
