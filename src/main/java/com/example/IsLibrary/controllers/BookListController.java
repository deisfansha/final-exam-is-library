package com.example.IsLibrary.controllers;

import com.example.IsLibrary.dto.request.DtoBookListRequest;
import com.example.IsLibrary.dto.response.DtoBookListIsbn;
import com.example.IsLibrary.dto.response.DtoBookListResponse;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.services.BookListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/page")
    public ResponseEntity pageViewAll(@RequestParam int page, @RequestParam int limit){
        Page<DtoBookListIsbn> bookList = bookListService.pageView(page, limit);
        if (bookList.isEmpty()){
            response.setMessage("Data Is Empty");
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else {
            response.setMessage("Success");
            response.setData(bookList);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updatedStatusBookList(@PathVariable Long id, @RequestBody BookList bookList){
        Boolean updated = bookListService.updateStatus(id, bookList, response);
        if (!updated){
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else {
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity softDeleteBook(@PathVariable Long id){
        Boolean deleted = bookListService.softDelete(id, response);
        if (!deleted){
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else {
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
