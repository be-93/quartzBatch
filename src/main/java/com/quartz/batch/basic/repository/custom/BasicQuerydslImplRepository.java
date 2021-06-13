package com.quartz.batch.basic.repository.custom;

import com.quartz.batch.spring.config.QueryDslRepositorySupportWrapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class BasicQuerydslImplRepository extends QueryDslRepositorySupportWrapper implements BasicCustomRepository {

    public BasicQuerydslImplRepository() {
        super(Class.class);
    }

    private EntityManager entityManager;
    private JPAQueryFactory factory ;

    @PersistenceContext(unitName = "basic")
    public void setOtherEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        factory = new JPAQueryFactory(entityManager);
        super.setEntityManager(entityManager);
    }

}
