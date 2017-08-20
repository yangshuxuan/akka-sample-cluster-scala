package cn.com.juran.robin

/**
  * Created by yangshuxuan on 2017/8/20.
  */
import akka.actor.Props
import scala.collection.immutable
import akka.actor.{ ActorRef}
object FSMDocSpec {
  // received events
  final case class SetTarget(ref: ActorRef)
  final case class Queue(obj: Any)
  case object Flush

  // sent events
  final case class Batch(obj: immutable.Seq[Any])
  // states
  sealed trait State
  case object Idle extends State
  case object Active extends State

  sealed trait Data
  case object Uninitialized extends Data
  final case class Todo(target: ActorRef, queue: immutable.Seq[Any]) extends Data

}

