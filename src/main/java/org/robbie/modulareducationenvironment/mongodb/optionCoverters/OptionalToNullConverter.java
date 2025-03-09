package org.robbie.modulareducationenvironment.mongodb.optionCoverters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import java.util.Optional;

@WritingConverter
public class OptionalToNullConverter implements Converter<Optional<?>, Object> {

    @Override
    public Object convert(Optional<?> source) {
        return source.orElse(null); // Convert Optional to null if empty
    }
}