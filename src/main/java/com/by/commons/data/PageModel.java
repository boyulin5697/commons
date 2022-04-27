package com.by.commons.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageModel {
    private Long currentPage;
    private Long totalPages;
    private Object object;
}
