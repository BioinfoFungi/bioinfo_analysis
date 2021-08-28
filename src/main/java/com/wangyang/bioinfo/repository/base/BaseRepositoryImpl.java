package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.util.CacheStore;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class BaseRepositoryImpl <T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID>{
    Class<T> clz;
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.clz=domainClass;
    }

    @Override
    public List<T> listAllCached(){
        String name = clz.getName();
        List<T> obj = CacheStore.getList(name, clz);
        if(obj==null){
            obj = this.findAll();
            CacheStore.save(name,obj);
        }
        return obj;
    }
}
