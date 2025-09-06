package net.vincent.Valkyrie.Exceptions;

public class FileExtensionIncorrectException extends Exception {

    public FileExtensionIncorrectException(){

    }

    public String getMessage(){
        return "FileExtensionIncorrectException: The file extension must be '.val'";
    }

}
