package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.OrganizeFile;
import com.wangyang.bioinfo.repository.OrganizeFileRepository;
import com.wangyang.bioinfo.service.IOrganizeService;
import com.wangyang.bioinfo.service.base.BaseFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @date 2021/7/8
 */
@Service
public class OrganizeFileServiceImpl
        extends BaseFileServiceImpl<OrganizeFile>
        implements IOrganizeService {

    @Autowired
    OrganizeFileRepository organizeFileRepository;
}
