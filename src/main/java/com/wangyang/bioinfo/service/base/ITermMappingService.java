package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import com.wangyang.bioinfo.pojo.param.TermMappingParam;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface ITermMappingService<TERMMAPPING extends TermMapping>
        extends ICrudService<TERMMAPPING,Integer>{
//    <MAPPING extends TERMMAPPING>Page<TERMMAPPING> pageBy(MAPPING termMapping, Pageable pageable);

//    List<TERMMAPPING> listBy(TERMMAPPING termMapping,String keyWard);

//    Page<TERMMAPPING> pageDTOBy(TermMappingDTO termMappingDTO, Pageable pageable);

    //    @Override
    TermMappingVo  convertVo(TERMMAPPING termmapping);

    <PARAM extends TermMappingParam> TERMMAPPING convert(PARAM param);

    <VO extends TermMappingVo> VO convertVo(TERMMAPPING termmapping, Class<VO> clz);

//    TERMMAPPING delBy(int id);
}
