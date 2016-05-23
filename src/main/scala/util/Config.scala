package util

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import text.similarity.{Overlap, Similarity}

/**
  * @author ynupc
  *         Created on 2016/05/22
  */
object Config {
  final private[this] var config: Config = ConfigFactory.load()

  def set(configFile: File): Unit = {
    config = ConfigFactory.load(ConfigFactory.parseFile(configFile))
  }

  final lazy val isFrequencyOtherwiseBinary: Boolean = config.as[Option[Boolean]]("vector.isFrequencyOtherwiseBinary").getOrElse(true)

  final lazy val nGram: Int = config.as[Option[Int]]("vector.concept.nGram.n") match {
    case Some(n) if 1 <= n =>
      n
    case otherwise =>
      1
  }

  final lazy val nGramGap: Int = config.as[Option[Int]]("vector.concept.nGram.gap") match {
    case Some(gap) if 0 <= gap =>
      gap
    case Some(gap) if gap < 0 =>
      Int.MaxValue
    case otherwise =>
      0
  }

  final lazy val tokenizer: String = config.as[Option[String]]("vector.concept.tokenizer").getOrElse("CharacterNGram")

  final lazy val sentenceSplitter: String = config.as[Option[String]]("vector.concept.sentenceSplitter").getOrElse("none")

  final lazy val tverskyA: Double = config.as[Option[Double]]("vector.tverskyA").getOrElse(1D)

  final lazy val tverskyB: Double = config.as[Option[Double]]("vector.tverskyA").getOrElse(1D)

  final lazy val minkowskyQ: Double = config.as[Option[Double]]("vector.minkowskyQ").getOrElse(2D)

  final lazy val fScoreBeta: Double = config.as[Option[Double]]("vector.fScoreBeta").getOrElse(1D)

  final lazy val jaroWinklerThreshold: Double = config.as[Option[Double]]("vector.jaroWinklerThreshold").getOrElse(0.7D)

  final lazy val jaroWinklerScalingFactor: Double = config.as[Option[Double]]("vector.jaroWinklerScalingFactor").getOrElse(0.1D)

  final lazy val damerauLevenshteinDeleteCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinDeleteCost").getOrElse(1D)

  final lazy val damerauLevenshteinInsertCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinInsertCost").getOrElse(1D)

  final lazy val damerauLevenshteinReplaceCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinReplaceCost").getOrElse(1D)

  final lazy val damerauLevenshteinSwapCost: Double = config.as[Option[Double]]("vector.damerauLevenshteinSwapCost").getOrElse(1D)

  final lazy val similarity: Similarity.Value = {
    config.as[Option[String]]("vector.similarity") match {
      case Some(sim) if sim equalsIgnoreCase "AverageTwoWayConversions" =>
        Similarity.AverageTwoWayConversions
      case Some(sim) if sim equalsIgnoreCase "Cosine" =>
        Similarity.Cosine
      case Some(sim) if sim equalsIgnoreCase "Covariance" =>
        Similarity.Covariance
      case Some(sim) if sim equalsIgnoreCase "Dice" =>
        Similarity.Dice
      case Some(sim) if sim equalsIgnoreCase "InnerProduct" =>
        Similarity.InnerProduct
      case Some(sim) if sim equalsIgnoreCase "Jaccard" =>
        Similarity.Jaccard
      case Some(sim) if sim equalsIgnoreCase "JaccardSimpson" =>
        Similarity.JaccardSimpson
      case Some(sim) if sim equalsIgnoreCase "Lin98" =>
        Similarity.Lin98
      case Some(sim) if sim equalsIgnoreCase "Mihalcea04" =>
        Similarity.Mihalcea04
      case Some(sim) if sim equalsIgnoreCase "PearsonProductMomentCorrelationCoefficient" =>
        Similarity.PearsonProductMomentCorrelationCoefficient
      case Some(sim) if sim equalsIgnoreCase "ReciprocalChebyshev" =>
        Similarity.ReciprocalChebyshev
      case Some(sim) if sim equalsIgnoreCase "ReciprocalEuclidean" =>
        Similarity.ReciprocalEuclidean
      case Some(sim) if sim equalsIgnoreCase "ReciprocalManhattan" =>
        Similarity.ReciprocalManhattan
      case Some(sim) if sim equalsIgnoreCase "ReciprocalMinkowsky" =>
        Similarity.ReciprocalMinkowsky
      case Some(sim) if sim equalsIgnoreCase "Simpson" =>
        Similarity.Simpson
      case Some(sim) if sim equalsIgnoreCase "Tversky" =>
        Similarity.Tversky
      case otherwise =>
        Similarity.Cosine
    }
  }

  final lazy val convergence: Overlap.Value = {
    config.as[Option[String]]("vector.convergence") match {
      case Some(con) if con equalsIgnoreCase "Rus05" =>
        Overlap.Rus05
      case Some(con) if con equalsIgnoreCase "F" =>
        Overlap.F
      case Some(con) if con equalsIgnoreCase "F1" =>
        Overlap.F1
      case Some(con) if con equalsIgnoreCase "Precision" =>
        Overlap.Precision
      case Some(con) if con equalsIgnoreCase "Recall" =>
        Overlap.Recall
      case otherwise =>
        Overlap.Rus05
    }
  }
}
