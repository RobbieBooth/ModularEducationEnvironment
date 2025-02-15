package org.robbie.modulareducationenvironment.mongodb.optionCoverters;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import java.util.Optional;

@ReadingConverter
public class NullToOptionalConverter implements Converter<Object, Optional<?>> {

    @Override
    public Optional<?> convert(Object source) {
        return Optional.ofNullable(source); // Convert null to Optional.empty()
    }
}
