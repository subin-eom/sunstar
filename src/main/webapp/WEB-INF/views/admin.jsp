<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
admin 권한이 부여된 페이지로 자동 이동. 로그인시 홈으로 이동 가능.


<br>
<a href="all">all</a>
<br>
<a href="member">member</a>
<br>
<a href="manager">manager</a>
<br>
<a href="admin">admin</a>
<br>
<a href="${pageContext.request.contextPath}"><img alt="home" src="${pageContext.request.contextPath}/resources/img/logo.png"></a>
 
<br>
<a href="userlogout">로그아웃</a>
</body>
</html>