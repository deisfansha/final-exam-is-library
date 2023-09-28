package com.example.IsLibrary.repositories;

import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.BookList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookListRepo extends JpaRepository<BookList, Long> {
    Long countByBookIdAndIsAvailableIsTrue(Long id);
    List<BookList> findByBookIdAndIsDeletedIsFalseAndIsAvailableIsTrue(Long codeBook);
    Optional<BookList> findByIdAndIsDeletedIsFalseAndIsAvailableIsTrue(Long id);
    Optional<BookList> findByIdAndIsDeletedIsFalse(Long id);
    Page<BookList> findByIsDeletedIsFalse(Pageable pageable);
}
