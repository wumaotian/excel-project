package com.excel.excelproject.exception;

public class OperationException extends RuntimeException {
    private static final long serialVersionUID = 8458544317507845657L;
    private String errorCode;
    private String code;
    private String friendlyMessage = "";
    private Object[] messageArgs;
    private String defaultFriendlyMessage;

    public OperationException() {
    }

    public OperationException(Throwable cause) {
        super(cause);
    }

    public OperationException(String logMsg) {
        super(logMsg);
    }

    public OperationException(String code, Throwable cause) {
        super(cause);
        this.revertCode(code, cause);
        this.code = code;
    }

    public OperationException(String code, Throwable cause, Object[] messageArgs) {
        super(cause);
        this.revertCode(code, cause);
        this.code = code;
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

    }

    public OperationException(String code, String logMsg) {
        super(logMsg);
        this.revertCode(code, (Throwable)null);
        this.code = code;
    }

    public OperationException(String code, String logMsg, Object[] messageArgs) {
        super(logMsg);
        this.revertCode(code, (Throwable)null);
        this.code = code;
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

    }

    public OperationException(String code, String logMsg, Throwable cause) {
        super(logMsg, cause);
        this.revertCode(code, cause);
        this.code = code;
    }

    public OperationException(String code, String logMsg, Throwable cause, Object[] messageArgs) {
        super(logMsg, cause);
        this.revertCode(code, cause);
        this.code = code;
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

    }

    public OperationException(String code, Throwable cause, String defaultFriendlyMessage) {
        super(cause);
        this.revertCode(code, cause);
        this.code = code;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    public OperationException(String code, Throwable cause, Object[] messageArgs, String defaultFriendlyMessage) {
        super(cause);
        this.revertCode(code, cause);
        this.code = code;
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    public OperationException(String code, String logMsg, String defaultFriendlyMessage) {
        super(logMsg);
        this.revertCode(code, (Throwable)null);
        this.code = code;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    public OperationException(String code, String logMsg, Object[] messageArgs, String defaultFriendlyMessage) {
        super(logMsg);
        this.revertCode(code, (Throwable)null);
        this.code = code;
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    public OperationException(String code, String logMsg, Throwable cause, String defaultFriendlyMessage) {
        super(logMsg, cause);
        this.revertCode(code, (Throwable)null);
        this.code = code;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    public OperationException(String code, String logMsg, Throwable cause, Object[] messageArgs, String defaultFriendlyMessage) {
        super(logMsg, cause);
        this.revertCode(code, cause);
        this.code = code;
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    public String getFriendlyMessage() {
        return this.friendlyMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setFriendlyMessage(String friendlyMessage) {
        this.friendlyMessage = friendlyMessage;
    }

    public Object[] getMessageArgs() {
        return this.messageArgs;
    }

    public void setMessageArgs(Object[] messageArgs) {
        if (null != messageArgs) {
            this.messageArgs = (Object[])messageArgs.clone();
        }

    }

    public String getDefaultFriendlyMessage() {
        return this.defaultFriendlyMessage;
    }

    public void setDefaultFriendlyMessage(String defaultFriendlyMessage) {
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    protected void revertCode(String code, Throwable cause) {
        if (null != code) {
            this.errorCode = code;
            if (!code.trim().toLowerCase().equals("error.dal.001") && !code.trim().toLowerCase().equals("error.dal.008") && null != cause) {
            }
        }

    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
