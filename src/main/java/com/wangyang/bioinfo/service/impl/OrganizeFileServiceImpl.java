package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.repository.OrganizeFileRepository;
import com.wangyang.bioinfo.service.IOrganizeFileService;
import com.wangyang.bioinfo.service.base.AbstractBaseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Service
public class OrganizeFileServiceImpl
        extends AbstractBaseFileService<OrganizeFile>
        implements IOrganizeFileService {

    @Autowired
    OrganizeFileRepository organizeFileRepository;




}
