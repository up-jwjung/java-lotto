package lotto.domain;

import lotto.domain.model.LottoGames;
import lotto.exception.LottoGameException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lotto.config.LottoGameConfig.PRICE_PER_GAME;
import static lotto.constants.Message.INPUT_SHOULD_NOT_LESS_THAN_PRICE_PER_GAME;

public class PurchaseAction {

  private static final int ZERO = 0;

  private final int purchasePrice;
  private final int numberOfGames;

  public PurchaseAction(int purchasePrice) {
    this.purchasePrice = purchasePrice;
    this.numberOfGames = computeNumberOfGames(purchasePrice, PRICE_PER_GAME);
  }

  //for test
  public PurchaseAction(int purchasePrice, int numberOfGames) {
    this.purchasePrice = purchasePrice;
    this.numberOfGames = numberOfGames;
  }

  private int computeNumberOfGames(int purchasePrice, int pricePerGame) {
    if (purchasePrice < pricePerGame) {
      throw new LottoGameException(String.format(INPUT_SHOULD_NOT_LESS_THAN_PRICE_PER_GAME, pricePerGame));
    }
    return purchasePrice / pricePerGame;
  }

  public int getPurchasePrice() {
    return purchasePrice;
  }

  public LottoGames purchase() {
    return new LottoGames(generateLottoGames());
  }

  private List<LottoGame> generateLottoGames() {
    return IntStream.range(ZERO, numberOfGames)
        .mapToObj(index -> LottoGameManager.newLottoGame())
        .collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PurchaseAction that = (PurchaseAction) o;
    return purchasePrice == that.purchasePrice &&
        numberOfGames == that.numberOfGames;
  }

  @Override
  public int hashCode() {
    return Objects.hash(purchasePrice, numberOfGames);
  }
}
