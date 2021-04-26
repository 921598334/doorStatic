package com.bank.doorstatic.service.sorftware;



import com.bank.doorstatic.entity.newList.News;
import com.bank.doorstatic.entity.sorftware.SorftwareType;

import com.bank.doorstatic.repository.sorftware.SorftwareTypeRepository;
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
public class SorftwareTypeService {



    @Autowired
    SorftwareTypeRepository sorftwareTypeRepository;


    public List<SorftwareType> queryFlows(int pageNo, int pageSize, String name, String id) {

        List<SorftwareType> result = null;

        // 构造自定义查询条件
        Specification<SorftwareType> queryCondition = new Specification<SorftwareType>() {
            @Override
            public Predicate toPredicate(Root<SorftwareType> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicateList = new ArrayList<>();

                if (name != null && !name.equals("")) {
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
                }
                if (id != null && !id.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), id));
                }

                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };

        // 分页和不分页，这里按起始页和每页展示条数为0时默认为不分页，分页的话按创建时间降序
        try {
            if (pageNo == 0 && pageSize == 0) {
                result = sorftwareTypeRepository.findAll(queryCondition);

            } else {
                result = sorftwareTypeRepository.findAll(queryCondition, PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"))).getContent();
            }
        } catch (Exception e) {
            log.error("--queryFlowByCondition-- error : ", e);
        }

        return result;
    }





    public Integer getCounts(String name, String id) {
        Integer total = 0;
        Specification<SorftwareType> countCondition = new Specification<SorftwareType>() {
            @Override
            public Predicate toPredicate(Root<SorftwareType> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (name != null && !name.equals("")) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+name+"%"));
                }
                if (id != null && !id.equals("")) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), id));
                }

                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };

        try {
            total = sorftwareTypeRepository.count(countCondition);
        } catch (Exception e) {
            log.error("--getCountsByCondition-- error: ", e);
        }
        return total;
    }

}
