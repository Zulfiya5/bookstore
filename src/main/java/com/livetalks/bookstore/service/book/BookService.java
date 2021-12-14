package com.livetalks.bookstore.service.book;

import com.livetalks.bookstore.model.receive.book.BookAddModel;
import com.livetalks.bookstore.model.response.book.BookDisplayModel;
import com.livetalks.bookstore.model.response.book.CustomPage;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookDisplayModel insertBook(BookAddModel bookAddModel);

    List<BookDisplayModel> searchBooksByTitle(String bookTitle);

    BookDisplayModel updateBook(BookAddModel bookAddModel, UUID bookId);

    CustomPage<BookDisplayModel> getAllBooks(int page, int size);
}
