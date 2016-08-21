package text.search

/**
  * @author ynupc
  *         Created on 2016/08/20
  */
object Searching extends Enumeration {
  val BruteForce,
  DeterministicFiniteAutomaton,
  KarpRabin,
  ShiftAnd,
  ShiftOr,
  MorrisPratt,
  KnuthMorrisPratt,
  Simon,
  Colussi,
  GalilGiancarlo,
  ApostolicoCrochemore,
  NotSoNaive,
  BoyerMoore,
  TurboBM,
  ApostolicoGiancarlo,
  ReverseColussi,
  Horspool,
  QuickSearch,
  TunedBoyerMoore,
  ZhuTakaoka,
  BerryRavindran,
  Smith,
  Raita,
  ReverseFactor,
  TurboReverseFactor,
  ForwardDawgMatching,
  BackwardNondeterministicDawgMatching,
  BackwardOracleMatching,
  GalilSeiferas,
  TwoWay,
  StringMatchingOnOrderedAlphabets,
  OptimalMismatch,
  MaximalShift,
  SkipSearch,
  KMPSkipSearch,
  AlphaSkipSearch = Value
}
