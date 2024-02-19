package com.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import com.exception.LoginException;
import com.global.DBManager;
import com.global.ResponseData;
import com.global.Status;
import com.vo.UserVO;

public class UserDAO {
    private static UserDAO dao = new UserDAO();
    private DBManager dbManager = new DBManager();
    private HashMap<Status, Object> returnMap = new HashMap<>();
    
    private UserDAO() {
        returnMap.put(Status.FAIL, null);
        returnMap.put(Status.DATA, null);
    }
    
    public static UserDAO getInstance() {
        return dao;
    }
    
    public void userInsert(UserVO user){
        dbManager.connect();
        PreparedStatement statement = null;
        System.out.println("Inserting to DB...");
        
        String sql = "INSERT INTO user_tb (id, pwd) VALUES (?, ?)";
        
        try {
            statement = dbManager.getJdbcConnection().prepareStatement(sql);
            statement.setString(1, user.getId());
            statement.setString(2, user.getPwd());
            statement.executeUpdate();
            
            dbManager.commit(); 
            
            System.out.println("=== [SUCCESS] User insert complete ===");
        } catch (SQLException e) {
            System.out.println("=== [ERROR] while inserting user " + e + " ===");
            dbManager.rollback();
            throw new LoginException("DB�� insert ���� ���� (���� ��� ���� ä���ּ���)",500, e);
        } finally {
            if (!dbManager.checkJdbcConnectionIsClosed()) {
                dbManager.disconnect(statement);    
            }
        }
        
    }
    
    
}
