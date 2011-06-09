/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.agents.search.searchresultenricher;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Christopher Krueger
 */
public class QueryTerm {

    private String term;

    private QueryTerm term1;

    private String operator;
    
    private QueryTerm term2;

    private boolean hasBracket;


    public static void main(String[] args) {
        // for testing purposes
//        QueryTerm query = QueryTerm.parse("(test  AND evaluation  test2)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(test evaluation)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("test evaluation");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(test AND evaluation)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(test OR evaluation)");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("first AND (test and evaluation) OR notLast last");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("last");
//        System.out.println(query.toString());
//        query = QueryTerm.parse("(last) ()");
//        System.out.println(query.toString());
    }


    private QueryTerm(String term) {
        this.term = term;
        this.hasBracket = false;
    }

    private QueryTerm(QueryTerm term1, String operator, QueryTerm term2) {
        this.term1 = term1;
        this.term2 = term2;
        this.operator = operator;
        this.hasBracket = false;
    }

    public boolean hasBracket() {
        return hasBracket;
    }

    public void setBracket(boolean hasBracket) {
        this.hasBracket = hasBracket;
    }

    public static QueryTerm parse(String query) {
        if(query == null) {
            throw new IllegalArgumentException("query can not be null");
        }
        String newQuery = insertWhiteSpaces(query);
        List<String> list = Arrays.asList(newQuery.split(" "));
        return parse(list);
    }

    private static QueryTerm parse(List<String> query) {
        if(query.isEmpty()) {
            return new QueryTerm("");
        } else if(query.size() == 1) {
            // only one term
            return new QueryTerm(query.get(0));
        } else if(query.size() == 2) {
            // two terms means they are linked by OR
            return new QueryTerm(new QueryTerm(query.get(0)), "OR", new QueryTerm(query.get(1)));
        } else {
            // three or more terms...

            if(!query.get(0).equals("(")) {
                // no bracket at the beginning

                if(query.get(1).equals("AND") || query.get(1).equals("OR")) {
                    return new QueryTerm(new QueryTerm(query.get(0)), query.get(1), parse(query.subList(2, query.size())));
                } else {
                    // if the second term is not AND and not OR, it must be a term
                    return new QueryTerm(new QueryTerm(query.get(0)), "OR", parse(query.subList(1, query.size())));
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
                    return new QueryTerm(sb.toString());

                } else {

                    if(endIndex == query.size() - 1) {
                        // no more terms behind the bracket... remove the brackets
                        QueryTerm qt = parse(query.subList(1, endIndex));
                        qt.setBracket(true);
                        return qt;
                    } else {
                        // there exist terms behind the bracket...

                        if(endIndex + 2 == query.size()) {
                            // one other term behind the bracket
                            QueryTerm leftTerm = parse(query.subList(0, endIndex));
                            QueryTerm rightTerm = new QueryTerm(query.get(endIndex + 1));
                            QueryTerm queryTerm = new QueryTerm(leftTerm, "OR", rightTerm);
                            queryTerm.setBracket(true);
                            return queryTerm;
                        } else {
                            // more than one term behind the bracket
                            if(query.get(endIndex + 1).equals("AND") || query.get(endIndex + 1).equals("OR")) {
                                QueryTerm leftTerm = parse(query.subList(0, endIndex + 1));
                                QueryTerm rightTerm = parse(query.subList(endIndex + 2, query.size()));
                                QueryTerm queryTerm = new QueryTerm(leftTerm, query.get(endIndex + 1), rightTerm);
                                queryTerm.setBracket(true);
                                return queryTerm;
                            } else {
                                // if the second term is not AND and not OR, it must be a term
                                QueryTerm leftTerm = parse(query.subList(0, endIndex + 1));
                                QueryTerm rightTerm = parse(query.subList(endIndex + 1, query.size() - 1));
                                QueryTerm queryTerm = new QueryTerm(leftTerm, "OR", rightTerm);
                                queryTerm.setBracket(true);
                                return queryTerm;

                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        if(this.term != null) {
            if(this.hasBracket()) {
                return "(" + this.term + ")";
            } else {
                return this.term;
            }
        } else {
            if(this.hasBracket()) {
                return "(" + this.term1 + " " + this.operator + " " + this.term2 + ")";
            } else {
                return this.term1 + " " + this.operator + " " + this.term2;
            }
        }
    }

    /*
     * this method will insert white spaces after opening bracket and before closing
     * bracket. So we can simply split the query using white spaces.
     */
    private static String insertWhiteSpaces(String query) {
        query = query.trim();
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
