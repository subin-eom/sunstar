<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>장바구니</title>
<script>
	$(document).ready(function() {
		/*전체선택*/
		$("#allCheck").click(function() {
			var chk = $("#allCheck").prop("checked");
			if (chk) {
				$(".chBox").prop("checked", true);
			} else {
				$(".chBox").prop("checked", false);
			}
		});
		/*전체선택 해제*/
		$(".chBox").click(function() {
			$("#allCheck").prop("checked", false)	
			
		});
	});
</script>
<style>
div.allCheck{ height: 100%}
div.delBtn{ height: 100%}
.allCheck { float:left; width:200px; }
.allCheck input { width:16px; height:16px; }
.allCheck label { margin-left:10px; }
.delBtn { float:right; width:300px; text-align:right; }
.delBtn button { font-size:18px; padding:5px 10px; border:1px solid #eee; background:#eee; }

.checkBox { float:left; width:30px; }
.checkBox input { width:16px; height:16px; }

.totalInfo { background:#eee; width:100%;}
.totalInfo .sum { width:45%; font-size:22px; }

.totalInfo .buyit { width:45%; text-align:right; }
.totalInfo .buyit button { font-size:18px; padding:5px 10px; border:1px solid #999; background:#fff;}


</style>
</head>
<body>
	<div id="content" class="mx-auto col-6">
		<div class="m-3">
			<h2>장바구니 확인</h2>
		</div>
		
		<ul style="border: 1px solid silver">
			<li>
				<div class="allCheck ">
					<input type="checkbox" name="allCheck" id="allCheck" />
					<label for="allCheck">모두 선택</label>
				</div>

				<div class="delBtn">
					<button type="button" class="selectDelete_btn">선택 삭제</button>
				</div>
				
				
			</li>
			
			<hr class="my-4">
			
			 <c:set var="sum" value="0"/>
			 <c:forEach var="cartList" items="${cartList}">
				<li style="border-bottom: 1px solid silver">
					<div class="checkBox m-1">
						<input type="checkbox" name="chBox" class="chBox" data-cart_no="${cartList.cart_no}" />
					</div>

					<div class="thumb">
						<img src="${cartList.thumb_img}" />
					</div>
					<div class="productInfo m-2">
						<p>
							<span>상 품 명 </span>${cartList.product_name}<br/>
							<span>옵     션 </span>${cartList.option1}&#45;${cartList.option2} &#40;&#43;${cartList.add_price}원 &#41;<br/>
							<span>구입수량 </span>${cartList.cart_quantity} 개<br/> 
							<span>가     격 </span><fmt:formatNumber pattern="###,###,###" value="${(cartList.price + cartList.add_price)}"/>원<br/>
							<span>배  송 비 </span><fmt:formatNumber pattern="###,###,###" value="${cartList.shipping_cost}"/>원<br/> 
							<span>주문합계 </span><fmt:formatNumber pattern="###,###,###" value="${(cartList.price + cartList.add_price) * cartList.cart_quantity + cartList.shipping_cost}"/>원<br/>
						</p>

						<div class="delete">
							<button type="button" class="delete_btn"
								data-cart_no="${cartList.cart_no}">삭제</button>
						</div>
					</div>
				</li>
			 <c:set var="sum" value="${sum+(cartList.price + cartList.add_price) * cartList.cart_quantity + cartList.shipping_cost}"/>
			</c:forEach>
		</ul>
		
		<div class="totalInfo m-0 mb-4 py-5 pl-1 row">
		<div class="sum">
		총 결제 금액 : <fmt:formatNumber pattern="###,###,###" value="${sum}"/>원
		</div>
		<div class="buyit mr-0">
		<button type="button" class="buyit_btn">주문하기</button>
		</div>
		</div>
	</div>
</body>
</html>