package string;

import java.util.Scanner;

public class ReplaceString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		System.out.println("���� text ���ڿ�");
		String originText = sc.nextLine();
		
		System.out.println("ġȯ ��� ���ڿ�");
		String before = sc.nextLine();
		
		System.out.println("ġȯ�� ���ڿ�");
		String after = sc.nextLine();
		
		originText = originText.replace(before, after);
		
		System.out.println(originText);
	}

}
