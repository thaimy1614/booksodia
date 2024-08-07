package com.thai.book_service.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai.book_service.dto.Kafka.PaymentStatus;
import com.thai.book_service.dto.Order;
import com.thai.book_service.dto.Order_Book;
import com.thai.book_service.dto.request.BookCreationRequest;
import com.thai.book_service.dto.response.BookDetailResponse;
import com.thai.book_service.dto.response.BookResponse;
import com.thai.book_service.entity.Book;
import com.thai.book_service.entity.Category;
import com.thai.book_service.enums.BookStatus;
import com.thai.book_service.mapper.BookMapper;
import com.thai.book_service.repository.BookRepository;
import com.thai.book_service.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final AmazonS3 s3;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> bookResponses = new ArrayList<>();
        books.forEach(book -> {
            BookResponse response = bookMapper.toBookResponse(book);
            response.setCategoryName(book.getCategory().getName());
            response.setRating(calculateRating(book));
            response.setStatus(book.getStatus());
            bookResponses.add(response);
        });
        return bookResponses;
    }

    public double calculateRating(Book book) {
        final double[] total = {0};
        book.getReviews().forEach(review -> total[0] += review.getRating());
        if (!book.getReviews().isEmpty()) {
            return total[0] / book.getReviews().size();
        } else {
            return 0;
        }

    }

    public BookDetailResponse getBookDetail(String id) {
        Book book = bookRepository.findById(id).orElseThrow();
        BookDetailResponse bookDetailResponse = bookMapper.toBookDetailResponse(book);
        bookDetailResponse.setCategoryName(book.getCategory().getName());
        bookDetailResponse.setRating(calculateRating(book));
        bookDetailResponse.setReviews(book.getReviews());
        return bookDetailResponse;
    }

    @Override
    public List<BookResponse> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        List<BookResponse> bookResponses = new ArrayList<>();
        books.forEach(book -> bookResponses.add(bookMapper.toBookResponse(book)));
        return bookResponses;
    }

    @Override
    public BookResponse getBookById(String id) {
        return bookMapper.toBookResponse(bookRepository.findById(id).orElseThrow());
    }

    @Override
    public BookResponse addBook(BookCreationRequest request, MultipartFile file) {
        Book book = bookMapper.toBook(request);
        book.setCategory(categoryRepository.findByName(request.getCategoryName()).orElseGet(() -> categoryRepository.save(Category.builder().name(request.getCategoryName()).build())));
        book.setStatus(request.getQuantity() > 0 ? BookStatus.AVAILABLE.name() : BookStatus.OUT_OF_STOCK.name());
        book = bookRepository.save(book);
        uploadImg(file, book.getId());
        BookResponse bookResponse = bookMapper.toBookResponse(book);
        bookResponse.setCategoryName(book.getCategory().getName());
        return bookResponse;
    }

    @Override
    public BookResponse updateBook(String id, BookCreationRequest bookCreationRequest, MultipartFile file) {
        Book book = bookRepository.findById(id).orElseThrow();
        Book bookToUpdate = bookMapper.toBook(bookCreationRequest);
        bookToUpdate.setId(id);
        bookToUpdate.setReviews(book.getReviews());
        bookToUpdate.setCategory(categoryRepository.findByName(bookCreationRequest.getCategoryName()).orElseThrow());
        bookToUpdate.setStatus(bookCreationRequest.getQuantity() > 0 ? BookStatus.AVAILABLE.name() : BookStatus.OUT_OF_STOCK.name());
        BookResponse response = bookMapper.toBookResponse(bookRepository.save(bookToUpdate));
        uploadImg(file, book.getId());
        return response;
    }

    @Override
    public void deleteBook(String id) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setStatus(BookStatus.DELETED.name());
        bookRepository.save(book);
    }

    public String getUrl(String objectName) {
        return s3.getUrl(bucketName, objectName).toString();
    }


    @Async
    public void uploadImg(MultipartFile file, String id) {
        try {
            log.info("Uploading image to " + id);
            InputStream in = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            metadata.setContentLength(in.available());
            PutObjectRequest request = new PutObjectRequest(bucketName, id, file.getInputStream(), metadata);
            s3.putObject(request);
            String url = getUrl(id);
            log.info(url);
            Book book = bookRepository.findById(id).orElseThrow();
            book.setImage(url);
            bookRepository.save(book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateGetUrl(String objectName, long time) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += time;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectName)
                .withExpiration(expiration).withMethod(HttpMethod.GET);
        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();

    }

    public String generateUploadUrl(String objectName, long time) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += time;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectName)
                .withExpiration(expiration).withMethod(HttpMethod.PUT);
        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();

    }

    private void revertQuantity(String bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setQuantity(book.getQuantity() + quantity);
    }

    public void subtractQuantity(String bookId, int quantity) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setQuantity(book.getQuantity() - quantity);
    }

    @KafkaListener(groupId = "update-book-group", topics = "payment-status")
    public void updateBookQuantity(PaymentStatus paymentStatus) throws JsonProcessingException {
        if (!paymentStatus.getStatus().equals("00")) {
            String orderId = paymentStatus.getOrderId();
            Order order = objectMapper.readValue((String) redisTemplate.opsForValue().get("order:" + orderId), Order.class);
            List<Order_Book> books = order.getBooks();
            books.forEach(book -> {
                revertQuantity(book.getBookId(), book.getQuantity());
            });
        }
        redisTemplate.delete("order:" + paymentStatus.getOrderId());
    }

    @KafkaListener(groupId = "subtract-quantity-group", topics = "created-order")
    public void subtractQuantity(String orderId) throws JsonProcessingException {
        if (orderId != null) {
            Order order = objectMapper.readValue((String) redisTemplate.opsForValue().get("order:" + orderId), Order.class);
            List<Order_Book> books = order.getBooks();
            books.forEach(book -> {
                subtractQuantity(book.getBookId(), book.getQuantity());
            });
        }
    }

}
