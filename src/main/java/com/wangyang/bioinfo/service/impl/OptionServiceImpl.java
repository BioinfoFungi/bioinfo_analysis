package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.Option;
import com.wangyang.bioinfo.repository.OptionRepository;
import com.wangyang.bioinfo.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Service
public class OptionServiceImpl implements IOptionService {

    @Autowired
    OptionRepository optionRepository;

    @Override
    public List<Option> listAll() {
        return optionRepository.findAll();
    }
}
