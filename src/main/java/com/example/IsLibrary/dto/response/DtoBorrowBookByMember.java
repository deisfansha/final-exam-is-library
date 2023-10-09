package com.example.IsLibrary.dto.response;

import com.example.IsLibrary.models.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoBorrowBookByMember {
    private String name;
    private String code;
    private String gender;
    private Long total;

}
