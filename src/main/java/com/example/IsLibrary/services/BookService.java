package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoMemberRequest;
import com.example.IsLibrary.dto.response.DtoBookResponse;
import com.example.IsLibrary.dto.response.DtoMemberResponse;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.Member;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.repositories.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<DtoBookResponse> pageView(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Book> result =  bookRepo.findAllByIsDeletedIsFalseOrderByIdAsc(pageable);
        return new PageImpl(result.getContent(), PageRequest.of(page, limit), result.getTotalPages());
    }

    public Boolean updateBook(String code, Book book, Response response){
        Optional<Book> existingBook = bookRepo.findByCodeBookAndIsDeletedIsFalse(code);
        Optional<Book> dataBook = bookRepo.findByTitleAndAuthorAndIsDeletedIsFalse(book.getTitle(), book.getAuthor());

        if (!existingBook.isPresent()){
            response.setMessage("Book not found");
            return false;
        } else if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()){
            response.setMessage("Data must be filled in");
            return false;
        } else if (dataBook.isPresent()) {
            response.setMessage("Book is already exists");
            return false;
        }

        existingBook.get().setTitle(book.getTitle());
        existingBook.get().setAuthor(book.getAuthor());
        bookRepo.save(existingBook.get());
        response.setData(new DtoBookResponse(existingBook.get().getCodeBook(), existingBook.get().getTitle(), existingBook.get().getAuthor()));
        return true;
    }

    public Boolean softDelete(String code, Response response){
        Optional<Book> existingBook = bookRepo.findByCodeBookAndIsDeletedIsFalse(code);

        if (!existingBook.isPresent()){
            response.setMessage("Member Not Found");
            return false;
        }

        existingBook.get().setDeleted(true);
        bookRepo.save(existingBook.get());
        response.setData(new DtoBookResponse(existingBook.get().getCodeBook(), existingBook.get().getTitle(), existingBook.get().getAuthor()));
        return true;
    }

    private String generateCode(){
        code+=11;
        return String.valueOf(code);
    }
}
