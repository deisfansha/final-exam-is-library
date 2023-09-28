package com.example.IsLibrary.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
public class Transaction extends BaseClass{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_member")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "id_book")
    private BookList bookList;
    private Date createDate;
    private Date dueDate;
    private Date returnDate;
    private Boolean isPenalty;
    private Integer pay;

    public Transaction() {
    }
}
