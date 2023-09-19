package com.example.IsLibrary.repositories;

import com.example.IsLibrary.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthorAndIsDeletedIsFalse(String title, String author);
}
