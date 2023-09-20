package com.example.IsLibrary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoTransactionByMonth {
    @JsonProperty("member")
    private String nameMember;
    private Long total;

    public DtoTransactionByMonth(String nameMember, Long total) {
        this.nameMember = nameMember;
        this.total = total;
    }

    public DtoTransactionByMonth(String nameMember) {
        this.nameMember = nameMember;
    }

    public String getNameMember() {
        return nameMember;
    }

    public void setNameMember(String nameMember) {
        this.nameMember = nameMember;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
