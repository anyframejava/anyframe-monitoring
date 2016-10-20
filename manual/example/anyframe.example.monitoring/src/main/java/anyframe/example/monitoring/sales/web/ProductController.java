package anyframe.example.monitoring.sales.web;

import java.io.File;
import java.io.FileOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import anyframe.common.Page;
import anyframe.common.util.StringUtil;
import anyframe.core.idgen.IIdGenerationService;
import anyframe.example.monitoring.domain.Product;
import anyframe.example.monitoring.sales.service.CategoryService;
import anyframe.example.monitoring.sales.service.ProductSearchVO;
import anyframe.example.monitoring.sales.service.ProductService;

@Controller
public class ProductController {
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "categoryService")
	private CategoryService categoryService;
	@Resource(name = "idGenerationService")
	private IIdGenerationService idGenerationService;

	/**
	 * display add product form.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monitoringAddViewProduct.do")
	public ModelAndView addView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("categoryList", categoryService
				.getDropDownCategoryList());
		request.setAttribute("product", new Product());

		return new ModelAndView(
				"/WEB-INF/jsp/monitoring/sales/product/viewProduct.jsp");
	}

	/**
	 * add product
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monitoringAddProduct.do")
	public ModelAndView add(HttpServletRequest request, Product product)
			throws Exception {

		product.setAsYn(request.getParameter("_asYn"));

		if (product.getAsYn() == null || product.getAsYn().equals(""))
			product.setAsYn("N");

		String pictureName = "";
		String picturefileName = "";
		String pictureExt = "";

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile picturefile = multipartRequest.getFile("realImageFile");

		if (picturefile != null
				&& !picturefile.getOriginalFilename().equals("")) {
			pictureName = picturefile.getOriginalFilename();

			if (pictureName.toLowerCase().endsWith("jpg")) {
				pictureExt = "jpg";
			} else if (pictureName.toLowerCase().endsWith("gif")) {
				pictureExt = "gif";
			}

			String genId = idGenerationService.getNextStringId();

			if (!pictureExt.equals(""))
				picturefileName = genId + "." + pictureExt;
			else
				picturefileName = genId + pictureExt;

			String destDir = request.getSession().getServletContext()
					.getRealPath("/upload/");

			File dirPath = new File(destDir);
			if (!dirPath.exists()) {
				boolean created = dirPath.mkdirs();
				if (!created) {
					throw new Exception(
							"Fail to create a directory for product image. ["
									+ destDir + "]");
				}
			}
			File destination = File.createTempFile("file", picturefileName,
					dirPath);

			FileCopyUtils.copy(picturefile.getInputStream(),
					new FileOutputStream(destination));

			product.setImageFile(destination.getName());
		}

		product.setSellerId("test");
		productService.create(product);

		return new ModelAndView("/monitoringListProduct.do");
	}

	/**
	 * get a product detail.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monitoringGetProduct.do")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String prodNo = request.getParameter("prodNo");

		if (!StringUtils.isBlank(prodNo)) {
			Product gettedProduct = productService.get(prodNo);
			gettedProduct.setImageFile("/upload/"
					+ gettedProduct.getImageFile());
			request.setAttribute("product", gettedProduct);
		}

		return new ModelAndView(
				"/WEB-INF/jsp/monitoring/sales/product/viewProduct.jsp");
	}

	/**
	 * update product
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monitoringUpdateProduct.do")
	public ModelAndView update(HttpServletRequest request, Product product) throws Exception {

		System.out.println(request.getParameter("prodNo"));
		if (product.getAsYn() == null || product.getAsYn().equals(""))
			product.setAsYn("N");

		productService.update(product);

		return new ModelAndView("/monitoringListProduct.do");
	}

	/**
	 * display product list
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monitoringListProduct.do")
	public ModelAndView list(HttpServletRequest request,
			ProductSearchVO searchVO) throws Exception {

		String pageParam = (new ParamEncoder("productList")
				.encodeParameterName(TableTagParameters.PARAMETER_PAGE));
		String pageParamValue = request.getParameter(pageParam);
		int pageIndex = StringUtil.isNotEmpty(pageParamValue) ? (Integer
				.parseInt(pageParamValue)) : 1;
		searchVO.setPageIndex(pageIndex);

		Page resultPage = productService.getPagingList(searchVO);

		request.setAttribute("search", searchVO);
		request.setAttribute("productList", resultPage.getList());
		request.setAttribute("size", resultPage.getTotalCount());
		request.setAttribute("pagesize", resultPage.getPagesize());
		request.setAttribute("pageunit", resultPage.getPageunit());

		return new ModelAndView(
				"/WEB-INF/jsp/monitoring/sales/product/listProduct.jsp");
	}

	/**
	 * delete product
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monitoringDeleteProduct.do")
	public ModelAndView delete(HttpServletRequest request, Product product)
			throws Exception {
		productService.remove(product.getProdNo());

		return new ModelAndView("/monitoringListProduct.do");
	}
}
