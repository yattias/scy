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

	/**
	 * The default lucene document to search in.
	 */
	private static final String DEFAULT_FIELD = "contents";
    
    private static final String OPERATOR_AND = "AND";
    
    private static final String OPERATOR_OR = "OR";

    private String term;

    private Query leftChild;

    private String operator;
    
    private Query rightChild;

    private boolean hasBracket;


    public static void main(String[] args) {
        // for testing purposes
        Query query;
        query = Query.parse("first AND (test and evaluation) OR notLast last");
        System.out.println(query.toLuceneString());
        System.out.println(query.replaceOperator("AND", "OR", new AtomicInteger(0)));
        System.out.println(query.replaceOperator("AND", "OR", new AtomicInteger(1)));
        System.out.println(query.replaceOperator("AND", "OR", new AtomicInteger(2)));
//        query = Query.parse("first AND (test and evaluation) OR notLast last");
//        System.out.println(query.replaceOperator("OR", "AND", new AtomicInteger(2)));
    }


    /**
     * Instantiates a new query which contains only one term.
     *
     * @param term the term
     */
    private Query(String term) {
        this.term = term;
        this.hasBracket = false;
    }

    /**
     * Instantiates a new query, which contains two sub-queries
     * connected by an operator.
     *
     * @param term1 the left sub-query
     * @param operator the operator
     * @param term2 the right sub-query
     */
    private Query(Query term1, String operator, Query term2) {
        this.leftChild = term1;
        this.rightChild = term2;
        this.operator = operator;
        this.hasBracket = false;
    }

    /**
     * Gets the operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Gets the term.
     *
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Gets the left child.
     *
     * @return the left child
     */
    public Query getLeftChild() {
        return leftChild;
    }

    /**
     * Gets the right child.
     *
     * @return the right child
     */
    public Query getRightChild() {
        return rightChild;
    }

    /**
     * Returns true if the sub-query has brackets.
     *
     * @return true, if sub-query has brackets
     */
    public boolean hasBracket() {
        return hasBracket;
    }

    /**
     * Sets that the sub-query has brackets. i.e.: (bla AND blubb)
     *
     * @param hasBracket the new bracket
     */
    public void setBracket(boolean hasBracket) {
        this.hasBracket = hasBracket;
    }

    /**
     * Parses a query and creates the corresponding query tree.
     *
     * @param query the query
     * @return the query
     */
    public static Query parse(String query) {
        if(query == null) {
            throw new IllegalArgumentException("query can not be null");
        }
        String newQuery = insertWhiteSpaces(query);
        List<String> list = Arrays.asList(newQuery.split(" "));
        return parse(list);
    }

    /**
     * Parses the.
     *
     * @param query the query
     * @return the query
     */
    private static Query parse(List<String> query) {
        if(query.isEmpty()) {
            return new Query("");
        } else if(query.size() == 1) {
            // only one term
            return new Query(query.get(0));
        } else if(query.size() == 2) {
            // two terms means they are linked by OR
            return new Query(new Query(query.get(0)), OPERATOR_OR, new Query(query.get(1)));
        } else {
            // three or more terms...

            if(!query.get(0).equals("(")) {
                // no bracket at the beginning

                if(query.get(1).equals(OPERATOR_AND) || query.get(1).equals(OPERATOR_OR)) {
                    return new Query(new Query(query.get(0)), query.get(1), parse(query.subList(2, query.size())));
                } else {
                    // if the second term is not AND and not OR, it must be a term
                    return new Query(new Query(query.get(0)), OPERATOR_OR, parse(query.subList(1, query.size())));
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
                            Query queryTerm = new Query(leftTerm, OPERATOR_OR, rightTerm);
                            queryTerm.setBracket(true);
                            return queryTerm;
                        } else {
                            // more than one term behind the bracket
                            if(query.get(endIndex + 1).equals(OPERATOR_AND) || query.get(endIndex + 1).equals(OPERATOR_OR)) {
                                Query leftTerm = parse(query.subList(0, endIndex + 1));
                                Query rightTerm = parse(query.subList(endIndex + 2, query.size()));
                                Query queryTerm = new Query(leftTerm, query.get(endIndex + 1), rightTerm);
                                queryTerm.setBracket(true);
                                return queryTerm;
                            } else {
                                // if the second term is not AND and not OR, it must be a term
                                Query leftTerm = parse(query.subList(0, endIndex + 1));
                                Query rightTerm = parse(query.subList(endIndex + 1, query.size() - 1));
                                Query queryTerm = new Query(leftTerm, OPERATOR_OR, rightTerm);
                                queryTerm.setBracket(true);
                                return queryTerm;

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method replaces a specific number of operators.
     *
     * @param from the operator which will be replaced
     * @param to the operator which will be used as replacement
     * @param numberOfReplacements the number of replacements
     * @return the lucene query 
     */
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

                result = leftTerm + " " + op + " " + rightTerm;
            } else {
            	result = this.leftChild.toLuceneString() + " " + this.operator + " " + this.rightChild.toLuceneString();
            }
            
            if(this.hasBracket()) {
                return "(" + result + ")";
            } else {
                return result;
            }
        }
    }

    /**
     * Creates a lucene query.
     *
     * @return the string
     */
    public String toLuceneString() {
        if(this.term != null) {
            if(this.hasBracket()) {
                return "(" + DEFAULT_FIELD + ":\"" + this.term + "\")";
            } else {
                return DEFAULT_FIELD + ":\"" + this.term + "\"";
            }
        } else {
            String result = this.leftChild.toLuceneString() + " " + this.operator + " " + this.rightChild.toLuceneString();
            if(this.hasBracket()) {
                return "(" + result + ")";
            } else {
                return result;
            }
        }
    }

    /**
     * Insert white spaces after opening bracket and before closing
     * bracket. So we can simply split the query using white spaces.
     *
     * @param query the query
     * @return the new query with whitespaces inserted
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
