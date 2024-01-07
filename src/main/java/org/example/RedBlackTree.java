package org.example;

public class RedBlackTree<E extends Comparable<E>> {
    private Node<E> root;
    static final boolean RED = false;
    static final boolean BLACK = true;

    public static class Node<E> {
        public Node(E data) {
            this.data = data;
        }
        E data;
        boolean color;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
    }

    private Node<E> searchNode(E element) {
        Node<E> node = root;
        while (node != null) {
            if (element.compareTo(root.data) == 0)
                return root;
            if (element.compareTo(root.data) < 0)
                node = node.left;
            else
                node = node.right;
        }
        return null;
    }

    // -- Insertion ----------------------------------------------------------------------------------
    public void insertNode(E key) {
        Node<E> node = root;
        Node<E> parent = null;

        // Traverse the tree to the left or right depending on the key
        while (node != null) {
            parent = node;
            if (key.compareTo(root.data) < 0) {
                node = node.left;
            } else if (key.compareTo(root.data) > 0) {
                node = node.right;
            } else {
                return;
            }
        }

        // Insert new node
        Node<E> newNode = new Node<>(key);
        newNode.color = RED;
        if (parent == null) {
            root = newNode;
        } else if (key.compareTo(root.data) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private void fixRedBlackPropertiesAfterInsert(Node<E> node) {
        Node<E> parent = node.parent;

        // Case 1: Parent is null, we've reached the root, the end of the recursion
        if (parent == null) {
            node.color = BLACK;
            return;
        }

        // Parent is black --> nothing to do
        if (parent.color == BLACK) {
            return;
        }

        // From here on, parent is red
        Node<E> grandparent = parent.parent;

        // Get the uncle (maybe null/nil, in which case its color is BLACK)
        Node<E> uncle = getUncle(parent);

        // Case 3: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            grandparent.color = RED;
            uncle.color = BLACK;

            // Call recursively for grandparent, which is now red.
            // It might be root or have a red parent, in which case we need to fix more...
            fixRedBlackPropertiesAfterInsert(grandparent);
        }

        // Note on performance:
        // It would be faster to do the uncle color check within the following code. This way
        // we would avoid checking the grandparent-parent direction twice (once in getUncle()
        // and once in the following else-if). But for better understanding of the code,
        // I left the uncle color check as a separate step.

        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
            // Case 4a: Uncle is black and node is left->right "inner child" of its grandparent
            if (node == parent.right) {
                rotateLeft(parent);

                // Let "parent" point to the new root node of the rotated subtree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5a: Uncle is black and node is left->left "outer child" of its grandparent
            rotateRight(grandparent);

            // Recolor original parent and grandparent
            parent.color = BLACK;
            grandparent.color = RED;
        }

        // Parent is right child of grandparent
        else {
            // Case 4b: Uncle is black and node is right->left "inner child" of its grandparent
            if (node == parent.left) {
                rotateRight(parent);

                // Let "parent" point to the new root node of the rotated subtree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = node;
            }

            // Case 5b: Uncle is black and node is right->right "outer child" of its grandparent
            rotateLeft(grandparent);

            // Recolor original parent and grandparent
            parent.color = BLACK;
            grandparent.color = RED;
        }
    }

    private Node<E> getUncle(Node<E> parent) {
        Node<E> grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else {
            return grandparent.left;
        }
    }

    // -- Deletion -----------------------------------------------------------------------------------
    public void deleteNode(E key) {
        Node<E> node = root;

        // Find the node to be deleted
        while (node != null && node.data != key) {
            // Traverse the tree to the left or right depending on the key
            if (key.compareTo(root.data) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        // Node not found?
        if (node == null) {
            return;
        }

        // At this point, "node" is the node to be deleted

        // In this variable, we'll store the node at which we're going to start to fix the R-B
        // properties after deleting a node.
        Node<E> movedUpNode;
        boolean deletedNodeColor;

        // Node has zero or one child
        if (node.left == null || node.right == null) {
            movedUpNode = deleteNodeWithZeroOrOneChild(node);
            deletedNodeColor = node.color;
        }

        // Node has two children
        else {
            // Find minimum node of right subtree ("inorder successor" of current node)
            Node<E> inOrderSuccessor = findMinimum(node.right);

            // Copy inorder successor's data to current node (keep its color!)
            node.data = inOrderSuccessor.data;

            // Delete inorder successor just as we would delete a node with 0 or 1 child
            movedUpNode = deleteNodeWithZeroOrOneChild(inOrderSuccessor);
            deletedNodeColor = inOrderSuccessor.color;
        }

        if (deletedNodeColor == BLACK) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);

            // Remove the temporary NIL node
            if (movedUpNode.getClass() == NilNode.class) {
                replaceParentsChild(movedUpNode.parent, movedUpNode, null);
            }
        }
    }

    private Node<E> deleteNodeWithZeroOrOneChild(Node<E> node) {
        // Node has ONLY a left child --> replace by its left child
        if (node.left != null) {
            replaceParentsChild(node.parent, node, node.left);
            return node.left; // moved-up node
        }

        // Node has ONLY a right child --> replace by its right child
        else if (node.right != null) {
            replaceParentsChild(node.parent, node, node.right);
            return node.right; // moved-up node
        }

        // Node has no children -->
        // * node is red --> just remove it
        // * node is black --> replace it by a temporary NIL node (needed to fix the R-B rules)
        else {
            Node<E> newChild = node.color == BLACK ? new NilNode() : null;
            replaceParentsChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private Node<E> findMinimum(Node<E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @SuppressWarnings("squid:S125") // Ignore SonarCloud complains about commented code line 256.
    private void fixRedBlackPropertiesAfterDelete(Node<E> node) {
        // Case 1: Examined node is root, end of recursion
        if (node == root) {
            node.color = BLACK;
            return;
        }

        Node<E> sibling = getSibling(node);

        // Case 2: Red sibling
        if (sibling.color == RED) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node); // Get new sibling for fall-through to cases 3-6
        }

        // Cases 3+4: Black sibling with two black children
        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            sibling.color = RED;

            // Case 3: Black sibling with two black children + red parent
            if (node.parent.color == RED) {
                node.parent.color = BLACK;
            }

            // Case 4: Black sibling with two black children + black parent
            else {
                fixRedBlackPropertiesAfterDelete(node.parent);
            }
        }

        // Case 5+6: Black sibling with at least one red child
        else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private void handleRedSibling(Node<E> node, Node<E> sibling) {
        // Recolor...
        sibling.color = BLACK;
        node.parent.color = RED;

        // ... and rotate
        if (node == node.parent.left) {
            rotateLeft(node.parent);
        } else {
            rotateRight(node.parent);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node<E> node, Node<E> sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;

        // Case 5: Black sibling with at least one red child + "outer nephew" is black
        // --> Recolor sibling and its child, and rotate around sibling
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

        // Fall-through to case 6...

        // Case 6: Black sibling with at least one red child + "outer nephew" is red
        // --> Recolor sibling + parent + sibling's child, and rotate around parent
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

    private Node<E> getSibling(Node<E> node) {
        Node<E> parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        } else if (node == parent.right) {
            return parent.left;
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private boolean isBlack(Node<E> node) {
        return node == null || node.color == BLACK;
    }

    private class NilNode extends RedBlackTree.Node<E> {
        private NilNode() {
            super(null);
            this.color = BLACK;
        }
    }

    // -- Helpers for insertion and deletion ---------------------------------------------------------

    private void rotateRight(Node<E> node) {
        Node<E> parent = node.parent;
        Node<E> leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node<E> node) {
        Node<E> parent = node.parent;
        Node<E> rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node<E> parent, Node<E> oldChild, Node<E> newChild) {
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

    // -- For toString() -----------------------------------------------------------------------------
    protected void appendNodeToString(Node<E> node, StringBuilder builder) {
        builder.append(node.data).append(node.color == RED ? "[R]" : "[B]");
    }
}
