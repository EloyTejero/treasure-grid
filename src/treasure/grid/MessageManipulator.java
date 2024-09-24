package treasure.grid;

/**
 *
 * @author Eloy
 */
public class MessageManipulator {
    private String message;
    
    public MessageManipulator(String message){
        this.message = message;
    }
    
    public MessageLevel getMessageLevel(){
        String code = getMessageCode();
        return switch(code){
            case "100" -> MessageLevel.WIN;
            case "200" -> MessageLevel.LOSS;
            case "50" -> MessageLevel.PAINT;
            case "25" -> MessageLevel.EVALUATE;
            case "1" -> MessageLevel.CONNECTION;
            case "2" -> MessageLevel.DISCONNECTION;
            case "3" -> MessageLevel.ERROR;
            default -> MessageLevel.UNKNOWN;
        };
    }
    
    public String getMessageInfo(){
        return message.substring(getColonIndex(message)+1).trim();
    }
    
    public String getOutputInProtocol() {
        return getMessageLevel().getNumber() + ":" + getMessageInfo();
    }
    
    public String getOutputInProtocol(MessageLevel level) {
        setMessageLevel(level);
        return getMessageLevel().getNumber()+":"+getMessageInfo();
    }
    
    private String getMessageCode(){
        return message.substring(0, getColonIndex(message)).trim();
    }
    
    private void setMessageLevel(MessageLevel level){
        message = level.toString() + ":" + message;
    }
    
    private int getColonIndex(String message){
        int finalIndex = 0;
        for(char i:message.toCharArray()){
            if(i==':'){
                break; 
            }
            finalIndex++;
        }
        return finalIndex;
    }
}
