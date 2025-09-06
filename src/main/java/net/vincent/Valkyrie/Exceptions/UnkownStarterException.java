package net.vincent.Valkyrie.Exceptions;

public class UnkownStarterException extends Exception{

    int line;
    String op;

    public UnkownStarterException(String op, int line){

        this.line = line;
        this.op = op;

    }
    public String getMessage(){
        return "UnkownStarterException: Unknown operator '" + op + "' at line: "+ line;
    }

}
