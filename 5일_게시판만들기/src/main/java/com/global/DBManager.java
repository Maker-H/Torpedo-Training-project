package com.global;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.exception.UserException;

public class DBManager {
    public static Logger logger = LogManager.getLogger(DBManager.class);
    
    private String driver;
    private String url;
    private String id;
    private String pwd;
    private Connection jdbcConnection;
    

    
    // ��� ���� �ʱ�ȭ
    public DBManager(){
        Properties properties = new Properties();
        String propertiesName = "/resources/db.properties";
        
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(propertiesName));
            logger.debug("DB Manager �ʱ�ȭ �Ϸ�");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new UserException("DBMangaer �ʱ�ȭ �� ���� �߻�", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UserException("DBMangaer �ʱ�ȭ �� ���� �߻�", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new UserException("DBMangaer �ʱ�ȭ �� ���� �߻�", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        this.driver = properties.getProperty("driver");
        this.url = properties.getProperty("url");
        this.id = properties.getProperty("id");
        this.pwd = properties.getProperty("pwd");
    }
    
    // Ŀ�� �ɼ� READ COMMITTED�� ����
    public void connect() {
            try {
                logger.debug("DB ���� ����...");
                
                if (jdbcConnection == null || jdbcConnection.isClosed()) {
                    Class.forName(driver);
                    
                    jdbcConnection = DriverManager.getConnection(url, id, pwd);
                    jdbcConnection.setAutoCommit(false);
                    jdbcConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                    
                    logger.debug("DB ���� ����");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new UserException("jdbc ����̹� �������� ����" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new UserException("db connection �� ���� �߻�" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
    }
    
    // Ŀ�� ���н� �ѹ� ����
    public void commit(){
        try {
            logger.debug("DB�� Ŀ�� ����...");
            
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.commit();
                
                logger.debug("db�� Ŀ�� �Ϸ�");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("db�� Ŀ�� �� ���� �߻�" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } 
    }
    
    public void rollback() {
     // �ѹ� ���� 
        try {
            logger.debug("Ʈ����� �ѹ� ����...");
            
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.rollback();
                
                logger.debug("Ʈ����� �ѹ� �Ϸ�");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("db �ѹ� �� ���� �߻�" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } 
    }

    public void disconnect(PreparedStatement ps){
        try {
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.setAutoCommit(true); // Ŀ�� �ɼ� �ٽ� ������� �ǵ�����
                jdbcConnection.close();
                
                if (ps != null) {
                    ps.close();
                    
                    logger.debug("db���� ���� ���� �Ϸ�");
                }
            }   
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("db�� ���� ���� �� ���� �߻�" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
    }
    
    public void disconnect(PreparedStatement ps, ResultSet rs){
        try {
            if (jdbcConnection != null && !jdbcConnection.isClosed()) {
                jdbcConnection.setAutoCommit(true); // Ŀ�� �ɼ� �ٽ� ������� �ǵ�����
                jdbcConnection.close();
                
                if (ps != null) {
                    ps.close();
                    
                    if (rs != null) {
                        rs.close();
                        logger.debug("db���� ���� ���� �Ϸ�");
                    }
                }
            }   
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("db�� ���� ���� �� ���� �߻�" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
    }
    
    
    public Connection getJdbcConnection() {
        return this.jdbcConnection;
    }
    
    public boolean checkJdbcConnectionIsClosed() {
        boolean isClosed = false;
        
        try {
            isClosed = this.jdbcConnection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("db���� ���� Ȯ�� �� ���� �߻�" , HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        return isClosed;
    }
    
}
