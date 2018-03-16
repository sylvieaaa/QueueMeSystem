package util.exception;

public class MenuItemNotFoundException extends Exception{
    public MenuItemNotFoundException(){
        
    }
    
    public MenuItemNotFoundException(String message){
        super(message);
    }
}
