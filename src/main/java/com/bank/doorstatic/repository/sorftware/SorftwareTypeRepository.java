package com.bank.doorstatic.repository.sorftware;


import com.bank.doorstatic.entity.newList.News;
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
public interface SorftwareTypeRepository extends JpaRepository<SorftwareType, Integer> {



    Integer count(Specification<SorftwareType> specification);

    Page<SorftwareType> findAll(Specification<SorftwareType> specification, Pageable pageable);

    List<SorftwareType> findAll(Specification<SorftwareType> specification);


    @Modifying
    @Transactional
    @Query("delete from SorftwareType  where id in (?1)")
    void deleteBatch(List<Integer> ids);

}