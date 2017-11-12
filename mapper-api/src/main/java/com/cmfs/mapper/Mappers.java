package com.cmfs.mapper;

import com.sun.istack.internal.NotNull;

/**
 * @author cmfs
 */

public class Mappers {

    private static MapperFactory mapperFactory = new DefaultMapperFactory();

    public static MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    public static void setMapperFactory(@NotNull MapperFactory mapperFactory) {
        Mappers.mapperFactory = mapperFactory;
    }

    private static final class DefaultMapperFactory implements MapperFactory {
        @Override
        public <SRC, DEST> Mapper<SRC, DEST> create(String mapperName) {
            try {
                return (Mapper<SRC, DEST>) Class.forName(mapperName).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
