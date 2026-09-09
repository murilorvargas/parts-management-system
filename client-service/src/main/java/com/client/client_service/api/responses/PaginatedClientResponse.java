package com.client.client_service.api.responses;

import java.util.List;

public class PaginatedClientResponse {

    private List<ListClientResponse> data;
    private Pagination pagination;

    public PaginatedClientResponse() {
    }

    public PaginatedClientResponse(
            List<ListClientResponse> data,
            Pagination pagination
    ) {
        this.data = data;
        this.pagination = pagination;
    }

    public List<ListClientResponse> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
