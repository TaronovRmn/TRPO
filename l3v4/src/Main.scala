import java.util.Random


object Main {
  def main(args: Array[String]): Unit = {
    val m = new Main
    val rnd = new Random(System.currentTimeMillis)
    val tree = new TreeVertMas[Integer]
    System.out.println("Создаем 10 строк и добавляем в дерево")
    var i = 0
    while ( {
      i < 10
    }) { // tree.add(m.generateString(rnd, 3));   //вставка 10 строк в массив
      tree.add(m.generateInt(rnd, 100))

      {
        i += 1; i - 1
      }
    }
    tree.printMas()
    tree.printTree()
    System.out.println("Выводим элемент с индексом 3")
    System.out.println(tree.get(4))
    System.out.println("Удаляем элемент с индексом 4")
    tree.remove(4)
    tree.printTree()
  }
}

class Main {
  private def generateString(rnd: Random, length: Int) = {
    var str = new String
    val characters = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    var i = 0
    while ( {
      i < length
    }) {
      str += characters.charAt(rnd.nextInt(characters.length))

      {
        i += 1; i - 1
      }
    }
    str
  }

  private def generateInt(rnd: Random, max: Int) = rnd.nextInt(max)
}
