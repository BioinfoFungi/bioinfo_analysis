package com.wangyang.bioinfo.pojo.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wangyang.bioinfo.pojo.enums.ProjectStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/6/12
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectParam {
    @NotBlank(message = "项目名称不能为空!")
    private String name;
    private String description;
    /**
     * 将Json反序列化成日期时会按照yyyy-MM-dd HH:mm指定的日期格式进行解析，
     * 若日期字符串的格式不满足以上指定的格式将会直接报错
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date deadline;
    private String jupyterUrl;
    private Set<Integer> userIds;
    private  ProjectStatus projectStatus;
}
