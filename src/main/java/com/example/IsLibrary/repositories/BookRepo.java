package com.example.IsLibrary.repositories;

import com.example.IsLibrary.models.Book;
import com.example.IsLibrary.models.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthorAndIsDeletedIsFalse(String title, String author);
    Optional<Book> findByCodeBookAndIsDeletedIsFalse(String codeBook);
    Page<Book> findAllByIsDeletedIsFalseOrderByIdAsc(Pageable pageable);
}
