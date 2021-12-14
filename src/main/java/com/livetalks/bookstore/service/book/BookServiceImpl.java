package com.livetalks.bookstore.service.book;

import com.livetalks.bookstore.entity.book.Book;
import com.livetalks.bookstore.exception.RestException;
import com.livetalks.bookstore.model.receive.book.BookAddModel;
import com.livetalks.bookstore.model.response.book.BookDisplayModel;
import com.livetalks.bookstore.model.response.book.CustomPage;
import com.livetalks.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDisplayModel insertBook(BookAddModel bookAddModel) {
        if (existsByTitle(bookAddModel.getTitle()))
            throw new RestException(HttpStatus.CONFLICT, "Book with this title already exists");
        Book newBook = bookAddModelToBook(bookAddModel);
        bookRepository.save(newBook);
        return bookToBookDisplayModel(newBook);
    }

    @Override
    public List<BookDisplayModel> searchBooksByTitle(String bookTitle) {
        List<BookDisplayModel> foundBooks = new ArrayList<>();
        List<Book> bookList = bookRepository.findAll();
        bookList.forEach(book -> {
                    if (book
                            .getTitle()
                            .toLowerCase(Locale.ROOT)
                            .contains(bookTitle.toLowerCase(Locale.ROOT)))
                        foundBooks.add(bookToBookDisplayModel(book));
                }
        );
        if (foundBooks.isEmpty())
            throw new RestException(HttpStatus.CONFLICT, "No books found");
        return foundBooks;
    }

    @Override
    public BookDisplayModel updateBook(BookAddModel bookAddModel, UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Book not found"));
        book.setTitle(bookAddModel.getTitle());
        book.setAuthor(bookAddModel.getAuthor());
        book.setPublishedYear(bookAddModel.getPublishedYear());
        bookRepository.save(book);
        return bookToBookDisplayModel(book);
    }

    @Override
    public CustomPage<BookDisplayModel> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return bookPageToCustomBookDisplayModelPage(booksPage);
    }

    private CustomPage<BookDisplayModel> bookPageToCustomBookDisplayModelPage(
            Page<Book> booksPage
    ) {
        List<Book> bookList = booksPage.getContent();
        List<BookDisplayModel> bookDisplayModelList = bookListToBookDisplayModelList(bookList);
        return new CustomPage<>(
                bookDisplayModelList,
                booksPage.getTotalPages(),
                booksPage.getNumber(),
                booksPage.getTotalElements(),
                booksPage.getSize(),
                booksPage.getNumberOfElements()
        );
    }

    private List<BookDisplayModel> bookListToBookDisplayModelList(List<Book> bookList) {
        List<BookDisplayModel> bookDisplayModelList = new ArrayList<>();
        bookList.forEach(book ->
                bookDisplayModelList.add(bookToBookDisplayModel(book))
        );
        return bookDisplayModelList;
    }

    private boolean existsByTitle(String bookTitle) {
        return bookRepository.existsByTitle(bookTitle);
    }

    private Book bookAddModelToBook(BookAddModel bookAddModel) {
        return new Book(
                bookAddModel.getTitle(),
                bookAddModel.getAuthor(),
                bookAddModel.getPublishedYear()
        );
    }

    private BookDisplayModel bookToBookDisplayModel(Book book) {
        return new BookDisplayModel(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedYear()
        );
    }


}
