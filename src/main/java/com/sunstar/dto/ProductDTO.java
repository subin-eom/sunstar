package com.sunstar.dto;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDTO {
	private int product_code;
	private String product_name;
	private int price;
	private int cost;
	private int shipping_cost;
	private String thumb_img;
	private String comm_img1;
	private String comm_img2;
	private String comm_img3;
	private String detail_img1;
	private String detail_img2;
	private String detail_img3;
	private String explains;
	private boolean public_state;
	private boolean review_state;
	private String category_code;
	private String seller_code;
	
	private List<OptionDTO> options;
	
	
}
