package fr.uca.i3s.sparks.compomaid.tools;

public class Logger {

    private boolean log = true;
    private boolean dbg = true;
    private boolean wrn = true;
    private boolean err = true;

    public Logger () {
        this.log = true;
        this.dbg = true;
        this.wrn = true;
        this.err = true;
    }

    public Logger (boolean dbg) {
        this.dbg = dbg;
        this.log = true;
        this.wrn = true;
        this.err = true;
    }

    private String head(String pre) {
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        return pre + ":" + className + ":" + lineNumber + ": ";
    }

    public void log() {
        if (log) {
            System.out.println();
        }
    }

    public void log(Object obj) {
        if (log) {
            System.out.println((char)27 + "[0;30m" + obj+ (char)27 + "[39m");
        }
    }

    public void dbg(Object obj) {
        if (dbg) {
            System.out.println((char)27 + "[0;93m" + this.head("DBG") + obj + (char)27 + "[39m");
        }
    }

    public void wrn(Object obj) {
        if (wrn) {
            System.out.println((char)27 + "[0;95m" + this.head("WRN") + obj + (char)27 + "[39m");
        }
    }

    public void err(Object obj) {
        if (err) {
            System.out.println((char)27 + "[0;91m" + this.head("ERR") + obj + (char)27 + "[39m");
        }
    }
}
