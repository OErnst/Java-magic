package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.ManaType;

import java.util.List;
import java.util.Objects;
/**
 * @author Ondřej Ernst
 */
public class CreatureCardImpl extends AbstractCard implements CreatureCard {

    private String name;
    private List<ManaType> cost;
    private int power;
    private int toughness;
    private boolean isSick = false;

    /**
     * constructor
     * @param name of the card
     * @param cost of the card
     * @param power of the card
     * @param toughness of the card
     */
    public CreatureCardImpl(String name, List<ManaType> cost, int power, int toughness) {
        if (name == null || name.length() == 0 || cost == null || power < 0 || toughness <= 0) {
            throw new IllegalArgumentException("wrong arguments");
        }
        for (ManaType m : cost) {
            if (m == null) {
                throw new IllegalArgumentException("wrong arguments");
            }
        }
        this.name = name;
        this.cost = cost;
        this.power = power;
        this.toughness = toughness;
    }

    @Override
    public int getTotalCost() {
        return cost.size();
    }

    @Override
    public int getSpecialCost(ManaType mana) {
        int count = 0;
        for (ManaType mt : cost) {
            if (mt == mana) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getToughness() {
        return toughness;
    }

    @Override
    public boolean hasSummoningSickness() {
        return isSick;
    }

    @Override
    public void setSummoningSickness() {
        isSick = true;
    }

    @Override
    public void unsetSummoningSickness() {
        isSick = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatureCardImpl that = (CreatureCardImpl) o;
        return power == that.power &&
                toughness == that.toughness &&
                Objects.equals(name, that.name) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, power, toughness);
    }

    @Override
    public String toString() {
        return name + " " + cost + " " + power + " / " + toughness + (!isSick ? " can attack" : "")
                + (isTapped() ? " TAPPED" : "");
    }
}
