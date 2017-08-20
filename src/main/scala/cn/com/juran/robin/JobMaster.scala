package cn.com.juran.robin

/**
  * Created by yangshuxuan on 2017/8/20.
  */
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.cluster.routing.{ClusterRouterGroup, ClusterRouterGroupSettings}
import akka.routing.{ConsistentHashingGroup, RoundRobinGroup}
import cn.com.juran.robin.RobinSpec.{Task, TaskIn}


abstract class JobMaster(jobDesc:String) extends Actor with ActorLogging{
  val workerRouter:ActorRef
  override def preStart(): Unit = {
    workerRouter ! Task(jobDesc)
  }
  override def receive: Receive = {
    case TaskIn => {
      context.watch(sender())
      context.parent ! TaskIn
    }
  }


}
trait CommonRouter{
  this:Actor =>
  val routeesPaths = List("/user/statsWorker")
  val useRoles = Set("compute")
  val name = "workerRouter"
  val workerRouter = context.actorOf(
    ClusterRouterGroup(RoundRobinGroup(Nil), ClusterRouterGroupSettings(totalInstances = 100,
      routeesPaths = routeesPaths, allowLocalRoutees = true, useRoles = useRoles)).props(),
    name = name)
}
trait HeavyRouter extends CommonRouter{this:Actor =>}
trait LightRouter extends CommonRouter{this:Actor =>}
