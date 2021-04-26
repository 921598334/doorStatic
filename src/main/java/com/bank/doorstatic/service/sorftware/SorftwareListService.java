package com.bank.doorstatic.service.sorftware;


import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.entity.sorftware.Sorftware;
import com.bank.doorstatic.entity.sorftware.SorftwareType;
import com.bank.doorstatic.repository.basicList.NewsRepository;
import com.bank.doorstatic.repository.sorftware.SorftwareRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class SorftwareListService {



    @Autowired
    SorftwareRepository sorftwareRepository;




    public List<Sorftware> queryFlows(int pageNo, int pageSize, String title, String id, SorftwareType sorftwareTypeid, Date createTimeStart, Date createTimeEnd) {
        List<Sorftware> result = null;

        // 构造自定义查询条件
        Specification<Sorftware> queryCondition = new Specification<Sorftware>() {

            @Override
            public Predicate toPredicate(Root<Sorftware> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                if (title != null && !title.equals("")) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+title+"%"));
                }
                if (id != null && !id.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (sorftwareTypeid != null && !sorftwareTypeid.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("sorftwareType"), sorftwareTypeid));
                }
                if (createTimeStart != null && createTimeEnd != null) {
                    predicateList.add(criteriaBuilder.between(root.get("create_time"), createTimeStart, createTimeEnd));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };

        // 分页和不分页，这里按起始页和每页展示条数为0时默认为不分页，分页的话按创建时间降序
        try {
            if (pageNo == 0 && pageSize == 0) {
                result = sorftwareRepository.findAll(queryCondition);
            } else {
                result = sorftwareRepository.findAll(queryCondition, PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"))).getContent();
            }
        } catch (Exception e) {
            log.error("--queryFlowByCondition-- error : ", e);
        }

        return result;
    }





    public Integer getCounts(String title, String id, SorftwareType sorftwareTypeid, Date createTimeStart, Date createTimeEnd) {
        Integer total = 0;
        Specification<Sorftware> countCondition = new Specification<Sorftware>() {

            @Override
            public Predicate toPredicate(Root<Sorftware> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (title != null && !title.equals("")) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+title+"%"));
                }
                if (id != null && !id.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (sorftwareTypeid != null && !sorftwareTypeid.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("sorftwareType"), sorftwareTypeid));
                }
                if (createTimeStart != null && createTimeEnd != null) {
                    predicateList.add(criteriaBuilder.between(root.get("create_time"), createTimeStart, createTimeEnd));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };

        try {
            total = sorftwareRepository.count(countCondition);
        } catch (Exception e) {
            log.error("--getCountsByCondition-- error: ", e);
        }
        return total;
    }

}
