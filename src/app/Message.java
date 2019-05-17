package app;

import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.util.Iterator;

public class Message {
    //////////////////////////////// Source
    //https://docs.oracle.com/cd/E19575-01/819-3669/bnbhs/index.html?fbclid=IwAR03vKvao_vocfrfVHyqprWXAmNZK9f2vugjciY8UUSmtfns-za2yWqqIvk

    private SOAPMessage soapMessage;
    private SOAPBody body;
    private QName bodyName;
    private SOAPHeader header;
    private QName headerName;


    Message(String sourceAddress) throws SOAPException {
        //stworzenie wiadomosci
        MessageFactory factory = MessageFactory.newInstance();
        this.soapMessage = factory.createMessage();

        /*
        I. A SOAPPart object that contains
             A.  A SOAPEnvelope object that contains
                 1.  An empty SOAPHeader object
                 2.  An empty SOAPBody object
         */

        /*
        QName objects associated with SOAPBodyElement or SOAPHeaderElement objects
        must be fully qualified; that is, they must be created with
        a namespace URI, a local part, and a namespace prefix.
         */
        //////////////////////////////// HEADER

        header  = soapMessage.getSOAPHeader();
        headerName = new QName("http://test.com", "Information", "h");
        SOAPHeaderElement infromation = header.addHeaderElement(headerName);

        QName childName = new QName("Addresses");
        SOAPElement addresses = infromation.addChildElement(childName);

        childName = new QName("Destination");
        SOAPElement destination = addresses.addChildElement(childName);
        destination.addTextNode("ALL");

        //////////////////////////////// BODY

        body = soapMessage.getSOAPBody();
        bodyName = new QName("http://test.com", "Messages", "m");
        SOAPBodyElement messages = body.addBodyElement(bodyName);

        childName = new QName("Source");
        SOAPElement source = messages.addChildElement(childName);
        source.addTextNode(sourceAddress);

        childName = new QName("Message");
        SOAPElement message = messages.addChildElement(childName);
        source.addTextNode("");

        ////////////////////////////////

    }
    //////////////////////////////// Source

    public void setSource(String sourceAddress) throws SOAPException {
        Iterator iterator = header.getChildElements(headerName);
        SOAPHeaderElement information = (SOAPHeaderElement) iterator.next();
        Iterator childIterator = information.getChildElements((new QName("Addresses")));
        SOAPElement element = (SOAPElement) childIterator.next();
        NodeList nodes = element.getChildNodes();
        nodes.item(0).setTextContent(sourceAddress);
    }

    public String getSource() throws SOAPException {
        Iterator iterator = header.getChildElements(headerName);
        SOAPHeaderElement information = (SOAPHeaderElement) iterator.next();
        Iterator childElementIt = information.getChildElements((new QName("Addresses")));
        SOAPElement element = (SOAPElement) childElementIt.next();
        NodeList nodes = element.getChildNodes();
        return nodes.item(0).getTextContent();
    }

    //////////////////////////////// Destination

    public void setDestination(String destAddress) {
        Iterator iterator = header.getChildElements(headerName);
        SOAPHeaderElement information = (SOAPHeaderElement) iterator.next();
        Iterator childIterator = information.getChildElements(new QName("Addresses"));
        SOAPElement element = (SOAPElement) childIterator.next();
        NodeList nodes = element.getChildNodes();
        nodes.item(1).setTextContent(destAddress);
    }

    public String getDestination() throws SOAPException {
        Iterator iterator = header.getChildElements(headerName);
        SOAPHeaderElement information = (SOAPHeaderElement) iterator.next();
        Iterator childIterator = information.getChildElements(new QName("Addresses"));
        SOAPElement element = (SOAPElement) childIterator.next();
        NodeList nodes = element.getChildNodes();
        return nodes.item(1).getTextContent();
    }

    //////////////////////////////// Message

    public void setMessage(String message) throws SOAPException {
        Iterator iterator = body.getChildElements(bodyName);
        SOAPBodyElement bodyElement = (SOAPBodyElement)iterator.next();
        Iterator childIterator = bodyElement.getChildElements(new QName("Message"));
        SOAPElement element = (SOAPElement) childIterator.next();
        element.addTextNode(message);
    }

    public String getMessage() {
        Iterator iterator = body.getChildElements(bodyName);
        SOAPBodyElement bodyElement = (SOAPBodyElement)iterator.next();
        Iterator childIterator = bodyElement.getChildElements(new QName("Message"));
        SOAPElement element = (SOAPElement) childIterator.next();
        return element.getTextContent();
    }

    public SOAPMessage getSoapMessage() {
        return soapMessage;
    }

    public void setSoapMessage(SOAPMessage soapMessage) {
        this.soapMessage = soapMessage;
    }

    @Override
    public String toString() {
        String string = null;
        try {
            string = new String("Message from: " + getSource() + " to " + getDestination());
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return string;
    }

}
