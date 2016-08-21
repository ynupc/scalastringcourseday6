package text.search

import java.util

/**
  * @author ynupc
  *         Created on 2016/08/20
  */
trait Search {
  def indexOf[T](source: Array[T], target: Array[T]): Int

  def indicesOf[T](source: Array[T], target: Array[T]): Array[Int]
}

object Search {

  def indexOf[T](source: Array[T], target: Array[T], searching: Searching.Value): Int = {
    searching match {
      case Searching.BruteForce =>
        source.indexOf(target)
      case Searching.DeterministicFiniteAutomaton =>
        -1
      case Searching.KarpRabin =>
        KarpRabin.indexOf(source, target)
      case Searching.ShiftAnd =>
        ShiftAnd.indexOf(source, target)
      case Searching.ShiftOr =>
        ShiftOr.indexOf(source, target)
      case Searching.MorrisPratt =>
        -1
      case Searching.KnuthMorrisPratt =>
        source.indexOfSlice(target)
      case Searching.Simon =>
        -1
      case Searching.Colussi =>
        -1
      case Searching.GalilGiancarlo =>
        -1
      case Searching.ApostolicoCrochemore =>
        -1
      case Searching.NotSoNaive =>
        -1
      case Searching.BoyerMoore =>
        -1
      case Searching.TurboBM =>
        -1
      case Searching.ApostolicoGiancarlo =>
        -1
      case Searching.ReverseColussi =>
        -1
      case Searching.Horspool =>
        -1
      case Searching.QuickSearch =>
        -1
      case Searching.TunedBoyerMoore =>
        -1
      case Searching.ZhuTakaoka =>
        -1
      case Searching.BerryRavindran =>
        -1
      case Searching.Smith =>
        -1
      case Searching.Raita =>
        -1
      case Searching.ReverseFactor =>
        -1
      case Searching.TurboReverseFactor =>
        -1
      case Searching.ForwardDawgMatching =>
        -1
      case Searching.BackwardNondeterministicDawgMatching =>
        -1
      case Searching.BackwardOracleMatching =>
        -1
      case Searching.GalilSeiferas =>
        -1
      case Searching.TwoWay =>
        -1
      case Searching.StringMatchingOnOrderedAlphabets =>
        -1
      case Searching.OptimalMismatch =>
        -1
      case Searching.MaximalShift =>
        -1
      case Searching.SkipSearch =>
        -1
      case Searching.KMPSkipSearch =>
        -1
      case Searching.AlphaSkipSearch =>
        -1
      case otherwise =>
        throw new NoSuchSearchingAlgorithmException()
    }
  }

  def indexOf[T](source: Array[T], target: Array[T], start: Int, searching: Searching.Value): Int = {
    start + indexOf[T](util.Arrays.copyOf(source, start), target, searching)
  }

  def lastIndexOf[T](source: Array[T], target: Array[T], searching: Searching.Value): Int = {
    indexOf[T](source.reverse, target.reverse, searching)
  }

  def lastIndexOf[T](source: Array[T], target: Array[T], end: Int, searching: Searching.Value): Int = {
    end + lastIndexOf[T](source, target, searching)
  }

  private class NoSuchSearchingAlgorithmException extends Exception
}
