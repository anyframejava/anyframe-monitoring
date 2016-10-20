package anyframe.example.monitoring.sales.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import anyframe.core.generic.dao.query.GenericDaoQuery;
import anyframe.core.query.IQueryService;
import anyframe.example.monitoring.domain.Category;
import anyframe.example.monitoring.sales.dao.CategoryDao;

@Repository("categoryDao")
public class CategoryDaoImpl extends GenericDaoQuery<Category, String> implements CategoryDao {

	@Resource
	IQueryService queryService;
	
    public CategoryDaoImpl() {
        super(Category.class);
    }
    
    @PostConstruct
    public void initialize() {
    	super.setQueryService(queryService);
    }    
    
    public List getDropDownCategoryList() throws Exception {
        return (List) getQueryService().find("findDropDownCategoryList", new Object[] {});
    }  
}
