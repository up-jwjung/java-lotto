package lotto.business;

import lotto.interfaces.Play;
import lotto.objects.AllNumbers;
import lotto.objects.MatchCount;
import lotto.objects.Lotto;
import lotto.objects.Lottos;
import lotto.objects.WinningType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayLotto implements Play {
    private static final int PRICE_PER_LOTTO = 1000;
    private static final int NUMBER_COUNT_PER_GAME = 6;
    private static final int MAXIMUM_NUMBER = 45;

    public int buyLotto(int money) {
        return money / PRICE_PER_LOTTO;
    }

    public int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(MAXIMUM_NUMBER - 1) + 1;
    }

    public ArrayList<Integer> createLotto() {
        ArrayList<Integer> lotto = new ArrayList<Integer>();
        for (int i = 0; i < NUMBER_COUNT_PER_GAME; i++) {
            lotto.add(getRandomNumber());
        }

        Collections.sort(lotto);

        return lotto;
    }

    public Lotto generateLotto(Lotto lotto) {
        AllNumbers allNumbers = new AllNumbers();

        List<Integer> randomNumbers = allNumbers.getRandomNumbers();

        for (int i = 0; i < NUMBER_COUNT_PER_GAME; i++) {
            lotto.addNumber(randomNumbers.get(i));
        }

        return lotto;
    }

    public static void countSameNumbers(Lotto initLotto, Lotto myLotto, MatchCount matchCount, int index) {
        if (myLotto.getNumbers().contains(initLotto.getNumbers().get(index))) {
            matchCount.add(1);
        }
    }

    public WinningType decideWinningType(int count, int bonusNumber, Lotto myLotto) {
        if (count == WinningType.THREE.getCounts()) {
            return WinningType.THREE;
        }
        if (count == WinningType.FOUR.getCounts()) {
            return WinningType.FOUR;
        }
        if (count == WinningType.FIVE.getCounts()) {
            return compareBonusNumber(bonusNumber, myLotto);
        }
        if (count == WinningType.SIX.getCounts()) {
            return WinningType.SIX;
        }
        return WinningType.LOSE;
    }

    public WinningType compareBonusNumber(int bonusNumber, Lotto myLotto) {
        if (myLotto.getNumbers().contains(bonusNumber)) {
            return WinningType.FIVE_AND_BONUS;
        }
        return WinningType.FIVE;
    }

    public WinningType findSameNumbers(Lotto initLotto, Lotto myLotto, int bonusBallNumber) {
        MatchCount matchCount = new MatchCount();
        for (int i = 0; i < NUMBER_COUNT_PER_GAME; i++) {
            countSameNumbers(initLotto, myLotto, matchCount, i);
        }

        return decideWinningType(matchCount.getCount(), bonusBallNumber, myLotto);
    }

    public List<WinningType> getWinningStatistics(Lottos createdLottos, Lotto lastWinningLotto, int bonusBallNumber) {
        List<WinningType> wins = new ArrayList<>();
        for (Lotto lotto : createdLottos.getLottos()) {
            wins.add(findSameNumbers(lastWinningLotto, lotto, bonusBallNumber));
        }

        return wins;
    }

    public Lottos autoCreateLottos(int totalLotto) {
        Lottos lottos = new Lottos();

        for (int i = 0; i < totalLotto; i++) {
            Lotto lotto = new Lotto();

            lottos.addLotto(generateLotto(lotto));
        }

        return lottos;
    }

}
