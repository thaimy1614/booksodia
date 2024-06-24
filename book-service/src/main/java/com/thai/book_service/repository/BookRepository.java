package com.thai.book_service.repository;

import com.thai.book_service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByAuthor(String author);
}
