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
    
    <script>
        $(document).ready(function(){
            $("#addRow").click(function(){
                var markup = "<tr><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><input type='text'></td><td><button class='deleteRow'>����</button></td></tr>";
                $("#addressBook").append(markup);
            });
            $(document).on("click", ".deleteRow", function(){
                $(this).closest('tr').remove();
            });
        });
    </script>
</body>
</html>