package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoTransactionRequest;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Member;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.repositories.BookListRepo;
import com.example.IsLibrary.repositories.MemberRepo;
import com.example.IsLibrary.repositories.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServices {
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private BookListRepo bookListRepo;

    private Response response;
    public Boolean addTransaction(DtoTransactionRequest transactionRequest){
        Optional<Member> existingMember = memberRepo.findByCodeMemberAndIsDeletedIsFalse(transactionRequest.getCodeMember());
        Optional<BookList> existingBookList = bookListRepo.findByIdAndIsDeletedIsFalse(transactionRequest.getIdBookList());

        if (existingMember.isPresent()){
            response.setMessage("Member Not Found");
            return false;
        }
        return true;
    }

}
