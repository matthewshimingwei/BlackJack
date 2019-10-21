import java.util.ArrayList;
import java.util.Scanner;

enum Color
{
    HEARTS, DIAMOND,SPADE,CLUB;
}
class Card
{
    private Color cardColor;
    private int pokerValue;

    public Card()
    {

    }

    public  Card(Color cardColor,int pokerValue)
    {
        this.cardColor = cardColor;
        this.pokerValue = pokerValue;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        else
        {
            if(obj instanceof Card)
            {
                return ((Card)obj).cardColor == this.cardColor &&
                        ((Card)obj).pokerValue == this.pokerValue;
            }
            else
            {
                return false;
            }
        }
    }
    @Override
    public int hashCode()
    {
        //personal hash algortihm
        /*
           We has 54 unit of poker and every time we select 5
           Any number that greater than 54 could be hash code
         */
        int hash = 7;//7 is arbitray value
        hash = 59 *hash+(this.cardColor !=null?this.cardColor.hashCode():0);
        hash = 59*hash +this.pokerValue;
        return hash;
    }
    @Override
    public String toString()
    {
        String symbol;
        String valueString = "";
        if(cardColor ==Color.HEARTS)
        {
            symbol = "Heart";
        }
        else if(cardColor == Color.DIAMOND)
        {
            symbol = "Diamond";
        }
        else if(cardColor == Color.SPADE)
        {
            symbol = "Spade";
        }
        else
        {
            symbol = "Club";
        }
        if(pokerValue == 11)
        {
            valueString+= "J";
        }
        else if(pokerValue ==12)
        {
            valueString += "Q";
        }
        else if (pokerValue ==13)
        {
            valueString +="K";
        }
        else if(pokerValue ==1)
        {
            valueString += "A";
        }
        return symbol +" "+valueString+" ";
    }

    public Color getCardColor()
    {
        return cardColor;
    }

    public void setCardColor(Color cardColor)
    {
        this.cardColor = cardColor;
    }

    public int getPokerValue()
    {
        return pokerValue;
    }

    public void setPokerValue(int pokerValue)
    {
        this.pokerValue = pokerValue;
    }
}
class CardGame
{
    private Card[] cardHeap;
    private  int cardHeapPosition;
    private ArrayList<Card>playerCards;
    private ArrayList<Card>computerCards;
    final int TOTAL_NUMBER_OF_TWO_CARDS = 104;
    final int CARD_TYPES = 4;
    public CardGame()
    {

        cardHeap = new Card[TOTAL_NUMBER_OF_TWO_CARDS];
        playerCards = new ArrayList<Card>();
        computerCards = new ArrayList<Card>();

        for(int i =0;i<TOTAL_NUMBER_OF_TWO_CARDS;i+=4)
        {
            //
            for(int j = 0;j<CARD_TYPES;j++)
            {
                switch (j)
                {
                    case 0:
                        cardHeap[i+j] = new Card(Color.HEARTS,i%13+1);
                        break;
                    case 1:
                        cardHeap[i+j] = new Card(Color.DIAMOND,i%13+1);
                        break;
                    case 2:
                        cardHeap[i+j] = new Card(Color.CLUB,i%13+1);
                        break;
                    default:
                        cardHeap[i+j] = new Card(Color.SPADE,i%13+1);
                        break;
                }
            }
            //by using two loops to filter Jokers
        }

    }
   void showCards(ArrayList<Card>cards)
   {
       for (Card element:cards)
       {
           System.out.print(element);
       }
       System.out.println();
   }
   void shuffle()
   {
       cardHeapPosition = 0;
       Card [] tempHeap = new Card[TOTAL_NUMBER_OF_TWO_CARDS];
       int position = 0;

       for(int i = 0;i<TOTAL_NUMBER_OF_TWO_CARDS;i++)
       {
           position=(int)(Math.random()*104);
           for(int j = 0;j<TOTAL_NUMBER_OF_TWO_CARDS;j++)
           {
               if(null == tempHeap[position])
               {
                   tempHeap[position] = new Card(cardHeap[i].getCardColor(),cardHeap[i].getPokerValue());
                   break;
               }
               else
               {
                   position = (position+1)%TOTAL_NUMBER_OF_TWO_CARDS;

               }
           }
       }

       for(int i = 0;i<TOTAL_NUMBER_OF_TWO_CARDS;i++)
       {
           cardHeap[i].setCardColor(tempHeap[i].getCardColor());
           cardHeap[i].setPokerValue(tempHeap[i].getPokerValue());
       }

   }
   void deal(ArrayList<Card>cards)
   {
       cards.add(cardHeap[++cardHeapPosition]);

   }
    void gameOver()
    {
        System.out.println("Banker's pokers");
        showCards(computerCards);
        System.out.println("Total Points of banker is :"+getValue(computerCards));
        showCards(playerCards);
        System.out.println("Total Points of yours is :"+getValue(playerCards));
    }
    int getValue(ArrayList<Card>cards)
    {
        int value = 0;
        final  int MAX_POINT = 21;
        for (Card e:cards)
        {
            if(e.getPokerValue()>=10)
            {
                value+=10;
            }
            else if(e.getPokerValue() ==1)
            {
                value += 11;// when picking up 'A',adding 1 or 11;at this moment add 11
            }
            else
            {
                value += e.getPokerValue();//other type directly add its' poker value
            }

        }
        if(value >MAX_POINT)
        {
            for(Card f:cards)
            {
                if(f.getPokerValue()==1)
                {
                    value -=10;
                    if(value <MAX_POINT)
                    {
                        break;
                    }

                }
            }
        }

        if(value > MAX_POINT)
        {
            return  -1;
        }
        return value;
    }
    boolean hasSame(ArrayList<Card>cards)
    {
        for(int i = 0;i<cards.size();i++)
        {
            for(int j = i+1;j<cards.size();j++)
            {
                if(cards.get(i).equals(cards.get(j)))
                {
                    return true;
                }
            }
        }
        return false;
    }
    int win()
    {
        int playerValue = getValue(playerCards);
        int computerValue = getValue(computerCards);
        if(playerValue>computerValue)
        {
            return 1;
        }
        else if(playerValue == computerValue)
        {
            return 2;
        }
        else
        {
            return 3;
        }
    }
    void startGame()
    {
        boolean finish = false;
        Scanner in = new Scanner(System.in);
        String playerInput = "";
        final String NO = "n";


        while(!finish)
        {
            playerCards.clear();
            computerCards.clear();
            shuffle();

            //banker's turn
            System.out.println("It is a turn for banker to pick up poker");
            int computerValue = 0;
            boolean getWinner = false;
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            /*
            When the total points of banker is less than 16 ,banker
            must pick up poker ;otherwise stop picking pocker
             */

            while(computerValue<=16)
            {
                deal(computerCards);
                getWinner = hasSame(computerCards);
                if(getWinner)
                {
                    System.out.println("Banker has picked up the same poker,Win the game!");
                    gameOver();
                    break;
                }
                computerValue = getValue(computerCards);
                if(computerValue == -1)
                {
                    System.out.println("You are the winner");
                    gameOver();
                    getWinner = true;
                    break;
                }
            }
            if(!getWinner) {
                System.out.println("Banker has total  " + computerCards.size() + " pokers");

                while(!playerInput.equals(NO)) {
                    deal(playerCards);

                    getWinner = hasSame(playerCards);
                    if (getWinner) {
                        System.out.println("You has picked up the same poker,Win win win！！！");
                        gameOver();
                        break;
                    }
                    System.out.println("Your pokers are :");
                    showCards(playerCards);
                    int playerValue = getValue(playerCards);
                    if (playerValue == -1) {
                        System.out.println("Banker win the game！！！");
                        gameOver();
                        getWinner = true;
                        break;
                    }
                    System.out.println("Total point is  " + getValue(playerCards));
                    System.out.println("Contining to picking poker input（y/or n）？");
                    playerInput = in.nextLine();
                }
            }

            if(!getWinner) {
                switch(win()) {
                    case 1: //winner successful
                        System.out.println("You win！！！");
                        gameOver();
                        break;
                    case 2:
                        System.out.println("Draw！！！");
                        gameOver();
                        break;
                    case 3:
                        System.out.println("Banker win！！！");
                        gameOver();
                        break;
                }
            }

            System.out.println("Conting to playing game（y/ or n）？");
            playerInput = in.nextLine();

            if(playerInput.equals(NO))
                finish = true;


        }
    }

}
public class Main {
    public static void main(String []args)
    {
        CardGame  cardGame = new CardGame();
        cardGame.startGame();

    }


}
