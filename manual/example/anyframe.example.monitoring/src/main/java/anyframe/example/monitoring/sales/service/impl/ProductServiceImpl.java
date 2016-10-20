package anyframe.example.monitoring.sales.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import anyframe.common.Page;
import anyframe.core.generic.service.impl.GenericServiceImpl;
import anyframe.core.idgen.IIdGenerationService;
import anyframe.example.monitoring.domain.Product;
import anyframe.example.monitoring.sales.dao.ProductDao;
import anyframe.example.monitoring.sales.service.ProductSearchVO;
import anyframe.example.monitoring.sales.service.ProductService;

@Service("productService")
public class ProductServiceImpl extends GenericServiceImpl<Product, String>
		implements ProductService {
	@Resource(name="idGenerationServiceProduct")
	IIdGenerationService idGenerationService;
	@Resource
	ProductDao productDao;

	@PostConstruct
	public void initialize() {
		super.setGenericDao(productDao);
	}

	public Page getPagingList(ProductSearchVO searchVO) throws Exception {
		return this.productDao.getPagingList(searchVO);
	}

	public int countProductListByCategory(String categoryNo) throws Exception {
		int count = productDao.countProductListByCategory(categoryNo);
		return count;
	}

	public void create(Product product) throws Exception {
		product.setProdNo(idGenerationService.getNextStringId());
		productDao.create(product);
	}
}