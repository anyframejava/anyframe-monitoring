package anyframe.example.monitoring.sales.dao;

import java.util.List;

import anyframe.core.generic.dao.GenericDao;

import anyframe.example.monitoring.domain.Category;

/**
 * An interface that provides a data management interface to the Category table.
 */
public interface CategoryDao extends GenericDao<Category, String> {

	List getDropDownCategoryList() throws Exception;
}