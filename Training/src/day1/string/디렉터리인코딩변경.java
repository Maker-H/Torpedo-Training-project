package day1.string;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Scanner;

public class ���͸����ڵ����� {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		System.out.print("���丮 ���: ");
		String directoryPath = sc.nextLine();
		
		System.out.print("��ȯ ���ڵ� ����: ");
		String targetEncoding = sc.nextLine();

        convertEncodingRecursively(new File(directoryPath), targetEncoding);
	}

    private static void convertEncodingRecursively(File file, String targetEncoding) throws IOException {
    	// ���丮�̸� ��� �Ʒ��� ����
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File nestedFile : files) {
                    convertEncodingRecursively(nestedFile, targetEncoding);
                }
            }
        // �����̸� ���ڵ� ����    
        } else {
        	FileReader fr = new FileReader(file);
        	BufferedReader br = new BufferedReader(fr);
        	
        	String data;
        	StringBuilder sb = new StringBuilder();
        	while((data = br.readLine()) != null) {
        		sb.append(data).append("\n");
        	}
        	
    		br.close();
    		
    		FileOutputStream fos= new FileOutputStream(file);
    		OutputStreamWriter osr = new OutputStreamWriter(fos, "UTF-8");
    		osr.write(sb.toString());

    		osr.close();
        }
    }
}
