package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.CacheStore;
import com.wangyang.bioinfo.util.CacheStoreList;
import org.hibernate.Session;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID>{

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final CacheStoreList<T,ID> cacheStore;
    private final PersistenceProvider provider;
    private final Class<T> clz;

    public BaseRepositoryImpl(Class<T> domainClass,
                              EntityManager em,
                              CacheStoreList<T,ID> cacheStore) {
        super(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
        this.em=em;
        this.cacheStore=cacheStore;
        this.provider = PersistenceProvider.fromEntityManager(em);
        this.clz = domainClass;

    }


    @Override
    public List<T> listAllCached(){
        List<T> list = cacheStore.get(clz);
        if(list==null){
            List<T> ts = this.findAll();
            Map<ID,T> newMap = new HashMap<>();
            ts.forEach(t -> {
                Session session = (Session)em.getDelegate();
                ID id = (ID)session.getIdentifier(t);
                newMap.put(id,t);
            });
            cacheStore.add(newMap);
        }
        return list;
    }

    @Override
    public T saveCached(T entity){
        Assert.notNull(entity, "Entity must not be null.");
        if (entityInformation.isNew(entity)) {
            this.em.persist(entity);
            Session session = (Session)em.getDelegate();
            ID id = (ID)session.getIdentifier(entity);
            cacheStore.add(id,entity);
        }else {
            entity = this.em.merge(entity);
            Session session = (Session)em.getDelegate();
            ID id = (ID)session.getIdentifier(entity);
            cacheStore.update(id,entity);
        }
        return entity;
    }

//    @Override
//    public T updateCached(ID id, T entity){
//        Assert.notNull(entity, "Entity must not be null.");
//        Class<T> domainType = this.getDomainClass();
//        String name = domainType.getName();
//        if (!entityInformation.isNew(entity)) {
//            this.em.persist(entity);
////            cacheStore.addItem(name,entity);
//            return entity;
//        } else {
//
//            throw  new BioinfoException("要更新的对象不存在");
//        }
//    }

}
