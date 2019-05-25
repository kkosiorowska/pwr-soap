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

    Message() throws SOAPException {
        //stworzenie wiadomosci
        MessageFactory factory = MessageFactory.newInstance();
        this.soapMessage = factory.createMessage();
        this.soapMessage = MessageFactory.newInstance().createMessage();

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

        headerName= new QName("http://soap-app.com", "Information", "h");
        SOAPHeaderElement infromation = soapMessage.getSOAPHeader().addHeaderElement(headerName);

        QName childName = new QName("Addresses");
        SOAPElement addresses = infromation.addChildElement(childName);

        childName = new QName("Source");
        SOAPElement source = addresses.addChildElement(childName);
        source.addTextNode("");

        childName = new QName("Destination");
        SOAPElement destination = addresses.addChildElement(childName);
        destination.addTextNode("");

        //////////////////////////////// BODY

        bodyName = new QName("http://soap-app.com", "Message", "m");
        SOAPBodyElement messages = this.soapMessage.getSOAPBody().addBodyElement(bodyName);

        childName = new QName("Message");
        SOAPElement message = messages.addChildElement(childName);
        source.addTextNode("");

    }

    //////////////////////////////// Message
    public void setMessage(String message) throws SOAPException {
        SOAPBody soapBody = this.soapMessage.getSOAPBody();

        Iterator bodyIt = soapBody.getChildElements(bodyName);
        SOAPBodyElement messeges = (SOAPBodyElement) bodyIt.next();

        Iterator bodyElementIt = messeges.getChildElements(new QName("Message"));
        SOAPElement element = (SOAPElement) bodyElementIt.next();

        element.addTextNode(message);
    }

    public String getMessage() throws SOAPException {
        SOAPBody soapBody = this.soapMessage.getSOAPBody();

        Iterator bodyIt = soapBody.getChildElements(bodyName);
        SOAPBodyElement messeges = (SOAPBodyElement) bodyIt.next();

        Iterator childElementIt = messeges.getChildElements(new QName("Message"));
        SOAPElement element = (SOAPElement) childElementIt.next();

        return element.getTextContent();
    }

    public SOAPElement getSOAPHeaderElement () throws SOAPException {

        SOAPHeader soapHeader = this.soapMessage.getSOAPHeader();
        Iterator headerIterator = soapHeader.getChildElements(headerName);
        SOAPHeaderElement information = (SOAPHeaderElement) headerIterator.next();

        Iterator childElementIt = information.getChildElements((new QName("Addresses")));

        return (SOAPElement) childElementIt.next();
    }

    //////////////////////////////// Source
    public void setSource(String sourceAddress) throws SOAPException {

        SOAPElement element = getSOAPHeaderElement();
        NodeList nodes = element.getChildNodes();
        nodes.item(0).setTextContent(sourceAddress);
    }

    public String getSource() throws SOAPException {

        SOAPElement element = getSOAPHeaderElement();
        NodeList nodes = element.getChildNodes();
        return nodes.item(0).getTextContent();
    }

    //////////////////////////////// Destination
    public void setDestination(String destAddress) throws SOAPException {
        SOAPElement element = getSOAPHeaderElement();
        NodeList nodes = element.getChildNodes();
        nodes.item(1).setTextContent(destAddress);
    }

    public String getDestination() throws SOAPException {
        SOAPElement element = getSOAPHeaderElement();
        NodeList nodes = element.getChildNodes();
        return nodes.item(1).getTextContent();
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
