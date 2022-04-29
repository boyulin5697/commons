package com.by.commons.tools.modelmappers;

import org.modelmapper.internal.util.Iterables;
import org.modelmapper.internal.util.MappingContextHelper;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author boyulin
 * @since 2022/4/29
 */
public class CollectionConverter implements ConditionalConverter<Object, Collection<Object>> {
    public static final CollectionConverter INSTANCE = new CollectionConverter();

    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return Iterables.isIterable(sourceType) && Collection.class.isAssignableFrom(destinationType) ? MatchResult.FULL
                : MatchResult.NONE;
    }

    public Collection<Object> convert(MappingContext<Object, Collection<Object>> context) {
        Object source = context.getSource();
        if (source == null) {
            return null;
        } else {
            int sourceLength = Iterables.getLength(source);
            Collection<Object> originalDestination = (Collection<Object>) context.getDestination();
            Collection<Object> destination = MappingContextHelper.createCollection(context);
            Class<?> elementType = Object.class;
            try {
                elementType = MappingContextHelper.resolveDestinationGenericType(context);
            } catch (IllegalArgumentException e) {
            }
            int index = 0;

            Iterator<?> iterator;
            Object element;
            for (iterator = Iterables.iterator(source); iterator.hasNext(); ++index) {
                element = iterator.next();
                Object element1 = null;
                if (originalDestination != null) {
                    element1 = Iterables.getElement(originalDestination, index);
                }

                if (element != null) {
                    MappingContext<?, ?> elementContext = element1 == null ? context.create(element, elementType)
                            : context.create(element, element1);
                    element1 = context.getMappingEngine().map(elementContext);
                }

                destination.add(element1);
            }

            iterator = Iterables.subIterable(originalDestination, sourceLength).iterator();

            while (iterator.hasNext()) {
                element = iterator.next();
                destination.add(element);
            }

            return destination;
        }
    }
}