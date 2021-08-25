package com.wangyang.bioinfo.pojo.authorize;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "t_base_authorize")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseAuthorize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Parsed
    private int id;
    private String username;
    private String avatar;
}
