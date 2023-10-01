package com.example.IsLibrary.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@Entity
public class Book extends BaseClass{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeBook;
    private String title;
    private String author;

    public Book() {
    }
}
