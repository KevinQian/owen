package com.orm.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.orm.constants.ORMConstants;
import com.orm.entity.Model;
import com.orm.entity.Project;
import com.raddle.jdbc.meta.table.ColumnInfo;
import com.raddle.jdbc.meta.table.TableInfo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public final class GenerateUtils {
    
    private static String pkType;

    /**
     * 生产项目相关的文件
     * 
     * @param path
     * @param templateName
     * @param configuration
     * @param context
     * @throws IOException
     * @throws TemplateException
     */
    public static void generateFile(String path, String templateName, Configuration configuration,  Map<Object, Object> context) throws IOException, TemplateException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), ORMConstants.DEFAULT_CHARSET));
        Template template = configuration.getTemplate(templateName);
        template.process(context, out);
        out.close();
    }
    
    /**
     * 该方法用于生成FreeMaker模板所需要的数据
     * @return
     */
    public static Map<Object, Object> getContext(Project project) {
        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put(ORMConstants.PROJECT, project);
        return context;
    }
    
    public static Map<Object, Object> getContext(Project project, TableInfo tableInfo, String tablePrefix) {
        String tableName = tableInfo.getTableName();
        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put(ORMConstants.PROJECT, project);
        context.put("tableName", tableName);
        context.put("modelClassSimpleName", getModelClassSimpleName(tableName, tablePrefix));
        
        List<String> propertyNameList = new ArrayList<String>();
        List<String> propertyTypeList = new ArrayList<String>();
        List<String> columnTypeList = new ArrayList<String>();
        List<ColumnInfo> formattedColumnInfos = new ArrayList<ColumnInfo>();
        
        Collection<ColumnInfo> columns = tableInfo.getColumnInfos();
        for(ColumnInfo column : columns) {
            String columnName = column.getColumnName();           
            propertyNameList.add(getPropertyName(columnName));
            propertyTypeList.add(getPropertyType(column));
            columnTypeList.add(getColumnType(column.getColumnTypeName()));
            formattedColumnInfos.add(getFormattedColumnInfo(column));
        }
        context.put("propertyNameList", propertyNameList);
        context.put("propertyTypeList", propertyTypeList);
        context.put("columnTypeList", columnTypeList);
        context.put("columnInfoList", formattedColumnInfos);
        context.put("pkType", pkType);
        return context;
    }
    
    public static ColumnInfo getFormattedColumnInfo(ColumnInfo columnInfo) {
        ColumnInfo formatted = new ColumnInfo();
        formatted.setColumnName(columnInfo.getColumnName().toLowerCase());
        formatted.setColumnType(columnInfo.getColumnType());
        formatted.setColumnTypeName(columnInfo.getColumnTypeName());
        formatted.setComment(columnInfo.getComment());
        formatted.setLength(columnInfo.getLength());
        formatted.setScale(columnInfo.getScale());
        formatted.setPrecision(columnInfo.getPrecision());
        formatted.setNullable(columnInfo.isNullable());
        formatted.setPrimaryKey(columnInfo.isPrimaryKey());
        formatted.setTableName(columnInfo.getTableName());
        return formatted;
    }
    
    public static String getModelClassSimpleName(String tableName, String tablePrefix) {
        return getClassSimpleName(tableName, tablePrefix);
    }
    /**
     * 根据表明生成Model的类名
     * 
     * @param tableName
     * @param tablePrefix
     * @return
     */
    private static String getClassSimpleName(String tableName, String tablePrefix) {
        if(tablePrefix == null) {
            tablePrefix = "";
        }
        String tName = tableName.toLowerCase();
        String prefix = tablePrefix.toLowerCase();
        
        if(!tName.startsWith(prefix)) {
            throw new RuntimeException("表名前缀不正确");
        }
        
        tName = tName.substring(prefix.length());
        String[] ss = tName.split(ORMConstants.UNDERLINE);
        StringBuilder sb = new StringBuilder();
        for(String s : ss) {
            sb.append(StringUtils.capitalize(s));
        }
        return sb.toString();
    }
    
    /**
     * 根据列名生产实体里的property
     * @param columnName
     * @return
     */
    private static String getPropertyName(String columnName) {
        String name = columnName.toLowerCase();
        String[] ss = name.split(ORMConstants.UNDERLINE);
        StringBuilder sb = new StringBuilder();
        for(String s : ss) {
            sb.append(StringUtils.capitalize(s));
        }
        return StringUtils.uncapitalize(sb.toString());
    }
    
    /**
     * 根据列类型名称返回列类型，主要用于mapper.xml的jdbcType中
     * 
     * @param columnTypeName
     * @return
     */
    private static String getColumnType(String columnTypeName) {
        if("char".equalsIgnoreCase(columnTypeName)) {
            return "CHAR";
        } else if("varchar2".equalsIgnoreCase(columnTypeName)) {
            return "VARCHAR";
        } else if("number".equalsIgnoreCase(columnTypeName)) {
            return "NUMBER";
        } else if("date".equalsIgnoreCase(columnTypeName)) {
            return "TIMESTAMP";
        } else if("integer".equalsIgnoreCase(columnTypeName)) {
            return "INTEGER";
        } else if("long".equalsIgnoreCase(columnTypeName)) {
            return "LONG";
        } else {
            return "UnknownType" + "[" + columnTypeName + "]";
        }
    }
    
//    private static Model generateModelInfo(List<String> propertyName, List<String> propertyType, String pkType) {
//        return new Model(propertyName, propertyType, pkType);
//    }
    
    /**
     * 根据columnTypeName生成Property类型
     * @param columnInfo
     * @return
     */
    private static String getPropertyType(ColumnInfo columnInfo) {
        String columnName = columnInfo.getColumnName();
        String columnTypeName = columnInfo.getColumnTypeName();
        if("char".equalsIgnoreCase(columnTypeName)) {
            if("id".equalsIgnoreCase(columnName)) {
                pkType = "java.lang.String";
            }
            return "java.lang.String";
        } else if("varchar2".equalsIgnoreCase(columnTypeName)) {
            if("id".equalsIgnoreCase(columnName)) {
                pkType = "java.lang.String";
            }
            return "java.lang.String";
        } else if("number".equalsIgnoreCase(columnTypeName)) {
            if(columnInfo.getScale() > 0) {
                if("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.math.BigDecimal";
                }
                return "java.math.BigDecimal";
            } else if(columnInfo.getPrecision() > 9) {
                if("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.Long";
                }
                return "java.lang.Long";
            } else {
                if("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.Integer";
                }
                return "java.lang.Integer";
            }
        } else if("date".equalsIgnoreCase(columnTypeName)) {
            if("id".equalsIgnoreCase(columnName)) {
                pkType = "java.util.Date";
            }
            return "java.util.Date";
        } else if("integer".equalsIgnoreCase(columnTypeName)) {
            if("id".equalsIgnoreCase(columnName)) {
                pkType = "java.lang.Integer";
            }
            return "java.lang.Integer";
        } else if("long".equalsIgnoreCase(columnTypeName)) {
            if("id".equalsIgnoreCase(columnName)) {
                pkType = "java.lang.Long";
            }
            return "java.lang.Long";
        } else {
            return "UnknownType" + "[" + columnTypeName + "]";
        }
    }
    
}
