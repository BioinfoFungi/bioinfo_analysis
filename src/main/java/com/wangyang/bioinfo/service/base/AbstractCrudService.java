package com.wangyang.bioinfo.service.base;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.File2Tsv;
import com.wangyang.bioinfo.util.StringCacheStore;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public abstract class AbstractCrudService<DOMAIN, ID> implements ICrudService<DOMAIN, ID> {
    private final String domainName;
    @Autowired
    BaseRepository<DOMAIN, ID> repository;
//    @Autowired
//    ConcurrentMapCacheManager concurrentMapCacheManager;
    public AbstractCrudService(){
        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(0);
        domainName = domainClass.getSimpleName();
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
    public void deleteAll(){
        repository.deleteAll();
    }

    @Override
    public Page<DOMAIN> pageBy(Pageable pageable){
        return repository.findAll(pageable);
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
                StringCacheStore.getValue("workDir")+"/export/"+name+".tsv", null);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte[] bytes = FileUtils.readFileToByteArray(tsvFile);
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition","attachment;filename="+tsvFile.getName());
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
    public List<DOMAIN> initData(String filePath){
//        Cache cache = concurrentMapCacheManager.getCache("TERM");
//        cache.clear();
        repository.deleteAll();
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
}
