package day3.xmls;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXML {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter destination file path: ");
		String dstPath = sc.nextLine();
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // ���ο� DOM Ʈ�� ����
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.setXmlStandalone(true);
			
			//Document�� product �±� �߰�
			Element products = document.createElement("products");
			document.appendChild(products);
			
			List<String> product1 = Arrays.asList("100", "�����", "1500");
			List<String> product2 = Arrays.asList("101", "���ĸ�", "2000");
			List<String> product3 = Arrays.asList("102", "Ȩ����", "3000");
			
			List<List<String>> productList = Arrays.asList(product1, product2, product3);
			
			for (List<String> line : productList) {
				Element product = document.createElement("product");
				
				Element number = document.createElement("number");
				number.setTextContent(line.get(0)); //line�� 1��°�� number �±� �ȿ� ����
				
				Element name = document.createElement("name");
				name.setTextContent(line.get(1)); // line�� 2��°�� name �±� �ȿ� ����
				
				Element price = document.createElement("price");
				price.setTextContent(line.get(2)); // line�� 3��°�� price �±� �ȿ� ����
				
				product.appendChild(number);
				product.appendChild(name);
				product.appendChild(price);
				
				products.appendChild(product); // product �±� �ȿ� ���� number name price �±� �־��ֱ�
				
			}
			
			// �ٸ� DOM Ʈ���� �ٸ� �������� ��ȯ�� �� ���
			// DOM Ʈ���� ���ڿ��� ��ȯ�� ���Ϸ� �����ϰų�, ��Ʈ��ũ�� ���� ����
			TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("encoding", "UTF-8");
			transformer.setOutputProperty("indent", "yes");
			transformer.setOutputProperty("doctype-public", "yes");
			
			Source source = new DOMSource(document);
			File file = new File(dstPath);
			StreamResult result = new StreamResult(file);
			
			transformer.transform(source, result);
			
			System.out.println("=== [Success] Create Document ===");
			
		} catch (ParserConfigurationException e) {
			// DocumentBuilder ������ �� ���� �߻�
			System.out.println("=== [Error] Error while making DOM ===");
			e.printStackTrace();
		} catch (TransformerException e) {
			// XML������ �ٸ� ���·� ��ȯ�ϴ� �������� ���� �߻�
			System.out.println("=== [Error] Error while transforming XML ===");
			e.printStackTrace();
		}

	}
}
