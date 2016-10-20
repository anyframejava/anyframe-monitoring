package anyframe.example.monitoring.sales.dao;

import anyframe.common.Page;
import anyframe.core.generic.dao.GenericDao;

import anyframe.example.monitoring.domain.Product;
import anyframe.example.monitoring.sales.service.ProductSearchVO;

/**
 * An interface that provides a data management interface to the Product table.
 */
public interface ProductDao extends GenericDao<Product, String> {

	Page getPagingList(ProductSearchVO searchVO) throws Exception;
	
	int countProductListByCategory(String categoryNo) throws Exception;
}