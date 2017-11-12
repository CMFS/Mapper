package com.cmfs.mapper;

/**
 * @author cmfs
 */

public interface Mapper<SRC, DEST> {

    DEST map(SRC src);

}
