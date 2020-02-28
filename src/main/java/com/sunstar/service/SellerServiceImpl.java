package com.sunstar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunstar.dto.CategoryDTO;
import com.sunstar.dto.OptionDTO;
import com.sunstar.dto.ProductDTO;
import com.sunstar.mapper.SellerMapper;

@Service("sellerservice")
public class SellerServiceImpl implements SellerService {

	@Autowired
	private SellerMapper mapper;

	@Override
	public List<CategoryDTO> getCategory() {
		return mapper.getCategory();
	}

	@Override
	public int getCategoryCount() {
		// TODO Auto-generated method stub
		return mapper.getCategoryCount();
	}

	@Override
	public void addProduct(ProductDTO dto) {
		//��ǰ�ڵ� ����
		mapper.addProduct(dto);
		
		//�ɼ� ����ŭ �ݺ�
		for(int i =0; i<dto.getOptions().size(); i++) {
			//��ǰ�ڵ� �ҷ��ͼ�
		//	ProductDTO dto2 = mapper.aProduct();
			//�� ��ǰ�ڵ忡 �´� �ɼ� �߰�
			System.out.println("****");
			
			OptionDTO dto2 = new OptionDTO();
			dto2.setProduct_code(mapper.aProduct());
			dto2.setColor(dto.getOptions().get(i).getColor());
			dto2.setSize(dto.getOptions().get(i).getSize());
			dto2.setInventory(dto.getOptions().get(i).getInventory());
			dto2.setAdd_price(dto.getOptions().get(i).getAdd_price());
			mapper.addOptions(dto2);
		}

	}

	@Override
	public List<ProductDTO> list() {
		// TODO Auto-generated method stub
		return mapper.list();
	}

	@Override
	public void deleteProduct(int pcode) {
		//�ɼ��� ���̺����� ���� ����
		mapper.deleteProductOptions(pcode);
		
		//��ǰ����Ʈ ���̺����� ����
		mapper.deleteProduct(pcode);
		
	}
	@Override
	public List<String> getOptioncolor(int pcode) {
		List<String> list = mapper.getOptionColor(pcode);
		

		return list;
	}
	
	@Override
	public void updateinventory(OptionDTO dto) {
		mapper.updateInventory(dto);
	}


	
	
	
	
	
}