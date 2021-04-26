package com.bank.doorstatic.repository.basicList;

import com.bank.doorstatic.entity.newList.News;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    Integer count(Specification<News> specification);

    Page<News> findAll(Specification<News> specification, Pageable pageable);

    List<News> findAll(Specification<News> specification);

    @Modifying
    @Transactional
    @Query("delete from News  where id in (?1)")
    void deleteBatch(List<Integer> ids);

}