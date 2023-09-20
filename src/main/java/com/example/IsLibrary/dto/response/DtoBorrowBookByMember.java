package com.example.IsLibrary.dto.response;

import com.example.IsLibrary.models.Member;

public class DtoBorrowBookByMember {
    private Member member;
    private Long total;

    public DtoBorrowBookByMember(Member member, Long total) {
        this.member = member;
        this.total = total;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
