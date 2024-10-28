package org.springframe.backend.utils;

import java.util.function.Supplier;

public class ControllerUtils {
    public static <T> ResponseResult<T> messageHandler(Supplier<T> supplier){
        return ResponseResult.Success(supplier.get());
    }
}
