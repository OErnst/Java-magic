package magicthegathering.impl;



import magicthegathering.game.ManaType;
import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.LandCard;
import magicthegathering.game.Player;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Ondřej Ernst
 */
public class PlayerImpl implements Player {

    private String name;
    private int life = 20;
    private List<Card> cards;

    /**
     * constructor
     * @param name of the player
     */
    public PlayerImpl(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("wrong name");
        }
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public void subtractLives(int lives) {
        life -= lives;
    }

    @Override
    public boolean isDead() {
        return life <= 0;
    }

    @Override
    public void initCards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }


    @Override
    public List<Card> getCardsInHand() {
        List<Card> list = new ArrayList<>();
        for (Card card : cards) {
            if (!card.isOnTable()) {
                list.add(card);
            }
        }
        return list;
    }

    @Override
    public List<Card> getCardsOnTable() {
        List<Card> list = new ArrayList<>();
        for (Card card : cards) {
            if (card.isOnTable()) {
                list.add(card);
            }
        }
        return list;
    }
    /**
     *
     * @param onTable if we want onTable or not
     * @return list with wanted cards
     */
    private List<LandCard> getLandsCondition(boolean onTable) {
        List<LandCard> list = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof LandCard && card.isOnTable() == onTable) {
                list.add((LandCard) card);
            }
        }
        return list;
    }
    @Override
    public List<LandCard> getLandsOnTable() {
        return getLandsCondition(true);
    }

    @Override
    public List<LandCard> getLandsInHand() {
        return getLandsCondition(false);
    }

    /**
     *
     * @param onTable if we want onTable or not
     * @return list with wanted cards
     */
    private List<CreatureCard> getCreaturesCondition(boolean onTable) {
        List<CreatureCard> list = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof CreatureCard && card.isOnTable() == onTable) {
                list.add((CreatureCard) card);
            }
        }
        return list;
    }

    @Override
    public List<CreatureCard> getCreaturesOnTable() {
        return getCreaturesCondition(true);
    }



    @Override
    public List<CreatureCard> getCreaturesInHand() {
        return getCreaturesCondition(false);
    }

    @Override
    public void untapAllCards() {
        for (Card card : cards) {
            card.untap();
        }
    }

    @Override
    public void prepareAllCreatures() {
        for (CreatureCard card : getCreaturesInHand()) {
            card.unsetSummoningSickness();
        }
        for (CreatureCard card : getCreaturesOnTable()) {
            card.unsetSummoningSickness();
        }
    }

    @Override
    public boolean putLandOnTable(LandCard landCard) {
        if (landCard.isOnTable()) {
            return false;
        }
        for (LandCard card : getLandsInHand()) {
            if (card.equals(landCard)) {
                card.putOnTable();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean putCreatureOnTable(CreatureCard creatureCard) {

        if (creatureCard.isOnTable()) {
            return false;
        }

        for (CreatureCard card : getCreaturesInHand()) {
            if (card.equals(creatureCard) && hasManaForCreature(creatureCard)) {
                tapManaForCreature(creatureCard);
                card.putOnTable();
                card.setSummoningSickness();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasManaForCreature(CreatureCard creature) {
        String[] colors = {"WHITE", "RED", "GREEN", "BLUE", "BLACK"};
        int[] untappedLands = calculateUntappedLands();
        for (int i = 0; i < 5; i++) {
            if (creature.getSpecialCost(ManaType.valueOf(colors[i])) > untappedLands[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int[] calculateUntappedLands() {
        int[] lands = new int[5];
        for (LandCard card : getLandsOnTable()) {
            if (!card.isTapped()) {
                lands[card.getManaType().ordinal()]++;
            }
        }
        return lands;
    }

    @Override
    public void tapManaForCreature(CreatureCard creature) {
        int[] cost = {creature.getSpecialCost(ManaType.WHITE),
                creature.getSpecialCost(ManaType.RED),
                creature.getSpecialCost(ManaType.GREEN),
                creature.getSpecialCost(ManaType.BLUE),
                creature.getSpecialCost(ManaType.BLACK)};
        for (LandCard card : getLandsOnTable()) {
            if (card.isTapped()) {
                continue;
            }
            if (cost[card.getManaType().ordinal()] > 0) {
                cost[card.getManaType().ordinal()]--;
                card.tap();
            }
        }
    }

    @Override
    public void destroyCreature(CreatureCard creature) {
        cards.remove(creature);
    }

    @Override
    public String toString() {
        return name + "("+life+")";
    }
}
