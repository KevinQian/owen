package ${project.basePackageName}.${project.projectName}.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${project.basePackageName}.common.dao.BaseDao;
import ${project.basePackageName}.${project.projectName}.dao.${modelClassSimpleName}Dao;
import ${project.basePackageName}.${project.projectName}.datamodel.${modelClassSimpleName};
import ${project.basePackageName}.${project.projectName}.dto.${modelClassSimpleName}Dto;

@Repository("${modelClassSimpleName?uncap_first}Dao")
public class ${modelClassSimpleName}DaoImpl extends BaseDao<${modelClassSimpleName}, ${modelClassSimpleName}Dto, ${pkType}> implements ${modelClassSimpleName}Dao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    protected SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Override
    protected String getNamespace() {
        return "${modelClassSimpleName?uncap_first}Dao";
    }
    
}
