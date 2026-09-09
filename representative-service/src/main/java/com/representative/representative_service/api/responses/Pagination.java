package com.representative.representative_service.api.responses;

public class Pagination {

    private Integer page;
    private Integer pageSize;

    public Pagination() {
    }

    public Pagination(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
