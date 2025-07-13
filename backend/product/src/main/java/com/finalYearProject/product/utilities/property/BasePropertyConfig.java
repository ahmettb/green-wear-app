package com.finalYearProject.product.utilities.property;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BasePropertyConfig {

    private Map<String, ErrorMessageDefinition> messages;

}
