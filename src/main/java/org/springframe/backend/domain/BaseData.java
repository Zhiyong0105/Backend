package org.springframe.backend.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

public interface BaseData {

    default <V> V asViewObject(Class<V> clazz, Consumer<V> consumer) {
        V v = this.asViewObject(clazz);
        consumer.accept(v);
        return v;
    }

    default <V> V asViewObject(Class<V> clazz) {
        try{
            Field[] fields = clazz.getDeclaredFields();
            Constructor<V> constructor = clazz.getConstructor();
            V v = constructor.newInstance();
            Arrays.asList(fields).forEach(field -> convert(field,v));
            return v;
        }catch (ReflectiveOperationException reflectiveOperationException){
            Logger logger = LoggerFactory.getLogger(BaseData.class);
            logger.error("Error");
            throw new RuntimeException(reflectiveOperationException);
        }
    }

    private void convert(Field field,Object value){
        try{
            Field source = this.getClass().getDeclaredField(field.getName());
            source.setAccessible(true);
            field.setAccessible(true);
            field.set(value,source.get(this));
        }catch (IllegalAccessException | NoSuchFieldException e){

        }
    }
}
