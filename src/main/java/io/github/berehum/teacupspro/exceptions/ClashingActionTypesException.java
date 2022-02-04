package io.github.berehum.teacupspro.exceptions;

import io.github.berehum.teacupspro.show.reader.lines.type.ShowActionType;

public class ClashingActionTypesException extends Exception {
    public ClashingActionTypesException(ShowActionType type1, ShowActionType type2) {
        this(String.format("ShowActionTypes: %s and %s are clashing. Please change the name or aliases.", type1.getName(), type2.getName()));
    }

    public ClashingActionTypesException(String errorMessage) {
        super(errorMessage);
    }
}
