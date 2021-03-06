package com.sunstar.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SellerDTO {
	private String seller_code;
	private String id;
	private String seller_name;
	private String seller_tel;
	private String seller_email;
	private String seller_address1;
	private String seller_address2;
	private String seller_address3;
	private String seller_zip;
	private String bank;
	private String bank_no;
	private String business_license;
	private String seller_deadline;
	private String seller_addr;
	private String shipping_company;
	private String basic_shipping_cost;
	private String free_shipping_cost;
	private String state;
	private String val;

	private String comm_img1;
	private String comm_img2;
	private String comm_img3;
	
	private MultipartFile acomm_img1;
	private MultipartFile acomm_img2;
	private MultipartFile acomm_img3;
	private MultipartFile aseller_bgcolor;

	private String seller_logo;
	private String seller_color;
	private String seller_bgcolor;
	
	private String optcol;
	private String cart_no;
	
}

