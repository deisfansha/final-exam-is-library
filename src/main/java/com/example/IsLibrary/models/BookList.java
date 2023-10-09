package com.example.IsLibrary.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Entity
public class BookList extends BaseClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String isbn;
    private Boolean isAvailable = true;
    @ManyToOne
    @JoinColumn(name = "code_book", referencedColumnName = "codeBook")
    private Book book;

    public BookList() {
    }

}
