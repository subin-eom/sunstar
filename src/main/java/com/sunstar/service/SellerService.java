package com.sunstar.service;

import java.util.List;

import com.sunstar.dto.AccountsDTO;
import com.sunstar.dto.CartDTO;
import com.sunstar.dto.CategoryDTO;
import com.sunstar.dto.ChartDTO;
import com.sunstar.dto.MakePage;
import com.sunstar.dto.OptionDTO;
import com.sunstar.dto.OrderDTO;
import com.sunstar.dto.ProductDTO;
import com.sunstar.dto.QnaDTO;
import com.sunstar.dto.ReviewDTO;
import com.sunstar.dto.ReviewImgDTO;
import com.sunstar.dto.SellerDTO;

public interface SellerService {

	List<CategoryDTO> getCategory();

	int getCategoryCount();

	void addProduct(ProductDTO dto);

	List<ProductDTO> list();

	void deleteProduct(int data);

	List<String> getOptioncolor(int pcode);
	
	void updateinventory(OptionDTO dto);

	List<OrderDTO> orderlist(String seller_code);

	List<OrderDTO> shippinglist(String seller_code);

	OrderDTO theOrderlist(String order_no);

	void changeStep(OrderDTO dto);

	void updateTracking(OrderDTO dto);

	SellerDTO sellerInfo(String seller_code);

	void changePublicState(ProductDTO dto);
	
	void changeReviewState(ProductDTO dto);

	void changeInfo(SellerDTO dto);

	int totalCount(String txt);

	List<ProductDTO> productlist(MakePage page);

	void update_seller_info(SellerDTO dto);

	List<ProductDTO> product_list_user(String seller_code);

	List<OrderDTO> viewStepOrder(OrderDTO dto);

	List<ProductDTO> viewProduct(int pcd);

	void updateProduct(ProductDTO dto);

	List<OrderDTO> getDayProfit(String order_code);

	int getShipping_Cost(int seller_code);

	String getSellerCode(String id);
	
	int getReadyCount(String seller_code);

	int getExchangeCount(String seller_code);

	int getBringBack(String seller_code);

	int getWaitAnswer(String seller_code);

	List<QnaDTO> getQnaList(String seller_code);

	void qna_reply(QnaDTO dto);

	int getPaid(String seller_code);

	List<OrderDTO> searchOrderView(OrderDTO user);

	List<ReviewDTO> getReview(String seller_code);

	List<ReviewImgDTO> getReviewImgCount(int review_no);

	void review_del(int review_no);

	Integer month_chart(ChartDTO dto);
	Integer month_chart_plus(ChartDTO dto);

	Integer day_chart(ChartDTO dto);

	Integer day_chart_plus(ChartDTO daydto);

	void delete_products(String seller_code);

	void delete_auth(String id);

	void search_order_update(OrderDTO dto);

	List<ChartDTO> get_top5(String seller_code);

	List<ChartDTO> get_top5items(String seller_code);

	Integer getOrderCount(ChartDTO getOrderCount);

	List<ProductDTO> product_list_new(String seller_code);

	AccountsDTO getTotal_price(AccountsDTO setdto);

	void insertAccount(AccountsDTO dto);

	List<AccountsDTO> getAccountList(String seller_code);








}
