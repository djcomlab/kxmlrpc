/* kxmlrpc - XML-RPC client for J2ME
 *
 * Copyright (C) 2001  Kyle Gabhart ( kyle@gabhart.com )
 *
 * Contributors: David Johnson ( djcomlab )
 * 				       Stefan Haustein
 */

package org.kxmlrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;


/**
 * A single-threaded, reusable XML-RPC client object.
 */
public class XmlRpcClient {

    /**
     * Stores the full URL the client will connect with
     */
    String url;

    /**
     * Stores the response sent back by the server
     */
    Object result = null;

    /**
     * Turns debugging on/off
     */
    boolean debug = true;

    /**
     * Constructs an XML-RPC client with a specified string representing a URL.
     *
     * @param url The full URL for the XML-RPC server
     */
    public XmlRpcClient( String url ) {
        this.url = url;
    }//end KxmlRpcClient( String )

    /**
     * Construct an XML-RPC client for the specified hostname and port.
     *
     * @param hostname the name of the host server
     * @param the server's port number
     */
    public XmlRpcClient( String hostname, int port ) {
        int delim = hostname.indexOf("/");
        String context = "";
        if (delim>0) {
            context = hostname.substring(delim);
            hostname = hostname.substring(0, delim);
        }
        this.url = "http://" + hostname + ":" + port + context;
    }//end KxmlRpcClient( String, int )

    public String getURL() {
        return url;
    }//end getURL()

    public void setURL( String newUrl ) {
        url = newUrl;
    }//end setURL( String )

    /**
     * This method is the brains of the XmlRpcClient class. It opens an
     * HttpConnection on the URL stored in the url variable, sends an XML-RPC
     * request and processes the response sent back from the server.
     *
     * @param method contains the method on the server that the
     * client will access
     * @param params contains a list of parameters to be sent to
     * the server
     * @return the primitive, collection, or custom object
     * returned by the server
     */
    public Object execute( String method, Vector params) throws Exception {
        // kxmlrpc classes
        KXmlSerializer          xw = null;
        XmlRpcWriter            writer = null;
        XmlRpcParser            parser = null;
        // J2ME classes
        HttpConnection          con = null;
        InputStream             in = null;
        OutputStream            out = null;
        // Misc objects for buffering request
        ByteArrayOutputStream   bos = null;
        byte[]                  request;
        int                     messageLength;

        try {
            bos = new ByteArrayOutputStream();
            xw = new KXmlSerializer();
            xw.setOutput(new OutputStreamWriter(bos));
            writer = new XmlRpcWriter(xw);

            writer.writeCall(method, params);
            xw.flush();

            if (debug) System.out.println(bos.toString());
            request = bos.toByteArray();

            messageLength = request.length;

            con = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
            con.setRequestMethod(HttpConnection.POST);
            con.setRequestProperty("Content-Length", Integer.toString(messageLength));
            con.setRequestProperty("Content-Type", "text/xml");

            // Obtain an output stream
            out = con.openOutputStream();
            // Push the request to the server
            out.write( request );
            // Open an input stream on the server's response
            in = con.openInputStream();

            // Parse response from server
            KXmlParser xp = new KXmlParser();
            xp.setInput(new InputStreamReader(in));
            parser = new XmlRpcParser(xp);
            result = parser.parseResponse();

        } catch (Exception x) {
            x.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }//end try/catch
        }//end try/catch/finally

        if(result instanceof Exception)
            throw (Exception) result;

        return result;
    }//end execute( String, Vector )

    /**
     * Called when the return value has been parsed.
     */
    void setParsedObject(Object parsedObject) {
        result = parsedObject;
    }//end objectCompleted( Object )

}//end class KXmlRpcClient
