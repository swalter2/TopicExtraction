/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rdf;

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

/**
 *
 * @author swalter
 */
public class Sparql {
    
    public String getQuery() {
        String query = "SELECT ?e1_lemma ?e2_lemma ?lemma ?prep WHERE {"
                + "?e1 <conll:head> ?term . "
                + "?e1 <conll:deprel> ?e1_deprel. "
                + "FILTER regex(?e1_deprel, \"subj\") . "
                + "?p <conll:deprel> \"prep\" . "
                + "?p <conll:head> ?term. "
                + "?p <conll:form> ?prep. "
                + "?term <conll:form> ?lemma. "
                + "?e2 <conll:deprel> ?e2_deprel. "
                + "FILTER regex(?e2_deprel, \"obj\") . "
                + "?e2 <conll:head> ?p. "
                + "?e2 <conll:form> ?e2_lemma. "
                + "?e1 <conll:form> ?e1_lemma. "
                + "}";
        return query;
    }


    public List<List<String>> extract(Model model) {
        QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
        ResultSet rs = qExec.execSelect() ;
        String lemma = "";
        String e1_lemma = "";
        String e2_lemma = "";
        String prep = "";
        List<List<String>> results = new ArrayList<>();
        while ( rs.hasNext() ) {
                QuerySolution qs = rs.next();
                try{
                        lemma = qs.get("?lemma").toString();
                        e1_lemma = qs.get("?e1_lemma").toString();
                        e2_lemma = qs.get("?e2_lemma").toString();
                        prep = qs.get("?prep").toString();
                        List<String> result = new ArrayList<String>();
                        result.add(lemma);
                        result.add(e1_lemma);
                        result.add(e2_lemma);
                        result.add(prep);
                        results.add(result);
                 }
                catch(Exception e){
               e.printStackTrace();
               }
            }

        qExec.close() ;
        return results;
                
    }
    
}
