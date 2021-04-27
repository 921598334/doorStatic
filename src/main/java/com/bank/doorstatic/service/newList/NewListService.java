package com.bank.doorstatic.service.newList;


import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.repository.basicList.NewsRepository;
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
public class NewListService {



    @Autowired
    NewsRepository newsRepository;





    public List<News> queryFlows(int pageNo, int pageSize, String title, String id, Date createTimeStart, Date createTimeEnd) {
        List<News> result = null;

        // 构造自定义查询条件
        Specification<News> queryCondition = new Specification<News>() {
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                if (title != null && !title.equals("")) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+title+"%"));
                }
                if (id != null && !id.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), id));
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
                result = newsRepository.findAll(queryCondition);
            } else {
                result = newsRepository.findAll(queryCondition, PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"))).getContent();
            }
        } catch (Exception e) {
            log.error("--queryFlowByCondition-- error : ", e);
        }

        return result;
    }





    public Integer getCounts(String title, String id, Date createTimeStart, Date createTimeEnd) {
        Integer total = 0;
        Specification<News> countCondition = new Specification<News>() {
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (title != null && !title.equals("")) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+title+"%"));
                }
                if (id != null && !id.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), id));
                }
                if (createTimeStart != null && createTimeEnd != null) {
                    predicateList.add(criteriaBuilder.between(root.get("create_time"), createTimeStart, createTimeEnd));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };

        try {
            total = newsRepository.count(countCondition);
        } catch (Exception e) {
            log.error("--getCountsByCondition-- error: ", e);
        }
        return total;
    }

}
