package anyframe.example.monitoring.sales.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import anyframe.core.generic.service.impl.GenericServiceImpl;
import anyframe.example.monitoring.domain.Category;
import anyframe.example.monitoring.sales.dao.CategoryDao;
import anyframe.example.monitoring.sales.service.CategoryService;
import anyframe.example.monitoring.sales.service.ProductService;

@Service("categoryService")
public class CategoryServiceImpl extends GenericServiceImpl<Category, String> implements CategoryService {
	@Resource
	ProductService productService;
	@Resource
	MessageSource messageSource;
	@Resource
	CategoryDao categoryDao;

	@PostConstruct
	public void initialize() {
		super.setGenericDao(categoryDao);
	}
    
    public List getDropDownCategoryList() throws Exception {
        return categoryDao.getDropDownCategoryList();
    }      
}