package servletpj;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import response.ResponseData;
import response.Status;

/**
 * Servlet implementation class AddressBook
 */

@MultipartConfig
public class AddressBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    Part filePart = request.getPart("file");
	    
	    RequestDispatcher dispatcher = null;
	    
	    String fileName = filePart.getSubmittedFileName().trim();
        // ������ ���ε���� ���� ä�� ä�� �� ���
        if (fileName.isEmpty()) {
            ResponseData responseData = new ResponseData(Status.FAIL, "������ ���ε���� �ʾҽ��ϴ�. ������ ���ε� �� �������ּ���.");
            request.setAttribute("responseData", responseData);
            dispatcher = getServletContext().getRequestDispatcher("/");
            dispatcher.forward(request, response);
            return;
        }
        
	    InputStream fileContent = filePart.getInputStream();

        XMLParser parser = new XMLParser();
        List<Employee> employees;
        employees = parser.parseXML(fileContent); // ��Ʈ���� XMLParser�� ����
        
        
        // ���Ŀ� ���� �ʴ� XML ������ ���
        if (employees == null) { 
            ResponseData responseData = new ResponseData(Status.FAIL, "���Ŀ� ���� �ʴ� XML �����Դϴ�. ���� Ȯ�� �� ���ε����ּ���.");
            request.setAttribute("responseData", responseData);
            dispatcher = getServletContext().getRequestDispatcher("/");
        } else {
            request.setAttribute("employees", employees);
            dispatcher = getServletContext().getRequestDispatcher("/employeeTable.jsp");
        }

        dispatcher.forward(request, response);
    }

}
