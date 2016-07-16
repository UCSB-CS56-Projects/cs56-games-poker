package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;


/**
   Class that represents a hand of 5 Cards.
*/
public class Hand extends ArrayList<Card> implements Serializable{

    private int handValue;
    
    /**
       No arg constructor for Hand
    */
    public Hand(){
	super(1);
    }
    
    /**
       Constructor that sets a hand given 7 Cards as arguments.
       @param a Card a
       @param b Card b
       @param c Card c
       @param d Card d
       @param e Card e
       @param f Card f
       @param g Card g
    */	
    public Hand(Card a, Card b, Card c, Card d, Card e, Card f, Card g){
	super(7);
	this.add(a); this.add(b); this.add(c); this.add(d); this.add(e);
	this.add(f); this.add(g);
    }
    
    /**
       Constructor that sets a hand given 5 Cards as arguments.
       @param a Card a
       @param b Card b
       @param c Card c
       @param d Card d
       @param e Card e
    */	
    public Hand(Card a, Card b, Card c, Card d, Card e){
	super(5);
	this.add(a); this.add(b); this.add(c); this.add(d); this.add(e);
    }
    
    /**
       Constructor that sets a 5 Card hand based on the deck passed in.
       @param deck deck of Cards
    */
    public Hand(Deck deck){
	super(7);
	for(Card c:deck.dealCards())
	    this.add(c);
	handValue=100;
    }
    /**
       Sets the Hand value.
    */	
    public void setHandValue(int handValue){
	this.handValue=handValue;
    }
    /**
       Gets the Hand value.
    */
    public int getHandValue(){
	return handValue;
    }
    
    
    /**
       Discards the hand into the discardPile of the deck.
       @param deck deck of Cards
    */	
    public void discardHand(Deck deck){
	deck.addToDiscardPile(this);
    }
    
    /**
       Adds Cards from the deck to the Hand.
       @param deck deck of Cards
    */	
    public void draw(Deck deck){
	for(Card c:deck.dealCards())
	    this.add(c);
    }
    
    /**
       Returns the Card with the highest value.
    */
    public Card getHighCard(){
	Card max=this.get(0);
	for(Card c:this){
	    if(c.getValue()>max.getValue())
		max=c;
	}
	return max; 
    }
    
    /**
       Returns the int value of the highest Card.
    */
    public int getHighCardValue(){
	int max=0;
	for(Card c:this)
	    {
		if(c.getValue()>max)
		    max=c.getValue();
	    }
	return max;
    }
    
    /**
       Returns an ArrayList of Card int values in a numerical order.
    */
    public ArrayList<Integer> sortHand(){
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	for(int i=0;i<this.size();i++)
	    {
		sortedHand.add(this.get(i).getValue());
	    }
	Collections.sort(sortedHand);
	return sortedHand;
    }
    
    /**
       Returns boolean for if the hand is a straight flush
    */	
    public boolean isStraightFlush(){
	int straightFlushCounter=0;
	int spadeCounter=0;
	int cloverCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	ArrayList<Integer> spades=new ArrayList<Integer>();
	ArrayList<Integer> clovers=new ArrayList<Integer>();
	ArrayList<Integer> diamonds=new ArrayList<Integer>();
	ArrayList<Integer> hearts=new ArrayList<Integer>();
	for(Card c: this){
	    if(c.getSuit()=="S"){
		spadeCounter++;
		spades.add(c.getValue());
	    }
	    else if(c.getSuit()=="C"){
		cloverCounter++;
		clovers.add(c.getValue());
	    }
	    else if(c.getSuit()=="D"){
		diamondCounter++;
		diamonds.add(c.getValue());
	    }
	    else if(c.getSuit()=="H"){
		heartCounter++;
		hearts.add(c.getValue());
	    }
	}
	int i=0;
	if(spadeCounter>=5){
	    Collections.sort(spades);
	    if(spades.get(i)==(spades.get(i+1)-1) && 
	       spades.get(i)==(spades.get(i+2)-2) &&
	       spades.get(i)==(spades.get(i+3)-3)	&&
	       spades.get(i)==(spades.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(cloverCounter>=5){
	    Collections.sort(clovers);
	    if(clovers.get(i)==(clovers.get(i+1)-1) && 
	       clovers.get(i)==(clovers.get(i+2)-2) &&
	       clovers.get(i)==(clovers.get(i+3)-3)	&&
	       clovers.get(i)==(clovers.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(heartCounter>=5){
	    Collections.sort(hearts);
	    if(spades.get(i)==(hearts.get(i+1)-1) && 
	       hearts.get(i)==(hearts.get(i+2)-2) &&
	       hearts.get(i)==(hearts.get(i+3)-3)	&&
	       hearts.get(i)==(hearts.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(diamondCounter>=5){
	    Collections.sort(diamonds);
	    if(diamonds.get(i)==(diamonds.get(i+1)-1) && 
	       diamonds.get(i)==(diamonds.get(i+2)-2) &&
	       diamonds.get(i)==(diamonds.get(i+3)-3)	&&
	       diamonds.get(i)==(diamonds.get(i+4)-4))
		straightFlushCounter=4;
	}
	else
	    return false;
	    
	if(straightFlushCounter==4)
	    return true;
	else
	    return false;
    }
    
    /**
       Returns boolean for if the hand has a four of a kind.
    */		
    public boolean isFourOfAKind(){
	ArrayList<Integer> sortedHand=this.sortHand();
	int quadCounter=0;
	for(int i=0;i<this.size()-3;i++)
	    {
		if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i+1)
		   ==sortedHand.get(i+2) && sortedHand.get(i+2)==sortedHand.get(i+3))
		    {
			quadCounter=3;
		    }
	    }
	int i=0;
	if(quadCounter==3)
	    return true;
	else
	    return false;
    }

    /**
       Returns boolean for if the hand is a full house.
    */			
    public boolean isFullHouse(){
	ArrayList<Integer> sortedHand=this.sortHand();
	int doubleCounter=0;
	int tripleCounter=0;
	for(int i=0;i<this.size()-1;i++)
	    {
		if(sortedHand.get(i)==sortedHand.get(i+1))
		    {
			if(tripleCounter==1)
			    {
				sortedHand.remove(i+1);
				sortedHand.remove(i);
				tripleCounter++;
				break;
			    }
			else
			    {
				if(i==1)
				    tripleCounter=0;
				else
				    tripleCounter++;
			    }
				
		    }
		else
		    tripleCounter=0;
			
			
	    }
	if(tripleCounter==2)
	    {
		sortedHand.trimToSize();
		int size=sortedHand.size();
		for(int i=0;i<(size-1);i++)
		    {
			if(sortedHand.get(i)==sortedHand.get(i+1))
			    {
				doubleCounter++;
			    }
		    }
	    }
	else
	    return false;
	if(doubleCounter>=1)
	    return true;
	else
	    return false;
    }
    
    /**
       Returns boolean for if the hand is a flush.
    */		
    public boolean isFlush(){
	
	int spadeCounter=0;
	int cloverCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	for(Card c: this){
	    if(c.getSuit()=="S")
		spadeCounter++;
	    else if(c.getSuit()=="C")
		cloverCounter++;
	    else if(c.getSuit()=="D")
		diamondCounter++;
	    else
		heartCounter++;
	}
	if(spadeCounter>=5 || cloverCounter>=5 || diamondCounter>=5 
	   || heartCounter>=5)
	    return true;
	else
	    return false;
    }
    
    
    /**
       Returns boolean for if the hand is a straight.
    */		
    public boolean isStraight(){
	ArrayList<Integer> sortedHand=this.sortHand();
	int straightCounter=0;
	for(int i=0;i<this.size()-4;i++)
	    {
		if(sortedHand.get(i)==(sortedHand.get(i+1)-1) && 
		   sortedHand.get(i)==(sortedHand.get(i+2)-2) &&
		   sortedHand.get(i)==(sortedHand.get(i+3)-3)	&&
		   sortedHand.get(i)==(sortedHand.get(i+4)-4)) 
		    {
			straightCounter=4;
		    }
	    }
	return(straightCounter==4);
    }
    
    /**
       Returns boolean for if the hand has three of a kind.
    */			
    public boolean isThreeOfAKind(){
	if(this.isFullHouse()){
	    return false;
	}
	
	ArrayList<Integer> sortedHand=this.sortHand();
	int tripleCounter=0;
	for(int i=0;i<this.size()-1;i++)
	    {
		if(sortedHand.get(i)==sortedHand.get(i+1))
		    tripleCounter++;
		else
		    {
			if(tripleCounter==1)
			    tripleCounter=0;
		    }
	    }
	if(tripleCounter==2)
	    return true;
	else
	    return false;
		
    }
    
    /**
       Returns boolean for if the hand has two pairs.
    */		
    public boolean isTwoPair(){
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	sortedHand=this.sortHand();
	int pair1Counter=0;
	int pair2Counter=0;
	int pair1Int=100;
	int pair2Int=100;
	for(int i=0;i<this.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1)){
		if(sortedHand.get(i)==pair1Int || sortedHand.get(i)==pair2Int){
		    return false;
		}
		    else if(pair1Counter==1){
			pair2Counter++;
			pair2Int=sortedHand.get(i);
		    }
		    else{
			pair1Counter++;
			pair1Int=sortedHand.get(i);
		    }
		    
	    }
	}
	return(pair1Counter==1 && pair2Counter>=1);
    }
    
    /**
       Returns boolean for if the hand has only one pair.
    */			
    public boolean isOnePair(){
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	sortedHand=this.sortHand();
	int pairCounter=0;
	for(int i=0;i<this.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1))
		pairCounter++;
	}
	if(pairCounter==1)
	    return true;
	else
	    return false;
    }
    
    /**
     * Returns 1 if "this" hand is better than "otherHand"
     * Returns 0 if the opposite.
     * Returns 2 if exact tie
     * @param otherHand hand to be compared
    */
    public int compareHands(Hand otherHand){
	int player1Value=calculateValue(this);
	int player2Value=calculateValue(otherHand);
	
	handValue=player1Value;
	otherHand.setHandValue(player2Value);
	
	if(player1Value>player2Value)
	    return 1;
	else if(player1Value<player2Value)
	    return 0;
	else {
	    int yourHandValue = sameHandUpdated(this, 1);
	    int otherHandValue = sameHandUpdated(otherHand, 1);
	    if(yourHandValue>otherHandValue)
		return 1;
	    else if (yourHandValue<otherHandValue)
		return 0;
	    else {
		Card yourmaxCard = this.get(0);
		Card othermaxCard = otherHand.get(0);
		if (yourmaxCard.getValue()<this.get(1).getValue())
		    yourmaxCard = this.get(1);
		if (othermaxCard.getValue()<otherHand.get(1).getValue())
		    othermaxCard = otherHand.get(1);

		if (yourmaxCard.getValue()>othermaxCard.getValue())
		    return 1;
		else if (yourmaxCard.getValue()<othermaxCard.getValue())
		    return 0;
		
		else {
		    yourHandValue = sameHandUpdated(this, 2);
		    otherHandValue = sameHandUpdated(otherHand, 2);
		    if(yourHandValue>otherHandValue)
			return 1;
		    else if (yourHandValue<otherHandValue)
			return 0;
		    else {
			Card yourminCard = this.get(0);
			Card otherminCard = otherHand.get(0);
			if (yourminCard.getValue()>this.get(1).getValue())
			    yourminCard = this.get(1);
			if (otherminCard.getValue()>otherHand.get(1).getValue())
			    otherminCard = otherHand.get(1);
			
			if (yourminCard.getValue()>otherminCard.getValue())
			    return 1;
			else if (yourminCard.getValue()<otherminCard.getValue())
			    return 0;
			else
			    return 2;// Only arrives here if cards are exactly the same		
		    }
		}
	    }		
	}
    }
	
    /**
     * This method is used if both hands are the same type
     * Note that the first two Cards in Hand are the player's cards
     * while the last five Cards in Hand are table's cards
     * @param Hand: hand is either this or the otherhand
     * @param recursion: Either 1 if called 1st, 2 if called 2nd
     * func won't work as intended if given a different number
     */
    private int sameHandUpdated(Hand hand, int recursion) {
	Hand hand_tmp = new Hand();
	Card yourCard = hand.get(0);
	if (recursion==1) {
	    if (hand.get(1).getValue()>yourCard.getValue())
	     	yourCard = hand.get(1);
	    hand_tmp.add(yourCard);
	    for (int i=2; i<hand.size(); i++)
		hand_tmp.add(hand.get(i));
	    return calculateValue(hand_tmp);
	}
	if (recursion==2) {
	    if (hand.get(1).getValue()<yourCard.getValue())
		yourCard = hand.get(1);
	    hand_tmp.add(yourCard);
	    for(int i=2; i<hand.size(); i++)
		hand_tmp.add(hand.get(i));
	    return calculateValue(hand_tmp);
	}
	return -1; // Should only pass in 1 or 2 in parameter
    }
    
    /**
       Method used to determine which hand has the higher high card. If high
       cards are the same, continuously checks for next high card until a 
       higher card is found.
       @param otherHand hand to be compared
    */
    public int sameHandMethod(Hand otherHand){	
	if(this.isEmpty())
	    return 2;
	else if(this.getHighCardValue()>otherHand.getHighCardValue())
	    return 1;
	else if(this.getHighCardValue()<otherHand.getHighCardValue())
	    return 0;
	else{
	    this.remove(this.getHighCard()); 
	    otherHand.remove(otherHand.getHighCard());
	    return sameHandMethod(otherHand);
	}
    }

    public int calculateValue(Hand hand) {
	if(hand.isStraightFlush())
	    return 8;
	else if(hand.isFourOfAKind())
	    return 7;
	else if(hand.isFullHouse())
	    return 6;
	else if(hand.isFlush())
	    return 5;
	else if(hand.isStraight())
	    return 4;
	else if(hand.isThreeOfAKind())
	    return 3;
	else if(hand.isTwoPair())
	    return 2;
	else if(hand.isOnePair())	
	    return 1;
	else
	    return 0;
    }   
}
