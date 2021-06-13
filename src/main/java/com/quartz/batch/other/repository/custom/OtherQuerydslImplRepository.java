package com.quartz.batch.other.repository.custom;

import com.quartz.batch.spring.config.QueryDslRepositorySupportWrapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Repository
public class OtherQuerydslImplRepository extends QueryDslRepositorySupportWrapper implements OtherCustomRepository {

    public OtherQuerydslImplRepository() {
        super(Class.class);
    }

    private EntityManager entityManager;
    private JPAQueryFactory factory ;

    @PersistenceContext(unitName = "other")
    public void setOtherEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        factory = new JPAQueryFactory(entityManager);
        super.setEntityManager(entityManager);
    }


}
