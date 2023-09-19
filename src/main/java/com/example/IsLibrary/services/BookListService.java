package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoBookListRequest;
import com.example.IsLibrary.dto.response.DtoBookListIsbn;
import com.example.IsLibrary.dto.response.DtoBookListResponse;
import com.example.IsLibrary.dto.response.DtoBookResponse;
import com.example.IsLibrary.dto.response.DtoMemberResponse;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Member;
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

    public Page<DtoBookListResponse> pageView(int page, int limit){
        Book book = bookRepo.findAllByIsDeletedIsFalseOrderByIdAsc();
        Long total = bookListRepo.countByBookAndAndIsAvailableIsTrue(book);
        Pageable pageable = PageRequest.of(page, limit);

        Page<Book> result =  bookRepo.findAllByIsDeletedIsFalseOrderByIdAsc(pageable);
        List<DtoBookListResponse> bookList = new ArrayList<>();
        for (Book bookData: result.getContent()){
            DtoBookListResponse bookListResponse = new DtoBookListResponse(
                    bookData.getCodeBook(),bookData.getTitle(),bookData.getAuthor(), total);
            bookList.add(bookListResponse);
        }
        return new PageImpl(bookList, PageRequest.of(page, limit), result.getTotalPages());
    }

    public Boolean updateStatus(Long id, BookList bookList, Response response){
        Optional<BookList> existingBookList = bookListRepo.findByIdAndIsDeletedIsFalse(id);
        if (!existingBookList.isPresent()){
            response.setMessage("Book List not found");
            return false;
        } else if (bookList.getAvailable() == null){
            response.setMessage("Data must be filled in");
            return false;
        }

        existingBookList.get().setAvailable(bookList.getAvailable());
        bookListRepo.save(existingBookList.get());
        response.setData(new DtoBookListIsbn(existingBookList.get().getBook().getTitle(), existingBookList.get().getIsbn(), existingBookList.get().getBook().getAuthor(),
                existingBookList.get().getAvailable()));
        return true;
    }

    public Boolean softDelete(Long id, Response response){
        Optional<BookList> existingBookList = bookListRepo.findByIdAndIsDeletedIsFalseAndIsAvailableIsTrue(id);

        if (!existingBookList.isPresent()){
            response.setMessage("Book List Not Found");
            return false;
        }

        existingBookList.get().setDeleted(true);
        bookListRepo.save(existingBookList.get());
        response.setData(new DtoBookListIsbn(existingBookList.get().getBook().getTitle(), existingBookList.get().getIsbn(), existingBookList.get().getBook().getAuthor(),
                existingBookList.get().getAvailable()));
        return true;
    }

}
