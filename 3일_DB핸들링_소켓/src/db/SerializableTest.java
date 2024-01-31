package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SerializableTest {
		
	
		public static void main(String[] args) {
	        ManageDB dbManager1 = new ManageDB(Connection.TRANSACTION_SERIALIZABLE);
	        ManageDB dbManager2 = new ManageDB(Connection.TRANSACTION_SERIALIZABLE);
	        
	        Statement statement1 = null;
            Statement statement2 = null;
            
            ResultSet resultSet = null;
	        
	        try {
	    		
	        	dbManager1.connect();
	        	Connection conn1 = dbManager1.getJdbcConnection();
	        	
	        	dbManager2.connect();
	        	Connection conn2 = dbManager2.getJdbcConnection();
	        	
	            
	            
	            // ù ��° Ʈ����� - ������ ��ȸ
	            statement1 = conn1.createStatement();
	            resultSet = statement1.executeQuery("SELECT * FROM employees_tb");

	            System.out.println("\n[Serializable Test]");
	            System.out.println("====== ù��° ��ȸ ���� =====");
	            
	            // ��� ���
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String name = resultSet.getString("name");
	                System.out.println("id: " + id + ", name: " + name);
	            }

	            System.out.println("========================\n");
	            // �� ��° Ʈ����� - ������ ����
	            statement2 = conn2.createStatement();
	            statement2.executeUpdate("UPDATE employees_tb SET name = 'NEW VALUE' WHERE id = 3");

	            System.out.println("UPDATE ���� (OLD VALUE -> NEW VALUE) Ŀ�� �Ϸ�");
	            dbManager2.commit();

	            System.out.println("\n====== �ι�° ��ȸ ���� =====");
	            // ù ��° Ʈ����ǿ��� �ٽ� ������ ��ȸ
	            resultSet = statement1.executeQuery("SELECT * FROM employees_tb");

	            // ��� ���
	            System.out.println("After update:");
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String name = resultSet.getString("name");
	                System.out.println("id: " + id + ", name: " + name);
	            }
	            System.out.println("========================\n");
	            
	            
	        } catch (SQLException e) {
	            System.out.println("=== [Error] SQL Error ===");
	            e.printStackTrace();
	        } finally {
	            try {
	                dbManager1.disconnect();
	                dbManager2.disconnect();
	            } catch (SQLException e) {
	                System.out.println("=== [Error] Disconnect Error ===");
	                e.printStackTrace();
	            }
	        }
	    }
}
