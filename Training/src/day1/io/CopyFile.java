package day1.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class CopyFile {
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter source file(dir) name: ");
		String srcFile = sc.nextLine();
		
		System.out.println("Enter destination file(dir) name: ");
		String dstFile = sc.nextLine();;
		
		File file = new File(srcFile);
		File newFile = new File(dstFile);

		try {
			
			if (file.isFile()) {
				// �����̶�� �ٷ� ����
				Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);	
			} else {
				// ���丮��� ��ø�Ǿ� �ִ� ���ϰ� ���丮 ����ؼ� ����
				copyDir(file, newFile);
			}
			
			System.out.println("=== Success File Copy ===");
			
		} catch (NoSuchFileException e) {
			e.printStackTrace();
			System.out.println("=== No Such file === ");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("=== IOException ===");
		}
		
	}
	
	private static void copyDir(File file, File newFile) throws IOException{
		
		if (!newFile.exists()) {
			newFile.mkdir();
		}
		
		File[] files = file.listFiles();
		
		if (files == null) {
			return; 
		}
		
		for (File nestedFile : files) {
			if (nestedFile.isDirectory()) {
				copyDir(nestedFile, new File(newFile, nestedFile.getName()));	
			} else {
				Files.copy(nestedFile.toPath(), new File(newFile, nestedFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);	
			}
		}
	
	}
}
