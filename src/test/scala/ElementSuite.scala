/**
  * Created by yangshuxuan on 2017/8/20.
  */


import org.scalatest.FunSuite
case class elem(name:Char,width:Int,height:Int)
class ElementSuite extends FunSuite {
  test("elem result should have passed width") {
    val ele = elem('x', 2, 3)
    assert(ele.width == 2)
  }
}
