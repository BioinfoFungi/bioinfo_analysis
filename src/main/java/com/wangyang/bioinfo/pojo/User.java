package com.wangyang.bioinfo.pojo;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/5/3
 */

@Entity(name = "t_user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String avatar;
    private String email;
    private Integer gender;


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
