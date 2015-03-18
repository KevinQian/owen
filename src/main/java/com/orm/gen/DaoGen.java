package com.orm.gen;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.orm.constants.ORMConstants;
import com.orm.dao.DaoUtils;
import com.orm.entity.Project;
import com.orm.io.DirUtils;
import com.orm.utils.GenerateUtils;
import com.raddle.jdbc.meta.table.ColumnInfo;
import com.raddle.jdbc.meta.table.TableInfo;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * 根据数据库不同的表，生成dao层，包含mapper.xml,dao接口,daoImpl实现类
 * 
 * @author kevin.qian
 *
 */
public class DaoGen {   

    public static void main(String[] args) throws Exception {
        final String baseDirName = "D:";
        final String basePackageName = "com.bailian";
        final String projectName = "wallet";
        final String daoPackageName = projectName + ORMConstants.DOT + "dao";
        final String modelPackageName = projectName + ORMConstants.DOT + "datamodel";
        final String dtoPackageName = projectName + ORMConstants.DOT + "dto";

        String[] tableNames = {"ZGM_TEST"};
        String tablePrefix = "";
        String schema = "OK_WALLET";
        
        List<TableInfo> tableInfos = DaoUtils.getTableInfo(tableNames, schema);       
        
        // dao组件名
        String projectBaseDirName = baseDirName + ORMConstants.SLASH + projectName;
        System.out.println(projectBaseDirName);
        
        // Dao接口存放的位置
        String daoDirName = projectBaseDirName + ORMConstants.JAVA + ORMConstants.SLASH + DirUtils.getPackageName(basePackageName, daoPackageName);
        System.out.println(daoDirName);
        DirUtils.mkdir(daoDirName);
        
        // Dao接口实现类存放的位置
        String daoImplDirName = daoDirName + ORMConstants.SLASH + "impl";
        DirUtils.mkdir(daoImplDirName);
        
        // model类存放的位置
        String modelDirName = projectBaseDirName + ORMConstants.JAVA + ORMConstants.SLASH + DirUtils.getPackageName(basePackageName, modelPackageName);
        DirUtils.mkdir(modelDirName);
        
        // dto类存放的位置
        String dtoDirName = projectBaseDirName + ORMConstants.JAVA + ORMConstants.SLASH + DirUtils.getPackageName(basePackageName, dtoPackageName);
        DirUtils.mkdir(dtoDirName);
        
        // mapper.xml文件存放的位置
        String mapperDirName = projectBaseDirName + ORMConstants.RESOURCES + ORMConstants.SLASH + ORMConstants.MYBATIS_BASE;
        DirUtils.mkdir(mapperDirName);
        
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding(ORMConstants.DEFAULT_CHARSET);
        configuration.setTemplateLoader(new ClassTemplateLoader(CommonDaoGen.class, "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        
        Project project = new Project();
        project.setProjectName(projectName);
        project.setBasePackageName(basePackageName);
        
        for(TableInfo tableInfo : tableInfos) {
            String tableName = tableInfo.getTableName();
            Map<Object, Object> context = GenerateUtils.getContext(project, tableInfo, "");
            // model类文件名
            String modelClassName = modelDirName + ORMConstants.SLASH + GenerateUtils.getModelClassSimpleName(tableName, tablePrefix) + ".java";
            GenerateUtils.generateFile(modelClassName, "template/datamodel/model.ftl", configuration, context);
            
            // dao接口文件名
            String daoClassName = daoDirName + ORMConstants.SLASH + GenerateUtils.getModelClassSimpleName(tableName, tablePrefix) + "Dao.java";
            GenerateUtils.generateFile(daoClassName, "template/dao/dao.ftl", configuration, context);
            
            // dao实现类文件名
            String daoImplClassName = daoImplDirName + ORMConstants.SLASH + GenerateUtils.getModelClassSimpleName(tableName, tablePrefix) + "DaoImpl.java";
            GenerateUtils.generateFile(daoImplClassName, "template/dao/dao-impl.ftl", configuration, context);
            
            // dto类文件名
            String dtoClassName = dtoDirName + ORMConstants.SLASH + GenerateUtils.getModelClassSimpleName(tableName, tablePrefix) + "Dto.java";
            GenerateUtils.generateFile(dtoClassName, "template/dto/vo.ftl", configuration, context);
            
            // mapper.xml文件
            String mapperName = mapperDirName + ORMConstants.SLASH + "mapper-" + tableName.toLowerCase() + ".xml";
            GenerateUtils.generateFile(mapperName, "template/mybatis/mapper-oracle.ftl", configuration, context);
            
        }

    }

}
