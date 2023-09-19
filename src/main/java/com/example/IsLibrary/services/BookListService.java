package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoBookListRequest;
import com.example.IsLibrary.dto.response.DtoBookListResponse;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.repositories.BookListRepo;
import com.example.IsLibrary.repositories.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookListService {
    @Autowired
    private BookListRepo bookListRepo;
    @Autowired
    private BookRepo bookRepo;

    public Boolean addBookList(DtoBookListRequest bookListRequest, Response response){
        Optional<Book> existingBook = bookRepo.findByCodeBookAndIsDeletedIsFalse(bookListRequest.getCodeBook());

        if (bookListRequest.getCodeBook().isEmpty() || bookListRequest.getIsbn().isEmpty()){
            response.setMessage("Data must be filled in");
            return false;
        } else if (!existingBook.isPresent()){
            response.setMessage("Book Not Found");
            return false;
        } else if (bookListRequest.getIsbn().matches("^{8,13}$")) {
            response.setMessage("Isbn must have 8 to 13 characters");
            return false;
        }

        BookList bookList = new BookList();
        bookList.setIsbn(bookListRequest.getIsbn());
        bookList.setBook(existingBook.get());
        bookListRepo.save(bookList);

        response.setData(new DtoBookListResponse(bookList.getBook().getCodeBook(),bookList.getBook().getTitle(), bookList.getBook().getAuthor(), bookListRepo.countByBookIdAndIsAvailableIsTrue(bookList.getBook().getId())));
        return true;
    }

}
