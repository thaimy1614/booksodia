package com.thai.book_service.configuration;

import com.thai.book_service.entity.Book;
import com.thai.book_service.entity.Category;
import com.thai.book_service.entity.Review;
import com.thai.book_service.enums.BookStatus;
import com.thai.book_service.repository.BookRepository;
import com.thai.book_service.repository.CategoryRepository;
import com.thai.book_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ApplicationConfiguration {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    @Bean
    ApplicationRunner applicationRunner(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.findByName("IT").isEmpty()) {
                Category category = Category.builder().name("IT").build();
//                category.setBooks(List.of(Book.builder().author("Duong Thai").category(category).title("Microservices Architecture Deep").price(1000).publishedDate(LocalDate.now()).build()));
                categoryRepository.save(category);
                categoryRepository.save(Category.builder().name("Language").build());
                categoryRepository.save(Category.builder().name("AI").build());
                categoryRepository.save(Category.builder().name("Logistic").build());
                categoryRepository.save(Category.builder().name("Medicine").build());
                Book book = bookRepository.save(Book.builder()
                        .author("Duong Thai")
                        .category(category)
                        .title("Microservices Architecture Deep")
                        .quantity(9)
                        .price(1000)
                        .status(BookStatus.AVAILABLE.name())
                        .publishedDate(LocalDate.now())
                        .build());
                bookRepository.save(Book.builder()
                        .author("Nguyen My")
                        .category(category)
                        .title("OOP JAVA LEARNING")
                        .price(800)
                        .publishedDate(LocalDate.now())
                        .quantity(8)
                        .build());
                reviewRepository.save(Review.builder().book(book).reviewDate(LocalDateTime.now()).reviewText("Good book").userId("lxlthailxl@gmail.com").rating(5).build());
                reviewRepository.save(Review.builder().book(book).reviewDate(LocalDateTime.now()).reviewText("Very useful").userId("thaidqce171563@gmail.com").rating(4).build());
            }

        };
    }
}
