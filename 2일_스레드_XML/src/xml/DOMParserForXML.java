package xml;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParserForXML {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter source file path: ");
		
		String filePath = sc.nextLine();
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			// XML ���� �Ľ��Ͽ� Document ��ü ����
			Document document = builder.parse(new File(filePath));
			
			// products �±׿� �ش��ϴ� NodeList ��������
			NodeList productList = document.getElementsByTagName("product");
			
			for (int i = 0; i < productList.getLength(); i++) {
				Element productElement = (Element) productList.item(i);
				
				String number = productElement.getElementsByTagName("number").item(0).getTextContent();
				String name = productElement.getElementsByTagName("name").item(0).getTextContent();
				String price = productElement.getElementsByTagName("price").item(0).getTextContent();
				
				System.out.println("��ȣ: " + number);
				System.out.println("�̸�: " + name);
				System.out.println("����: " + price);
				
				System.out.println();
			}
			
			System.out.println("=== [Success] Parsing XML ===");
			
		} catch (ParserConfigurationException e) {
			System.out.println("=== [Error] Parser Configuration Error ===");
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("=== [Error] Error While Parsing ===");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("=== [Error] Error While Handing File ===");
			e.printStackTrace();
		}
	}

}
