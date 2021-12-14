package com.livetalks.bookstore.model.response.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDisplayModel {
    private UUID id;
    private String title;
    private String author;
    private int publishedYear;

}
