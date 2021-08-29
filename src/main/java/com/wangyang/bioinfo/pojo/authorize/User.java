package com.wangyang.bioinfo.pojo.authorize;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/5/3
 */

@Entity
@DiscriminatorValue(value = "0")
@Data
public class User extends BaseAuthorize{
//    private String username;
//    private String avatar;
    private String password;
    private String email;
    private Integer gender;

    public User(int id){
        this.id = id;
    }
    public User(){

    }

    //    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//    @JoinTable(name = "t_user_role",joinColumns = @JoinColumn(name = "userId"),
//    inverseJoinColumns = @JoinColumn(name = "roleId"))
//    @JsonManagedReference
//    private Set<Role> roles =new HashSet<>();
//
//
//    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//    @JoinTable(name = "t_user_project",joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "projectId"))
//    @JsonManagedReference
//    private Set<Project> projects =new HashSet<>();


}
