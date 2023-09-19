package com.example.IsLibrary.repositories;

import com.example.IsLibrary.models.BookList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookListRepo extends JpaRepository<BookList, Long> {
    Long countByBookIdAndIsAvailableIsTrue(Long id);
}
