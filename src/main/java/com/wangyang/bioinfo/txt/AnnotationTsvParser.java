package com.wangyang.bioinfo.txt;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.wangyang.bioinfo.pojo.support.Annotation;

/**
 * @author wangyang
 * @date 2021/7/10
 */
public class AnnotationTsvParser extends BeanListProcessor<Annotation> {
    private final String stringToMatch;
    public AnnotationTsvParser(String stringToMatch){
        super(Annotation.class);
        this.stringToMatch  =stringToMatch;
    }

    @Override
    public void beanProcessed(Annotation bean, ParsingContext context) {
        if(bean.getGeneType().equals(stringToMatch)){
            super.beanProcessed(bean, context);
        }
    }

}
