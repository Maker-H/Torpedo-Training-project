package string;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReplaceStringAndStore {

	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("���� text ���ϸ�:");
        String sourceFilePath = sc.nextLine();

        System.out.print("ġȯ�� text ���ϸ�:");
        String targetFilePath = sc.nextLine();

        System.out.print("ġȯ��� ���ڿ�:");
        String targetString = sc.nextLine();

        System.out.print("ġȯ�� ���ڿ�");
        String replacementString = sc.nextLine();

        try {
        	 FileReader fr = new FileReader(sourceFilePath);
        	 BufferedReader br = new BufferedReader(fr);
        	
        	 FileWriter pw = new FileWriter(targetFilePath);
        	 
        	 String data;
        	 StringBuilder sb = new StringBuilder();
        	 while ((data = br.readLine()) != null) {
        		sb.append(data).append("\n");
        	}
        	
        	String result = sb.toString().replace(targetString, replacementString);
     		pw.write(result);
        	pw.close();
        	
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
