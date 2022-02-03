package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.service.base.ICrudService;
import com.wangyang.bioinfo.service.task.ITaskService;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Component
public class CrudHandlers {
    @Autowired
    private ITaskService taskService;
    private final Collection<ICrudService> fileTermMappingHandlers= new LinkedList<>();
    public CrudHandlers(ApplicationContext applicationContext) {
        // Add all file handler
        addTermMappingHandlers(applicationContext.getBeansOfType(ICrudService.class).values());
    }
    public CrudHandlers addTermMappingHandlers(@Nullable Collection<ICrudService> termMappingHandlers) {
        if (!CollectionUtils.isEmpty(termMappingHandlers)) {
            this.fileTermMappingHandlers.addAll(termMappingHandlers);
        }
        return this;
    }


    public Page<? extends TermMapping> pageBy(Pageable pageable, CrudType termMappingEnum) {
//        Assert.notNull(file, "Multipart file must not be null");
//        As
//        sert.notNull(fileLocation, "Attachment type must not be null");

        for (ICrudService crudService : fileTermMappingHandlers) {
            if (crudService.supportType(termMappingEnum)) {
                return crudService.pageBy(pageable);
            }
        }
        throw new BioinfoException("不能找到Crud！！");
    }

    public List<CancerStudy> initData(String path, Boolean isEmpty, CrudType termMappingEnum) {
        for (ICrudService crudService : fileTermMappingHandlers) {
            if (crudService.supportType(termMappingEnum)) {
                return crudService.initData(path,isEmpty);
            }
        }
        throw new BioinfoException("文件长传出错!");
    }

    public Task addTask(CrudType crudEnum, Integer id, Integer codeId, User user) {
        ICrudService crudService = getCrudService(crudEnum);
        return taskService.addTask(crudEnum,crudService,id,codeId,user);
    }
    public Task runTask(CrudType crudEnum, Integer id, User user) {
        ICrudService crudService = getCrudService(crudEnum);
        return taskService.runTask(crudService,id,user);
    }

    public BaseEntity findById(CrudType crudEnum, Integer id) {
        ICrudService crudService = getCrudService(crudEnum);
        return crudService.findById(id);
    }

    public List<String> getFields(CrudType crudEnum) {
        ICrudService crudService = getCrudService(crudEnum);
        return crudService.getFields();
    }

    public ICrudService getCrudService(CrudType crudEnum){
        for (ICrudService crudService : fileTermMappingHandlers) {
            if (crudService.supportType(crudEnum)) {
                return crudService;
            }
        }
        throw new BioinfoException("不能找到Crud！！");
    }
}
