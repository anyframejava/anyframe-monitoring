package anyframe.example.monitoring.sales.service;

import java.util.List;

import anyframe.core.generic.service.GenericService;

import anyframe.example.monitoring.domain.Category;

public interface CategoryService extends GenericService<Category, String> {

	List getDropDownCategoryList() throws Exception;
}