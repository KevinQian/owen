package ${project.basePackageName}.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import ${project.basePackageName}.common.dao.IDao;
import ${project.basePackageName}.common.dao.OrderBy;
import com.google.common.collect.Maps;

public abstract class BaseDao<T, VO, PK extends Serializable> implements IDao<T, VO, PK> {
    protected abstract SqlSessionTemplate getSqlSessionTemplate();

    protected abstract String getNamespace();

    @Override
    public void insert(T entity) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("entity", entity);

        getSqlSessionTemplate().insert(getNamespace() + ".insert", params);
    }

    @Override
    public T get(VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("condition", condition);

        return getSqlSessionTemplate().<T> selectOne(getNamespace() + ".get", params);
    }

    @Override
    public T get(PK id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);

        return getSqlSessionTemplate().<T> selectOne(getNamespace() + ".getById", params);
    }

    @Override
    public List<T> find() {
        return find((VO) null);
    }

    @Override
    public List<T> find(OrderBy orderBy) {
        return find(null, orderBy, -1, -1);
    }

    @Override
    public List<T> find(int pageSize, int pageNumber) {
        return find(null, null, pageSize, pageNumber);
    }

    @Override
    public List<T> find(OrderBy orderBy, int pageSize, int pageNumber) {
        return find(null, orderBy, pageSize, pageNumber);
    }

    @Override
    public List<T> find(VO condition) {
        return find(condition, -1, -1);
    }

    @Override
    public List<T> find(VO condition, OrderBy orderBy) {
        return find(condition, orderBy, -1, -1);
    }

    @Override
    public List<T> find(VO condition, int pageSize, int pageNumber) {
        return find(condition, null, pageSize, pageNumber);
    }

    @Override
    public List<T> find(VO condition, OrderBy orderBy, int pageSize, int pageNumber) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("condition", condition);
        if (orderBy != null) {
            params.put("orderBy", orderBy.toString());
        }
        if (pageSize > 0 && pageNumber > 0) {
            int startRow = pageSize * (pageNumber - 1) + 1;
            int endRow = pageSize * pageNumber;
            int offset = pageSize * (pageNumber - 1);

            params.put("startRow", Integer.valueOf(startRow));
            params.put("endRow", Integer.valueOf(endRow));
            params.put("offset", Integer.valueOf(offset));
            params.put("limit", pageSize);
        }

        return getSqlSessionTemplate().<T> selectList(getNamespace() + ".find", params);
    }

    @Override
    public List<T> findByIdList(List<PK> idList, VO condition, OrderBy orderBy) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("idList", idList);
        params.put("condition", condition);
        if (orderBy != null) {
            params.put("orderBy", orderBy.toString());
        }

        return getSqlSessionTemplate().<T> selectList(getNamespace() + ".findByIdList", params);
    }

    @Override
    public List<T> findByIdList(List<PK> idList, VO condition) {
        return findByIdList(idList, condition, null);
    }

    @Override
    public List<T> findByIdList(List<PK> idList, OrderBy orderBy) {
        return findByIdList(idList, null, orderBy);
    }

    @Override
    public List<T> findByIdList(List<PK> idList) {
        return findByIdList(idList, null, null);
    }

    @Override
    public int count() {
        return count("*");
    }

    @Override
    public int count(String column) {
        return count(null, column);
    }

    @Override
    public int count(VO condition) {
        return count(condition, "*");
    }

    @Override
    public int count(VO condition, String column) {
        DaoUtils.checkColumn(column);

        Map<String, Object> params = Maps.newHashMap();
        params.put("condition", condition);
        params.put("count_column", column);

        return getSqlSessionTemplate().<Integer> selectOne(getNamespace() + ".count", params);
    }

    @Override
    public Map<String, Object> aggregate(String[] functions, String[] columns) {
        return aggregate(null, functions, columns);
    }

    @Override
    public Map<String, Object> aggregate(VO condition, String[] functions, String[] columns) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("condition", condition);
        params.put("aggregate_sql", DaoUtils.buildAggregateSql(functions, columns));

        Map<String, Object> result = getSqlSessionTemplate().<Map<String, Object>> selectOne(getNamespace() + ".aggregate", params);

        Map<String, Object> processedResult = Maps.newHashMapWithExpectedSize(result.size());
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            processedResult.put(entry.getKey().toLowerCase(), entry.getValue());
        }

        return processedResult;
    }

    @Override
    public int update(T entity, VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("entity", entity);
        params.put("condition", condition);

        return getSqlSessionTemplate().update(getNamespace() + ".update", params);
    }

    @Override
    public int update(T entity, PK id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("entity", entity);
        params.put("id", id);

        return getSqlSessionTemplate().update(getNamespace() + ".updateById", params);
    }

    @Override
    public int updateByIdList(T entity, List<PK> idList, VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("entity", entity);
        params.put("idList", idList);
        params.put("condition", condition);

        return getSqlSessionTemplate().update(getNamespace() + ".updateByIdList", params);
    }

    @Override
    public int updateByIdList(T entity, List<PK> idList) {
        return updateByIdList(entity, idList, null);
    }

    @Override
    public int update(Map<String, Object> entity, VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : entity.entrySet()) {
            params.put("entity_" +  entry.getKey(), entry.getValue());
        }
        params.put("condition", condition);

        return getSqlSessionTemplate().update(getNamespace() + ".forceUpdate", params);
    }

    @Override
    public int update(Map<String, Object> entity, PK id) {
        Map<String, Object> params = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : entity.entrySet()) {
            params.put("entity_" +  entry.getKey(), entry.getValue());
        }
        params.put("id", id);

        return getSqlSessionTemplate().update(getNamespace() + ".forceUpdateById", params);
    }

    @Override
    public int updateByIdList(Map<String, Object> entity, List<PK> idList, VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : entity.entrySet()) {
            params.put("entity_" +  entry.getKey(), entry.getValue());
        }
        params.put("idList", idList);
        params.put("condition", condition);

        return getSqlSessionTemplate().update(getNamespace() + ".forceUpdateByIdList", params);
    }

    @Override
    public int updateByIdList(Map<String, Object> entity, List<PK> idList) {
        return updateByIdList(entity, idList, null);
    }

    @Override
    public int remove(VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("condition", condition);

        return getSqlSessionTemplate().delete(getNamespace() + ".remove", params);
    }

    @Override
    public int remove(PK id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);

        return getSqlSessionTemplate().delete(getNamespace() + ".removeById", params);
    }

    @Override
    public int removeByIdList(List<PK> idList, VO condition) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("idList", idList);
        params.put("condition", condition);

        return getSqlSessionTemplate().delete(getNamespace() + ".removeByIdList", params);
    }

    @Override
    public int removeByIdList(List<PK> idList) {
        return removeByIdList(idList, null);
    }
}