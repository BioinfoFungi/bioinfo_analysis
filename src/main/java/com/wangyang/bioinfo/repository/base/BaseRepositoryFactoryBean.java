package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.util.CacheStoreList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class BaseRepositoryFactoryBean <R extends JpaRepository<T, I>, T,
        I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {
    private final CacheStoreList<T,I> cacheStore;

    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface,
                                     CacheStoreList<T,I> cacheStore) {
        super(repositoryInterface);
        this.cacheStore=cacheStore;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BaseRepositoryFactory(entityManager,cacheStore);
    }

    private static class BaseRepositoryFactory<T, I extends Serializable>
            extends JpaRepositoryFactory {
        private final CacheStoreList<T,I> cacheStore;

        public BaseRepositoryFactory(EntityManager em,CacheStoreList<T,I> cacheStore) {
            super(em);
            this.cacheStore=cacheStore;
        }
        //设置具体的实现类是BaseRepositoryImpl
        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new BaseRepositoryImpl<T, I>((Class<T>) information.getDomainType(), entityManager,cacheStore);
        }

        //设置具体的实现类的class
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }

}
