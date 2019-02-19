public class AVL {
    private Node ferst;

    public boolean add(int e) {
        Node newNode = new Node();//Создаем элемент добавления(тот который будет вставлен в дерево)
        newNode.setID(e);//Задаем ключ у нового элемента
        if (ferst == null) {//Проверка на первый элемент(Есть ли корень у дерева)
            this.ferst = newNode;
            this.ferst.setID(e);//Добавляем элемент в корень если он не заполнен
        } else {
            newNode.setParent(this.ferst);//Создаем указатель на родителя
            while (true) {//"Бесконечный" цикл прохождения по дереву
                if (newNode.getID() == newNode.getParent().getID()) {//Если элемент уже существует, то возвращаем False
                    return false;
                }
                if (newNode.getID() < newNode.getParent().getID()) {//Сравнение элемента в вставляемом объекте и его родителя
                    if (newNode.getParent().getLeft() == null) {//Если элемент меньше родительского, то проверяем существование левого потомка
                        newNode.getParent().setLeft(newNode);//Если его нет, то родителю нашего элемента назначаем его левым потомком
                        break;//выходим из цикла
                    } else {//если левый потомок существует, то присваеваем левый потомок родителя, родителм нашего элемента
                        newNode.setParent(newNode.getParent().getLeft());
                    }
                } else {//Если ключ элемента оказался больше, проделываем эдентичные операций для правого потомка
                    if (newNode.getParent().getRight() == null) {
                        newNode.getParent().setRight(newNode);
                        break;
                    } else {
                        newNode.setParent(newNode.getParent().getRight());
                    }
                }
            }
        }
        ballans(newNode.getParent());

        return true;
    }

    private void ballans(Node node) {
        if (node == null) return;
        Node traveler = node;
        Node helper;
        while (true) {
            helper = traveler;
            int sizePast = traveler.getSizeTree();//Запоминаем значение размера до пересчета
            sizeTree(traveler);//Пересчитываем
            if (sizePast == traveler.getSizeTree()) {//Сравниваем размеры до и после
                if (traveler.getParent() != null) {//если это лист переходим на одну вершину наверх
                    traveler = traveler.getParent();
                    continue;
                } else {
                    return;//Если это первый элемент выходим
                }
            } else {
                if (differenceSize(traveler) >= 2) {
                    if (traveler.getRight().getLeft() != null && traveler.getRight().getRight() == null) {
                        helper = leftBig(traveler);
                    } else if (traveler.getRight().getLeft() == null && traveler.getRight().getRight() != null) {
                        helper = leftSmall(traveler);
                    } else if (differenceSize(traveler.getRight()) >= 0) {
                        helper = leftSmall(traveler);
                    } else if (differenceSize(traveler.getRight()) < 0) {
                        helper = leftBig(traveler);
                    }
                } else if (differenceSize(traveler) <= -2) {
                    if (traveler.getLeft().getRight() != null && traveler.getLeft().getLeft() == null) {
                        helper = rightBig(traveler);
                    } else if (traveler.getLeft().getRight() == null && traveler.getLeft().getLeft() != null) {
                        helper = rightSmall(traveler);
                    } else if (differenceSize(traveler.getLeft()) <= 0) {
                        helper = rightSmall(traveler);
                    } else if (differenceSize(traveler.getLeft()) > 0) {
                        helper = rightBig(traveler);
                    }
                }
                if (helper.getParent() != null) {
                    traveler = helper.getParent();
                } else return;
            }
        }
    }

    private int differenceSize(Node node) {
        int left, right;
        if (node.getLeft() == null && node.getRight() == null) {
            left = 0;
            right = 0;
        } else if (node.getLeft() != null && node.getRight() == null) {
            left = node.getLeft().getSizeTree();
            right = 0;
        } else if (node.getLeft() == null && node.getRight() != null) {
            left = 0;
            right = node.getRight().getSizeTree();
        } else {
            left = node.getLeft().getSizeTree();
            right = node.getRight().getSizeTree();
        }
        return (right - left);
    }

    public void sizeTree(Node node) {
        int left, right;
        if (node.getLeft() == null && node.getRight() == null) {
            left = 0;
            right = 0;
        } else if (node.getLeft() != null && node.getRight() == null) {
            left = node.getLeft().getSizeTree();
            right = 0;
        } else if (node.getLeft() == null && node.getRight() != null) {
            left = 0;
            right = node.getRight().getSizeTree();
        } else {
            left = node.getLeft().getSizeTree();
            right = node.getRight().getSizeTree();
        }
        node.setSizeTree(Math.max(left, right) + 1);
    }

    public Node leftSmall(Node node) {
        Node a = node;
        Node b = node.getRight();
        smallHelpAB(a, b);
        a.setParent(b);
        a.setRight(b.getLeft());
        b.setLeft(a);
        if (a.getRight() != null) {
            a.getRight().setParent(a);
        }
        sizeTree(a);
        sizeTree(b);
        return b;
    }

    public Node rightSmall(Node node) {
        Node a = node;
        Node b = node.getLeft();
        smallHelpAB(a, b);
        a.setParent(b);
        a.setLeft(b.getRight());
        b.setRight(a);
        if (a.getLeft() != null) {
            a.getLeft().setParent(a);
        }
        sizeTree(a);
        sizeTree(b);
        return b;
    }

    public Node leftBig(Node node) {
        rightSmall(node.getRight());
        return leftSmall(node);
    }

    public Node rightBig(Node node) {
        leftSmall(node.getLeft());
        return rightSmall(node);
    }

    private void smallHelpAB(Node a, Node b) {
        if (a == this.ferst) {
            this.ferst = b;
            b.setParent(null);
        } else {
            b.setParent(a.getParent());
            if (a.getParent().getLeft() == a) {
                a.getParent().setLeft(b);
            } else {
                a.getParent().setRight(b);
            }
        }
    }


    public boolean remove(int e) {//Удаление элемента
        Node node = getNode(e);//Находим требуемый для удаления элемент
        Node pass = node;
        if (node.getLeft() == null && node.getRight() == null) {//У нашего элемента есть 2 потомка?
            if (node.getParent() == null) {//Да. У нашего элемента есть родитель?
                this.ferst = null;//Нет. Значит это первый и единственный элеменит в дереве
                return true;//Возвращаем удачное удаление
            } else {//Да, у нашего элемента еть родитель
                if (node.getParent().getLeft().equals(node)) {//Наш элемент левый?
                    node.getParent().setLeft(null);//Да. Удаляем связь родителя с левым потомком
                    return true;//Возвращаем удачное удаление
                } else {//Нет, он правый
                    node.getParent().setRight(null);//Удаляем связь родителя с правым потомком
                    return true;//Возвращаем удачное удаление
                }
            }
        }
        if (!(node.getLeft() == null) && node.getRight() == null) {//Наш элемент имеет только левый потомок?
            if (node.getParent() == null) {//Да. У нашего элемента есть родитель?
                this.ferst = node.getLeft();//Нет. Делаем корневым элементом левый потомок преведущего
                this.ferst.setParent(null);//Обнуляем значение родителя
                return true;//Возвращаем удачное удаление
            } else {//Да, есть родитель
                Node ret = node.getParent();//Создаем ссылку на родителя нашего элемента
                if (node.getParent().getLeft().equals(node)) {//
                    ret.setLeft(node.getLeft());
                    node.getLeft().setParent(ret);
                    return true;
                } else {
                    ret.setRight(node.getLeft());
                    node.getLeft().setParent(ret);
                    return true;
                }
            }
        }
        if (node.getLeft() == null && !(node.getRight() == null)) {
            if (node.getParent() == null) {
                this.ferst = node.getRight();
                this.ferst.setParent(null);
                return true;
            } else {
                Node ret = node.getParent();
                if (node.getParent().getLeft() == node) {
                    ret.setLeft(node.getRight());
                    node.getRight().setParent(ret);
                    return true;
                } else {
                    ret.setRight(node.getRight());
                    node.getRight().setParent(ret);
                    return true;
                }
            }
        }
        if (!(node.getLeft() == null) && !(node.getRight() == null)) {
            Node nodeNext = next(node);
            if (nodeNext.getRight() == null) {
                if (nodeNext.getParent().getLeft().equals(nodeNext)) {
                    nodeNext.getParent().setLeft(null);
                } else {
                    nodeNext.getParent().setRight(null);
                }
                insertNode(node, nodeNext);
                return true;
            } else {
                if (nodeNext.getParent().getLeft() == nodeNext) {
                    nodeNext.getParent().setLeft(nodeNext.getRight());
                    nodeNext.getRight().setParent(nodeNext.getParent());
                    nodeNext.setRight(null);
                } else {
                    nodeNext.getParent().setRight(nodeNext.getRight());
                    nodeNext.getRight().setParent(nodeNext.getParent());
                    nodeNext.setRight(null);
                }
                insertNode(node, nodeNext);

                return true;
            }
        }

        return false;
    }

    private void insertNode(Node delNode, Node nodeNext) {//Вставка элемента вместо существующего
        nodeNext.setSizeTree(delNode.getSizeTree());
        nodeNext.setParent(delNode.getParent());
        nodeNext.setLeft(delNode.getLeft());
        nodeNext.setRight(delNode.getRight());
        if (delNode.getLeft() != null) {
            delNode.getLeft().setParent(nodeNext);
        }
        if (delNode.getRight() != null) {
            delNode.getRight().setParent(nodeNext);
        }
        if (delNode.getParent() != null) {
            if (delNode.getParent().getLeft().equals(delNode)) {
                delNode.getParent().setLeft(nodeNext);
            }
            if (delNode.getParent().getRight().equals(delNode)) {
                delNode.getParent().setRight(nodeNext);
            }
        } else {
            this.ferst = nodeNext;
        }
    }

    public Node minElement() {
        return min(this.ferst);
    }

    public Node getNode(int e) {
        Node node = this.ferst;
        while (true) {
            if (node.getID() == e) {
                return node;
            }
            if (e < node.getID()) {
                if (node.getLeft() == null) {
                    return null;
                }
                node = node.getLeft();
            }
            if (e > node.getID()) {
                if (node.getRight() == null) {
                    return null;
                }
                node = node.getRight();
            }
        }
    }

    private Node min(Node Rode) {
        Node node = Rode;
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    public Node next(Node node) {//Следующий элемент
        if (node.getRight() == null) {//Проверка на существование правого потомка
            while (true) {//Цикл подъема
                if (node.getParent() == null) {//Если дошли до родительского возвращаем Null
                    return null;//Возврат Null
                } else {//Если родительский элемент существует
                    if (node.equals(node.getParent().getLeft())) {//Проверяем, если наш элемент левый
                        return node.getParent();//Возвращаем родительский элемент
                    } else {//Если элемент правый
                        node = node.getParent();//Поднимаемся пока наш элемент не правый или родитель Null
                    }
                }
            }
        } else {//Если существует правый потомок
            return min(node.getRight());//Возвращаем минимум из правого поддерева
        }
    }

    public Node getFerst() {
        return this.ferst;
    }

    public void visual(Node node) {
        if (node.getLeft() != null) {
            visual(node.getLeft());
        }
        //System.out.println(node.getID() + " " + node.getSizeTree());
        testNode(node);
        if (node.getRight() != null) {
            visual(node.getRight());
        }
    }

    public void testNode(Node node) {
        if (node != null) {
            System.out.println("Node: " + node.getID());
            if (node.getParent() != null) {
                System.out.println("Parent: " + node.getParent().getID());
            }
            if (node.getLeft() != null) {
                System.out.println("Left: " + node.getLeft().getID());
            }
            if (node.getRight() != null) {
                System.out.println("Right: " + node.getRight().getID());
            }
            System.out.println("Size: " + node.getSizeTree());
            System.out.println();
        } else {
            System.out.println("Don't node");
            System.out.println();
        }
    }
}