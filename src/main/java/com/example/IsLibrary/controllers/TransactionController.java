package com.example.IsLibrary.controllers;

import com.example.IsLibrary.dto.request.DtoTransactionRequest;
import com.example.IsLibrary.dto.response.Dto3MemberWithPenalty;
import com.example.IsLibrary.dto.response.DtoBorrowBookByMember;
import com.example.IsLibrary.dto.response.DtoReturnBook;
import com.example.IsLibrary.dto.response.DtoTop5Book;
import com.example.IsLibrary.dto.response.DtoTransactionResponse;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private Response response = new Response();

    @PostMapping("")
    public ResponseEntity createTransaction(@RequestBody DtoTransactionRequest transactionRequest){
        Boolean added = transactionService.addTransaction(transactionRequest, response);
        if (added){
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/pages")
    public ResponseEntity pageViewAll(@RequestParam int page, @RequestParam int limit){
        Page<DtoTransactionResponse> transactionLists = transactionService.viewByBorrow(page, limit);
        response.setMessage("Success");
        response.setData(transactionLists);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/returns")
    public ResponseEntity pageViewReturn(@RequestParam Boolean isPenalty, int page, @RequestParam int limit){
        Page<DtoReturnBook> transactionLists = transactionService.viewReturnBook(isPenalty, page, limit);
        response.setMessage("Success");
        response.setData(transactionLists);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/members")
    public ResponseEntity viewMember(@RequestParam int year, @RequestParam int month){
        List<DtoBorrowBookByMember> listTransaction = transactionService.getMemberTransactionByMonth(year, month);
        response.setMessage("Success");
        response.setData(listTransaction);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/top-books")
    public ResponseEntity viewTopBook(){
        List<DtoTop5Book> topBook = transactionService.top5Book();
        response.setMessage("Success");
        response.setData(topBook);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/top-penaltys")
    public ResponseEntity viewTopMemberPenalty(){
        List<Dto3MemberWithPenalty> member = transactionService.top3MemberPenalty();
        response.setMessage("Success");
        response.setData(member);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
