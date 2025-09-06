package net.vincent.Valkyrie.Exceptions;

public class IllegalFormatException extends Exception{

    String msg;

    public IllegalFormatException(String msg){

        this.msg = msg;

    }

    public String getMessage(){
        return "IllegalFormatException: " + msg;
    }

}
