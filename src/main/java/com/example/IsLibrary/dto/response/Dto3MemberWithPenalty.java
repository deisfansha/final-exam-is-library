package com.example.IsLibrary.dto.response;

import com.example.IsLibrary.models.Member;

public class Dto3MemberWithPenalty {
    private Member member;
    private Long payFee;

    public Dto3MemberWithPenalty(Member member, Long payFee) {
        this.member = member;
        this.payFee = payFee;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getPayFee() {
        return payFee;
    }

    public void setPayFee(Long payFee) {
        this.payFee = payFee;
    }
}
