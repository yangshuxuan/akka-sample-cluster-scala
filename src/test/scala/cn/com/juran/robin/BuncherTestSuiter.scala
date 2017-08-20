package cn.com.juran.robin

/**
  * Created by yangshuxuan on 2017/8/20.
  */
import akka.actor.{ActorRef, ActorSystem, FSM, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.immutable
import cn.com.juran.robin.Worker
import FSMDocSpec._
import cn.com.juran.robin.RobinSpec.{Task, TaskFinish, TaskIn}

import scala.concurrent.duration._
class BuncherTestSuiter {

}
class MySpec() extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An Echo actor" must {

    "send back messages unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }

  }
  "simple finite state machine" must {

    "demonstrate NullFunction" in {
      class A extends FSM[Int, Null] {
        val SomeState = 0
        when(SomeState)(FSM.NullFunction)
      }
    }

    "batch correctly" in {
      val buncher = system.actorOf(Props(classOf[Buncher]))
      buncher ! SetTarget(testActor)
      buncher ! Queue(42)
      buncher ! Queue(43)
      expectMsg(Batch(immutable.Seq(42, 43)))
      buncher ! Queue(44)
      buncher ! Flush
      buncher ! Queue(45)
      expectMsg(Batch(immutable.Seq(44)))
      expectMsg(Batch(immutable.Seq(45)))
    }

    "not batch if uninitialized" in {
      val buncher = system.actorOf(Props(classOf[Buncher]))
      buncher ! Queue(42)
      expectNoMsg
    }
    "worker correctly" in {
      val worker = system.actorOf(Props(classOf[cn.com.juran.robin.Worker]))
      worker ! Task("YangShuxuan")
      expectMsg(TaskIn)
      expectMsg(TaskFinish)

    }
  }
}
