package com.wangyang.bioinfo.handle;

import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.enums.CodeType;

public interface ICodeResult {
    CodeType getType();
    void call(CodeMsg codeMsg);
}
