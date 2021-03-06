<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<style>


</style>
</head>
<body>
	<div class="container-fluid">
		<!-- Page Heading -->
		<h1 class="h3 mb-2 text-gray-800">정산내역 확인</h1>
		<p class="mb-4">
			과거 정산 내역을 확인 하실 수 있습니다. <a target="_blank" href="https://datatables.net">추가 링크 삽입</a>.
		</p>

		<!-- DataTales Example -->
		<div class="card shadow mb-4">
			<div class="card-header py-3">
				<h6 class="m-0 font-weight-bold text-primary">정산내역 확인</h6>
			</div>
			<div class="card-body">
			<!-- 여기 내부만 수정하시면 됩니다  -->
			<table class="table">
				  <thead class="thead-light">

					<tr>
						<th scope="col">정산월</th>
						<th scope="col">판매금액</th>
						<th scope="col">수수료</th>
						<th scope="col">정산금액</th>
						<th scope="col">상태</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="l" items="${alist}">
					<tr>
					<td scope="row">${l.yyyymm}</td>
					
					<td><fmt:formatNumber pattern="###,###,###" value="${l.total_profit}" />원</td>
					<td><fmt:formatNumber pattern="###,###,###" value="${l.commission}" />원</td>
					<td style="color:red;"><fmt:formatNumber pattern="###,###,###" value="${l.balance_accounts}" />원</td>
					<td>${l.account_state}</td>
					
					</tr>
					</c:forEach>
				</tbody>
			
			
			
			
			</table>
			
					
					
			<!-- 여기 내부만 수정하시면 됩니다  -->
			</div>
		</div>
	</div>
</body>
</html>