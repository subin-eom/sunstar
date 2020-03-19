package com.sunstar.controller;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.api.client.util.Data;
import com.mysql.cj.xdevapi.JsonArray;
import com.sunstar.dto.CartDTO;
import com.sunstar.dto.CategoryDTO;
import com.sunstar.dto.OptionDTO;
import com.sunstar.dto.OrderDTO;
import com.sunstar.dto.ProductDTO;
import com.sunstar.dto.SellerDTO;
import com.sunstar.dto.DataDTO;
import com.sunstar.dto.MakePage;
import com.sunstar.service.FileUploadService;
import com.sunstar.service.SellerService;

@Controller
public class SellerController {

	@Autowired
	private SellerService sellerservice;
	@Autowired
	private FileUploadService fileservice;


	@RequestMapping("/seller")
	public String seller(Model model) {

		model.addAttribute("sellerpage", "temp_main.jsp");
		return "sellers/temp";
	}

	//상품 목록 보기
	@RequestMapping("/productlist")
	public String product(Model model,
			@RequestParam(required=false, defaultValue="1")int currPage,
			@RequestParam(required=false, defaultValue="10")int psize,
			@RequestParam(required=false, defaultValue="")String txt
			) {

		//페이징 및 검색
		/*		Pattern p = Pattern.compile("(^[0-9]*$)");

		if(txt != null || !txt.equals("")) {
			Matcher m = p.matcher(txt);
			if(!m.find()) {
				txt="";
				model.addAttribute("txt", txt);
			}else {
				model.addAttribute("txt",txt);
			}
		}*/

		//총 갯수 구하기
		int totalCount = sellerservice.totalCount(txt);
		System.out.println(totalCount);

		MakePage pa = new MakePage();
		pa.setSizePerPage(10); 
		if(psize == 0 ) {
			pa.setSizePerPage(10); 
		}else {
			pa.setSizePerPage(psize); 
		}

		int blockSize = 10;
		MakePage page = new MakePage(currPage, totalCount, pa.getSizePerPage(), blockSize);


		System.out.println(page.getStartRow());
		System.out.println(page.getEndRow());
		int a = ( page.getStartRow()  );


		page.setStartRow(a);

		System.out.println("입력psize: "+ psize);

		System.out.println(page.getSizePerPage());
		//	List<ProductDTO> list = sellerservice.productlist(category, txt, page.getStartRow(), page.getEndRow() );


		System.out.println("======page======");

		page.setStartRow(page.getStartRow()-1);

		System.out.println("현재페이지"+page.getCurrPage());
		System.out.println("총 갯수"+page.getTotalCount());
		System.out.println("1페이지당 표시개수"+page.getSizePerPage());
		System.out.println("블럭사이즈"+page.getBlockSize());
		System.out.println("시작줄"+page.getStartRow());
		System.out.println("끝줄"+page.getEndRow());
		System.out.println("시작블럭"+page.getStartBlock());
		System.out.println("끝블럭"+page.getEndBlock());
		System.out.println("이전있니"+page.isPrev());
		System.out.println("다음있니"+page.isNext());
		System.out.println("======page==end===");

		List<ProductDTO> list = sellerservice.productlist(page);
		System.out.println(list.get(1).getDetail_img1()+ "*********************************************");

		/*		for(ProductDTO a : list) {
			System.out.println("list의 이미지 및 설명 " + a.getDetail_img1() + " //  " + a.getExplains());

		}*/



		//	System.out.println("list ::"+list );
		model.addAttribute("list", list);

		model.addAttribute("page", page);


		//페이징 및  끝

		model.addAttribute("sellerpage", "productlist.jsp");
		return "sellers/temp";
	}

	//상품 상세보기
	@ResponseBody
	@RequestMapping("/detailview/{pcode}")
	public ProductDTO detailview(@PathVariable String pcode) {

		int pcd = Integer.parseInt(pcode);

		ProductDTO dto = sellerservice.viewProduct(pcd);
		System.out.println(dto);

		//페이지에 카테고리 표시
		List<CategoryDTO> clist = sellerservice.getCategory();
		List<CategoryDTO> dlist = new ArrayList<>();
		for(CategoryDTO a : clist) {
			a.setLv123(a.getLv1()+" - "+a.getLv2()+" - "+a.getLv3()); 
			dlist.add(a);

			dto.setCategorydto(dlist);
		}


		return dto;
	}


	@RequestMapping("/changePublicState/{changePublicState}/{pcodes}")
	public String changePublicState(@PathVariable String changePublicState, @PathVariable String pcodes) {
		String[] pcode = pcodes.split(",");
		System.out.println(changePublicState);

		for(String i : pcode) {
			ProductDTO dto = new ProductDTO();

			dto.setProduct_code(Integer.parseInt(i));

			if("publicStateTrue".equals(changePublicState)) {
				dto.setPublic_state(true);
				sellerservice.changePublicState(dto); 

			}else if("publicStateFalse".equals(changePublicState)) {
				dto.setPublic_state(false);
				sellerservice.changePublicState(dto); 

			}else if("reviewStateTrue".equals(changePublicState)) {
				dto.setReview_state(true);
				sellerservice.changeReviewState(dto); 

			}else if("reviewStateFalse".equals(changePublicState)) {
				dto.setReview_state(false);
				sellerservice.changeReviewState(dto); 
			}
		}
		return "redirect:/productlist";
	}

	//상품목록 출력
	@RequestMapping(value="/productExcel/")
	public void productExcel(HttpServletResponse response) throws Exception{
		List<ProductDTO> productList = new ArrayList<>();


		String seller_code = "11111";
		//	productList = sellerservice.list(seller_code);   //변경해야돼!!!!!!@@@@
		productList = sellerservice.list();

		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("상품 목록");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;


		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 노란색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		// 헤더 생성

		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("상품코드");

		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("상품이름");

		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("가격");

		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("원가");

		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("배송비");

		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("옵션1");

		cell = row.createCell(6);
		cell.setCellStyle(headStyle);
		cell.setCellValue("옵션2");

		cell = row.createCell(7);
		cell.setCellStyle(headStyle);
		cell.setCellValue("재고");
		cell = row.createCell(8);
		cell.setCellStyle(headStyle);
		cell.setCellValue("추가금액");
		cell = row.createCell(9);
		cell.setCellStyle(headStyle);
		cell.setCellValue("상품공개여부");
		cell = row.createCell(10);
		cell.setCellStyle(headStyle);
		cell.setCellValue("리뷰공개여부");

		// 데이터 부분 생성

		for(ProductDTO dto : productList) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getProduct_code());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getProduct_name());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getPrice());
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getCost());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getShipping_cost());

			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getOption1());
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getOption2());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getInventory());
			cell = row.createCell(8);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getAdd_price());



			cell = row.createCell(9);
			cell.setCellStyle(bodyStyle);
			String public_state = "";
			if(dto.isPublic_state()==true) {
				public_state = "공개";
			}else {
				public_state = "비공개";
			}
			cell.setCellValue(public_state);


			cell = row.createCell(10);
			cell.setCellStyle(bodyStyle);

			String review_state = "";
			if(dto.isPublic_state()==true) {
				review_state = "공개";
			}else {
				review_state = "비공개";
			}
			cell.setCellValue(review_state);
		}
		// 컨텐츠 타입과 파일명 지정

		response.setContentType("ms-vnd/excel");
		int a = 2;
		String b = Integer.toString(a);


		SimpleDateFormat frm = new SimpleDateFormat("yyyyMMddHHmmss");

		Date date = new Date();

		String time1 = frm.format(date);

		String filename = "attachment;filename="+time1+".xls";

		response.setHeader("Content-Disposition", filename );

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}


	//상품 추가 하기
	@RequestMapping("/addproduct")
	public String addproduct(Model model) {

		//페이지에 카테고리 표시
		List<CategoryDTO> clist = sellerservice.getCategory();
		List<CategoryDTO> dlist = new ArrayList<>();
		for(CategoryDTO dto : clist) {
			dto.setLv123(dto.getLv1()+" - "+dto.getLv2()+" - "+dto.getLv3()); 
			dlist.add(dto);
		}
		model.addAttribute("dlist", dlist);

		model.addAttribute("sellerpage", "addsproduct.jsp");
		return "sellers/temp";
	}

	//상품 추가 결과
	@RequestMapping(value = "/addproductresult", method = {RequestMethod.GET, RequestMethod.POST},
			headers = ("content-type=multipart/*"))
	public String addproductresult(ProductDTO dto, HttpServletRequest req, Model model) {

		//multipart 파일을 multi에 담아줌
		MultipartFile multi = dto.getAthumb_img();
		MultipartFile multi1 = dto.getAdetail_img1();		
		MultipartFile multi2 = dto.getAdetail_img2();
		MultipartFile multi3 = dto.getAdetail_img3();

		System.out.println(multi);
		System.out.println(multi1);
		System.out.println(multi2);
		System.out.println(multi3);
		SimpleDateFormat frm = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time1 = frm.format(date);
		String thumb = "thumb_"+time1+".jpg";
		String detail1 = "detail1_" + time1+".jpg";
		String detail2 = "detail2_" + time1+".jpg";
		String detail3 = "detail3_" + time1+".jpg";

		try {
			//저장 경로 구하기
			String uploadpath = "C:\\finalgit\\sunstar\\src\\main\\webapp\\resources\\product_img";
			System.out.println(uploadpath);

			//파일이 비어있지 않다면!
			if(!multi.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file = new File(uploadpath, multi.getOriginalFilename());
				multi.transferTo(file);
				String new_file_url_name = uploadpath+"/"+thumb;
				File file2 = new File(new_file_url_name);
				file.renameTo(file2);
				dto.setThumb_img("resources\\product_img\\" + thumb);
			}

			if(!multi1.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file1 = new File(uploadpath, multi1.getOriginalFilename());
				multi1.transferTo(file1);
				String new_file_url_name = uploadpath+"/"+detail1;
				File nfile1 = new File(new_file_url_name);
				file1.renameTo(nfile1);
				dto.setDetail_img1("resources\\product_img\\" + detail1);
			}

			if(!multi2.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file2 = new File(uploadpath, multi2.getOriginalFilename());
				multi2.transferTo(file2);
				String new_file_url_name = uploadpath+"/"+detail2;
				File nfile2 = new File(new_file_url_name);
				file2.renameTo(nfile2);
				dto.setDetail_img2("resources\\product_img\\" + detail2);
			}

			if(!multi3.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file3 = new File(uploadpath, multi3.getOriginalFilename());
				multi3.transferTo(file3);
				String new_file_url_name = uploadpath+"/"+detail3;
				File nfile3 = new File(new_file_url_name);
				file3.renameTo(nfile3);
				dto.setDetail_img3("resources\\product_img\\" + detail3);
			}


		}catch(IOException e){
			e.getMessage();

		}
		sellerservice.addProduct(dto);
		System.out.println(dto);


		System.out.println(dto.getExplains());

		return "redirect:/productlist";
	}

	//상품 삭제
	@RequestMapping("/deleteproduct/{pcode}")
	public String deleteproduct(@PathVariable int pcode ) {

		sellerservice.deleteProduct(pcode);
		return "redirect:/productlist";
	}


	private List<Integer> plist = new ArrayList<>();

	//상품 수정  //일괄 업데이트 테스트하다가 일시중지
	@RequestMapping("/updateinventory/{pcode}/{gesu}")
	public String updateinventory(@PathVariable int pcode, @PathVariable int gesu){

		Object a = (Object)pcode;

		if(!plist.contains(a)) {
			plist.add(pcode);
			System.out.println("성공" + pcode);
		}else {
			System.out.println("실패" + pcode);
		}


		System.out.println("size: "+plist.size());

		for(int i=0;i<plist.size();i++) {
			System.out.println("index  -------- "+i);
			System.out.println("value ----------"+plist.get(i));
		}

		/*System.out.println("////");
		System.out.println(plist);
		System.out.println("////");
		for(int i=0;i<plist.size();i++) {

			int product = plist.get(i);		
			List<String> color = sellerservice.getOptioncolor(product);

			for(String aaa : color) {
				System.out.println("--------------시작--");
				System.out.println(pcode);
				System.out.println(aaa);
				System.out.println("---------------끝-");
				OptionDTO dto = new OptionDTO();
				dto.setProduct_code(pcode);
				dto.setInventory(gesu);
				dto.setColor(aaa);
				sellerservice.updateinventory(dto);
			}
		}*/

		//	System.out.println("-----------pcode---시작--");
		//	System.out.println(pcode);
		//	System.out.println("-----------pcode----끝-");


		//	List<String> color = sellerservice.getOptioncolor(pcode);

		//	System.out.println("color 출력" + color);	*/
		/*	for(String a : color) {
			System.out.println("--------------시작--");

			System.out.println(pcode);
			System.out.println(a);
			System.out.println("---------------끝-");
			OptionDTO dto = new OptionDTO();
			dto.setProduct_code(pcode);
			dto.setInventory(gesu);
			dto.setColor(a);
			sellerservice.updateinventory(dto);
		}*/

		return "redirect:/productlist";
	}


	//주문 목록
	@RequestMapping("/orders")
	public String orders(Model model) {

		List<OrderDTO> orderlist = sellerservice.orderlist();
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("sellerpage", "orders.jsp");
		return "sellers/temp";

	}

	//주문목록 출력
	@RequestMapping(value="/orderexcel/{state}")
	public void orderexcel(HttpServletResponse response, @PathVariable String state) throws Exception{
		List<OrderDTO> orderlist = new ArrayList<>();

		String[] data = state.split(",");

		System.out.println("state"+state);
		System.out.println("data"+data.toString());


		//배송대기 출력
		if("shipping_list".equals(state)) {
			orderlist =  sellerservice.shippinglist();

			//전체출력
		}else if("all".equals(state)){
			orderlist = sellerservice.orderlist();

			//선택한 값 출력
		}else {
			for(String order_code : data) {	     
				System.out.print("order_code : ");
				System.out.println(order_code);
				orderlist.add(sellerservice.theOrderlist(order_code));
			}
		}

		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("주문 목록");
		Row row = null;
		Cell cell = null;
		int rowNo = 0;


		// 테이블 헤더용 스타일
		CellStyle headStyle = wb.createCellStyle();

		// 가는 경계선을 가집니다.
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 배경색은 노란색입니다.
		headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// 데이터는 가운데 정렬합니다.
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		// 데이터용 경계 스타일 테두리만 지정
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		// 헤더 생성

		row = sheet.createRow(rowNo++);
		cell = row.createCell(0);
		cell.setCellStyle(headStyle);
		cell.setCellValue("주문번호");

		cell = row.createCell(1);
		cell.setCellStyle(headStyle);
		cell.setCellValue("상품코드");

		cell = row.createCell(2);
		cell.setCellStyle(headStyle);
		cell.setCellValue("옵션");

		cell = row.createCell(3);
		cell.setCellStyle(headStyle);
		cell.setCellValue("구매수량");

		cell = row.createCell(4);
		cell.setCellStyle(headStyle);
		cell.setCellValue("금액");

		cell = row.createCell(5);
		cell.setCellStyle(headStyle);
		cell.setCellValue("특이사항");

		cell = row.createCell(6);
		cell.setCellStyle(headStyle);
		cell.setCellValue("결제방법");

		cell = row.createCell(7);
		cell.setCellStyle(headStyle);
		cell.setCellValue("구매자 이름");
		cell = row.createCell(8);
		cell.setCellStyle(headStyle);
		cell.setCellValue("연락처");
		cell = row.createCell(9);
		cell.setCellStyle(headStyle);
		cell.setCellValue("배송지");
		cell = row.createCell(10);
		cell.setCellStyle(headStyle);
		cell.setCellValue("우편번호");
		cell = row.createCell(11);
		cell.setCellStyle(headStyle);
		cell.setCellValue("주문 상태");


		// 데이터 부분 생성

		for(OrderDTO dto : orderlist) {
			row = sheet.createRow(rowNo++);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getOrder_code());
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getProduct_code());
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getOption1()+"/"+dto.getOption2()+"/");

			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getQuantity());
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getPrice());
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getMessage());
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getOrder_way());
			cell = row.createCell(7);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getName());
			cell = row.createCell(8);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getTel());



			cell = row.createCell(9);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getShipping_addr1()+" "+dto.getShipping_addr2()+" "+dto.getShipping_addr3());
			cell = row.createCell(10);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getShipping_zip());
			cell = row.createCell(11);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue(dto.getDelivery_state());

		}
		// 컨텐츠 타입과 파일명 지정

		response.setContentType("ms-vnd/excel");
		int a = 2;
		String b = Integer.toString(a);


		SimpleDateFormat frm = new SimpleDateFormat("yyyyMMddHHmmss");

		Date date = new Date();

		String time1 = frm.format(date);

		String filename = "attachment;filename="+time1+".xls";

		response.setHeader("Content-Disposition", filename );

		// 엑셀 출력
		wb.write(response.getOutputStream());
		wb.close();
	}

	//주문목록 단계변경
	@RequestMapping("/view_step/{stp}")
	public String viewStep(Model model, @PathVariable String stp) {

		System.out.println("stp" +stp);

		String view_step = "";


		if("step3".equals(stp)) {				//배송준비
			view_step = "배송준비";
		}else if("step4".equals(stp)) {		//배송중
			view_step = "배송중";
		}else if("step5".equals(stp)) {		//배송완료
			view_step = "배송완료";
		}else if("step7".equals(stp)) {		//반품대기
			view_step = "반품대기";
		}else if("step8".equals(stp)) {		//반품완료
			view_step = "반품완료";
		}else if("step9".equals(stp)) {		//교환요청
			view_step = "교환요청";
		}else if("step10".equals(stp)) {				//반송대기
			view_step = "반송대기";
		}else if("step11".equals(stp)) {				//반송완료 및 배송준비
			view_step = "반송완료 및 배송준비";
		}else if("step12".equals(stp)) {				//반품배송중
			view_step = "반품배송중";
		}else if("step13".equals(stp)) {				//배송 및 교환완료
			view_step = "배송 및 교환완료";
		}else if("step15".equals(stp)) {				//결제취소(판매자사유)
			view_step = "결체취소(판매자사유)";
		}

		List<OrderDTO> list = sellerservice.viewStepOrder(view_step);
		model.addAttribute("orderlist", list);


		model.addAttribute("sellerpage", "orders.jsp");
		return "sellers/temp";		
	}

	//주문목록 단계변경
	@RequestMapping("/change_step/{stp}/{wantChangeOrderCode}")
	public String changeState(@PathVariable String stp, @PathVariable String wantChangeOrderCode) {

		System.out.println("stp" +stp);

		String change_step = "";
		String[] ordercodes = wantChangeOrderCode.split(",");


		System.out.println("change_step" +  change_step);
		System.out.println("ordercodes" + ordercodes);
		System.out.println("wantChange..." + wantChangeOrderCode);

		if("step3".equals(stp)) {				//배송준비
			change_step = "배송준비";
		}else if("step4".equals(stp)) {		//배송중
			change_step = "배송중";
		}else if("step5".equals(stp)) {		//배송완료
			change_step = "배송완료";
		}else if("step7".equals(stp)) {		//반품대기
			change_step = "반품대기";
		}else if("step8".equals(stp)) {		//반품완료
			change_step = "반품완료";
		}else if("step9".equals(stp)) {		//교환요청
			change_step = "교환요청";
		}else if("step10".equals(stp)) {				//반송대기
			change_step = "반송대기";
		}else if("step11".equals(stp)) {				//반송완료 및 배송준비
			change_step = "반송완료 및 배송준비";
		}else if("step12".equals(stp)) {				//반품배송중
			change_step = "반품배송중";
		}else if("step13".equals(stp)) {				//배송 및 교환완료
			change_step = "배송 및 교환완료";
		}else if("step15".equals(stp)) {				//결제취소(판매자사유)
			change_step = "결체취소(판매자사유)";
		}

		for(String ordercode : ordercodes) {	     
			OrderDTO dto = new OrderDTO();
			dto.setOrder_code(ordercode);
			dto.setDelivery_state(change_step);

			System.out.println(dto.getOrder_code());
			System.out.println(dto.getDelivery_state());
			sellerservice.changeStep(dto);
		}
		return "redirect:/orders";
	}


	//운송장번호 입력(업데이트)
	@RequestMapping("/updateTracking/{codes}/{trackings}")
	public String updateTracking(@PathVariable String codes, @PathVariable String trackings) {

		String[] code = codes.split(",");  
		String[] tracking = trackings.split(",");


		for(int i = 0 ; i < code.length  ; i++) {
			OrderDTO dto = new OrderDTO();
			dto.setOrder_code(code[i]);
			dto.setTracking_no(tracking[i]);

			sellerservice.updateTracking(dto);
		}

		return "redirect:/orders";
	}


	/*	@RequestMapping("/updateTracking")
	public String updateTracking(@RequestParam(required=false) List<String> ordercode, 
			@RequestParam(required=false) List<String> tracking_no ) {

		System.out.println(ordercode);
		System.out.println(tracking_no);

		return "redirect:/orders";


	}*/


	//통계 연결
	@RequestMapping("/charts1")
	public String daylist(Model model) {

		//일별
		String order_code = "20200317";
		int orderSum = 0;
		
		List<OrderDTO> daylist = sellerservice.getDayProfit(order_code);
		
		for(OrderDTO dto : daylist) {
			System.out.println();
			System.out.println(dto);
			System.out.println();
			orderSum += (dto.getPrice() + dto.getAdd_price() + dto.getShipping_cost() * dto.getQuantity());
		}
		
		//하루 수익 (가격 + 추가금액 + 
		System.out.println(orderSum);
		
//		상품1개	price + add_price + shippingcost ) * cart_quantity
//		sum += price + add_price + shippingcost ) * cart_quantity
// 		sum  == 총결제금액
		
//		price 
		//월별
		
		
		
		
		//상품별
		
		model.addAttribute("sellerpage", "charts.jsp");
		return "sellers/temp";
	}
	
	// 월별
	@RequestMapping("/charts")
	public String monthlist(Model model) {
		
		String order_code = "202003";
		int orderSum = 0;

		List<OrderDTO> monthlist = sellerservice.getDayProfit(order_code);
		
		for(OrderDTO dto : monthlist) {
			System.out.println();
			System.out.println(dto);
			System.out.println();
			orderSum += (dto.getPrice() + dto.getAdd_price() + dto.getShipping_cost() * dto.getQuantity());
		}

		//3월수익
		System.out.println(orderSum);

		
		model.addAttribute("sellerpage", "charts.jsp");
		return "sellers/temp";
	}
	

	//정산 신청
	@RequestMapping("/requestaccounting")
	public String requestaccounting(Model model) {



		model.addAttribute("sellerpage", "requestaccounting.jsp");
		return "sellers/temp";
	}

	//정산 리스트
	@RequestMapping("/accounting")
	public String accounting(Model model) {

		model.addAttribute("sellerpage", "accounting.jsp");
		return "sellers/temp";
	}

	//판매자 정보
	@RequestMapping("/sellerinfo")
	public String sellerinfo(Model model) {

		SellerDTO dto = sellerservice.sellerInfo();

		dto.setSeller_addr(dto.getSeller_address1()+" "+dto.getSeller_address2()+" "+dto.getSeller_address3());

		model.addAttribute("dto", dto);
		model.addAttribute("sellerpage", "seller_info.jsp");
		return "sellers/temp";
	}

	@RequestMapping("/changeInfo/{colname}/{newvalues}")
	public String changeInfo(@PathVariable String colname, @PathVariable String newvalues ) {

		String[] col = colname.split(",");
		String[] vals = newvalues.split(",");

		System.out.println("--col / vals");
		System.out.println(col[0]);
		System.out.println(vals[0]);


		System.out.println("--col / vals");
		String val ="";


		for(int i=0; i<col.length;i++) {
			SellerDTO dto = new SellerDTO();

			if("seller_tel".equals(col[i])) {
				dto.setSeller_tel(vals[i]);
			}else if("seller_email".equals(col[i])) {
				dto.setSeller_email(vals[i]);
			}else if("seller_addr".equals(col[i])) {
				dto.setSeller_address1(vals[i]);
			}else if("seller_zip".equals(col[i])){
				dto.setSeller_zip(vals[i]);
			}else if("bank".equals(col[i])) {
				dto.setBank(vals[i]);
			}else if("bank_no".equals(col[i])) {
				dto.setBank_no(vals[i]);
			}else if("seller_deadline".equals(col[i])) {
				dto.setSeller_deadline(vals[i]);
			}else if("shipping_company".equals(col[i])) {
				dto.setShipping_company(vals[i]);
			}else if("basic_shipping_cost".equals(col[i])) {
				dto.setBasic_shipping_cost(vals[i]);
			}

			System.out.println("vals i " + vals[i]);
			System.out.println("val = " + val);
			dto.setState(col[i]);
			dto.setVal(val);
			sellerservice.changeInfo(dto);
		}
		return "redirect:/sellerinfo";
	}



	//판매자 설정
	@RequestMapping("/sellersetting")
	public String sellersetting(Model model) {
		SellerDTO dto = sellerservice.sellerInfo();
		model.addAttribute("dto", dto);
		System.out.println(dto.getComm_img1());
		model.addAttribute("sellerpage", "seller_setting.jsp");
		return "sellers/temp";
	}

	//판매자 설정 리절트
	@RequestMapping(value="/settingUpdate", method = {RequestMethod.GET, RequestMethod.POST},
			headers = ("content-type=multipart/*"))
	public String settingUpdate(Model model, SellerDTO dto) {

		//
		//multipart 파일을 multi에 담아줌
		MultipartFile multi = dto.getAseller_bgcolor();
		MultipartFile multi1 = dto.getAcomm_img1();
		MultipartFile multi2 = dto.getAcomm_img2();
		MultipartFile multi3 = dto.getAcomm_img3();

		System.out.println(multi);
		System.out.println(multi1);
		System.out.println(multi2);
		System.out.println(multi3);
		SimpleDateFormat frm = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time1 = frm.format(date);
		String bgcolor = "bgcolor"+time1+".jpg";
		String comm1 = "comm_img1_" + time1+".jpg";
		String comm2 = "comm_img2_" + time1+".jpg";
		String comm3 = "comm_img3_" + time1+".jpg";

		try {
			//저장 경로 구하기
			String uploadpath = "C:\\finalgit\\sunstar\\src\\main\\webapp\\resources\\comm_img";
			System.out.println(uploadpath);

			//파일이 비어있지 않다면!
			if(!multi.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file = new File(uploadpath, multi.getOriginalFilename());
				multi.transferTo(file);
				String new_file_url_name = uploadpath+"/"+bgcolor;
				File file2 = new File(new_file_url_name);
				file.renameTo(file2);
				dto.setSeller_bgcolor("resources\\comm_img\\" + bgcolor);

			}

			if(!multi1.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file1 = new File(uploadpath, multi1.getOriginalFilename());
				multi1.transferTo(file1);
				String new_file_url_name = uploadpath+"/"+comm1;
				File nfile1 = new File(new_file_url_name);
				file1.renameTo(nfile1);
				dto.setComm_img1("resources\\comm_img\\" + comm1);
			}

			if(!multi2.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file2 = new File(uploadpath, multi2.getOriginalFilename());
				multi2.transferTo(file2);
				String new_file_url_name = uploadpath+"/"+comm2;
				File nfile2 = new File(new_file_url_name);
				file2.renameTo(nfile2);
				dto.setComm_img2("resources\\comm_img\\" + comm2);
			}

			if(!multi3.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file3 = new File(uploadpath, multi3.getOriginalFilename());
				multi3.transferTo(file3);
				String new_file_url_name = uploadpath+"/"+comm3;
				File nfile3 = new File(new_file_url_name);
				file3.renameTo(nfile3);
				dto.setComm_img3("resources\\comm_img\\" + comm3);
			}


		}catch(IOException e){
			e.getMessage();

		}

		System.out.println(dto);

		sellerservice.update_seller_info(dto);
		System.out.println(dto.getComm_img1());
		System.out.println(dto.getComm_img2());
		System.out.println(dto.getComm_img3());


		model.addAttribute("sellerpage", "seller_setting.jsp");
		return "sellers/temp";
	}




	//판매자별 상품리스트
	@RequestMapping("/seller_list")
	public String seller_list(Model model) {

		List<ProductDTO> pdto = sellerservice.list();
		SellerDTO sdto = sellerservice.sellerInfo();
		List<ProductDTO> productlist = sellerservice.product_list_user();

		System.out.println(sdto.getSeller_code());
		System.out.println(sdto.getSeller_color());

		model.addAttribute("productlist",productlist);

		model.addAttribute("pdto", pdto);
		model.addAttribute("sdto", sdto);
		model.addAttribute("contentpage", "sellers/sellers_list.jsp");
		return "home";
	}


	//dataTable
	@RequestMapping("/dataTableTest")
	public String datatable_test(Model model) {
		List<OrderDTO> orderlist = sellerservice.orderlist();
		model.addAttribute("olist", orderlist);

		model.addAttribute("sellerpage", "datatableTest.jsp");
		return "sellers/temp";

	}

	@RequestMapping("/datatest")
	@ResponseBody
	public List<OrderDTO> ajaxtest(){
		List<OrderDTO> data = sellerservice.orderlist();

		return data;
	}


	@RequestMapping("/datatest2")
	@ResponseBody
	public DataDTO bbbtest(){
		List<OrderDTO> data = sellerservice.orderlist();

		DataDTO dto = new DataDTO();
		dto.setAaData(data);
		return dto;
	}

	@RequestMapping("/order")
	public String order(Model model) {

		List<OrderDTO> orderlist = sellerservice.orderlist();
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("sellerpage", "order.jsp");
		return "sellers/temp";
	}

	@RequestMapping(value="/productUpdate", method = {RequestMethod.GET, RequestMethod.POST},
			headers = ("content-type=multipart/*"))
	public String productUpdate(ProductDTO dto) {

		//multipart 파일을 multi에 담아줌
		MultipartFile multi = dto.getAthumb_img();
		MultipartFile multi1 = dto.getAdetail_img1();		
		MultipartFile multi2 = dto.getAdetail_img2();
		MultipartFile multi3 = dto.getAdetail_img3();

		System.out.println(multi);
		System.out.println(multi1);
		System.out.println(multi2);
		System.out.println(multi3);
		SimpleDateFormat frm = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time1 = frm.format(date);
		String thumb = "thumb_"+time1+".jpg";
		String detail1 = "detail1_" + time1+".jpg";
		String detail2 = "detail2_" + time1+".jpg";
		String detail3 = "detail3_" + time1+".jpg";

		try {
			//저장 경로 구하기
			String uploadpath = "C:\\finalgit\\sunstar\\src\\main\\webapp\\resources\\product_img";
			System.out.println(uploadpath);

			//파일이 비어있지 않다면!
			if(!multi.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file = new File(uploadpath, multi.getOriginalFilename());
				multi.transferTo(file);
				String new_file_url_name = uploadpath+"/"+thumb;
				File file2 = new File(new_file_url_name);
				file.renameTo(file2);
				dto.setThumb_img("resources\\product_img\\" + thumb);
			}

			if(!multi1.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file1 = new File(uploadpath, multi1.getOriginalFilename());
				multi1.transferTo(file1);
				String new_file_url_name = uploadpath+"/"+detail1;
				File nfile1 = new File(new_file_url_name);
				file1.renameTo(nfile1);
				dto.setDetail_img1("resources\\product_img\\" + detail1);
			}

			if(!multi2.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file2 = new File(uploadpath, multi2.getOriginalFilename());
				multi2.transferTo(file2);
				String new_file_url_name = uploadpath+"/"+detail2;
				File nfile2 = new File(new_file_url_name);
				file2.renameTo(nfile2);
				dto.setDetail_img2("resources\\product_img\\" + detail2);
			}

			if(!multi3.isEmpty()) {
				//파일 = 새파일(경로, 파일이름);
				File file3 = new File(uploadpath, multi3.getOriginalFilename());
				multi3.transferTo(file3);
				String new_file_url_name = uploadpath+"/"+detail3;
				File nfile3 = new File(new_file_url_name);
				file3.renameTo(nfile3);
				dto.setDetail_img3("resources\\product_img\\" + detail3);
			}

			if(dto.isPublic_state()== true) {
				dto.setPublic_state(true);
			}else if(dto.isPublic_state() != true) {
				dto.setPublic_state(false);
			}else if(dto.isReview_state() == true) {
				dto.setReview_state((true));
			}else if(dto.isReview_state() != true) {
				dto.setReview_state((false));
			}
			
		}catch(IOException e){
				e.getMessage();
		}


		sellerservice.updateProduct(dto);
		System.out.println(">>> \n"+ dto +"\n<<<");
		return "redirect:/productlist";
		

		
	}
}




