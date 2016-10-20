package anyframe.example.monitoring.sales.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import anyframe.example.monitoring.domain.Category;
import anyframe.example.monitoring.sales.service.CategoryService;

@Controller
public class CategoryController {
	@Resource(name = "categoryService")
    private CategoryService categoryService;
    
    /**
     * get a category detail.
     * @param request
     * @param response
     * @return 
     * @throws Exception
     */
	@RequestMapping("/monitoringGetCategory.do")
    public ModelAndView get(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
 		
       String categoryNo = request.getParameter("categoryNo");

       if (!StringUtils.isBlank(categoryNo)) {
       		Category gettedCategory = categoryService.get(categoryNo);        	
        	request.setAttribute("category", gettedCategory);        	
       }
        
       return new ModelAndView("/WEB-INF/jsp/monitoring/sales/category/viewCategory.jsp");
    }  
}
