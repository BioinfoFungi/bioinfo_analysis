package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.file.OrganizeFile;
import com.wangyang.bioinfo.service.base.IAbstractBaseFileService;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wangyang
 * @date 2021/7/8
 */
public interface IOrganizeFileService extends IAbstractBaseFileService<OrganizeFile> {
    OrganizeFile download(String enName, HttpServletResponse response);

}
