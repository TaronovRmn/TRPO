import java.awt._
import java.awt.event.ActionEvent
import java.io._
import java.util.Random
import javax.swing._
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.SwingConstants.CENTER

import com.intellij.uiDesigner.core._


object Main {
  def main(args: Array[String]): Unit = {
    try
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    catch {
      case e@(_: UnsupportedLookAndFeelException | _: ClassNotFoundException | _: InstantiationException | _: IllegalAccessException) =>
        System.out.println("Не удалось установить Look And Feel")
    }
    def foo() = new Main

    foo()
  }
}

class Main private() extends JFrame {
  setTitle("Вариант 4 Дерево упорядоченное вертикально в массиве")
  setPreferredSize(new Dimension(300, 300))
  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  ChooseType()
  pack()
  setLocationRelativeTo(null)
  setVisible(true)
  private var intTree = new TreeVertMas[Integer]
  private var strTree = new TreeVertMas[String]
  private var isInt = false
  private val rnd = new Random(System.currentTimeMillis)
  private var sizee = 0

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

  private def chooseLength(): Unit = {
    var chooseLabel = new JLabel
    val selectButton = new JButton("OK")
    if (isInt) chooseLabel = new JLabel("Введите максимальное число")
    else chooseLabel = new JLabel("Введите размер строки")
    val len = new JTextField
    len.setText("")
    val mainP = new JPanel(new GridLayout(3, 1))
    this.add(mainP)
    mainP.add(chooseLabel)
    mainP.add(len)
    chooseLabel.setHorizontalAlignment(CENTER)
    mainP.add(selectButton)
    selectButton.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent): Unit = {
        if (len.getText ne "") sizee = len.getText.toInt
        remove(mainP)
        repaint()
        setSize(new Dimension(800, 500))
        paintFrame()
      }

      foo(e)
    })
  }

  private def ChooseType(): Unit = {
    val group = new ButtonGroup
    val intButton = new JRadioButton("Целые числа", true)
    group.add(intButton)
    val strButton = new JRadioButton("Строки", false)
    group.add(strButton)
    val selectButton = new JButton("Выбрать")
    val chooseLabel = new JLabel("Какой тип данных будет содержать список?")
    val mainPanel = new JPanel(new GridLayout(4, 1))
    this.add(mainPanel)
    mainPanel.add(chooseLabel)
    chooseLabel.setHorizontalAlignment(CENTER)
    mainPanel.add(intButton)
    mainPanel.add(strButton)
    mainPanel.add(selectButton)
    selectButton.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent): Unit = {
        if (intButton.isSelected) {
          isInt = true
          intTree = new TreeVertMas[Integer]
        }
        else strTree = new TreeVertMas[String]
        remove(mainPanel)
        repaint()
        setSize(new Dimension(300, 100))
        chooseLength()
      }

      foo(e)
    })
  }

  private def fileMenu(): Unit = {
    val menuBar = new JMenuBar
    val file = new JMenu("Файл")
    var saveItem = new JMenuItem
    var loadItem = new JMenuItem
    var exitItem = new JMenuItem
    menuBar.add(file)
    saveItem = new JMenuItem("Сохранить")
    file.add(saveItem)
    loadItem = new JMenuItem("Загрузить")
    file.add(loadItem)
    file.addSeparator()
    exitItem = new JMenuItem("Выход")
    file.add(exitItem)
    saveItem.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent): Unit = {
        val chooser = new JFileChooser
        chooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"))
        chooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"))
        val returnVal = chooser.showSaveDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          var saveFile = new File(chooser.getSelectedFile.getAbsolutePath)
          if (!saveFile.getAbsolutePath.endsWith(".txt")) saveFile = new File(saveFile.getAbsolutePath + ".txt")
          if (saveFile.exists) {
            val response = JOptionPane.showConfirmDialog(this, "Файл с таким именем уже существует. Заменить?", "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
            if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) return
          }
          try {
            val flout = new FileOutputStream(saveFile)
            val oos = new ObjectOutputStream(flout)
            if (isInt) oos.writeObject(intTree)
            else oos.writeObject(strTree)
            oos.close()
          } catch {
            case ex: Exception =>
              System.err.format("IOException: %s%n", ex)
          }
        }
      }

      foo(e)
    })
    loadItem.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent): Unit = {
        if (isInt) intTree = new TreeVertMas[Integer]
        else strTree = new TreeVertMas[String]
        val chooser = new JFileChooser
        chooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"))
        chooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"))
        val returnVal = chooser.showOpenDialog(this)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          if (!chooser.getSelectedFile.exists) {
            JOptionPane.showConfirmDialog(this, "Такого файла не существует!", "Предупреждение", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE)
            //return
          }

          try {
            val fin = new FileInputStream(chooser.getSelectedFile)
            val objectinputstream = new ObjectInputStream(fin)
            if (isInt) intTree = objectinputstream.readObject.asInstanceOf[TreeVertMas[Integer]]
            else strTree = objectinputstream.readObject.asInstanceOf[TreeVertMas[String]]
          } catch {
            case ex: Exception =>
              ex.printStackTrace()
          }
          printTr()
        }
      }

      foo(e)
    })
    exitItem.addActionListener((e: ActionEvent) => System.exit(0))
    this.setJMenuBar(menuBar)
  }


  private var outTree = new JTextArea
  private var listModel = new DefaultListModel[String]

  private def paintFrame(): Unit = {
    fileMenu()
    val mainPanel = new JPanel
    this.add(mainPanel)
    mainPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1))
    mainPanel.setOpaque(true)
    mainPanel.setPreferredSize(new Dimension(600, 500))
    val panel1 = new JPanel
    panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1))
    mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
    val panel2 = new JPanel
    panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1))
    panel1.add(panel2, new GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
    val outputText = new JTextArea
    val scroll = new JScrollPane(outputText)
    panel2.add(scroll, new GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false))
    val panel3 = new JPanel
    panel3.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1))
    panel1.add(panel3, new GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 50), null, 0, false))
    val addButton = new JButton
    addButton.setText("Добавить")
    val clrButton = new JButton
    clrButton.setText("Очистить")
    panel3.add(addButton, new GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    panel3.add(clrButton, new GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    val delButton = new JButton
    delButton.setText("Удалить")
    panel3.add(delButton, new GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    val findButton = new JButton
    findButton.setText("Вывести элемент")
    panel3.add(findButton, new GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    val nDel = new JTextField
    nDel.setText("")
    panel3.add(nDel, new GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false))
    val nFind = new JTextField
    panel3.add(nFind, new GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false))
    val label1 = new JLabel
    label1.setText("Индекс:")
    panel3.add(label1, new GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    val label2 = new JLabel
    label2.setText("Индекс:")
    panel3.add(label2, new GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    val panel4 = new JPanel
    panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1))
    mainPanel.add(panel4, new GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
    outTree = new JTextArea
    panel4.add(outTree, new GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(250, -1), new Dimension(250, 50), null, 0, false))
    val panel5 = new JPanel
    panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1))
    mainPanel.add(panel5, new GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
    listModel = new DefaultListModel[String]
    val masTree = new JList(listModel)
    panel5.add(masTree, new GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(50, 50), new Dimension(50, -1), 0, false))
    val label3 = new JLabel
    label3.setText("В виде массива:")
    mainPanel.add(label3, new GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    val label4 = new JLabel
    label4.setText("В виде дерева:")
    mainPanel.add(label4, new GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    addButton.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent) : Unit = {
        if (isInt) intTree.add(generateInt(rnd, sizee))
       else strTree.add(generateString(rnd, sizee))
        outputText.append("Добавлен случайный элемент\n")
        printTr()
      }

      foo(e)
    })
    clrButton.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent) : Unit = {
        if (isInt) intTree = new TreeVertMas[Integer]
        else strTree = new TreeVertMas[String]
        outputText.append("Очищено\n")
        printTr()
      }

      foo(e)
    })
    delButton.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent) : Unit = {
        var b = false
        if (isInt) b = intTree.remove(nDel.getText.toInt)
        else b = strTree.remove(nDel.getText.toInt)
        if (!b) outputText.append("Не существующий индекс!\n")
        else outputText.append("Элемент " + nDel.getText.toInt + " удален\n")
        printTr()
      }

      foo(e)
    })
    findButton.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent) : Unit = {
        val i = nFind.getText.toInt
        var sz = 0
        if (isInt) sz = intTree.size
        else sz = strTree.size
        if (i < 0 || i >= sz) outputText.append("Не существующий индекс!\n")
        else {
          var el = ""
          if (isInt) el = intTree.get(nFind.getText.toInt).toString
          else el = strTree.get(nFind.getText.toInt)
          outputText.append("Под индексом " + nFind.getText.toInt + " элемент " + el + "\n")
        }
      }

      foo(e)
    })
  }

  private def printTr(): Unit = {
    if (isInt) {
      intTree.printMas(listModel)
      intTree.printTree(outTree)
    } else {
      strTree.printMas(listModel)
      strTree.printTree(outTree)
    }
  }
}
