package ${project.basePackageName}.${project.projectName}.dao;

import ${project.basePackageName}.common.dao.IDao;
import ${project.basePackageName}.${project.projectName}.datamodel.${modelClassSimpleName};
import ${project.basePackageName}.${project.projectName}.dto.${modelClassSimpleName}Dto;

public interface ${modelClassSimpleName}Dao extends IDao<${modelClassSimpleName}, ${modelClassSimpleName}Dto, ${pkType}> {
}
