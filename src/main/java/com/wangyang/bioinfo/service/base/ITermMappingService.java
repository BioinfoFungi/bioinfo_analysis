package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.TermMapping;
import com.wangyang.bioinfo.pojo.param.TermMappingParam;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface ITermMappingService<TERMMAPPING extends TermMapping> extends IBaseFileService<TERMMAPPING>{
//    <MAPPING extends TERMMAPPING>Page<TERMMAPPING> pageBy(MAPPING termMapping, Pageable pageable);

//    List<TERMMAPPING> listBy(TERMMAPPING termMapping,String keyWard);

//    Page<TERMMAPPING> pageDTOBy(TermMappingDTO termMappingDTO, Pageable pageable);

    //    @Override
    TermMappingVo  convertVo(TERMMAPPING termmapping);

    <PARAM extends TermMappingParam> TERMMAPPING convert(PARAM param);

    <VO extends TermMappingVo> VO convertVo(TERMMAPPING termmapping, Class<VO> clz);

//    TERMMAPPING delBy(int id);
}
