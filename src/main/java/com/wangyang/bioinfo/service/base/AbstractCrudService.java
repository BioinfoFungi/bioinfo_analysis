package com.wangyang.bioinfo.service.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public abstract class AbstractCrudService<DOMAIN extends BaseEntity, ID extends Serializable> implements ICrudService<DOMAIN, ID> {

    @PersistenceContext
    private EntityManager em;
    private final String domainName;
    private final BaseRepository<DOMAIN, ID> repository;
    public AbstractCrudService(BaseRepository<DOMAIN, ID> repository) {
        this.repository = repository;
        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(0);
        domainName = domainClass.getSimpleName();
    }


    @Override
    @Transactional
    public void truncateTable(){
        Entity entity = getInstance().getClass().getAnnotation(Entity.class);
        String name = entity.name();
        Query query = em.createNativeQuery("truncate table "+name);
        query.executeUpdate();
        Query resetQuery = em.createNativeQuery("ALTER TABLE "+name+" ALTER COLUMN ID RESTART WITH 1");
        resetQuery.executeUpdate();
    }


    private Type fetchType(int index) {
        Assert.isTrue(index >= 0 && index <= 1, "type index must be between 0 to 1");
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }

    @Override
    public DOMAIN findById(ID Id){
        Optional<DOMAIN> fileOptional = repository.findById(Id);
        if(!fileOptional.isPresent()){
            throw new BioinfoException("查找的象不存在!");
        }
        return fileOptional.get();
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
    public DOMAIN add(DOMAIN domain, User user) {
        return repository.save(domain);
    }

    @Override
    public DOMAIN update(ID id, DOMAIN inputDomain) {
        DOMAIN domain = findById(id);
        inputDomain.setId(null);
        BeanUtil.copyProperties(inputDomain,domain);
        return save(domain);
    }

    @Override
    public DOMAIN update(ID id, DOMAIN inputDomain, User user) {
        return update(id,inputDomain);
    }

    @Override
    public DOMAIN add(Map<String, Object> map, User user) {
        DOMAIN domain = mapToObj(map);
        return add(domain,user);
    }

    @Override
    public DOMAIN update(ID id, Map<String, Object> map, User user) {
        DOMAIN domain = mapToObj(map);
        return update(id,domain,user);
    }
    protected DOMAIN mapToObj(Map<String, Object> map) {
        DOMAIN domain = getInstance();
        List<Field> fields = ObjectToCollection.getFields(domain.getClass());
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = map.get(field.getName());
                if(value!=null){
                    value = convertValType( value, field.getType());
                    field.set(domain, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return domain;
    }

    private Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else if(fieldTypeClass.isEnum()){
            retVal = Enum.valueOf((Class<Enum>) fieldTypeClass,value.toString());
        }else if (Date.class.getName().equals(fieldTypeClass.getName())){
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                retVal = format.parse(value.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            retVal = value;
        }
        return retVal;
    }

    private DOMAIN mapToObj2(Map<String, Object> map) {
        DOMAIN domain = getInstance();
        try {
            org.apache.commons.beanutils.BeanUtils.populate(domain, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return domain;
    }

    @Override
    public DOMAIN save(DOMAIN domain) {
        return repository.save(domain);
    }

    protected Class<DOMAIN> getInstanceClass(){
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<DOMAIN> type = (Class<DOMAIN>) superClass.getActualTypeArguments()[0];
        return type;
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
    public void deleteAll(){
        repository.deleteAll();
    }
    @Override
    public void delete(DOMAIN t){
        repository.delete(t);
    }



    @Override
    public Page<DOMAIN> pageBy(Pageable pageable){
        return repository.findAll(pageable);
    }

    @Override
    public Page<DOMAIN> pageBy(Pageable pageable, String keywords) {
        return pageBy(pageable,keywords,null);
    }

    public Page<DOMAIN> pageBy(Pageable pageable,String keywords,DOMAIN domain) {
        Specification<DOMAIN> specification = buildSpecByQuery(domain, keywords);
        return repository.findAll(specification,pageable);
    }
    protected Specification<DOMAIN> buildSpecByQuery(DOMAIN domain, String keywords) {
        return (Specification<DOMAIN>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates= new LinkedList<>();
            if(domain!=null){
                toPredicate(domain,root, criteriaBuilder,predicates);
            }
            if(keywords!=null&!"".equals(keywords)){
                Set<String> fields = ObjectToCollection.getSpecialFields(getInstanceClass(), QueryField.class);
                if(fields!=null && fields.size()!=0 && keywords!=null ){
                    String likeCondition = String
                            .format("%%%s%%", StringUtils.strip(keywords));
                    List<Predicate> orPredicates = new ArrayList<>();
                    for (String filed : fields){
                        Predicate predicate = criteriaBuilder
                                .like(root.get(filed), likeCondition);
                        orPredicates.add(predicate);
                    }
                    predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
                }
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    protected List<Predicate> toPredicate(DOMAIN domain,Root<DOMAIN> root, CriteriaBuilder criteriaBuilder,List<Predicate> predicates) {
        try {
            List<Field> fields = ObjectToCollection.setConditionFieldList(domain);
            for(Field field : fields){
                boolean fieldAnnotationPresent = field.isAnnotationPresent(QueryField.class);
                if(fieldAnnotationPresent){
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = field.get(domain);
                    if(value!=null){
                        predicates.add(criteriaBuilder.equal(root.get(fieldName),value));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return predicates;
    }

    @Override
    public void deleteAll(Iterable<DOMAIN> domains){
        repository.deleteAll(domains);
    }

    @Override
    public  void   createTSVFile(HttpServletResponse response){
        List<DOMAIN> domains = listAll();
        String name = getInstance().getClass().getSimpleName();
        File tsvFile = createTSVFile(domains,
                CacheStore.getValue("workDir")+"/export/"+name+".tsv", null);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = FileUtils.readFileToByteArray(tsvFile);
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("content-disposition","attachment;filename="+tsvFile.getName());
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<DOMAIN> saveAll(Iterable<DOMAIN> domain) {
        return repository.saveAll(domain);
    }

    @Override
    public File createTSVFile(List<DOMAIN> domains, String filePath,String[] heads){
        File csvFile = new File(filePath);
        File parent = csvFile.getParentFile();
        if (parent != null && !parent.exists())
        {
            parent.mkdirs();
        }
        TsvWriter writer=null;
        Writer fileWriter =null;
        try {
            TsvWriterSettings settings = new TsvWriterSettings();
            BeanWriterProcessor<DOMAIN> beanWriterProcessor = new BeanWriterProcessor<>((Class<DOMAIN>) getInstance().getClass());
            settings.setRowWriterProcessor(beanWriterProcessor);
            fileWriter = new FileWriter(csvFile);
            settings.setHeaderWritingEnabled(true);
            if(heads!=null){
                settings.setHeaders(heads);
            }
            writer = new TsvWriter(fileWriter,settings);

            writer.writeHeaders();
            writer.processRecords(domains);
            return csvFile;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BioinfoException(e.getMessage());
        } finally {
            if(writer!=null){
                writer.close();
            }
            try {
                if(fileWriter!=null){
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<DOMAIN> tsvToBean(String filePath){
        return File2Tsv.tsvToBean((Class<DOMAIN>) getInstance().getClass(),filePath);
//        try {
//            try(FileInputStream inputStream = new FileInputStream(filePath)){
//                DOMAIN instance = getInstance();
//                BeanListProcessor<DOMAIN> beanListProcessor = new BeanListProcessor<>((Class<DOMAIN>) getInstance().getClass());
//                TsvParserSettings settings = new TsvParserSettings();
//                settings.setProcessor(beanListProcessor);
//                settings.setHeaderExtractionEnabled(true);
//                TsvParser parser = new TsvParser(settings);
//                parser.parse(inputStream);
//                List<DOMAIN> beans = beanListProcessor.getBeans();
//                inputStream.close();
//                return beans;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }


    @Override
    @Transactional
    public List<DOMAIN> initData(String filePath,Boolean isEmpty){
//        Cache cache = concurrentMapCacheManager.getCache("TERM");
//        cache.clear();
        if(isEmpty){
            truncateTable();
        }
        List<DOMAIN> beans = tsvToBean(filePath);
        if(beans==null){
            throw new BioinfoException(filePath+" 不存在！");
        }
        if(beans.size()!=0){
            beans = repository.saveAll(beans);
        }
        return beans;
    }

    @Override
    public DOMAIN delBy(ID id) {
        DOMAIN domain = findById(id);
        repository.delete(domain);
        return domain;
    }

    @Override
    public List<String> getFields() {
        List<Field> fields = ObjectToCollection.getFields(getInstanceClass());
        return ServiceUtil.fetchPropertyList(fields, Field::getName);
    }
}
