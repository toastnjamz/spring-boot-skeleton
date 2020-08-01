package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "RuleName", catalog = "demo")
@EntityListeners(AuditingEntityListener.class)
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Length(max = 125)
    private String name;

    @NotBlank(message = "Description is mandatory")
    @Length(max = 125)
    private String description;

    @NotBlank(message = "JSON is mandatory")
    @Length(max = 125)
    private String json;

    @NotBlank(message = "Template is mandatory")
    @Length(max = 125)
    private String template;

    @NotBlank(message = "SQLStr is mandatory")
    @Length(max = 125)
    private String sqlStr;

    @NotBlank(message = "SQLPart is mandatory")
    @Length(max = 125)
    private String sqlPart;

    public RuleName() {
    }

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public String getSqlPart() {
        return sqlPart;
    }

    public void setSqlPart(String sqlPart) {
        this.sqlPart = sqlPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuleName)) return false;
        RuleName ruleName = (RuleName) o;
        return id.equals(ruleName.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
