package com.vueboot.boot.dao;

import com.vueboot.boot.pojo.Book;
import com.vueboot.boot.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDAO extends JpaRepository<Book,Integer> {
    List<Book> findAllByCategory(Category category);
    List<Book> findAllByTitleLikeOrAuthorLike(String keyword1, String keyword2);
}
