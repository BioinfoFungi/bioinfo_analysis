package com.wangyang.bioinfo.pojo.authorize;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//import javax.persistence.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Entity(name = "t_resource")
@Getter
@Setter
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String name;
    private String url;
    private String method;

//    @ManyToMany(cascade = {CascadeType.MERGE})
//    @JoinTable(name = "t_role_resource",joinColumns = @JoinColumn(name = "resourceId"),
//    inverseJoinColumns = @JoinColumn(name = "roleId"))
//    private Set<Role> roles = new HashSet<>();
}
