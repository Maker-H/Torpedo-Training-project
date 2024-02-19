package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.exception.LoginException;
import com.global.HttpUtil;
import com.global.ResponseData;
import com.global.Status;
import com.service.UserService;
import com.vo.UserVO;

public class LoginController implements Controller{

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        String id = req.getParameter("id").trim();
        String pwd = req.getParameter("pwd").trim();
        
        if (id.isEmpty() || pwd.isEmpty()) {
            req.setAttribute("error", "모든 항목을 입력해주세요");
            HttpUtil.forward(req, res, "/userInsert.jsp");
        }
        
        UserVO user = new UserVO();
        user.setId(id);
        user.setPwd(pwd);
        
        UserService service = UserService.getInstance();
        service.userInsert(user);
        

        PrintWriter out = res.getWriter();
        req.setAttribute("id", id);
        HttpUtil.forward(req, res, "/result/memberInsertOutput.jsp");
    }

}
