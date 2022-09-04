
package boardgame;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class Reversi extends TurnBasedGame{
    int passes = 0;
    public Reversi()
    {
        super(8, 8, "BLACK", "WHITE");
        this.setTitle("Othello");
    }
    
    public static final String BLANK = " ";
    String winner;
    


    /*** TO-DO: STUDENT'S WORK HERE ***/
    protected int[] columnVacancyLeft;
    
    @Override
    protected void initGame()
    {
        for (int y = 0; y < yCount; y++)
            for (int x = 0; x < xCount; x++)
                pieces[x][y].setText(" ");
        
        pieces[3][4].setEnabled(false);
        pieces[3][4].setText("BLACK");
        pieces[3][4].setBackground(Color.GRAY);
        pieces[3][4].setOpaque(true);
        
        pieces[3][3].setEnabled(false);
        pieces[3][3].setText("WHITE");
        pieces[3][3].setBackground(Color.WHITE);
        pieces[3][3].setOpaque(true);
        
        pieces[4][3].setEnabled(false);
        pieces[4][3].setText("BLACK");
        pieces[4][3].setBackground(Color.GRAY);
        pieces[4][3].setOpaque(true);
       
        pieces[4][4].setEnabled(false);
        pieces[4][4].setText("WHITE");
        pieces[4][4].setBackground(Color.WHITE);
        pieces[4][4].setOpaque(true);
        
    }    
    
    @Override
    protected void gameAction(JButton triggeredButton, int x, int y){
        if (mustPass()){
                passes += 1;
                changeTurn();}
        else{
            if (IsValidMove(x,y)){
                pieces[x][y].setEnabled(false);
                if ("WHITE".equals(this.currentPlayer)){
                    pieces[x][y].setBackground(Color.WHITE);
                    pieces[x][y].setText("WHITE");
                }
                else{
                    pieces[x][y].setBackground(Color.GRAY);
                    pieces[x][y].setText("BLACK");
                }
                addLineToOutput(currentPlayer + " move at (" + x + ", " + y + ")");
                pieces[x][y].setOpaque(true);
                change(x,y);
                changeTurn();
                
                while(mustPass()){
                     passes += 1;
                     changeTurn();
                     if (passes == 2){
                         break;
                     }
                }
                gameEnded = checkEndGame(x,y);
                if(gameEnded){
                    countPieces();
                }
                
                
            }
            else{
                addLineToOutput("Invalid move!");
            }
        }
        
      
    }
    protected void countPieces(){
        int white_num = 0;
        int black_num = 0;
        for (int x = 0; x < xCount; x++)
            for (int y = 0; y < yCount; y++){
                if ("WHITE".equals(pieces[x][y].getText())){
                    white_num += 1;
                }
                else if("BLACK".equals(pieces[x][y].getText())){
                    black_num += 1;
                }
            }
        addLineToOutput("BLACK score:" + black_num);
        addLineToOutput("WHITE score:" + white_num);
        if (black_num > white_num){
            this.winner = this.player1;
            addLineToOutput("Winner is " + this.winner +"!");
        }
        else if(black_num < white_num){
            this.winner = this.player2;
            addLineToOutput("Winner is " + this.winner +"!");
        }
        else{
            this.winner = "Draw game!";
            addLineToOutput(this.winner);
        }
        JOptionPane.showMessageDialog(null, "Game ended!");
    }
            
    protected boolean mustPass(){
        boolean pass = true;
        for (int x = 0; x < xCount; x++)
            for (int y = 0; y < yCount; y++)
            {
                if (!isFriend(x, y)) continue;
                
                for (int deltaX=-1; deltaX <= 1; deltaX++){
                    for (int deltaY=-1; deltaY <= 1; deltaY++){
                        if (deltaX == 0 && deltaY == 0) continue;
                        int count = 1;
                        
                        try {
                            while (isOpponent(x+count*deltaX, y+count*deltaY)){
                                pass = !isBlank(x+(count+1)*deltaX, y+(count+1)*deltaY);
                                if (!pass){
                                    return pass;
                                }
                                else{
                                    pass = true;
                                }
                                count++;   
                            }
                            
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            }
                    } 
                }
            }
        return pass;
    }

    
    protected void change(int pos1, int pos2){
        for (int x = 0; x < xCount; x++)
            for (int y = 0; y < yCount; y++)
            {
                // skip opponent and blank pieces
                if (!isFriend(x, y)) continue;
                if (!(x==pos1 && y==pos2)){continue;}

                for (int deltaX=-1; deltaX <= 1; deltaX++){
                    for (int deltaY=-1; deltaY <= 1; deltaY++){
                        if (deltaX == 0 && deltaY == 0){continue;}
                        int count = 1;
                        try {
                            while (isOpponent(x+(count*deltaX), y+(count*deltaY))){
                                count++;
                            }
                            if (isFriend(x+((count)*deltaX), y+((count)*deltaY))){
                                count += 1;
                            }
                            else{
                                count = 1;
                            }
                            if (count>1){
                                do {
                                    if(x+count*deltaX>8 || y+count*deltaY>8){
                                        continue;
                                    }
                                    count--;
                                    if ("WHITE".equals(this.currentPlayer)){
                                        pieces[x+count*deltaX][y+count*deltaY].setBackground(Color.WHITE);
                                        pieces[x+count*deltaX][y+count*deltaY].setText("WHITE");
                                    }
                                    else{
                                        pieces[x+count*deltaX][y+count*deltaY].setBackground(Color.GRAY);
                                        pieces[x+count*deltaX][y+count*deltaY].setText("BLACK");
                                    }
                                    pieces[x+count*deltaX][y+count*deltaY].setOpaque(true);
                                } while (count >= 1);
                        }
                            
                            
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            }                      
                    }
                }
            }  
    }
    
    
    protected boolean isFriend(int x, int y){
        return pieces[x][y].getText().equals(currentPlayer);
    }
    protected boolean isOpponent(int x, int y)
    {
        if(this.currentPlayer.equals(this.player1)){
            return pieces[x][y].getText().equals("WHITE");
        }
        else {
            return pieces[x][y].getText().equals("BLACK");
        }
    }
     protected boolean isBlank(int x, int y)
    {
        return pieces[x][y].getText().equals(" ");
    }
    
    
    @Override
    protected boolean checkEndGame(int moveX, int moveY){
        boolean no_choice = false;
        
        if (passes == 2){
            no_choice = true;
            return no_choice;
        }
        return no_choice;
    }
    
    protected boolean IsValidMove(int pos1, int pos2){

        boolean Valid_move = false;
        for (int x = 0; x < xCount; x++)
            for (int y = 0; y < yCount; y++)
            {
                // skip opponent and blank pieces
                if (isBlank(x, y)) continue;
                if (isOpponent(x, y)) continue;

                for (int deltaX=-1; deltaX <= 1; deltaX++){
                    for (int deltaY=-1; deltaY <= 1; deltaY++){
                        if (deltaX == 0 && deltaY == 0) continue;
                        
                        int count = 1;
                        try {
                            
                            while (isOpponent(x+count*deltaX, y+count*deltaY)){
                                if(!isOpponent((x+(count+1)*deltaX), (y+(count+1)*deltaY))){
                                   Valid_move = pos1==(x+(count+1)*deltaX) && pos2==(y+(count+1)*deltaY);
                                   if(Valid_move){
                                       return Valid_move;
                                   }
                                }
                                count++;   
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            Valid_move = false;
                            }
                    }
                }
            }
        return Valid_move;
    }
    
    
    public static void main(String[] args)
    {
        Reversi reversi;
        reversi = new Reversi();
        reversi.setLocation(400, 20);
        reversi.verbose = false;

        System.out.println("You are running class Reversi");
        

    }
}
