package com.orm.gen;


import java.io.IOException;
import java.util.Map;

import com.orm.constants.ORMConstants;
import com.orm.entity.Project;
import com.orm.io.DirUtils;
import com.orm.utils.GenerateUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 生成Base的Dao组件
 * 
 * @author kevin.qian
 *
 */
public class CommonDaoGen {    

    public static void main(String[] args) throws IOException, TemplateException {
        final String baseDirName = "D:";
        final String basePackageName = "com.bailian";
        final String packageName = "common.dao";
        final String projectName = "common-dao";
        
        // common-dao的项目位置
        String projectBaseDirName = baseDirName + ORMConstants.SLASH + projectName;
        System.out.println(projectBaseDirName);
        
        // common-dao base dao类的存放位置
        String daoDirName = projectBaseDirName + ORMConstants.JAVA + ORMConstants.SLASH + DirUtils.getPackageName(basePackageName, packageName);
        System.out.println(daoDirName);
        DirUtils.mkdir(daoDirName);
        
        // common-dao pom.xml的存放位置
        String daoPomDirName = projectBaseDirName;
        
        
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding(ORMConstants.DEFAULT_CHARSET);
        configuration.setTemplateLoader(new ClassTemplateLoader(CommonDaoGen.class, "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);   
        
        Project project = new Project();
        project.setProjectName(projectName);
        project.setBasePackageName(basePackageName);
        Map<Object, Object> context = GenerateUtils.getContext(project);
        
        // pom.xml文件
        String daoPomFileName = daoPomDirName + ORMConstants.SLASH + "pom.xml";
        // 生成common-dao的pom.xml文件
        GenerateUtils.generateFile(daoPomFileName, "template/common-dao/pom.xml.ftl", configuration, context);
        
        // OrderBy类
        String orderByClassName = daoDirName + ORMConstants.SLASH + "OrderBy.java";
        // 生成 OrderBy类
        GenerateUtils.generateFile(orderByClassName, "template/common-dao/OrderBy.ftl", configuration, context);
        
        // IDao接口文件
        String iDaoClassName = daoDirName + ORMConstants.SLASH + "IDao.java";
        GenerateUtils.generateFile(iDaoClassName, "template/common-dao/IDao.ftl", configuration, context);
        
        // BaseDao类文件
        String baseDaoClassName = daoDirName + ORMConstants.SLASH + "BaseDao.java";
        GenerateUtils.generateFile(baseDaoClassName, "template/common-dao/BaseDao.ftl", configuration, context);
        
        // DaoUtils类文件
        String daoUtilsClassName = daoDirName + ORMConstants.SLASH + "DaoUtils.java";
        GenerateUtils.generateFile(daoUtilsClassName, "template/common-dao/DaoUtils.ftl", configuration, context);
    }

}
