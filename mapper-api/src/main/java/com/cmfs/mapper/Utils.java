package com.cmfs.mapper;

/**
 * @author cmfs
 */

final class Utils {

    private static final String DELIMITER = "$$";
    private static final String REFLECTOR_SUFFIX = "Mapper";

    static String getMapperName(Class<?> source, Class<?> target) {
        return concat(source.getSimpleName(), DELIMITER, target.getSimpleName(), DELIMITER, REFLECTOR_SUFFIX);
    }
    static String getMapperName(String source, String target) {
        return concat(source, DELIMITER, target, DELIMITER, REFLECTOR_SUFFIX);
    }

    static String concat(String... strings) {
        StringBuilder builder = new StringBuilder(strings.length * 5);
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

}
