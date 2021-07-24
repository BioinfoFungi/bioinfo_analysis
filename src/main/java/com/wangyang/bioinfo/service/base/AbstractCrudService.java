package com.wangyang.bioinfo.service.base;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public abstract class AbstractCrudService<DOMAIN, ID> implements ICrudService<DOMAIN, ID> {
    private final String domainName;
    @Autowired
    BaseRepository<DOMAIN, ID> repository;

    public AbstractCrudService(){
        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(0);
        domainName = domainClass.getSimpleName();
    }
    private Type fetchType(int index) {
        Assert.isTrue(index >= 0 && index <= 1, "type index must be between 0 to 1");
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }

    @Override
    public List<DOMAIN> listAll() {
//        Assert.notNull(id, domainName + " id must not be null");

        return repository.findAll();
    }

    @Override
    public DOMAIN add(DOMAIN domain) {
//        System.out.println(domainName);
        return repository.save(domain);
    }

    @Override
    public DOMAIN save(DOMAIN domain) {
        return repository.save(domain);
    }

    protected DOMAIN getInstance()
    {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<DOMAIN> type = (Class<DOMAIN>) superClass.getActualTypeArguments()[0];
        try
        {
            return type.newInstance();
        }
        catch (Exception e)
        {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DOMAIN> initData(String filePath){
        try {
            try(FileInputStream inputStream = new FileInputStream(filePath)){
                repository.deleteAll();
                DOMAIN instance = getInstance();
                BeanListProcessor<DOMAIN> beanListProcessor = new BeanListProcessor<>((Class<DOMAIN>) getInstance().getClass());
                TsvParserSettings settings = new TsvParserSettings();
                settings.setProcessor(beanListProcessor);
                settings.setHeaderExtractionEnabled(true);
                TsvParser parser = new TsvParser(settings);
                parser.parse(inputStream);
                List<DOMAIN> beans = beanListProcessor.getBeans();
                List<DOMAIN> list = repository.saveAll(beans);
                return list;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
