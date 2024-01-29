package string;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReplaceStringAndStore {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		
        Scanner sc = new Scanner(System.in);

        System.out.print("���� text ���ϸ�: ");
        String sourceFilePath = sc.nextLine();

        System.out.print("ġȯ�� text ���ϸ�: ");
        String targetFilePath = sc.nextLine();

        System.out.print("ġȯ��� ���ڿ�: ");
        String targetString = sc.nextLine();

        System.out.print("ġȯ�� ���ڿ�: ");
        String replacementString = sc.nextLine();

        try {
        	 isr = new InputStreamReader(new FileInputStream(sourceFilePath)); 
        	 br = new BufferedReader(isr);
        	 osw = new OutputStreamWriter(new FileOutputStream(sourceFilePath), isr.getEncoding());
        	 
        	 System.out.println("Reading file...");
        	 String data;
        	 StringBuilder sb = new StringBuilder();
        	 while ((data = br.readLine()) != null) {
        		sb.append(data).append("\n");
        	}
        	
        	String result = sb.toString().replace(targetString, replacementString);
        	
        	System.out.println("Storing file...");
     		osw.write(result);
        	
     		System.out.println("=== [Success] Replace string and store ===");
        	
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
			try {
				System.out.println("Closing stream...");
				
				// Inputstream ���� - BufferedReader�� ���������� ������⿡ br�� close
				if (br != null) br.close();
				if (osw != null) osw.close();
			} catch (IOException e) {
				System.out.println("=== [Error] IO Exception while closing  ===");
				e.printStackTrace();
			}
        }
    }
}
