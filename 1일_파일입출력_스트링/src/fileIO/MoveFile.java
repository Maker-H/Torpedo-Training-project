package fileIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class MoveFile {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter source file(dir) name: ");
		String srcFile = sc.nextLine();
		
		System.out.print("Enter destination file(dir) name: ");
		String dstFile = sc.nextLine();;
		
		File src = new File(srcFile);
		File dst = new File(dstFile);
		
		try {
			MoveFile moveFile = new MoveFile();
			
			if (src.isFile()) {
				// �����̶�� �ٷ� �̵�
				System.out.println("Start moving file: " + src.getName() + " ...");
				Files.move(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);	
			} else {
				// ���丮��� ��ø�Ǿ� �ִ� ���ϰ� ���丮 ����ؼ� �̵� �� ���� ����
				System.out.println("Start moving folder: " + src.getName() + "/ ...");
				moveFile.moveDir(src, dst);
			}
			
			System.out.println("=== [Success] File Moving ===");
			
		} catch (NoSuchFileException e) {
			e.printStackTrace();
			System.out.println("=== [Error] No Such file ===");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("=== [Error] IOException ===");
		}

	}
	
	private void moveDir(File srcFile, File dstFile) throws IOException{
		
		if (!dstFile.exists()) {
			dstFile.mkdir();
		}
		
		File[] files = srcFile.listFiles();
		
		if (files == null) {
			return;
		}
		
		for (File nestedFile : files) {
			if (nestedFile.isFile()) {
				Files.move(nestedFile.toPath(), new File(dstFile, nestedFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} else {
				moveDir(nestedFile, new File(dstFile, nestedFile.getName()));
			}
		}
		
		// ���� �� �ű�� ���� ���� ���丮 ����
		srcFile.delete();
		
	}

}
