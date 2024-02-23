package com.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.exception.CustomException;
import com.global.HttpUtil;
import com.service.ArticleService;
import com.vo.UserVO;

public class ArticleRegisterController implements Controller {
    
    public static Logger logger = LogManager.getLogger(ArticleRegisterController.class);
    
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        logger.debug("게시글 등록 시작");
        
        HttpSession session = req.getSession();
        UserVO user = (UserVO) session.getAttribute("user");
        // 세션이 존재하지 않는 경우 리다이렉트
        if (user == null) {
            throw new CustomException("다시 로그인 해주세요" , HttpServletResponse.SC_BAD_REQUEST, "login.jsp");
        }
        
        logger.debug("user " + user.getId() + " 세션 받아오기 완료");
        
        String title = null;
        String content = null;
        List<byte[]> files = new ArrayList<byte[]>();
        
        byte[] fileBytes = null;
        
        // multipart 헤더 맞는지 확인
        String contentType = req.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            Collection<Part> parts = req.getParts();
            
            for (Part part : parts) {
                // 파일인 경우 
                if (part.getHeader("Content-Disposition").contains("filename=")) {
                   Part file = req.getPart(part.getName());
                   fileBytes = readPart(file);
                   files.add(fileBytes);
                   
                // 파일이 아닌 경우
                } else {
                  String formValue = req.getParameter(part.getName());  
                  switch(part.getName()) {
                      case "title":
                          title = formValue;
                          break;
                      
                      case "content":
                          content = formValue;
                          break;
                          
                      default:
                          throw new CustomException("잘못된 요청입니다", HttpServletResponse.SC_BAD_REQUEST, "post.jsp");
                  }
                }
               
            }
        } //end of if
        
        
        ArticleService articleService = ArticleService.getInstance();
        articleService.registerArticle(user, title, content, files);
    
        HttpUtil.forward(req, res, "index.jsp");
    }
    
    private byte[] readPart(Part part) throws IOException {
        InputStream inputStream = part.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        byte[] buffer = new byte[1024];
        int bytesRead;
     
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        
        return outputStream.toByteArray();
    }

}
