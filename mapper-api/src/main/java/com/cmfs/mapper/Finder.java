package com.cmfs.mapper;

/**
 * @author cmfs
 */

public interface Finder {

    <SRC, DEST> Mapper<SRC, DEST> find(Class<SRC> srcClass, Class<DEST> destClass);

}
