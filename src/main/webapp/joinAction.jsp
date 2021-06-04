<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page"/>
<jsp:setProperty name="user" property="userID"/>
<jsp:setProperty name="user" property="userPassword"/>
<jsp:setProperty name="user" property="userName"/>
<jsp:setProperty name="user" property="userGender"/>
<jsp:setProperty name="user" property="userEmail"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" charset="UTF-8">

<title>JSP 게시판 웹사이트</title>
</head>
<body>
	<%
		String userID	=	null; //할당된 세션 아이디를 담을 수 있게함.
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		if (userID != null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인이 돠어있습니다.')");
			script.println("location.href = 'main.jsp'");
			script.println("</script>");
		}
		if (user.getUserID() ==null || user.getUserPassword() == null || user.getUserName() ==null
			|| user.getUserGender() ==null || user.getUserEmail() ==null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안된 상황이 있습니다.')");
			script.println("history.back()"); //뒤로가기로 사용자가 고칠수있도록 해줌
			script.println("</script>");
		} else {
			UserDAO userDAO = new UserDAO();
			int result = userDAO.join(user);
			if (result == -1){ //db에 문제가 생긴경우
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.')");//Primarykey값이 부여되기 때문에 중복불가.
				script.println("history.back()"); //전페이지로 돌아감
				script.println("</script>");
			}
			else { //result-1이아닌경우는 전부다 성공한 경우. 
				session.setAttribute("userID",user.getUserID());
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'main.jsp'"); //완료되면 메인페이지로 돌아감.
				script.println("</script>");
			}

		}
			
	%>
</body>
</html>