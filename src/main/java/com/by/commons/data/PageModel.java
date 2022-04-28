package com.by.commons.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * Page carrier
 * @author by.
 */
@Data
@AllArgsConstructor
public class PageModel<T> {
    private Long currentPage;
    private Long totalPages;
    private T content;
}
