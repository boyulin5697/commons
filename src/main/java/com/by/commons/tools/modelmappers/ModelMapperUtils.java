package com.by.commons.tools.modelmappers;

import org.aspectj.lang.annotation.AfterThrowing;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.NameTokenizer;
import org.modelmapper.spi.NameableType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A group of ModelMapper Utils based on ModelMapper,
 * use in transform between two classes which has similar definition.
 *
 * @author by.
 * @since 2022/4/29
 */
public abstract class ModelMapperUtils {
    public static final ModelMapper mapper = new ModelMapper();
    public static final NameTokenizer nameTokenizer = new OriginalNameTokenizer();
    //Firstly, initialize the mapper.
    static{
        initMapper(mapper);
    }
    private static void initMapper(ModelMapper modelMapper){
        mapper.getConfiguration().setSourceNameTokenizer(nameTokenizer);
        mapper.getConfiguration().setDestinationNameTokenizer(nameTokenizer);
        List<ConditionalConverter<?,?>> converters = mapper.getConfiguration().getConverters();
        converters.set(1,CollectionConverter.INSTANCE);
    }

    public static ModelMapper newMapper(){
        ModelMapper modelMapper = new ModelMapper();
        initMapper(modelMapper);
        return modelMapper;
    }

    public static class OriginalNameTokenizer implements NameTokenizer{
        public static final OriginalNameTokenizer INSTANCE = new OriginalNameTokenizer();

        @Override
        public String[] tokenize(String name, NameableType nameableType) {
            return new String[]{name};
        }
    }

    /**
     * Inner map handle
     * @param mapper
     * @param collection
     * @param targetClass
     * @param <T>
     * @return
     */
    private static <T> List<T> mapList(ModelMapper mapper, Collection<?>collection, Class<T>targetClass){
        Assert.notNull(mapper,"Mapper cannot be null!");
        if(null==collection){
            return null;
        }
        else if(CollectionUtils.isEmpty(collection)){
            return new ArrayList<>();
        }
        //Map old class with the new class, and collect them to the new one, using stream api.
        return collection.stream().map(obj -> mapper.map(obj,targetClass)).collect(Collectors.toList());
    }

    private static <T> Set<T> mapSet(ModelMapper modelMapper,Collection<?>collection, Class<T>targetClass){
        Assert.notNull(mapper,"Mapper cannot be null!");
        if(null==collection){
            return null;
        }
        if(CollectionUtils.isEmpty(collection)){
            return new HashSet<>();
        }
        return collection.stream().map(obj -> mapper.map(obj,targetClass)).collect(Collectors.toSet());
    }

    /**
     * Exposed map
     * @param collection
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> mapList(Collection<?>collection, Class<T>targetClass){
        return mapList(mapper,collection,targetClass);
    }
    public static <T> Set<T> mapSet(Collection<?>collection, Class<T>targetClass){
        return mapSet(mapper,collection,targetClass);
    }

    /**
     * map between two existing objects, no return value provided.
     */
    public static void map(Object source, Object target){
        if(source!=null){
            mapper.map(source,target);
        }
    }

    /**
     * map for new object
     */
    public static <T> T map(Object source, Class<T>targetClass){
        if(source==null)
            return null;

        return mapper.map(source,targetClass);
    }

}
