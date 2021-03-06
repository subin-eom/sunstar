package com.sunstar.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CartDTO {

	/*  create table cart( 
	 *   cart_no int(5) not null auto_increment 
	 * , id varchar(30) not null 
	 * , product_code int(9) not null 
	 * , cart_quantity varchar(30) not null
	 * , constraint cart_cart_no_id_pk primary key(cart_no, id) 
	 * , constraint cart_id_fk foreign key(id) references customer(id) ON DELETE CASCADE );
	 */
	private String cart_no;
	private String id;
	private int product_code;
	private String cart_quantity;
	
	private String product_name;
	private int price;
	private int cost;
	private int basic_shipping_cost;
	private int free_shipping_cost;
	
	private String thumb_img;
	private String category_code;
	private String seller_code;
	private String seller_name;
	private int inventory;
	private int total_price;
	
	private String option1;
	private String option2;
	private int add_price;
	
	private String name;
	private String email;
	private String tel;
	private String address1;
	private String address2;
	private String address3;
	private String zip;
	private int accumulation;
	private List<CartDTO> pdto;
}
