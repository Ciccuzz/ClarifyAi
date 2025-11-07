package com.example.ClarifyAi.mapper;

import com.example.ClarifyAi.exception.UnknownActionException;
import com.example.ClarifyAi.model.ActionEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActionMapper {

    default ActionEnum toEnum(String value) {
        if (value == null || value.isBlank()) {
            throw new UnknownActionException("Action cannot be null or empty.");
        }
        try {
            String normalized = value.trim().toUpperCase().replace("-", "_");
            return ActionEnum.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new UnknownActionException("Invalid action: " + value);
        }
    }

    default String toString(ActionEnum action) {
        return action.name().toLowerCase().replace("_", "-");
    }
}