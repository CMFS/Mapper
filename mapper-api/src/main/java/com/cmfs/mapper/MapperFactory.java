package com.cmfs.mapper;

/**
 * @author cmfs
 */

public interface MapperFactory {

    <SRC, DEST> Mapper<SRC, DEST> create(String mapperName);

}
