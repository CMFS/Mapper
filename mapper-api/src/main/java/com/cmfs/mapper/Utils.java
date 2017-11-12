package com.cmfs.mapper;

/**
 * @author cmfs
 */

final class Utils {

    private static final String PACKAGE_MAPPERS = "com.cmfs.mapper.mappers";
    private static final String DOT = ".";
    private static final String DELIMITER = "$$";
    private static final String REFLECTOR_SUFFIX = "Mapper";

    static String getMapperName(Class<?> source, Class<?> target) {
        return concat(PACKAGE_MAPPERS, DOT, source.getSimpleName(), DELIMITER, target.getSimpleName(), DELIMITER, REFLECTOR_SUFFIX);
    }

    static String getMapperName(String source, String target) {
        return concat(PACKAGE_MAPPERS, DOT, source, DELIMITER, target, DELIMITER, REFLECTOR_SUFFIX);
    }

    static String concat(String... strings) {
        StringBuilder builder = new StringBuilder(strings.length * 5);
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }

}
