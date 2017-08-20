package cn.com.juran.robin

import akka.actor.{Actor, ActorLogging, ActorRef, FSM, Props, Terminated}

import scala.collection.immutable

/**
  * Created by yangshuxuan on 2017/8/20.
  */
object RobinSpec{
  // received events
  case class Task(jobDesc:String)
  case object  TaskIn
  case object TaskFinish


  // states
  sealed trait WorkerState
  case object WorkerIdle extends WorkerState
  case object WorkerActive extends WorkerState

  case class  WorkerData()


}
import RobinSpec._
class Worker extends FSM[WorkerState, WorkerData] {
  startWith(WorkerIdle, WorkerData())

  when(WorkerIdle) {
    case Event(Task(jobDesc), _) => {
      sender() ! TaskIn
      val p = context.actorOf(JobWorker.props(sender(),jobDesc))
      context.watch(p)

      goto(WorkerActive)
    }

  }
  when(WorkerActive){
    case Event(Terminated(_),_) => goto(WorkerIdle)
  }

}
object JobWorker {
  def props(jobMaster:ActorRef, jobDesc:String): Props = {
    Props(new JobWorker(jobMaster,jobDesc))
  }
}
case class JobWorker(jobMaster:ActorRef,jobDesc:String) extends  Actor with ActorLogging{

  override def preStart(): Unit = {
    log.info("I'm in JobWorker")

    context.stop(self)
    jobMaster ! TaskFinish
  }
  override def receive: Receive = {
    case _ =>
  }
}
