package com.example.IsLibrary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoTransactionRequest {
    @JsonProperty("member")
    private String codeMember;
    @JsonProperty("id_book_list")
    private Long idBookList;

    public String getCodeMember() {
        return codeMember;
    }

    public void setCodeMember(String codeMember) {
        this.codeMember = codeMember;
    }

    public Long getIdBookList() {
        return idBookList;
    }

    public void setIdBookList(Long idBookList) {
        this.idBookList = idBookList;
    }
}
