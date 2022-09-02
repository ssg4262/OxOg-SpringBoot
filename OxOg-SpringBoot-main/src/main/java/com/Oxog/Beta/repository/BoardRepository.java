package com.Oxog.Beta.repository;

import com.Oxog.Beta.model.Board;
import com.Oxog.Beta.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

        List<Board> findByTitle(String title);
        List<Board> findByTitleOrContent(String title,String content);

        Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);


    }
