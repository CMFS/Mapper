package com.cmfs.mapper.compiler.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cmfs
 */

public class FieldContainer {

    private final Map<String, FieldReflection> elementMap = new HashMap<>();

    private final Map<String, List<FieldReflection>> classElementMap = new HashMap<>();

    public void add(FieldReflection element) {
        elementMap.put(element.getFieldPath(), element);
//        elementMap.put(element.getTargetFieldPath(), element);

        List<FieldReflection> fieldReflectionList = classElementMap.get(element.getClassName());
        if (fieldReflectionList == null) {
            fieldReflectionList = new ArrayList<>();
            classElementMap.put(element.getClassName(), fieldReflectionList);
        }
        fieldReflectionList.add(element);
    }

    public FieldReflection get(String typeName, String fieldName) {
        String key = typeName + "#" + fieldName;
        return elementMap.get(key);
    }

    public List<FieldReflection> get(String typeName) {
        List<FieldReflection> elementList = classElementMap.get(typeName);
        return elementList == null ? Collections.<FieldReflection>emptyList() : elementList;
    }

    public Map<String, FieldReflection> getElementMap() {
        return elementMap;
    }

    public Set<Map.Entry<String, FieldReflection>> entrySet() {
        return elementMap.entrySet();
    }
}
