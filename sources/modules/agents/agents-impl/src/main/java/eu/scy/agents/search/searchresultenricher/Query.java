/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.agents.search.searchresultenricher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Christopher Krueger
 */
public class Query {

	private static final String DEFAULT_FIELD = "contents";
    private String term;

    private Query leftChild;

    private String operator;
    
    private Query rightChild;

    private boolean hasBracket;


    public static void main(String[] args) {
        // for testing purposes
        Query query;
//        query = QueryTerm.parse("(test  AND evaluation  test2)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(test evaluation)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("test evaluation");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(test AND evaluation)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(test OR evaluation)");
//        System.out.println(query.toString());
        query = Query.parse("first AND (test and evaluation) OR notLast last");
        System.out.println(query.toString());
        query = Query.parse("first AND (test and evaluation) OR notLast last");
        System.out.println(query.replaceOperator("OR", "AND", new AtomicInteger(2)));
//        query = QueryTerm.parse("last");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(last) ()");
//        System.out.println(query.toString());
    }


    private Query(String term) {
        this.term = term;
        this.hasBracket = false;
    }

    private Query(Query term1, String operator, Query term2) {
        this.leftChild = term1;
        this.rightChild = term2;
        this.operator = operator;
        this.hasBracket = false;
    }

    public String getOperator() {
        return operator;
    }

    public String getTerm() {
        return term;
    }

    public Query getLeftChild() {
        return leftChild;
    }

    public Query getRightChild() {
        return rightChild;
    }

    public boolean hasBracket() {
        return hasBracket;
    }

    public void setBracket(boolean hasBracket) {
        this.hasBracket = hasBracket;
    }

    public static Query parse(String query) {
        if(query == null) {
            throw new IllegalArgumentException("query can not be null");
        }
        String newQuery = insertWhiteSpaces(query);
        List<String> list = Arrays.asList(newQuery.split(" "));
        return parse(list);
    }

    private static Query parse(List<String> query) {
        if(query.isEmpty()) {
            return new Query("");
        } else if(query.size() == 1) {
            // only one term
            return new Query(query.get(0));
        } else if(query.size() == 2) {
            // two terms means they are linked by OR
            return new Query(new Query(query.get(0)), "OR", new Query(query.get(1)));
        } else {
            // three or more terms...

            if(!query.get(0).equals("(")) {
                // no bracket at the beginning

                if(query.get(1).equals("AND") || query.get(1).equals("OR")) {
                    return new Query(new Query(query.get(0)), query.get(1), parse(query.subList(2, query.size())));
                } else {
                    // if the second term is not AND and not OR, it must be a term
                    return new Query(new Query(query.get(0)), "OR", parse(query.subList(1, query.size())));
                }
            } else {
                // The term has brackets so we need to find the end...
                int openBracketNumber = 0;
                int endIndex = -1;
                for(int i = 0; i < query.size(); i++) {
                    if(query.get(i).equals("(")) {
                        openBracketNumber++;
                    } else if(query.get(i).equals(")")) {
                        openBracketNumber--;
                    }
                    if(openBracketNumber == 0) {
                        endIndex = i;
                        break;
                    }
                }

                if(endIndex == -1) {
                    // malformed query... a bracket has not been closed... so stop here
                    StringBuilder sb = new StringBuilder();
                    for(String s : query) {
                        sb.append(s).append(' ');
                    }
                    return new Query(sb.toString());

                } else {

                    if(endIndex == query.size() - 1) {
                        // no more terms behind the bracket... remove the brackets
                        Query qt = parse(query.subList(1, endIndex));
                        qt.setBracket(true);
                        return qt;
                    } else {
                        // there exist terms behind the bracket...

                        if(endIndex + 2 == query.size()) {
                            // one other term behind the bracket
                            Query leftTerm = parse(query.subList(0, endIndex));
                            Query rightTerm = new Query(query.get(endIndex + 1));
                            Query queryTerm = new Query(leftTerm, "OR", rightTerm);
                            queryTerm.setBracket(true);
                            return queryTerm;
                        } else {
                            // more than one term behind the bracket
                            if(query.get(endIndex + 1).equals("AND") || query.get(endIndex + 1).equals("OR")) {
                                Query leftTerm = parse(query.subList(0, endIndex + 1));
                                Query rightTerm = parse(query.subList(endIndex + 2, query.size()));
                                Query queryTerm = new Query(leftTerm, query.get(endIndex + 1), rightTerm);
                                queryTerm.setBracket(true);
                                return queryTerm;
                            } else {
                                // if the second term is not AND and not OR, it must be a term
                                Query leftTerm = parse(query.subList(0, endIndex + 1));
                                Query rightTerm = parse(query.subList(endIndex + 1, query.size() - 1));
                                Query queryTerm = new Query(leftTerm, "OR", rightTerm);
                                queryTerm.setBracket(true);
                                return queryTerm;

                            }
                        }
                    }
                }
            }
        }
    }

    public String replaceOperator(String from, String to, AtomicInteger numberOfReplacements) {
        if(this.term != null) {
            if(this.hasBracket()) {
                return "(" + DEFAULT_FIELD + ":\"" + this.term + "\")";
            } else {
                return DEFAULT_FIELD + ":\"" + this.term + "\"";
            }
        } else {
            String result;
            if(numberOfReplacements.get() > 0) {
                String op;
                if(this.operator.equals(from)) {
                    // replace the operator
                    op = to;
                    numberOfReplacements.decrementAndGet();
                } else {
                    op = this.operator;
                }

                // Replace on the left child
                String leftTerm = this.leftChild.replaceOperator(from, to, numberOfReplacements);

                // Replace on the right child
                String rightTerm = this.rightChild.replaceOperator(from, to, numberOfReplacements);

                if(!op.equals("AND")) {
                    result = leftTerm + " " + rightTerm;
                } else {
                    result = leftTerm + " " + op + " " + rightTerm;
                }
            } else {
                if(!this.operator.equals("AND")) {
                    result = this.leftChild.toString() + " " + this.rightChild.toString();
                } else {
                    result = this.leftChild.toString() + " " + this.operator + " " + this.rightChild.toString();
                }
            }
            
            if(this.hasBracket()) {
                return "(" + result + ")";
            } else {
                return result;
            }
        }
    }

    public String toLuceneString() {
        if(this.term != null) {
            if(this.hasBracket()) {
                return "(" + DEFAULT_FIELD + ":\"" + this.term + "\")";
            } else {
                return DEFAULT_FIELD + ":\"" + this.term + "\"";
            }
        } else {
            String result;
            if(!this.operator.equals("AND")) {
                result = this.leftChild.toString() + " " + this.rightChild.toString();
            } else {
                result = this.leftChild.toString() + " " + this.operator + " " + this.rightChild.toString();
            }
            if(this.hasBracket()) {
                return "(" + result + ")";
            } else {
                return result;
            }
        }
    }

    /*
     * this method will insert white spaces after opening bracket and before closing
     * bracket. So we can simply split the query using white spaces.
     */
    private static String insertWhiteSpaces(String query) {
        StringBuilder newQuery = new StringBuilder();
        for(int i = 0; i < query.length(); i++) {
            if(query.charAt(i) == '(') {
                // put whitespaces in front and behind opening bracket
                if(i > 0 && query.charAt(i - 1) != ' ') {
                    newQuery.append(' ');
                }
                newQuery.append('(');
                if(i + 1 < query.length() && query.charAt(i + 1) != ' ') {
                    newQuery.append(' ');
                }
            } else if(query.charAt(i) == ')') {
                // put whitespaces in front and behind closing bracket
                if(i > 0 && query.charAt(i - 1) != ' ') {
                    newQuery.append(' ');
                }
                newQuery.append(')');
                if(i + 1 < query.length() && query.charAt(i + 1) != ' ') {
                    newQuery.append(' ');
                }
            } else {
                // eliminate multiple whitespaces
                if(i > 0 && query.charAt(i) == ' ' && query.charAt(i - 1) == ' ') {
                    continue;
                }
                newQuery.append(query.charAt(i));
            }
        }
        return newQuery.toString();
    }

}
