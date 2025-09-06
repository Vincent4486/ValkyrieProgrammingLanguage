package net.vincent.Valkyrie.Exceptions;

public class NoInputFileException extends Exception {

    public NoInputFileException(){
        System.err.println(getMessage());
    }

    public String getMessage(){
        return "NoInputFileException: Input a file";
    }

}
