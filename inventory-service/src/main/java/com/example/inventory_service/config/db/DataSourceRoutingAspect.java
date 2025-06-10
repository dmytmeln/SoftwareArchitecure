package com.example.inventory_service.config.db;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceRoutingAspect {

    @Before("@annotation(ReadOnly)")
    public void markReadOnly() {
        DataSourceContextHolder.setMode(DataSourceType.READ);
    }

    @Before("@annotation(tx)")
    public void markTx(Transactional tx) {
        if (tx.readOnly()) {
            DataSourceContextHolder.setMode(DataSourceType.READ);
            return;
        }

        DataSourceContextHolder.setMode(DataSourceType.WRITE);
    }

    @After("@annotation(ReadOnly) || @annotation(org.springframework.transaction.annotation.Transactional)")
    public void clearContext() {
        DataSourceContextHolder.clear();
    }

}
