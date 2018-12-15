package tic2.project;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LoginController extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            Document doc = null;
            try {
                builder = factory.newDocumentBuilder();
                doc = builder.parse("/opt/tomcat/webapps/FirstPP/WEB-INF/database.xml");

                // Create XPathFactory object
                XPathFactory xpathFactory = XPathFactory.newInstance();

                // Create XPath object
                XPath xpath = xpathFactory.newXPath();
                
                String un=request.getParameter("username");
                String pw=request.getParameter("password");

                if(authenticateUser(doc, xpath, un, pw)==true)
                {
                        response.sendRedirect("success.html");
                        return;
                }
                else
                {
                        response.sendRedirect("error.html");
                        return;
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
  
            }
	private static Boolean authenticateUser(Document doc, XPath xpath, String login, String password){
			boolean isValid = false;
    		try {
    			XPathExpression expr = 
		xpath.compile("//user[email/text()='" + sanitize(login.trim()) + "' and password/text()='" + sanitize(password.trim()) +"']");
                        isValid = (boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
                    } catch (XPathExpressionException e) {
           	        e.printStackTrace();
       		    }
    	return isValid;
    }
	static String sanitize(String text) {
		return text.replace("&", "&amp;").replace("'", "&apos;");
	}

}

