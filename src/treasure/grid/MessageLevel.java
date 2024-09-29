package treasure.grid;

/**
 *
 * @author Eloy
 */
public enum MessageLevel {
    WIN(100),
    LOSS(200),
    PAINT(50),
    EVALUATE(25),
    CONNECTION(1),
    DISCONNECTION(2),
    RESET(10),
    ERROR(3),
    UNKNOWN(0);
    
    private final int number;
    
    private MessageLevel(int number){
        this.number = number;
    }
    
    public int getNumber(){
        return number;
    }
}
