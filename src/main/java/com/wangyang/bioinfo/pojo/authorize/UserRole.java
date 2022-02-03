package com.wangyang.bioinfo.pojo.authorize;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.*;

@Data
@Entity(name = "t_usr_role")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class UserRole extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private Integer userId;
    private Integer roleId;

    public UserRole(){}


    public UserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
