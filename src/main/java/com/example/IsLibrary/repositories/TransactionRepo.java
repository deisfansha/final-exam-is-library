package com.example.IsLibrary.repositories;

import com.example.IsLibrary.dto.response.Dto3MemberWithPenalty;
import com.example.IsLibrary.dto.response.DtoBorrowBookByMember;
import com.example.IsLibrary.dto.response.DtoTop5Book;
import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Long countByBookList(BookList bookList);
    List<Transaction> findByCreateDateBetween(Date startEnd, Date endDate);
    Optional<Transaction> findByIdAndIsDeletedFalseAndReturnDateIsNull(Long id);
    Page<Transaction> findByIsDeletedFalseAndReturnDateIsNullOrderByIdAsc(Pageable pageable);
    Page<Transaction> findByIsMulctAndIsDeletedFalseOrderByIdAsc(Boolean isMulct, Pageable pageable);

    List<Transaction> findByMemberIdAndIsMulctIsNull(Long id);

    @Query("SELECT new com.example.IsLibrary.dto.response.DtoTop5Book(t.bookList, COUNT(t)) FROM Transaction t GROUP BY t.bookList ORDER BY COUNT(t) DESC")
    List<DtoTop5Book> findTop5BooksByLoanCount(Pageable pageable);
    @Query("SELECT new com.example.IsLibrary.dto.response.DtoBorrowBookByMember(t.member, COUNT(t.bookList)) FROM Transaction t WHERE t.createDate BETWEEN :startDate AND :endDate GROUP BY t.member ORDER BY COUNT(t.bookList) DESC")
    List<DtoBorrowBookByMember> findTop5BorrowBook(@Param("startDate") Date startDate, @Param("endDate")Date endDate, Pageable pageable);
    @Query("SELECT new com.example.IsLibrary.dto.response.Dto3MemberWithPenalty(t.member ,SUM(t.pay)) FROM Transaction t WHERE t.isMulct = true GROUP BY t.member ORDER BY COUNT(t.member) DESC")
    List<Dto3MemberWithPenalty> findTop3MemberWithPenalty(Pageable pageable);

}
