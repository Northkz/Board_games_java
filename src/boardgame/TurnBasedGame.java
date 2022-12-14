package boardgame;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class TurnBasedGame extends BoardGame {

    protected String player1;
    protected String player2;
    protected String currentPlayer;
    protected int turn;
    
    /**
     * TurnBasedGame default constructor of a 4x4 game.
     */
    public TurnBasedGame()
    {
        this(4, 4, "Player 1", "Player 2");
    }
    
    /**
     * TurnBasedGame constructor.
     * @param xCount is number of columns (width)
     * @param yCount is number of rows (height)
     * @param player1 is name of player 1
     * @param player2 is name of player 2
     */
    public TurnBasedGame(int xCount, int yCount, String player1, String player2)
    {
        // superclass BoardGame constructor invokes initGame(), via initializeBoardGame()
        // therefore, fields player1 and player2 are UNINITIALIZED during initGame()
        super(xCount, yCount);
        this.player1 = player1;
        this.player2 = player2;
        this.setTitle("TurnBasedGame " + xCount + "x" + yCount + ": " + player1 + " vs " + player2);
        turn = 0;
        changeTurn();
    }


    protected final String getOpponent()
    {
        if (currentPlayer.equals(player1))
            return player2;
        else
            return player1;
    }


    protected final int changeTurn()
    {
        turn++;
        if (turn % 2 == 1)
            currentPlayer = player1;
        else
            currentPlayer = player2;
        addLineToOutput("Turn " + turn + " Current Player: " + currentPlayer);
        return turn;
    }

    @Override
    protected void initGame()
    {
        for (int y = 0; y < yCount; y++)
            for (int x = 0; x < xCount; x++)
                pieces[x][y].setText("?");
    }
    
    @Override
    protected void gameAction(JButton triggeredButton, int x, int y)
    {
        triggeredButton.setEnabled(false);
        triggeredButton.setText(currentPlayer);
        addLineToOutput(currentPlayer + " move at (" + x + ", " + y + ")");

        checkEndGame(x, y);
        
        if (gameEnded)
        {
            addLineToOutput("Game ended!");
            JOptionPane.showMessageDialog(null, "Game ended!");
        }
        else
            changeTurn();
    }

    /**
     * checkEndGame considers the latest move at (moveX, moveY)
     * and determines if gameEnded is true or false.
     * @param moveX is the x-coordinate of the latest move
     * @param moveY is the y-coordinate of the latest move
     * @return end game status: true or false
     */
    protected boolean checkEndGame(int moveX, int moveY)
    {
        gameEnded = true;
        for (int y = 0; y < yCount; y++)
            for (int x = 0; x < xCount; x++)
                if (pieces[x][y].getText().equals("?"))
                {
                    gameEnded = false;
                    return gameEnded;
                }
        addLineToOutput("All pieces filled!");
        return gameEnded;
    }
            
    
    /**
     * TurnBasedGame main() DEMO
     * @param args 
     */
    public static void main(String[] args)
    {
        TurnBasedGame tbg;
        tbg = new TurnBasedGame();
        tbg.verbose = true;

        tbg = new TurnBasedGame(3, 3, "Player A", "Player B");
        tbg.setLocation(400, 200);
        tbg.verbose = false;
        // the game has started and GUI thread will take over here
    }
}
