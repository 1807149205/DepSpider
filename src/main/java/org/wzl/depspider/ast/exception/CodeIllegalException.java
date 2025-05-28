package org.wzl.depspider.ast.exception;

/**
 * 代码文件非法异常类
 * 通常是代码没有遵循规范导致
 * @author weizhilong
 */
public class CodeIllegalException extends RuntimeException {

    public CodeIllegalException(String message) {
        super(message);
    }

}
