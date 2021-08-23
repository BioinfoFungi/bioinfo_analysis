package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.dto.term.TermMappingDTO;
import com.wangyang.bioinfo.pojo.file.TermMapping;
import com.wangyang.bioinfo.pojo.param.TermMappingParam;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface IBaseDataCategoryService<TERMMAPPING extends TermMapping> extends IBaseFileService<TERMMAPPING>{
//    <MAPPING extends TERMMAPPING>Page<TERMMAPPING> pageBy(MAPPING termMapping, Pageable pageable);

    List<TERMMAPPING> listBy(TERMMAPPING termMapping,String keyWard);

//    Page<TERMMAPPING> pageDTOBy(TermMappingDTO termMappingDTO, Pageable pageable);

    Page<? extends TermMappingVo> convertVo(Page<TERMMAPPING> fromCancerStudies);

    //    @Override
    TermMappingVo  convertVo(TERMMAPPING termmapping);

    <PARAM extends TermMappingParam> TERMMAPPING convert(PARAM param);

    <VO extends TermMappingVo> VO convertVo(TERMMAPPING termmapping, Class<VO> clz);

//    TERMMAPPING delBy(int id);
}
