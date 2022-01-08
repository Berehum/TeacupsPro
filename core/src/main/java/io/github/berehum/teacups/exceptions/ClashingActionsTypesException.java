package io.github.berehum.teacups.exceptions;

import io.github.berehum.teacups.show.actions.type.ShowActionType;

public class ClashingActionsTypesException extends Exception {
    public ClashingActionsTypesException(ShowActionType<?> type1, ShowActionType<?> type2) {
        this(String.format("ShowActionTypes: %s and %s are clashing. Please change the name or aliases.", type1.getName(), type2.getName()));
    }

    public ClashingActionsTypesException(String errorMessage) {
        super(errorMessage);
    }
}
