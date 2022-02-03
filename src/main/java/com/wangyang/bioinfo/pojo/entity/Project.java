package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.pojo.enums.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangyang
 * @date 2021/5/3
 */
@Getter
@Setter
@Entity(name = "t_project")
public class Project extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String name;
    @Column(columnDefinition = "longtext not null")
    private String description;
    private String jupyterUrl;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone="GMT+8")
    @Column(name = "t_deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    ProjectStatus projectStatus;

    private Integer userId;
    @Column(columnDefinition = "longtext")
    private String originalContent;
    @Column(columnDefinition = "longtext")
    private String formatContent;

//    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//    @JoinTable(name = "t_user_project",joinColumns = @JoinColumn(name = "projectId"),
//            inverseJoinColumns = @JoinColumn(name = "userId"))
//    @JsonManagedReference
//    private Set<User> projects =new HashSet<>();
}
