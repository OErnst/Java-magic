package magicthegathering.impl;

import magicthegathering.game.CreatureCard;
import magicthegathering.game.Game;
import magicthegathering.game.Generator;
import magicthegathering.game.Player;


import java.util.List;

/**
 * @author Ondřej Ernst
 */
public class GameImpl implements Game {

    private Player[] players;
    private int currentPlayer = 0;
    private int secondPlayer = 1;

    /**
     * constructor
     * @param player1 first
     * @param player2 second
     */
    public GameImpl(Player player1, Player player2) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("player null");
        }
        players = new Player[]{player1, player2};
    }


    @Override
    public void initGame() {
        players[0].initCards(Generator.generateCards());
        players[1].initCards(Generator.generateCards());
    }

    @Override
    public void changePlayer() {
        currentPlayer = secondPlayer;
        if (secondPlayer == 0) {
            secondPlayer = 1;
        } else {
            secondPlayer = 0;
        }
    }

    @Override
    public void prepareCurrentPlayerForTurn() {
        players[currentPlayer].untapAllCards();
        players[currentPlayer].prepareAllCreatures();
    }

    @Override
    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    @Override
    public Player getSecondPlayer() {
        return players[secondPlayer];
    }

    @Override
    public void performAttack(List<CreatureCard> creatures) {
        for (CreatureCard card : creatures) {
            card.tap();
        }
    }

    @Override
    public boolean isCreaturesAttackValid(List<CreatureCard> attackingCreatures) {
        for (int i = 0; i < attackingCreatures.size(); i++) {
            if (!players[currentPlayer].getCreaturesOnTable().contains(attackingCreatures.get(i))) {
                return false;
            }
            if (attackingCreatures.get(i).isTapped() || attackingCreatures.get(i).hasSummoningSickness()) {
                return false;
            }
            for (int j = 0; j < attackingCreatures.size(); j++) {
                if (attackingCreatures.get(i).equals(attackingCreatures.get(j)) && i != j) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isCreaturesBlockValid(List<CreatureCard> attackingCreatures, List<CreatureCard> blockingCreatures) {

        if (attackingCreatures.size() != blockingCreatures.size()) {
            return false;
        }
        for (int i = 0; i < blockingCreatures.size(); i++) {
            if (!players[currentPlayer].getCreaturesOnTable().contains(attackingCreatures.get(i))) {
                return false;
            }
            for (int j = 0; j < attackingCreatures.size(); j++) {
                if (attackingCreatures.get(i).equals(attackingCreatures.get(j)) && i != j) {
                    return false;
                }
            }
            if (blockingCreatures.get(i) == null) {
                continue;
            }
            if (blockingCreatures.get(i).isTapped()) {
                return false;
            }
            for (int j = 0; j < blockingCreatures.size(); j++) {
                if (blockingCreatures.get(i).equals(blockingCreatures.get(j)) && i != j) {
                    return false;
                }
            }
            if (!players[secondPlayer].getCreaturesOnTable().contains(blockingCreatures.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void performBlockAndDamage(List<CreatureCard> attackingCreatures, List<CreatureCard> blockingCreatures) {
        for (int i = 0; i < attackingCreatures.size(); i++) {
            if (blockingCreatures.get(i) == null) {
                players[secondPlayer].subtractLives(attackingCreatures.get(i).getPower());
                continue;
            }
            boolean attackingWon = attackingCreatures.get(i).getPower() >= blockingCreatures.get(i).getToughness();
            boolean defendingWon = blockingCreatures.get(i).getPower() >= attackingCreatures.get(i).getToughness();
            if (attackingWon) {
                players[secondPlayer].destroyCreature(blockingCreatures.get(i));
            }
            if (defendingWon) {
                players[currentPlayer].destroyCreature(attackingCreatures.get(i));
            }
        }
    }
}
