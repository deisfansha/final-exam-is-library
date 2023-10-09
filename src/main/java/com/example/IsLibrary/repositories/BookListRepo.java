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
    Optional<BookList> findByIsbnAndIsDeletedIsFalseAndIsAvailableIsTrue(String isbn);
    Optional<BookList> findByIdAndIsDeletedIsFalse(Long id);
    Optional<BookList> findByIsbnAndIsDeletedIsFalse(String isbn);
    @Query("SELECT new com.example.IsLibrary.dto.response.DtoBookListResponse(bl.book.codeBook AS code, bl.book.title AS title, bl.book.author AS author, COUNT(bl.isbn) AS total) FROM BookList bl " +
            "WHERE bl.isDeleted = false GROUP BY code, title, author ORDER BY bl.book.title ASC")
    Page<BookList> findBookListWithTotalBook(Pageable pageable);
}
