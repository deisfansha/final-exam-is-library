package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.response.DtoBookResponse;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.repositories.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;
    private Integer code=0;
    public Boolean addBook(Book book, Response response){
        Optional<Book> existingBook = bookRepo.findByTitleAndAuthorAndIsDeletedIsFalse(book.getTitle(), book.getAuthor());
        if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()){
            response.setMessage("Data must be filled in");
            return false;
        } else if (existingBook.isPresent()) {
            response.setMessage("Book is already exists");
            return false;
        }

        book.setCodeBook(generateCode());
        bookRepo.save(book);
        response.setData(new DtoBookResponse(book.getCodeBook(), book.getTitle(), book.getAuthor()));
        return true;
    }

    private String generateCode(){
        code+=11;
        return String.valueOf(code);
    }
}
