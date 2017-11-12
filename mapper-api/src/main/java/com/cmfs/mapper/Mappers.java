package com.cmfs.mapper;

/**
 * @author cmfs
 */

public class Mappers {

    private static MapperFactory sMapperFactory = new DefaultMapperFactory();

    private static Finder sFinder;

    static {
        try {
            //noinspection unchecked
            Class<? extends Finder> finderClass =
                    (Class<? extends Finder>) Class.forName("com.cmfs.mapper.MapperFinder");
            sFinder = finderClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static MapperFactory getMapperFactory() {
        return sMapperFactory;
    }

    public static void setMapperFactory(MapperFactory mapperFactory) {
        Mappers.sMapperFactory = mapperFactory;
    }

    public static Finder getFinder() {
        return sFinder;
    }

    public static void setFinder(Finder sFinder) {
        Mappers.sFinder = sFinder;
    }

    public static <SRC> Source<SRC> source(SRC src) {
        return new Source<>(src);
    }

    public static <SRC, DEST> DEST map(SRC src, Class<DEST> destClass) {
        return new Source<>(src).to(destClass);
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

    public static class Source<SRC> {

        private SRC src;

        Source(SRC src) {
            this.src = src;
        }

        public <DEST> DEST to(Class<DEST> destClass) {
            //noinspection unchecked
            Class<SRC> srcClass = (Class<SRC>) src.getClass();
            Mapper<SRC, DEST> mapper = sFinder.find(srcClass, destClass);
            return mapper.map(src);
        }
    }

}
