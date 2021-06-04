<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/>
<jsp:setProperty name="bbs" property="bbsTitle"/>
<jsp:setProperty name="bbs" property="bbsContent"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" charset="UTF-8">

<title>JSP 게시판 웹사이트</title>
</head>
<body>
	<%
		String userID = null; //할당된 세션 아이디를 담을 수 있게함.
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		if (userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		} else {
			if (bbs.getBbsTitle() ==null || bbs.getBbsContent() == null){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('입력이 안된 상황이 있습니다.')");
					script.println("history.back()"); //뒤로가기로 사용자가 고칠수있도록 해줌
					script.println("</script>");
				} else { //인스턴스 만들기
					BbsDAO bbsDAO = new BbsDAO();
					int result = bbsDAO.write(bbs.getBbsTitle(),userID,bbs.getBbsContent());
					if (result == -1){ //db에 문제가 생긴경우
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('글쓰기에 실패했습니다.')");//Primarykey값이 부여되기 때문에 중복불가.
						script.println("history.back()"); //전페이지로 돌아감
						script.println("</script>");
					}
					else { //result-1이아닌경우는 전부다 성공한 경우. 
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("location.href = 'bbs.jsp'"); //완료되면 게시판으로 돌아감.
						script.println("</script>");
					}

				}
					
			
		}
		
	%>
</body>
</html>