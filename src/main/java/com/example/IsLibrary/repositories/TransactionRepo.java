package com.example.IsLibrary.repositories;

import com.example.IsLibrary.dto.response.Dto3MemberWithPenalty;
import com.example.IsLibrary.dto.response.DtoBorrowBookByMember;
import com.example.IsLibrary.dto.response.DtoTop5Book;
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
    Optional<Transaction> findByIdAndIsDeletedFalseAndReturnDateIsNull(Long id);
    Page<Transaction> findByIsDeletedFalseAndReturnDateIsNullOrderByIdAsc(Pageable pageable);
    Page<Transaction> findByIsPenaltyAndIsDeletedFalseOrderByIdAsc(Boolean isPenalty, Pageable pageable);
    List<Transaction> findByMemberIdAndIsPenaltyIsNull(Long id);

    @Query("SELECT new com.example.IsLibrary.dto.response.DtoTop5Book(t.bookList.book.codeBook AS code, t.bookList.isbn AS isbn, t.bookList.book.title AS title, t.bookList.book.author AS author, COUNT(t.bookList) AS total) " +
            "FROM Transaction t GROUP BY code, isbn, title, author  ORDER BY total DESC")
    List<DtoTop5Book> findTop5BooksByLoanCount(Pageable pageable);
    @Query("SELECT new com.example.IsLibrary.dto.response.DtoBorrowBookByMember(t.member.codeMember AS code, t.member.name AS name, t.member.gender AS gender,COUNT(t.bookList) AS total) " +
            "FROM Transaction t WHERE t.createDate BETWEEN :startDate AND :endDate GROUP BY code, name, gender ORDER BY total DESC")
    List<DtoBorrowBookByMember> findTop5BorrowBook(@Param("startDate") Date startDate, @Param("endDate")Date endDate, Pageable pageable);
    @Query("SELECT new com.example.IsLibrary.dto.response.Dto3MemberWithPenalty(t.member.codeMember AS code, t.member.name AS name, SUM(t.pay) AS pay) FROM Transaction t " +
            "WHERE t.isPenalty = true GROUP BY t.member.codeMember ,t.member.name  ORDER BY SUM(t.pay) DESC")
    List<Dto3MemberWithPenalty> findTop3MemberWithPenalty(Pageable pageable);

}
