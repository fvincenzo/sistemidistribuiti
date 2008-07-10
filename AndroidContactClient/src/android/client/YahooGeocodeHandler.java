package android.client;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class YahooGeocodeHandler extends DefaultHandler {
	private String latitude = "";
    private String longitude = "";

    private boolean isLatitude = false;
    private boolean isLongitude = false;

    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        if (localName.equals("Latitude")) {
            isLatitude = true;
        } else if (localName.equals("Longitude")) {
            isLongitude = true;
        }
    }

    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (localName.equals("Latitude")) {
            isLatitude = false;
        } else if (localName.equals("Longitude")) {
            isLongitude = false;
        }
    }

    public void characters(char[] chars, int i, int i1) throws SAXException {
        if (isLatitude) {
            latitude = new String(chars, i, i1);
        } else if (isLongitude) {
            longitude = new String(chars, i, i1);
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    
    public long getLatitudeAsLong() {
    	if (latitude!=null && latitude.length()>0) {
    		return (long)(Double.parseDouble(latitude)*1000000);
    	} else {
    		return -1;
    	}
    }
    
    public long getLongitudeAsLong() {
    	if (longitude!=null && longitude.length()>0) {
    		return (long)(Double.parseDouble(longitude)*1000000);
    	} else {
    		return -1;
    	}
    }
}