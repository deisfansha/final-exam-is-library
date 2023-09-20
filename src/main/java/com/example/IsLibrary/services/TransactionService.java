package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoTransactionRequest;
import com.example.IsLibrary.dto.response.DtoBookListResponse;
import com.example.IsLibrary.dto.response.DtoReturnBook;
import com.example.IsLibrary.dto.response.DtoTransactionByMonth;
import com.example.IsLibrary.dto.response.DtoTransactionResponse;
import com.example.IsLibrary.dto.response.DtoTransactionReturnBook;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Member;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.models.Transaction;
import com.example.IsLibrary.repositories.BookListRepo;
import com.example.IsLibrary.repositories.MemberRepo;
import com.example.IsLibrary.repositories.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private BookListRepo bookListRepo;
    @Autowired
    private BookListService bookListService;
    public Boolean addTransaction(DtoTransactionRequest transactionRequest, Response response){
        Optional<Member> existingMember = memberRepo.findByCodeMemberAndIsDeletedIsFalse(transactionRequest.getCodeMember());
        Optional<BookList> existingBookList = bookListRepo.findByIdAndIsDeletedIsFalseAndIsAvailableIsTrue(transactionRequest.getIdBookList());

        if (!existingMember.isPresent()){
            response.setMessage("Member Not Found");
            return false;
        } else if (!existingBookList.isPresent()) {
            response.setMessage("Book List Not Found");
            return false;
        }

        Transaction transaction = new Transaction();
        Calendar currentDate = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date newDate = calendar.getTime();

        transaction.setCreateDate(currentDate.getTime());
        transaction.setDueDate(newDate);
        transaction.setBookList(existingBookList.get());
        transaction.setMember(existingMember.get());

        transactionRepo.save(transaction);
        bookListService.updateAvailableBook(transactionRequest.getIdBookList());

        response.setData(new DtoTransactionResponse(existingBookList.get().getBook().getTitle(),
                existingBookList.get().getIsbn(), existingBookList.get().getBook().getAuthor(),
                existingMember.get().getName(), String.valueOf(transaction.getDueDate())));
        return true;
    }

    public Boolean returnBook(Long id, Response response){
        Optional<Transaction> existingTransaction = transactionRepo.findByIdAndIsDeletedFalseAndReturnDateIsNull(id);
        if (!existingTransaction.isPresent()){
            response.setMessage("Transaction Not Found");
            return false;
        }

        LocalDateTime returnBook = LocalDateTime.now();
        LocalDateTime dueDate = convertToLocalDateTime(existingTransaction.get().getDueDate());
        Long days = ChronoUnit.DAYS.between(dueDate, returnBook);
        existingTransaction.get().setReturnDate(convertToDate(returnBook));
        if (days<0){
            existingTransaction.get().setMulct(false);
            existingTransaction.get().setPay(0);
        } else {
            Long total = 10000*days;
            existingTransaction.get().setMulct(true);
            existingTransaction.get().setPay(Integer.parseInt(String.valueOf(total)));
        }
        bookListService.updateAvailableReturnBook(existingTransaction.get().getBookList().getId());
        transactionRepo.save(existingTransaction.get());
        response.setData(new DtoTransactionReturnBook(
                existingTransaction.get().getMember().getName(), existingTransaction.get().getBookList().getBook().getTitle(),
                existingTransaction.get().getBookList().getIsbn(), existingTransaction.get().getDueDate(), existingTransaction.get().getReturnDate(),
                existingTransaction.get().getMulct(), existingTransaction.get().getPay()));
        return true;
    }

    public List<DtoTransactionByMonth> getMemberTransactionByMonth(int year, int month) {
        BookList bookList = bookListRepo.findAllByIsDeletedIsFalseOrderByIdAsc();
        Member member = memberRepo.findAllByIsDeletedIsFalseOrderByIdAsc();
        Long total = transactionRepo.countByBookList(bookList);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        List<Transaction> listMember = transactionRepo.findByCreateDateBetween(startDate, endDate);

        List<DtoTransactionByMonth> transactionByMonths = new ArrayList<>();
        for (Transaction transactionData: listMember){
            DtoTransactionByMonth listTransaction = new DtoTransactionByMonth(
                    transactionData.getMember().getName());
            transactionByMonths.add(listTransaction);
        }
        return transactionByMonths;
    }

    public Page<DtoTransactionResponse> viewByBorrow(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Transaction> result =  transactionRepo.findByIsDeletedFalseAndReturnDateIsNullOrderByIdAsc(pageable);
        List<DtoTransactionResponse> transactions = new ArrayList<>();
        for (Transaction transactionData: result.getContent()){
            DtoTransactionResponse transactionResponse = new DtoTransactionResponse(
                    transactionData.getBookList().getBook().getTitle(),
                    transactionData.getBookList().getIsbn(), transactionData.getBookList().getBook().getAuthor(),
                    transactionData.getMember().getName(),String.valueOf(transactionData.getDueDate()));
            transactions.add(transactionResponse);
        }
        return new PageImpl(transactions, PageRequest.of(page, limit), result.getTotalPages());
    }

    public Page<DtoReturnBook> viewReturnBook(Boolean isMulct, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Transaction> result =  transactionRepo.findByIsMulctAndIsDeletedFalseOrderByIdAsc(isMulct,pageable);
        List<DtoReturnBook> transactionsList = new ArrayList<>();
        for (Transaction transactionData: result.getContent()){
            DtoReturnBook transactionResponse = new DtoReturnBook(
                    transactionData.getBookList().getBook().getTitle(),
                    transactionData.getBookList().getIsbn(), transactionData.getBookList().getBook().getAuthor(),
                    transactionData.getMember().getName(),String.valueOf(transactionData.getReturnDate()));
            transactionsList.add(transactionResponse);
        }
        return new PageImpl(transactionsList, PageRequest.of(page, limit), result.getTotalPages());
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
