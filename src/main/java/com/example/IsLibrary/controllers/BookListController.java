package com.example.IsLibrary.controllers;

import com.example.IsLibrary.dto.request.DtoBookListRequest;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.services.BookListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list-books")
public class BookListController {
    @Autowired
    private BookListService bookListService;
    private Response response = new Response();

    @PostMapping("")
    public ResponseEntity saveBookList(@RequestBody DtoBookListRequest bookList){
        Boolean added = bookListService.addBookList(bookList, response);
        if (added){
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
