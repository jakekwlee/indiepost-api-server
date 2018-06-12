package com.indiepost.model.analytics;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by jake on 8/9/17.
 */
@Entity
@DiscriminatorValue("Action")
public class Action extends Stat implements Serializable {

    private static final long serialVersionUID = -527267775197998523L;

    @NotNull
    @Size(max = 50)
    private String actionType;

    @Size(max = 30)
    private String label;

    private Integer value;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
