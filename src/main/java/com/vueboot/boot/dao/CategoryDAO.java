package com.vueboot.boot.dao;

import com.vueboot.boot.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> {

}
