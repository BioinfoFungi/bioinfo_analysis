package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.pojo.enums.FileLocation;
import com.wangyang.bioinfo.pojo.entity.OrganizeFile;
import com.wangyang.bioinfo.pojo.param.OrganizeFileParam;
import com.wangyang.bioinfo.pojo.param.OrganizeFileQuery;
import com.wangyang.bioinfo.pojo.support.UploadResult;
import com.wangyang.bioinfo.repository.OrganizeFileRepository;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.service.base.BaseFileService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Service
public class OrganizeFileServiceImpl
        extends BaseFileService<OrganizeFile>
        implements IOrganizeFileService {

    private final OrganizeFileRepository organizeFileRepository;
    private final FileHandlers fileHandlers;
    public OrganizeFileServiceImpl(FileHandlers fileHandlers,OrganizeFileRepository organizeFileRepository) {
        super(fileHandlers, organizeFileRepository);
        this.organizeFileRepository=organizeFileRepository;
        this.fileHandlers = fileHandlers;
    }


    @Override
    public OrganizeFile findByEnName(String enName){
        List<OrganizeFile> files = organizeFileRepository.findAll(new Specification<OrganizeFile>() {
            @Override
            public Predicate toPredicate(Root<OrganizeFile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),enName)).getRestriction();
            }
        });
        if(files.size()==0){
            return null;
        }
        return files.get(0);
    }

    public OrganizeFile checkOrganizeFile(OrganizeFileParam organizeFileParam){
        OrganizeFile organizeFile = findByEnName(organizeFileParam.getEnName());
        if (organizeFile==null){
            organizeFile = new OrganizeFile();
        }
        BeanUtils.copyProperties(organizeFileParam,organizeFile);
        return organizeFile;
    }
    @Override
    public OrganizeFile save(OrganizeFileParam organizeFileParam) {
        OrganizeFile organizeFile = checkOrganizeFile(organizeFileParam);
        return saveAndCheckFile(organizeFile);
    }

    @Override
    public OrganizeFile upload(MultipartFile file, OrganizeFileParam organizeFileParam) {
        OrganizeFile organizeFile = checkOrganizeFile(organizeFileParam);
        UploadResult uploadResult = fileHandlers.uploadFixed(file,"organizeFile" , FileLocation.LOCAL);
        return super.upload(uploadResult,organizeFile);
    }

    @Override
    public Page<OrganizeFile> pageBy(OrganizeFileQuery organizeFileQuery, Pageable pageable) {
        OrganizeFile organizeFile = new OrganizeFile();
        Set<String> sets = new HashSet<>();
        sets.add("enName");
        Specification<OrganizeFile> specification = buildSpecByQuery(organizeFile, organizeFileQuery.getKeywords(), sets);
        return organizeFileRepository.findAll(specification,pageable);
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
