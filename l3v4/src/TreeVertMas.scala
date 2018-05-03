import scala.collection.mutable.ArrayBuffer

class TreeVertMas[T] {

  class Elem(var value: T) extends Comparable[TreeVertMas[T]#Elem] {
    override def compareTo(obj: TreeVertMas[T]#Elem): Int = {
      if (value.isInstanceOf[String] && obj.value.isInstanceOf[String]) return value.asInstanceOf[String].compareTo(obj.value.asInstanceOf[String])
      else if (value.isInstanceOf[Integer] && obj.value.isInstanceOf[Integer]) return value.asInstanceOf[Integer] - obj.value.asInstanceOf[Integer]
      0
    }

    def getLength: Int = {
      if (value.isInstanceOf[String]) return (value.asInstanceOf[String]).length
      else if (value.isInstanceOf[Integer]) return Math.ceil(Math.log10(value.asInstanceOf[Integer] + 0.5)).toInt
      0
    }
  }

  private val x = new ArrayBuffer[Elem]()

  def add(value: T): Unit = {
    val e = new Elem(value)
    x += e
    var index = x.size - 1
    var p = index / 2
    if (index % 2 == 0) {
      p -= 1
    }
    while ( {
      index > 1 && (x(p).compareTo(x(index)) >= 0)
    }) {
      val tmp = x(p)
      x.update(p, e)
      x.update(index, tmp)
      index = p
      p = index / 2
      if (index % 2 == 0) {
        p -= 1; p + 1
      }
    }
  }

  def getLength: Int = x.size

  def get(index: Int): T = x(index).value

  def remove(ind: Int): Unit = {
    var index = ind
    var tmp = x(index)
    x.update(index, x(x.size - 1))
    x.update(x.size - 1, tmp)
    x.remove(x.size - 1)
    var c = index * 2
    while ( {
      c < x.size - 1 && x(index).compareTo(x(c)) > 0
    }) {
      c = index * 2
      if (c + 1 < x.size - 1 && x(c + 1).compareTo(x(c)) < 0) {
        c += 1
      }
      tmp = x(index)
      x.update(index, x(c))
      x.update(c, tmp)
      index = c
    }
  }

  def printMas(): Unit = {
    if (!x.isEmpty) {
      var i = 0
      while ( {
        i < x.size
      }) {
        System.out.println(i + " " + x(i).value)

        {
          i += 1; i - 1
        }
      }
    }
  }

  def printTree(): Unit = {
    if (!x.isEmpty) {
      val len = x(x.size - 1).getLength
      val lg2 = Math.log10(x.size) / Math.log10(2)
      val nOfS = Math.floor(lg2).toInt + 1
      //высота дерева
      var i = 0
      var str = 0
      while ( {
        str < nOfS
      }) {
        val fsp = Math.pow(2, nOfS - str - 1).toInt - 1
        var nsp = 1
        if (fsp != 0) nsp = Math.pow(2, nOfS - str).toInt - 1
        var sp = 0
        while ( {
          sp < fsp
        }) {
          var l = 0
          while ( {
            l < len
          }) {System.out.print(" ")
            l += 1;
          }
          {
            sp += 1; sp - 1
          }
        }
        val nel = Math.pow(2, str).toInt - 1
        var el = 0
        while ( {
          el < nel
        }) {
          if (i < x.size) {
            System.out.print(x(i).value)
            i += 1
            var sp = 0
            while ( {
              sp < nsp
            }) {
              var l = 0
              while ( {
                l < len
              }) {System.out.print(" ")
                l += 1;
              }
              {
                sp += 1; sp - 1
              }
            }
          }

          {
            el += 1; el - 1
          }
        }
        if (i < x.size) {
          System.out.print(x(i).value)
          i += 1
        }
        System.out.println()

        {
          str += 1; str - 1
        }
      }
    }
  }
}
