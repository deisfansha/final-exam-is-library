package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoTransactionRequest;
import com.example.IsLibrary.dto.response.Dto3MemberWithPenalty;
import com.example.IsLibrary.dto.response.DtoBorrowBookByMember;
import com.example.IsLibrary.dto.response.DtoReturnBook;
import com.example.IsLibrary.dto.response.DtoTop5Book;
import com.example.IsLibrary.dto.response.DtoTransactionResponse;
import com.example.IsLibrary.dto.response.DtoTransactionReturnBook;
import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Member;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.models.Transaction;
import com.example.IsLibrary.repositories.BookListRepo;
import com.example.IsLibrary.repositories.BookRepo;
import com.example.IsLibrary.repositories.MemberRepo;
import com.example.IsLibrary.repositories.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
    private BookRepo bookRepo;
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

        Calendar currentDate = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date newDate = calendar.getTime();
        Transaction transaction = new Transaction(null, existingMember.get(), existingBookList.get(), currentDate.getTime(), newDate, null, null, null);

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
            existingTransaction.get().setIsPenalty(false);
            existingTransaction.get().setPay(0);
        } else {
            Long total = 10000*days;
            existingTransaction.get().setIsPenalty(true);
            existingTransaction.get().setPay(Integer.parseInt(String.valueOf(total)));
        }
        bookListService.updateAvailableReturnBook(existingTransaction.get().getBookList().getId());
        transactionRepo.save(existingTransaction.get());
        response.setData(new DtoTransactionReturnBook(
                existingTransaction.get().getMember().getName(), existingTransaction.get().getBookList().getBook().getTitle(),
                existingTransaction.get().getBookList().getIsbn(), existingTransaction.get().getDueDate(), existingTransaction.get().getReturnDate(),
                existingTransaction.get().getIsPenalty(), existingTransaction.get().getPay()));
        return true;
    }

    public List<DtoBorrowBookByMember> getMemberTransactionByMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        Pageable pageable = PageRequest.of(0,3);
        return transactionRepo.findTop5BorrowBook(startDate, endDate, pageable);
    }

    public Page<DtoTransactionResponse> viewByBorrow(int page, int limit){
        if (transactionRepo.count() == 0) seedData();
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

    public Page<DtoReturnBook> viewReturnBook(Boolean isPenalty, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Transaction> result =  transactionRepo.findByIsPenaltyAndIsDeletedFalseOrderByIdAsc(isPenalty,pageable);
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

    public List<DtoTop5Book> top5Book(){
        Pageable pageable = PageRequest.of(0,5);
        return transactionRepo.findTop5BooksByLoanCount(pageable);
    }

    public List<Dto3MemberWithPenalty> top3MemberPenalty(){
        Pageable pageable = PageRequest.of(0,3);
        return transactionRepo.findTop3MemberWithPenalty(pageable);
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

    private void seedData() {

        Member m1 = new Member(null,"Kiki Eko", "005","08976538463", "Laki - Laki");
        Member m2 = new Member(null,"Andreas", "006","08976878400", "Laki - Laki");
        Member m3 = new Member(null,"Cahyani", "008","0895627118", "Perempuan");
        Member m4 = new Member(null,"Ika Rusid", "0010","0897762231", "Perempuan");
        Member m5 = new Member(null,"Lazuardi", "009","085067457382", "Laki - Laki");
        Member m6 = new Member(null,"Darusman", "0011","08978254449", "Laki - Laki");
        Member m7 = new Member(null,"Putri", "007","08586542234", "Perempuan");
        memberRepo.saveAll(Arrays.asList(m1,m2,m3,m4,m5,m6,m7));

        Book b1 = new Book(null,"11", "Soekarno","Budoyono");
        Book b2 = new Book(null,"22", "Harry Potter","JK Rowling");
        Book b3 = new Book(null,"33", "One Piece Vol 1","Oda Sensei");
        Book b4 = new Book(null,"44", "Legends","Jack");
        Book b5 = new Book(null,"55", "Lambada","Eti");
        Book b6 = new Book(null,"66", "Melati Putih","Dede Sulaiman");
        Book b7 = new Book(null,"77", "NKCTHI","Agus Kuncoro");
        bookRepo.saveAll(Arrays.asList(b1,b2,b3,b4,b5,b6,b7));

        BookList l1 = new BookList(null,"1111231211", true, b1);
        BookList l2 = new BookList(null,"1111231212", true, b1);
        BookList l4 = new BookList(null,"2221231211", true, b2);
        BookList l7 = new BookList(null,"3311231214", true, b3);
        BookList l8 = new BookList(null,"3311231215", true, b3);
        BookList l10 = new BookList(null,"4411231217", true, b4);
        BookList l12 = new BookList(null,"4411231219", true, b4);
        BookList l13 = new BookList(null,"4411231220", true, b4);
        BookList l15 = new BookList(null,"5511231222", true, b5);
        BookList l20 = new BookList(null,"6621231217", true, b6);
        BookList l21 = new BookList(null,"6621231218", true, b6);
        BookList l17 = new BookList(null,"6621231214", true, b6);
        BookList l25 = new BookList(null,"7721231222", true, b7);
        BookList l23 = new BookList(null,"7721231220", true, b7);

        BookList l3 = new BookList(null,"1111231213", false, b1);
        BookList l5 = new BookList(null,"2221231212", false, b2);
        BookList l6 = new BookList(null,"2221231213", false, b2);
        BookList l9 = new BookList(null,"3311231216", false, b3);
        BookList l11 = new BookList(null,"4411231218", false, b4);
        BookList l14 = new BookList(null,"5511231221", false, b5);
        BookList l16 = new BookList(null,"5511231223", false, b5);
        BookList l18 = new BookList(null,"6621231215", false, b6);
        BookList l19 = new BookList(null,"6621231216", true, b6);
        BookList l22 = new BookList(null,"7721231219", true, b7);
        BookList l24 = new BookList(null,"7721231221", true, b7);
        BookList l26 = new BookList(null,"7721231223",true, b7);
        bookListRepo.saveAll(Arrays.asList(l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14,l15,l16,l17,l18,l19,l20,
                l21, l22, l23, l24,l25, l26));

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2023, Calendar.JULY, 12);
        Date date1 = calendar1.getTime();
        calendar1.set(2023, Calendar.JULY, 19);
        Date due1 = calendar1.getTime();
        Transaction t1 = new Transaction(null,m1, l3, date1, due1,null, null, null);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2023, Calendar.APRIL, 15);
        Date date2 = calendar2.getTime();
        calendar2.set(2023, Calendar.APRIL, 22);
        Date due2 = calendar2.getTime();
        Transaction t2 = new Transaction(null ,m1, l5, date2, due2,null, null, null);

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2023, Calendar.MARCH, 28);
        Date date3 = calendar3.getTime();
        calendar3.set(2023, Calendar.APRIL, 4);
        Date due3 = calendar3.getTime();
        Transaction t3 = new Transaction(null,m2, l6, date3, due3,null, null, null);

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(2023, Calendar.FEBRUARY, 10);
        Date date4 = calendar4.getTime();
        calendar4.set(2023, Calendar.FEBRUARY, 17);
        Date due4 = calendar4.getTime();
        Transaction t4 = new Transaction(null, m2, l9, date4, due4,null, null, null);

        Calendar calendar5 = Calendar.getInstance();
        calendar5.set(2023, Calendar.AUGUST, 2);
        Date date5 = calendar5.getTime();
        calendar5.set(2023, Calendar.AUGUST, 9);
        Date due5 = calendar5.getTime();
        Transaction t5 = new Transaction(null, m3, l11, date5, due5,null, null, null);

        Calendar calendar6 = Calendar.getInstance();
        calendar6.set(2023, Calendar.JANUARY, 21);
        Date date6 = calendar6.getTime();
        calendar6.set(2023, Calendar.JANUARY, 28);
        Date due6 = calendar6.getTime();
        Transaction t6 = new Transaction(null,m3, l14, date6, due6, null, null, null);

        Calendar calendar7 = Calendar.getInstance();
        calendar7.set(2023, Calendar.MAY, 13);
        Date date7 = calendar7.getTime();
        calendar7.set(2023, Calendar.MAY, 20);
        Date due7 = calendar7.getTime();
        Transaction t7 = new Transaction(null, m4, l16, date7, due7,null, null, null);

        Calendar calendar8 = Calendar.getInstance();
        calendar8.set(2023, Calendar.JULY, 20);
        Date date8 = calendar8.getTime();
        calendar8.set(2023, Calendar.JULY, 27);
        Date due8 = calendar8.getTime();
        Transaction t8 = new Transaction(null, m4, l18, date8, due8,null, null, null);

        Calendar calendar9 = Calendar.getInstance();
        calendar9.set(2023, Calendar.AUGUST, 22);
        Date date9 = calendar8.getTime();
        calendar9.set(2023, Calendar.AUGUST, 29);
        Date due9 = calendar8.getTime();
        calendar9.set(2023, Calendar.AUGUST, 30);
        Date r9 = calendar9.getTime();
        Transaction t9 = new Transaction(null, m4, l1, date9, due9,r9, true, 20000);

        Calendar calendar10 = Calendar.getInstance();
        calendar10.set(2023, Calendar.JULY, 14);
        Date date10 = calendar8.getTime();
        calendar10.set(2023, Calendar.JULY, 21);
        Date due10 = calendar8.getTime();
        calendar10.set(2023, Calendar.JULY, 24);
        Date r10 = calendar9.getTime();
        Transaction t10 = new Transaction(null,m4, l16, date10, due10,r10, true, 30000);

        Calendar calendar11 = Calendar.getInstance();
        calendar11.set(2023, Calendar.FEBRUARY, 11);
        Date date11 = calendar8.getTime();
        calendar11.set(2023, Calendar.FEBRUARY, 18);
        Date due11 = calendar8.getTime();
        calendar11.set(2023, Calendar.FEBRUARY, 20);
        Date r11 = calendar9.getTime();
        Transaction t11 = new Transaction(null,m4, l8, date11, due11,r11, true, 20000);

        Calendar calendar12 = Calendar.getInstance();
        calendar12.set(2023, Calendar.MARCH, 1);
        Date date12 = calendar8.getTime();
        calendar12.set(2023, Calendar.MARCH, 8);
        Date due12 = calendar8.getTime();
        calendar12.set(2023, Calendar.MARCH, 9);
        Date r12 = calendar9.getTime();
        Transaction t12 = new Transaction(null,m6, l11, date12, due12,r12, true, 10000);

        Calendar calendar13 = Calendar.getInstance();
        calendar13.set(2023, Calendar.MARCH, 19);
        Date date13 = calendar8.getTime();
        calendar13.set(2023, Calendar.MARCH, 26);
        Date due13 = calendar8.getTime();
        calendar13.set(2023, Calendar.MARCH, 29);
        Date r13 = calendar9.getTime();
        Transaction t13 = new Transaction(null,m2, l17, date13, due13,r13, true, 30000);

        transactionRepo.saveAll(Arrays.asList(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12, t13));

    }

}
