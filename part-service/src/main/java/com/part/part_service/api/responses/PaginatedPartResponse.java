package com.part.part_service.api.responses;

import java.util.List;

public class PaginatedPartResponse {

    private List<ListPartResponse> data;
    private Pagination pagination;

    public PaginatedPartResponse() {
    }

    public PaginatedPartResponse(
            List<ListPartResponse> data,
            Pagination pagination
    ) {
        this.data = data;
        this.pagination = pagination;
    }

    public List<ListPartResponse> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
