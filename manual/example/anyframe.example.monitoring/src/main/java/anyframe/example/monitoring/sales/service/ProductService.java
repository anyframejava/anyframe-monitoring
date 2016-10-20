package anyframe.example.monitoring.sales.service;

import anyframe.common.Page;
import anyframe.core.generic.service.GenericService;

import anyframe.example.monitoring.domain.Product;

public interface ProductService extends GenericService<Product, String> {

	Page getPagingList(ProductSearchVO searchVO) throws Exception;        
	
	int countProductListByCategory(String categoryNo) throws Exception;
	
	void create(Product product) throws Exception;
}