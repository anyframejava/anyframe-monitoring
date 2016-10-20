package anyframe.example.monitoring.sales.dao.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.util.ClassUtils;

import anyframe.common.Page;
import anyframe.common.util.StringUtil;
import anyframe.core.generic.dao.query.GenericDaoQuery;
import anyframe.core.properties.IPropertiesService;
import anyframe.core.query.IQueryService;
import anyframe.example.monitoring.domain.Category;
import anyframe.example.monitoring.domain.Product;
import anyframe.example.monitoring.sales.dao.ProductDao;
import anyframe.example.monitoring.sales.service.ProductSearchVO;

@Repository("productDao")
public class ProductDaoImpl extends GenericDaoQuery<Product, String> implements ProductDao {

	@Resource
	IPropertiesService propertiesService;
	@Resource
	IQueryService queryService;
	
    public ProductDaoImpl() {
        super(Product.class);
    }
    
	@PostConstruct
	public void initialize() {
		super.setQueryService(queryService);
		super.setPropertiesService(propertiesService);
	}    
    
	public Page getPagingList(ProductSearchVO searchVO) throws Exception {
        int pageIndex = searchVO.getPageIndex();
        int pageSize = this.getPropertiesService().getInt("PAGE_SIZE");
        int pageUnit = this.getPropertiesService().getInt("PAGE_UNIT");
        
		Product product = new Product();
		String searchKeyword = StringUtil.null2str(searchVO.getSearchKeyword());		
		product.setProdName("%"+searchKeyword+"%");
		String asYn = searchVO.getSearchAsYn();
		product.setAsYn(asYn);        
        
        return this.findListWithPaging(ClassUtils.getShortName(getPersistentClass()), product, pageIndex, pageSize, pageUnit);
	}  
	
    public int countProductListByCategory(String categoryNo) throws Exception {
        Category category = new Category();
        category.setCategoryNo(categoryNo);

        Collection countCollection =
            getQueryService().find("countProductListByCategory",
                new Object[] {new Object[] {"vo", category } });

        Iterator countItr = countCollection.iterator();
        if (countItr.hasNext()) {
            Map countMap = (Map) countItr.next();

            int count = ((Integer) countMap.get("total")).intValue();

            return count;
        }
        return 0;
    }	
}
