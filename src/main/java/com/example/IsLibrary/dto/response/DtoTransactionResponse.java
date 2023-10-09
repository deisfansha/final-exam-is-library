package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DtoTransactionResponse {

    private String code;
    private String isbn;
    private String title;
    private String author;
    @JsonProperty("name_member")
    private String nameMember;
    @JsonProperty("borrow_date")
    private String borrowDate;
    @JsonProperty("due_date")
    private String dueDate;



}
