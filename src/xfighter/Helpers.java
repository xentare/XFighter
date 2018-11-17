package xfighter;

import org.rspeer.runetek.api.ClientSupplier;

public class Helpers {
    
    private static boolean inGame;
    private static final int IN_GAME = 30;
    
    public static boolean isInGame() {
        return (ClientSupplier.get() != null && ClientSupplier.get().getGameState() == IN_GAME);
    }
    
}
