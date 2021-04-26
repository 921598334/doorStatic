package com.bank.doorstatic.repository.sorftware;


import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.entity.sorftware.Sorftware;
import com.bank.doorstatic.entity.sorftware.SorftwareType;
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
public interface SorftwareRepository extends JpaRepository<Sorftware, Integer> {





    Integer count(Specification<Sorftware> specification);

    Page<Sorftware> findAll(Specification<Sorftware> specification, Pageable pageable);

    List<Sorftware> findAll(Specification<Sorftware> specification);


    @Modifying
    @Transactional
    @Query("delete from Sorftware  where id in (?1)")
    void deleteBatch(List<Integer> ids);
}