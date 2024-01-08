package org.example;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

public class RedBlackTree {
    private Node root;
    static final boolean RED = false;
    static final boolean BLACK = true;
    private int size = 0;

    public static class Node {
        public Node(MyEntry data) {
            this.data = data;
        }
        MyEntry data;
        boolean color;
        Node left;
        Node right;
        Node parent;
    }

    public MyEntry searchData(String id) {
        Node node = root;
        while (node != null) {
            if (id.compareTo(node.data.getKey().getId()) == 0)
                return node.data;
            if (id.compareTo(node.data.getKey().getId()) < 0)
                node = node.left;
            else
                node = node.right;
        }
        return null;
    }

    public void insertEntry(MyEntry entry) {
        Node node = root;
        Node parent = null;
        while (node != null) {
            parent = node;
            if (entry.compareTo(node.data) < 0) {
                node = node.left;
            } else if (entry.compareTo(node.data) > 0) {
                node = node.right;
            } else {
                return;
            }
        }
        Node newNode = new Node(entry);
        newNode.color = RED;
        if (parent == null) {
            root = newNode;
        } else if (entry.compareTo(root.data) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;
        fixRedBlackPropertiesAfterInsert(newNode);
        size++;
    }

    private void fixRedBlackPropertiesAfterInsert(Node node) {
        Node parent = node.parent;
        if (parent == null) {
            node.color = BLACK;
            return;
        }
        if (parent.color == BLACK) {
            return;
        }

        Node grand = parent.parent;
        Node uncle = getUncle(parent);

        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            grand.color = RED;
            uncle.color = BLACK;
            fixRedBlackPropertiesAfterInsert(grand);
        }

        else if (parent == grand.left) {

            // left-right
            if (node == parent.right) {
                rotateLeft(parent);
                parent = node;
            }

            // left-left
            rotateRight(grand);

            parent.color = BLACK;
            grand.color = RED;
        } else {
            // right-left
            if (node == parent.left) {
                rotateRight(parent);
                parent = node;
            }

            // right-right
            rotateLeft(grand);

            parent.color = BLACK;
            grand.color = RED;
        }
    }

    private Node getUncle(Node parent) {
        Node grand = parent.parent;
        if (grand.left == parent) {
            return grand.right;
        } else {
            return grand.left;
        }
    }

    public void deleteEntry(String id) {
        Node node = root;
        while (node != null && !Objects.equals(node.data.getKey().getId(), id)) {
            if (id.compareTo(node.data.getKey().getId()) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (node == null) {
            return;
        }

        Node movedUpNode;
        boolean deletedNodeColor;

        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.color;
        } else {
            Node minimum = findMinimum(node.right);
            node.data = minimum.data;
            movedUpNode = deleteNodeWithZeroOrOneChild(minimum);
            deletedNodeColor = minimum.color;
        }
        if (deletedNodeColor == BLACK) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }
        size--;
    }

    private Node deleteNodeWithZeroOrOneChild(Node node) {
        if (node.left != null) {
            replaceParentsChild(node.parent, node, node.left);
            return node.left;
        } else if (node.right != null) {
            replaceParentsChild(node.parent, node, node.right);
            return node.right;
        } else {
            Node newChild = node.color == BLACK ? new NilNode() : null;
            replaceParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private Node findMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void fixRedBlackPropertiesAfterDelete(Node node) {
        if (node == root) {
            node.color = BLACK;
            return;
        }

        Node sibling = getSibling(node);

        if (sibling.color == RED) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node);
        }

        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            sibling.color = RED;
            if (node.parent.color == RED) {
                node.parent.color = BLACK;
            } else {
                fixRedBlackPropertiesAfterDelete(node.parent);
            }
        } else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private void handleRedSibling(Node node, Node sibling) {
        sibling.color = BLACK;
        node.parent.color = RED;
        if (node == node.parent.left) {
            rotateLeft(node.parent);
        } else {
            rotateRight(node.parent);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node node, Node sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;
        if (nodeIsLeftChild && isBlack(sibling.right)) {
            sibling.left.color = BLACK;
            sibling.color = RED;
            rotateRight(sibling);
            sibling = node.parent.right;
        } else if (!nodeIsLeftChild && isBlack(sibling.left)) {
            sibling.right.color = BLACK;
            sibling.color = RED;
            rotateLeft(sibling);
            sibling = node.parent.left;
        }

        sibling.color = node.parent.color;
        node.parent.color = BLACK;
        if (nodeIsLeftChild) {
            sibling.right.color = BLACK;
            rotateLeft(node.parent);
        } else {
            sibling.left.color = BLACK;
            rotateRight(node.parent);
        }
    }

    private Node getSibling(Node node) {
        Node parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == BLACK;
    }

    private static class NilNode extends RedBlackTree.Node {
        private NilNode() {
            super(null);
            this.color = BLACK;
        }
    }

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }
        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    public int getSize() {
        return size;
    }

    private class TreeIterator implements Iterator<Route> {
        private Stack<Node> stack;

        public TreeIterator() {
            stack = new Stack<>();
            pushLeftMostNodes(root);
        }

        private void pushLeftMostNodes(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Route next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the tree");
            }
            Node current = stack.pop();
            pushLeftMostNodes(current.right);
            return current.data.getKey();
        }
    }

    public Iterator<Route> iterator() {
        return new TreeIterator();
    }
}
