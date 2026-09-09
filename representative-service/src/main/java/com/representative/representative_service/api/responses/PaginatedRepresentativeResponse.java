package com.representative.representative_service.api.responses;

import java.util.List;

public class PaginatedRepresentativeResponse {

    private List<ListRepresentativeResponse> data;
    private Pagination pagination;

    public PaginatedRepresentativeResponse() {
    }

    public PaginatedRepresentativeResponse(
            List<ListRepresentativeResponse> data,
            Pagination pagination
    ) {
        this.data = data;
        this.pagination = pagination;
    }

    public List<ListRepresentativeResponse> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
