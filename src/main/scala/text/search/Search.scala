package text.search

import java.util

import scala.collection.mutable.ListBuffer

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
        DeterministicFiniteAutomaton.indexOf[T](source, target)
      case Searching.KarpRabin =>
        KarpRabin.indexOf[T](source, target)
      case Searching.ShiftAnd =>
        ShiftAnd.indexOf[T](source, target)
      case Searching.ShiftOr =>
        ShiftOr.indexOf[T](source, target)
      case Searching.MorrisPratt =>
        MorrisPratt.indexOf[T](source, target)
      case Searching.KnuthMorrisPratt =>
        source.indexOfSlice(target)
      case Searching.Simon =>
        Simon.indexOf[T](source, target)
      case Searching.Colussi =>
        Colussi.indexOf[T](source, target)
      case Searching.GalilGiancarlo =>
        GalilGiancarlo.indexOf[T](source, target)
      case Searching.ApostolicoCrochemore =>
        ApostolicoCrochemore.indexOf[T](source, target)
      case Searching.NotSoNaive =>
        NotSoNaive.indexOf[T](source, target)
      case Searching.BoyerMoore =>
        BoyerMoore.indexOf[T](source, target)
      case Searching.TurboBM =>
        TurboBM.indexOf[T](source, target)
      case Searching.ApostolicoGiancarlo =>
        ApostolicoGiancarlo.indexOf[T](source, target)
      case Searching.ReverseColussi =>
        ReverseColussi.indexOf[T](source, target)
      case Searching.Horspool =>
        Horspool.indexOf[T](source, target)
      case Searching.QuickSearch =>
        QuickSearch.indexOf[T](source, target)
      case Searching.TunedBoyerMoore =>
        TunedBoyerMoore.indexOf[T](source, target)
      case Searching.ZhuTakaoka =>
        ZhuTakaoka.indexOf[T](source, target)
      case Searching.BerryRavindran =>
        BerryRavindran.indexOf[T](source, target)
      case Searching.Smith =>
        Smith.indexOf[T](source, target)
      case Searching.Raita =>
        Raita.indexOf[T](source, target)
      case Searching.ReverseFactor =>
        ReverseFactor.indexOf[T](source, target)
      case Searching.TurboReverseFactor =>
        TurboReverseFactor.indexOf[T](source, target)
      case Searching.ForwardDawgMatching =>
        ForwardDawgMatching.indexOf[T](source, target)
      case Searching.BackwardNondeterministicDawgMatching =>
        BackwardNondeterministicDawgMatching.indexOf[T](source, target)
      case Searching.BackwardOracleMatching =>
        BackwardOracleMatching.indexOf[T](source, target)
      case Searching.GalilSeiferas =>
        GalilSeiferas.indexOf[T](source, target)
      case Searching.TwoWay =>
        TwoWay.indexOf[T](source, target)
      case Searching.StringMatchingOnOrderedAlphabets =>
        StringMatchingOnOrderedAlphabets.indexOf[T](source, target)
      case Searching.OptimalMismatch =>
        OptimalMismatch.indexOf[T](source, target)
      case Searching.MaximalShift =>
        MaximalShift.indexOf[T](source, target)
      case Searching.SkipSearch =>
        SkipSearch.indexOf[T](source, target)
      case Searching.KMPSkipSearch =>
        KMPSkipSearch.indexOf[T](source, target)
      case Searching.AlphaSkipSearch =>
        AlphaSkipSearch.indexOf[T](source, target)
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

  def indicesOf[T](source: Array[T], target: Array[T], searching: Searching.Value): Array[Int] = {
    searching match {
      case Searching.BruteForce =>
        //TODO: fix here
        val buffer: ListBuffer[Int] = ListBuffer[Int]()
        for (i <- source.indices) {
          buffer += source.indexOf(target, i)
        }
        buffer.toArray
      case Searching.DeterministicFiniteAutomaton =>
        DeterministicFiniteAutomaton.indicesOf[T](source, target)
      case Searching.KarpRabin =>
        KarpRabin.indicesOf[T](source, target)
      case Searching.ShiftAnd =>
        ShiftAnd.indicesOf[T](source, target)
      case Searching.ShiftOr =>
        ShiftOr.indicesOf[T](source, target)
      case Searching.MorrisPratt =>
        MorrisPratt.indicesOf[T](source, target)
      case Searching.KnuthMorrisPratt =>
        //TODO: fix here
        val buffer: ListBuffer[Int] = ListBuffer[Int]()
        for (i <- source.indices) {
          buffer += source.indexOfSlice(target, i)
        }
        buffer.toArray
      case Searching.Simon =>
        Simon.indicesOf[T](source, target)
      case Searching.Colussi =>
        Colussi.indicesOf[T](source, target)
      case Searching.GalilGiancarlo =>
        GalilGiancarlo.indicesOf[T](source, target)
      case Searching.ApostolicoCrochemore =>
        ApostolicoCrochemore.indicesOf[T](source, target)
      case Searching.NotSoNaive =>
        NotSoNaive.indicesOf[T](source, target)
      case Searching.BoyerMoore =>
        BoyerMoore.indicesOf[T](source, target)
      case Searching.TurboBM =>
        TurboBM.indicesOf[T](source, target)
      case Searching.ApostolicoGiancarlo =>
        ApostolicoGiancarlo.indicesOf[T](source, target)
      case Searching.ReverseColussi =>
        ReverseColussi.indicesOf[T](source, target)
      case Searching.Horspool =>
        Horspool.indicesOf[T](source, target)
      case Searching.QuickSearch =>
        QuickSearch.indicesOf[T](source, target)
      case Searching.TunedBoyerMoore =>
        TunedBoyerMoore.indicesOf[T](source, target)
      case Searching.ZhuTakaoka =>
        ZhuTakaoka.indicesOf[T](source, target)
      case Searching.BerryRavindran =>
        BerryRavindran.indicesOf[T](source, target)
      case Searching.Smith =>
        Smith.indicesOf[T](source, target)
      case Searching.Raita =>
        Raita.indicesOf[T](source, target)
      case Searching.ReverseFactor =>
        ReverseFactor.indicesOf[T](source, target)
      case Searching.TurboReverseFactor =>
        TurboReverseFactor.indicesOf[T](source, target)
      case Searching.ForwardDawgMatching =>
        ForwardDawgMatching.indicesOf[T](source, target)
      case Searching.BackwardNondeterministicDawgMatching =>
        BackwardNondeterministicDawgMatching.indicesOf[T](source, target)
      case Searching.BackwardOracleMatching =>
        BackwardOracleMatching.indicesOf[T](source, target)
      case Searching.GalilSeiferas =>
        GalilSeiferas.indicesOf[T](source, target)
      case Searching.TwoWay =>
        TwoWay.indicesOf[T](source, target)
      case Searching.StringMatchingOnOrderedAlphabets =>
        StringMatchingOnOrderedAlphabets.indicesOf[T](source, target)
      case Searching.OptimalMismatch =>
        OptimalMismatch.indicesOf[T](source, target)
      case Searching.MaximalShift =>
        MaximalShift.indicesOf[T](source, target)
      case Searching.SkipSearch =>
        SkipSearch.indicesOf[T](source, target)
      case Searching.KMPSkipSearch =>
        KMPSkipSearch.indicesOf[T](source, target)
      case Searching.AlphaSkipSearch =>
        AlphaSkipSearch.indicesOf[T](source, target)
      case otherwise =>
        throw new NoSuchSearchingAlgorithmException()
    }
  }

  def indicesOf[T](source: Array[T], target: Array[T], start: Int, searching: Searching.Value): Array[Int] = {
    indicesOf[T](util.Arrays.copyOf(source, start), target, searching) map (_ + start)
  }

  def lastIndicesOf[T](source: Array[T], target: Array[T], searching: Searching.Value): Array[Int] = {
    indicesOf[T](source.reverse, target.reverse, searching)
  }

  def lastIndicesOf[T](source: Array[T], target: Array[T], end: Int, searching: Searching.Value): Array[Int] = {
    lastIndicesOf[T](source, target, searching) map (_ + end)
  }

  private class NoSuchSearchingAlgorithmException extends Exception
}
