package anyframe.example.monitoring.sales.service;

import anyframe.common.util.SearchVO;

public class ProductSearchVO extends SearchVO {

    private static final long serialVersionUID = 1L;

    private String searchAsYn = "Y";

    public String getSearchAsYn() {
        return searchAsYn;
    }

    public void setSearchAsYn(String searchAsYn) {
        this.searchAsYn = searchAsYn;
    }

}
