package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import response.ResponseData;
import response.Status;
import servletpj.EmployeeDAO;

public class EmployeeService {
    private DBManager dbManager;
    
    public EmployeeService() {
        this.dbManager = new DBManager();
    }
    
    /**
     * DB�� ���� ���̺� ������ �����ϴ� �޼���
     * 
     * @param userTableEmployees
     * @return 
     */
    public ResponseData syncToEmployeeTable(List<EmployeeDAO> userTableEmployees) {
        System.out.println("Start sync to table -> db...");
        
        try {
            // 1. selectAll�� �����ϴ� employee ���� ��������
            List<EmployeeDAO> dbEmployees = this.selectAllEmployee();
            
            // selectAll�� employees ��ȯ���� �ʴ� ���(null)�� ��� ����
            if (dbEmployees == null) {
                return new ResponseData(Status.FAIL, "DB���� �� �������� �� ����");
            }
            // 2.�̸� ���� ���� �� ��
            Collections.sort(userTableEmployees, Comparator.comparing(EmployeeDAO::getName));
            Collections.sort(dbEmployees, Comparator.comparing(EmployeeDAO::getName));
            
            // 3. ����ڰ� ���� ǥ�� �� 
            int userIndex = 0;
            int dbIndex = 0;
    
            ResponseData response = null;
            
            while (userIndex < userTableEmployees.size() && dbIndex < dbEmployees.size()) {
                EmployeeDAO userEmployee = userTableEmployees.get(userIndex);
                EmployeeDAO dbEmployee = dbEmployees.get(dbIndex);
    
                int departComp = userEmployee.getDepartment().compareTo(dbEmployee.getDepartment());
                int nameComp = userEmployee.getName().compareTo(dbEmployee.getName());
                int positionComp = userEmployee.getPosition().compareTo(dbEmployee.getPosition());
                int engNameComp = userEmployee.getEnglishName().compareTo(dbEmployee.getEnglishName());
                int phoneNumComp = userEmployee.getPhoneNumber().compareTo(dbEmployee.getPhoneNumber());
                int emailComp = userEmployee.getEmail().compareTo(dbEmployee.getEmail());
                int sumComp = departComp + nameComp + positionComp + engNameComp + phoneNumComp + emailComp;
                
                if (sumComp == 0) {
                    userIndex++;
                    dbIndex++;
                } else if (sumComp < 0) {
                    // 3-1. db�� �������� �ʴ� employee ��� insert
                    response = this.insertEmployee(userEmployee);
                    userIndex++;
                    
                } else {
                    // 3-2. db�� �����ϴ� employee�ε� ǥ�� �������� �ʴ´ٸ� delete
                    response = this.deleteEmployee(dbEmployee);
                    dbIndex++;
                }
                
                // insert�� delete�� ���� �� response �� ����
                if (response != null) {
                    return response;
                }
            }
            
    
            // 3-3 ǥ�� ���� �ִ� employee insert
            while (userIndex < userTableEmployees.size()) {
                response = this.insertEmployee(userTableEmployees.get(userIndex));
                userIndex++;
                
                // insert�� delete�� ���� �� response �� ����
                if (response != null) {
                    return response;
                }
            }
    
            // 3-4 db�� �����ִ� employee delete
            while (dbIndex < dbEmployees.size()) {
                response = this.deleteEmployee(dbEmployees.get(dbIndex));
                dbIndex++;
                
                // insert�� delete�� ���� �� response �� ����
                if (response != null) {
                    return response;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(Status.FAIL, "DB ó�� �� ���� �߻�");
        }
        
        System.out.println("Complete sync");
        
        return new ResponseData(Status.SUCCESS, "DB�� ���� ����");
    }
    
    /**
     * ���̺� �ִ� ���ڵ� �� �������� �޼���
     * @return ������ employee ����Ʈ ��ȯ, ���ܽ� null ��ȯ
     */
    private List<EmployeeDAO> selectAllEmployee() {
        dbManager.connect();
        
        System.out.println("Selecting all employee from db...");
        
        String sql = "SELECT * FROM employees_tb";
        
        List<EmployeeDAO> employees = new ArrayList<EmployeeDAO>();
        
        try {
            PreparedStatement statement = dbManager.getJdbcConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                EmployeeDAO em_tmp = new EmployeeDAO();
                
                em_tmp.setPk(String.valueOf(resultSet.getInt("id")));
                em_tmp.setDepartment(resultSet.getString("department"));
                em_tmp.setName(resultSet.getString("name"));
                em_tmp.setPosition(resultSet.getString("position"));
                em_tmp.setEnglishName(resultSet.getString("english_name"));
                em_tmp.setPhoneNumber(resultSet.getString("phone_number"));
                em_tmp.setEmail(resultSet.getString("email"));
                
                employees.add(em_tmp);
            }
            
            resultSet.close();
            statement.close();
            
            System.out.println("=== [SUCCESS] Selecing all complete ===");
        } catch (SQLException e) {
            System.out.println("=== [ERROR] while selecting all employee in db ===");
            e.printStackTrace();
            return null;
        } finally {
            if (!dbManager.checkJdbcConnectionIsClosed()) {
                dbManager.disconnect();    
            }
        }
       
        return employees;
    }
    
    /**
     * DB�� ���ڵ� �����ϴ� �޼���
     * @param employee
     * @return ���� �� ResponseData ��ȯ, ������ ��� null ��ȯ 
     */
    private ResponseData insertEmployee(EmployeeDAO employee) {
        dbManager.connect();
        
        System.out.println("Inserting to DB...");
        
        String sql = "INSERT INTO employees_tb (department, name, position, english_name, phone_number, email) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement statement = dbManager.getJdbcConnection().prepareStatement(sql);
            statement.setString(1, employee.getDepartment());
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getPosition());
            statement.setString(4, employee.getEnglishName());
            statement.setString(5, employee.getPhoneNumber());
            statement.setString(6, employee.getEmail());
            
            statement.executeUpdate();
            statement.close();
            
            dbManager.commit(); 
            
            System.out.println("=== [SUCCESS] Employee insert complete ===");
        } catch (SQLException e) {
            System.out.println("=== [ERROR] while inserting employee ===");
            e.printStackTrace();
            dbManager.rollback();
            
            return new ResponseData(Status.FAIL, "DB�� insert ���� ���� (���� ��� ���� ä���ּ���)");
        } finally {
            if (!dbManager.checkJdbcConnectionIsClosed()) {
                dbManager.disconnect();    
            }
        }
        
        return null;
    }
    
    /**
     * DB���� ���ڵ� �����ϴ� �޼��� 
     * @param employee
     * @return ���� �� ReponseData ��ȯ, ���� �� null ��ȯ
     */
    private ResponseData deleteEmployee(EmployeeDAO employee) {
        dbManager.connect();
        
        System.out.println("Deleting from DB...");
        
        String sql = "DELETE FROM employees_tb WHERE id = ?";
        
        try {
            PreparedStatement statement = dbManager.getJdbcConnection().prepareStatement(sql);
            statement.setString(1, employee.getPk());

            statement.executeUpdate();
            statement.close();
            
            dbManager.commit(); 
            
            System.out.println("=== [SUCCESS] employee delete complete ===");
        } catch (SQLException e) {
            System.out.println("=== [ERROR] while deleting employee ===");
            e.printStackTrace();
            dbManager.rollback();
            return new ResponseData(Status.FAIL, "DB���� delete ���� ����");
        } finally {
            if (!dbManager.checkJdbcConnectionIsClosed()) {
                dbManager.disconnect();    
            }
            
        }
        
        return null;
    }
}
