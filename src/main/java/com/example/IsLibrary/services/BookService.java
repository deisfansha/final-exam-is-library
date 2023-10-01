package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.response.DtoBookResponse;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.repositories.BookListRepo;
import com.example.IsLibrary.repositories.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private BookListRepo bookListRepo;
    public Boolean addBook(Book book, Response response){
        Optional<Book> existingBook = bookRepo.findByTitleAndAuthorAndIsDeletedIsFalse(book.getTitle(), book.getAuthor());
        if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()){
            response.setMessage("Data must be filled in");
            return false;
        } else if (existingBook.isPresent()) {
            response.setMessage("Book is already exists");
            return false;
        }

        book.setCodeBook(String.valueOf(bookRepo.count()*11));
        bookRepo.save(book);
        response.setData(new DtoBookResponse(book.getCodeBook(), book.getTitle(), book.getAuthor()));
        return true;
    }

    public Page<DtoBookResponse> pageView(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Book> result =  bookRepo.findAllByIsDeletedIsFalseOrderByIdAsc(pageable);
        List<DtoBookResponse> dataBook = new ArrayList<>();
        for(Book books : result.getContent()){
            DtoBookResponse listBook = new DtoBookResponse(books.getCodeBook(), books.getTitle(), books.getAuthor());
            dataBook.add(listBook);
        }
        return new PageImpl(dataBook, PageRequest.of(page, limit), result.getTotalPages());
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
        }

        if(existingBook.get().getAuthor().equalsIgnoreCase(book.getAuthor()) &&
                existingBook.get().getTitle().equalsIgnoreCase(book.getTitle())){
            response.setMessage("Data no changes");
        }else {
            if (dataBook.isPresent()) {
                response.setMessage("Book is already exists");
                return false;
            }
            response.setMessage("Success");
            existingBook.get().setTitle(book.getTitle());
            existingBook.get().setAuthor(book.getAuthor());
            bookRepo.save(existingBook.get());
        }

        response.setData(new DtoBookResponse(existingBook.get().getCodeBook(), existingBook.get().getTitle(), existingBook.get().getAuthor()));
        return true;
    }

    public Boolean softDelete(String code, Response response){
        Optional<Book> existingBook = bookRepo.findByCodeBookAndIsDeletedIsFalse(code);
        if (!existingBook.isPresent()){
            response.setMessage("Book Not Found");
            return false;
        }

        List<BookList> existingBookList = bookListRepo.findByBookIdAndIsDeletedIsFalseAndIsAvailableIsTrue(existingBook.get().getId());

        for (BookList listBook: existingBookList){
            listBook.setDeleted(true);
            bookListRepo.save(listBook);
        }
        existingBook.get().setDeleted(true);
        bookRepo.save(existingBook.get());
        response.setData(new DtoBookResponse(existingBook.get().getCodeBook(), existingBook.get().getTitle(), existingBook.get().getAuthor()));
        return true;
    }
}
