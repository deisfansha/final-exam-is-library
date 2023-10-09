package com.example.IsLibrary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoTop5Book {
    private String code;
    private String isbn;
    private String title;
    private String author;
    private Long total;
}
