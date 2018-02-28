package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

final class PokerSinglePlayer extends PokerGameGui {

    /**
     * Timer for calling turnDecider()
     */
    Timer timer;

    /**
     * The milliseconds for timer
     */
    int timer_value = 2000; // milliseconds

    /**
     * Whether or not it's your turn to bet
     */
    boolean yourTurnToBet = true;


    /**
     * No arg constructor to create instance of PokerSinglePlayer to begin game
     */
    public PokerSinglePlayer(){
	for(Player player:players)
	    player.setDelegate(this);
    }
    
    /**
     * Constructor to set the player and opponent's initial chips. 
     * This is used when we continue playing the game
     * @param pChips the player's chips
     * @param oChips the opponent's chips
     */
    public PokerSinglePlayer(int pChips, int oChips){
	for(Player player:players) {
        player.setChips(pChips);
        player.setDelegate(this);
       }
    }
    
    /**
     * Starts game between you and AI
     */
    public void go() {
        super.setUp();
        layoutSubViews(); //sets up all of the buttons and panels and GUI
        controlButtons(); //this function is in PokerGameGui.

        if(!gameOver){ 
            step = Step.BLIND; //This is the first step in the game.
            int rng = (int) (Math.random()*5); //generate random num 0-4 
	    Player first = players.get(rng);
	    first.turn = true;
	    turn = rng;
	    rng++;
            if (turn == 0) { //1 = player 1 goes first.
		message = "player 1 goes first!";
                prompt = "what will you do?";
            }
	    else 
		prompt = "player " + rng + " goes first!";
            //Here, we are using a timer to control how the turns work
            //The timer comes from the swing class if you want to read up on it
            //Another thing to note is we used a lambda function deal with the thread in timer.
            timer = new Timer(timer_value, e -> turnDecider(rng) );
            timer.setRepeats(false); //We want the timer to go off once. We will restart it as needed instead.
            updateFrame(); //function is inside of PokerGameGui
            timer.restart();
	    first.turn = false;
        }
    }

    /**
     * Method that directs the turn to who it needs to go to
     */
    public void turnDecider (int index) {
        Player currentTurn = players.get(index);
	currentTurn.takeTurn();
    }

    
    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AI
     */
    public void changeTurn() {
        if (turn == 0) {
            if (responding == true) {
                turn = 1;
                controlButtons();
                message = "player 2 is thinking...";
                updateFrame();		
                timer.restart();
                } else {
                    updateFrame();
                    nextStep();
                    if (step != Step.SHOWDOWN) {
                        turn = 1;
                        controlButtons();
                        prompt = "player 2's Turn.";
                        message = "player 2 is thinking...";
                        updateFrame();
                        timer.restart();
                    }
                }
        } else {
            if (responding == true) {
                if (turn < 3) {
		    turn++;
   		    controlButtons();
        	    message = "player 2 is thinking...";
               	    updateFrame();
               	    timer.restart();
		}
		else {
			turn = 0;
                	controlButtons();
                	responding = false;
                	prompt = "What will you do?";
			updateFrame();
		}
            } else {
		if (turn < 3) {
		    updateFrame();
                    nextStep();
                    turn += 2;
                    controlButtons();
                    prompt = "player " + turn + "'s Turn.";
                    message = "player " + turn + " is thinking...";
		    turn--;
                    updateFrame();
                    timer.restart();
		}
		else {
		    prompt = "What will you do?";
    		    turn = 0;
    		    controlButtons();
    		    updateFrame();
		}
            }
        }
    }

    /**
     * Updates GUI based on the player's decision
     */
    public void userTurn() {
        controlButtons();
        updateFrame();
    }

    /**
     * Method that moves the game to the next phase
     */
	public void nextStep() {
        if (step == Step.BLIND) { // Most like able to skip/remove this step
            step = Step.FLOP;
        } else if (step == Step.FLOP) {
            step = Step.TURN;
        } else if (step == Step.TURN) {
            step = Step.RIVER;
        } else {
            step = Step.SHOWDOWN;
            message = "All bets are in.";
            prompt = "Determine Winner: ";
            controlButtons();
        }
    }


    /**
     * Method overridden to allow for a new single player game to start.
     */    
    public void showWinnerAlert() {
    	if(!gameOver){
    	    String message = "";
    	    oSubPane2.remove(backCardLabel1);
    	    oSubPane2.remove(backCardLabel2);
    	    for(Player player:players){
    		oSubPane2.add(new JLabel(getCardImage((player.getHand()).get(0))));
		oSubPane2.add(new JLabel(getCardImage((player.getHand()).get(1))));
    	    }
    	    updateFrame();

	    message = winningHandMessage();
	    
	    int winner = 0;

	    for (Player player:players) {
		if (player.winStatus == true) {
		    int index = players.indexOf(player);
		    if (index == 0) {
			System.out.println("player 1");
			message = message + ("\n\nYou win!\n\nNext round?");
		    } else {
			index++;
			System.out.println("opponent");
			message = message + ("\n\nOpponent " + index + " wins.\n\nNext round?");
		    }
		    winner++;
		}
	    }
	    if (winner == 0){
                System.out.println("tie");
                message = message + ("\n\nTie \n\nNext round?");
    	    }
    	    
    	    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
    						       JOptionPane.YES_NO_OPTION);
    	    
	    if (option == JOptionPane.YES_OPTION) {
    		// Restart
		mainFrame.dispose();
		PokerSinglePlayer singlePlayerReplay;
		int Continue = 0;

		// Check if players have enough chips.	
		// Create new game.
		for (Player player:players) {
		    if (player.getChips() < 5){
		    	JOptionPane.showMessageDialog(null, "Resetting chips...");
		    	singlePlayerReplay = new PokerSinglePlayer();
		    	singlePlayerReplay.go();
			Continue++;
		    }
		}
		if (Continue == 0) {
		    singlePlayerReplay = new PokerSinglePlayer(player.getChips(),opponent.getChips());
		    singlePlayerReplay.go();
		}
	    }
	    else if (option == JOptionPane.NO_OPTION) {
		for (Player player:players) {
		    if(player.getChips() < 5) {
		         gameOver("GAME OVER! No chips left!");
		    }
		}
		gameOver("GAME OVER! Thanks for playing.\n\n");
	    } 
	    
	    else {
    		// Quit
    		System.exit(1);
    	    }
    	}
    }

    /**
     * Restarts the timer controlling turnDecider()
     */
    public void restartTimer() {
        timer.restart();
    }
}
