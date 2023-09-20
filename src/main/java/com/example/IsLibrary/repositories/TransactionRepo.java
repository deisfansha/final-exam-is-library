package com.example.IsLibrary.repositories;

import com.example.IsLibrary.models.BookList;
import com.example.IsLibrary.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Transaction findAllByIsDeletedIsFalseOrderByIdAsc();
    Long countByBookList(BookList bookList);
    List<Transaction> findByCreateDateBetween(Date startEnd, Date endDate);
    Optional<Transaction> findByIdAndIsDeletedFalseAndReturnDateIsNull(Long id);
    Page<Transaction> findByIsDeletedFalseAndReturnDateIsNullOrderByIdAsc(Pageable pageable);
    Page<Transaction> findByIsMulctAndIsDeletedFalseOrderByIdAsc(Boolean isMulct, Pageable pageable);

}
