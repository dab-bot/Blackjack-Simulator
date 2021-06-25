JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        Blackjack.java \
        Blackjackgame.java \
        Card.java \
		Dealer.java \
		Deck.java \
		Hand.java \
		InvalidCardSuitException.java \
		InvalidCardValueException.java \
		InvalidDeckPositionException.java \
        Player.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class