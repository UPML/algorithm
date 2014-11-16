package Utils;

/**
 * Created by kagudkov on 15.11.14.
 */
public class treap {

    private class indexForString {
        int numberOfTemplate, lengthOfSuffix;

        public indexForString(int numberOfTemplateTmp, int lengthOfSuffixTmp) {
            numberOfTemplate = numberOfTemplateTmp;
            lengthOfSuffix = lengthOfSuffixTmp;
        }
    }

    indexForString currentIndex;
    int y;
    treap left, right, father;

    public treap(int numberOfTemplateTmp, int lengthOfSuffixTmp, int yTmp, treap leftTmp, treap rightTmp, treap fatherTmp) {
        currentIndex = new indexForString(numberOfTemplateTmp, lengthOfSuffixTmp);
        y = yTmp;
        left = leftTmp;
        right = rightTmp;
        father = fatherTmp;
    }

    public treap(indexForString currentIndexTmp, int yTmp, treap leftTmp, treap rightTmp, treap fatherTmp) {
        currentIndex = currentIndexTmp;
        y = yTmp;
        left = leftTmp;
        right = rightTmp;
        father = fatherTmp;
    }

    public treap Merge(treap left, treap right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (left.y > right.y) {
            treap newRight = Merge(left.right, right);
            return new treap(left.currentIndex, left.y, left.left, newRight, left.father);
        } else {
            treap newLeft = Merge(left, right.left);
            return new treap(right.currentIndex, right.y, newLeft, right.right, right.father);
        }
    }

    public treap Split(int numberOfTemplateTmp, int lengthOfSuffixTmp) {
        indexForString indexTmp = new indexForString(numberOfTemplateTmp, lengthOfSuffixTmp);
        treap newTreap = null;

        return null;
    }

}

