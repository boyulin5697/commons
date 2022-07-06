package com.by.commons.mongodb;

import com.by.commons.data.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Mongo Operation
 *
 * This is the abstract mongo operations which packed several simple mongoTemplate
 * methods.
 *
 * Please override these methods in your own way if necessary.
 *
 * <p>About the route to the destination collection, if need to define by yourself,</p>
 * please initial  COLLECTION in the code block after inheritance this class.
 *
 * @author by.
 * @date 2022/5/12
 * @since 1.0.3
 */
@Slf4j
public abstract class StandardMongoOperations<T>{

    protected Class<T> entity = currentClass();

    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * The default collection name would be the name of the class, default.
     * If you wish to change, use code block to update is previously.
     *
     */
    protected String COLLECTION;

     {
        COLLECTION = entity.getSimpleName().toLowerCase();
     }


    public StandardMongoOperations(){
    }

    protected Class<T> currentClass(){
        return (Class<T>) getEntityClass(this.getClass());
    }

    protected Class<?> getEntityClass(Class<?> clazz){
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return Object.class;
        }else{
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            return (Class)params[0];
        }
    }

    /**
     * save, includes update old one and create new one.
     * @param entity
     */
    public void save(T entity){
        mongoTemplate.save(entity,COLLECTION);
    }

    /**
     * remove entity -> delete
     * @param entity
     */
    public void remove(T entity){
        mongoTemplate.remove(entity,COLLECTION);
    }

    /**
     * remove by id
     */
    public void removeById(String id){
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)),currentClass(),COLLECTION);
    }

    /**
     * The default id type is String.
     * @param id
     * @return
     */
    public T findById(String id){
       return mongoTemplate.findById(id,currentClass(),COLLECTION);
    }

    /**
     * Query method
     *
     * Since it's impossible to define the query item type,
     * we wish the developers to offer queries by themselves.
     */
    public List<T> findByQuery(Query query){
        return mongoTemplate.find(query,currentClass(),COLLECTION);
    }

    /**
     * Find one By query
     * @param query
     * @return
     */
    public T findOne(Query query){
        return mongoTemplate.findOne(query, currentClass(), COLLECTION);
    }

    /**
     * count query result
     * @param query query
     * @return count value
     */
    public long count(Query query){
        return mongoTemplate.count(query,currentClass());
    }

    /**
     * Search function construction(rough and pageable)
     * <p>This function supports mongodb specific search and page function.</p>
     * <p>Only support data which not defined as fake deletion.</p>
     * @param isRough Whether this search is rough or exact
     * @param searchString the searched string
     * @param searchItem the searched item
     * @param pageNum the page Num
     * @param pageSize the page size
     */
    public PageModel<List<T>> roughAndPageableSearch(boolean isRough, String searchString, String searchItem, long pageNum, long pageSize){
        Query query = new Query();
        long skip = (pageNum-1)*pageSize;
        if (isRough) {
            Pattern pattern = Pattern.compile("^.*"+searchString+".*");
            query.addCriteria(new Criteria().and(searchItem).regex(pattern));
        }else {
            Criteria criteria = Criteria.where(searchItem).is(searchString);
            query.addCriteria(criteria);
        }
        query.skip(skip);
        query.limit((int)pageSize);
        int totalCount = (int)count(query);
        long totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        return new PageModel<>(pageNum,totalPage,findByQuery(query));
    }
}
