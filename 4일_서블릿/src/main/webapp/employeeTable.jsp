<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="servletpj.Employee" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Address Book</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<table id="addressBook">
        <tr>
            <th>�μ�</th>
            <th>�̸�</th>
            <th>��å</th>
            <th>�����̸�</th>
            <th>�޴���</th>
            <th>�����ּ�</th>
            <th></th>
        </tr>
        <% 
            List<Employee> employees = (ArrayList<Employee>) request.getAttribute("employees");
            for(Employee employee : employees) { 
        %>
        <tr>
            <td><%= employee.getDepartment()%></td>
            <td><%= employee.getName()%></td>
            <td><%= employee.getPosition()%></td>
            <td><%= employee.getEnglishName()%></td>
            <td><%= employee.getPhoneNumber()%></td>
            <td><%= employee.getEmail()%></td>
            <td><button class="deleteRow">����</button></td>
        </tr>
        <% } %>
</table>
    <button id="addRow">�� �߰�</button>
    <button id="saveToXML">XML�� ����</button>
    <button id="saveRow">�� ����</button>
    <script language="JavaScript">
    	
        $(document).ready(function(){
        	// �� �߰�
			$("#addRow").click(function(){
			    var markup = "<tr id='row" + employees.length + "'><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><button class='deleteRow'>����</button></td></tr>";
			    $("#addressBook").append(markup);
			});
        	
         	// �� ����
            $("#saveRow").click(function(){
            	var newEmployee = {
            	        department: $("#addressBook tr:last td:eq(0) input").val(),
            	        name: $("#addressBook tr:last td:eq(1) input").val(),
            	        position: $("#addressBook tr:last td:eq(2) input").val(),
            	        englishName: $("#addressBook tr:last td:eq(3) input").val(),
            	        phoneNumber: $("#addressBook tr:last td:eq(4) input").val(),
            	        email: $("#addressBook tr:last td:eq(5) input").val(),
            	        id: 'row' + employees.length // ���� id�� �����մϴ�.
            	    };
            	    employees.push(newEmployee);
            });
	           
	       // �� ����
			$(document).on("click", ".deleteRow", function(){
			    var id = $(this).closest('tr').attr('id'); // ������ ���� id�� �����ɴϴ�.
			    employees = employees.filter(function(employee) { // employees �迭���� �ش� id�� ���� �׸��� �����մϴ�.
			        return employee.id !== id;
			    });
			    $(this).closest('tr').remove(); // ���̺��� �ش� ���� �����մϴ�.
			});
				       
	       // XML�� ����
	       $("#saveToXML").click(function(){
	    	    var employees = [];
	    	    $("#addressBook tr").each(function() {
	    	        var department = $(this).find("td:eq(0)").text();
	    	        var name = $(this).find("td:eq(1)").text();
	    	        var position = $(this).find("td:eq(2)").text();
	    	        var englishName = $(this).find("td:eq(3)").text();
	    	        var phoneNumber = $(this).find("td:eq(4)").text();
	    	        var email = $(this).find("td:eq(5)").text();

	    	        var employee = {
	    	            department: department,
	    	            name: name,
	    	            position: position,
	    	            englishName: englishName,
	    	            phoneNumber: phoneNumber,
	    	            email: email
	    	        };
	    	        employees.push(employee);
	    	    });
	    	    
	    	    $.ajax({
	    	        url: '/SaveToXML', // ���� URL
	    	        method: 'POST',
	    	        data: JSON.stringify(employees),
	    	        contentType: 'application/json',
	    	        success: function(data) {
	    	            var blob = new Blob([data], {type: "text/xml"});
	    	            var link = document.createElement('a');
	    	            link.href = window.URL.createObjectURL(blob);
	    	            link.download = "employees.xml";
	    	            link.click();
	    	        }
	    	    });
        	});
        });
    </script>
</body>
</html>