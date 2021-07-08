package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.AttachmentType;
import com.wangyang.bioinfo.pojo.param.BaseFileQuery;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.repository.base.BaseFileRepository;
import com.wangyang.bioinfo.util.BioinfoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public class BaseFileServiceImpl<FILE extends BaseFile>
        extends AbstractCrudService<FILE,Integer>
        implements IBaseFileService<FILE> {

    @Autowired
    BaseFileRepository<FILE> baseFileRepository;
    @Autowired
    protected  FileHandlers fileHandlers;



    public UploadResult upload(MultipartFile file,String path,String filename,String fileType){
        UploadResult uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL,path,filename,fileType);
        return uploadResult;
    }

    public UploadResult upload(MultipartFile file){
        UploadResult uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL);
        return uploadResult;
    }


    @Override
    public FILE findByEnNameAndCheck(String name){
        FILE file = findByEnName(name);
        if (file==null){
            throw new BioinfoException("要查找的文件对象不存在!");
        }
        return file;
    }

    @Override
    public FILE findByEnName(String name){
        List<FILE> files = baseFileRepository.findAll(new Specification<FILE>() {
            @Override
            public Predicate toPredicate(Root<FILE> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        if(files.size()==0){
            return null;
        }
        return files.get(0);
    }
    @Override
    public Page<FILE> pageBy(BaseFileQuery baseFileQuery, Pageable pageable) {
        Page<FILE> page = baseFileRepository.findAll(buildSpecByQuery(baseFileQuery),pageable);
        return page;
    }

    private Specification<FILE> buildSpecByQuery(BaseFileQuery baseFileQuery) {
        return (Specification<FILE>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(baseFileQuery.getEnName()!=null){
                predicates.add(criteriaBuilder.equal(root.get("fileName"),baseFileQuery.getFileName()));
            }
            if(baseFileQuery.getKeyword()!=null){
                String likeCondition = String
                        .format("%%%s%%", StringUtils.strip(baseFileQuery.getKeyword()));
                Predicate name = criteriaBuilder.like(root.get("enName"), likeCondition);
                Predicate fileName = criteriaBuilder
                        .like(root.get("fileName"), likeCondition);

                predicates.add(criteriaBuilder.or(name, fileName));

            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public FILE add(FILE inputFile) {
        FILE file = findByEnName(inputFile.getEnName());
        if (file==null){
            file = super.getInstance();
        }
        BeanUtils.copyProperties(inputFile,file,"id");
        return super.add(file);
    }
}
