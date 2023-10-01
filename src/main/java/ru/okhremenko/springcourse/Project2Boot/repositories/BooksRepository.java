package ru.okhremenko.springcourse.Project2Boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.okhremenko.springcourse.Project2Boot.models.Book;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByNameStartingWith(String name);

    List<Book> findByOwnerId(int id);

}
