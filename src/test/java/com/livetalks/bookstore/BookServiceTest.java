package com.livetalks.bookstore;

import com.livetalks.bookstore.entity.book.Book;
import com.livetalks.bookstore.exception.RestException;
import com.livetalks.bookstore.model.receive.book.BookAddModel;
import com.livetalks.bookstore.model.response.book.BookDisplayModel;
import com.livetalks.bookstore.model.response.book.CustomPage;
import com.livetalks.bookstore.repository.BookRepository;
import com.livetalks.bookstore.service.book.BookServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookServiceImpl bookService;

    @Test
    public void shouldReturnBookDisplayModelWhenBookInsertedSuccessfully() {
        BookAddModel bookAddModel = new BookAddModel(
                "So good they can't ignore you",
                "Cal Newport",
                2012
        );
        Book book = new Book(
                bookAddModel.getTitle(),
                bookAddModel.getAuthor(),
                bookAddModel.getPublishedYear()
        );

        when(bookRepository.existsByTitle(book.getTitle())).thenReturn(false);

        when(bookRepository.save(book)).thenReturn(book);

        BookDisplayModel bookDisplayModel = new BookDisplayModel(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedYear()
        );
        BookDisplayModel bookDisplayModel1 = bookService.insertBook(bookAddModel);

        assertThat(bookDisplayModel1.getTitle()).isNotNull();

        assertEquals(bookDisplayModel, bookDisplayModel1);

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void shouldThrowErrorWhenInsertingBookWithExistingTitle() {
        BookAddModel bookAddModel = new BookAddModel(
                "So good they can't ignore you",
                "Cal Newport",
                2012
        );
        Book book = new Book(
                bookAddModel.getTitle(),
                bookAddModel.getAuthor(),
                bookAddModel.getPublishedYear()
        );

        when(bookRepository.existsByTitle(book.getTitle())).thenReturn(true);

        assertThrows(RestException.class, () ->
                bookService.insertBook(bookAddModel)
        );

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void shouldReturnListOfBooksByContainingGivenTitle() {
        List<BookDisplayModel> foundBooks = new ArrayList<>();
        List<Book> bookList = createBookList();

        when(bookRepository.findAll()).thenReturn(bookList);

        bookList.forEach(book -> {
                    if (book
                            .getTitle()
                            .toLowerCase(Locale.ROOT)
                            .contains("No excuses".toLowerCase(Locale.ROOT)))
                        foundBooks.add(
                                new BookDisplayModel(
                                        book.getId(),
                                        book.getTitle(),
                                        book.getAuthor(),
                                        book.getPublishedYear()
                                )
                        );
                }
        );
        List<BookDisplayModel> foundBooks1 = bookService.searchBooksByTitle("No excuses");

        assertEquals(foundBooks1, foundBooks);
    }

    @Test
    public void shouldThrowErrorWhenThereIsNoBookContainingGivenTitle() {
        List<BookDisplayModel> foundBooks = new ArrayList<>();
        List<Book> bookList = createBookList();

        when(bookRepository.findAll()).thenReturn(bookList);

        bookList.forEach(book -> {
                    if (book
                            .getTitle()
                            .toLowerCase(Locale.ROOT)
                            .contains("Deep work".toLowerCase(Locale.ROOT)))
                        foundBooks.add(
                                new BookDisplayModel(
                                        book.getId(),
                                        book.getTitle(),
                                        book.getAuthor(),
                                        book.getPublishedYear()
                                )
                        );
                }
        );
        assertThat(foundBooks).isEmpty();

        assertThrows(RestException.class, () ->
                bookService.searchBooksByTitle("Deep work")
        );
    }

    @Test
    public void shouldUpdateBook() {
        BookAddModel bookAddModel = new BookAddModel(
                "So good they can't ignore you",
                "Cal Newport",
                2012
        );
        final Book book = new Book(
                bookAddModel.getTitle(),
                bookAddModel.getAuthor(),
                bookAddModel.getPublishedYear()
        );

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        when(bookRepository.save(book)).thenReturn(book);

        BookDisplayModel bookDisplayModel = new BookDisplayModel(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedYear()
        );
        BookDisplayModel bookDisplayModel1 = bookService.updateBook(bookAddModel, book.getId());

        assertThat(bookDisplayModel1).isNotNull();

        assertEquals(bookDisplayModel, bookDisplayModel1);

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void shouldThrowErrorWhenThereIsNoBookWithGivenId() {
        BookAddModel bookAddModel = new BookAddModel(
                "So good they can't ignore you",
                "Cal Newport",
                2012
        );

        when(bookRepository.findById(UUID.fromString("94ad45b7-4c46-43cc-9636-654615802bbc")))
                .thenReturn(Optional.empty());

        assertThrows(RestException.class, () ->
                bookService.updateBook(bookAddModel, UUID.fromString("94ad45b7-4c46-43cc-9636-654615802bbc"))
        );
    }

    @Test
    public void shouldReturnListOfBooks() {
        List<Book> bookList = createBookList();
        Page<Book> booksPage = new PageImpl<>(bookList);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageable)).thenReturn(booksPage);

        CustomPage<BookDisplayModel> customBooksPage = bookPageToCustomBookDisplayModelPage(booksPage);

        CustomPage<BookDisplayModel> customBooksPage1 = bookService.getAllBooks(0, 10);

        assertEquals(customBooksPage1, customBooksPage);
    }


    private List<Book> createBookList() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(
                new Book(
                        "So good they can't ignore you",
                        "Cal Newport",
                        2012)
        );
        bookList.add(
                new Book(
                        "No Excuses!",
                        "Brian Tracy",
                        2010)
        );
        bookList.add(
                new Book(
                        "The power of habit",
                        "Charles Duhigg",
                        2002)
        );
        return bookList;
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

    private BookDisplayModel bookToBookDisplayModel(Book book) {
        return new BookDisplayModel(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishedYear()
        );
    }
}
